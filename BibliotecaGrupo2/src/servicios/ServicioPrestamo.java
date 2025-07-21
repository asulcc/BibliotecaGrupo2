package servicios;

import modelos.materiales.Material;
import modelos.materiales.PrestamoMaterial;
import modelos.recursos.Recurso;
import modelos.recursos.ReservaRecurso;
import modelos.recursos.SalaEstudio;
import modelos.usuarios.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ServicioPrestamo {
    private BaseDatos dbManager;
    private ServicioMaterial servicioMaterial; 
    private ServicioRecurso servicioRecurso;  
    private ServicioSancion servicioSancion;  

    public ServicioPrestamo(BaseDatos dbManager, ServicioMaterial servicioMaterial, ServicioRecurso servicioRecurso, ServicioSancion servicioSancion) {
        this.dbManager = dbManager;
        this.servicioMaterial = servicioMaterial;
        this.servicioRecurso = servicioRecurso;
        this.servicioSancion = servicioSancion;
    }
    
    public boolean prestarMaterial(Usuario usuario, Material material, boolean esReserva) {
        if (usuario == null || material == null) {
            System.err.println("Error: Usuario o material no pueden ser nulos.");
            return false;
        }
        if (servicioSancion.usuarioEstaSancionado(usuario)) {
            System.out.println("El usuario " + usuario.getNombreCompleto() 
                    + " está sancionado y no puede realizar préstamos/reservas.");
            return false;
        }
        if (material.getCantidadDisponible() <= 0) {
            System.out.println("El material '" + material.getTitulo() 
                    + "' no tiene unidades disponibles para " 
                    + (esReserva ? "reserva" : "préstamo") + ".");
            return false;
        }

        LocalDate fechaPrestamo = LocalDate.now();
        LocalDate fechaDevolucionEstimada = fechaPrestamo.plusDays(7);

        String sql = "INSERT INTO PrestamosMaterial (usuarioId, materialId, "
                + "fechaPrestamo, fechaDevolucionEstimada, esReserva, activo) "
                + "VALUES (?, ?, ?, ?, ?, ?);";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbManager.getConnection();
            conn.setAutoCommit(false); // Iniciar transacción

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, usuario.getId());
            pstmt.setInt(2, material.getId());
            pstmt.setString(3, fechaPrestamo.toString());
            pstmt.setString(4, fechaDevolucionEstimada.toString());
            pstmt.setBoolean(5, esReserva);
            pstmt.setBoolean(6, true); // Activo
            pstmt.executeUpdate();
            
            conn.commit(); // Confirmar transacción

            // Decrementar la cantidad disponible del material
            material.decrementarCantidadDisponible(1);
            servicioMaterial.updateMaterial(material); // Actualizar en la base de datos

            System.out.println((esReserva ? "Reserva" : "Préstamo") + " de '" + material.getTitulo() + "' registrado para " + usuario.getNombreCompleto() + ".");
            return true;
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback(); // Revertir transacción en caso de error
            } catch (SQLException ex) {
                System.err.println("Error al hacer rollback: " + ex.getMessage());
            }
            System.err.println("Error al registrar " + (esReserva ? "reserva" : "préstamo") + " de material: " + e.getMessage());
            return false;
        } finally {
            dbManager.closeConnection(conn);
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { /* ignore */ }
        }
    }

    public boolean devolverMaterial(PrestamoMaterial prestamo) {
        if (prestamo == null || !prestamo.isActivo()) {
            System.out.println("El préstamo no es válido o ya ha sido devuelto/finalizado.");
            return false;
        }

        LocalDate fechaDevolucionReal = LocalDate.now();
        int diasRetraso = (int) prestamo.getFechaFinPrevista().until(fechaDevolucionReal, java.time.temporal.ChronoUnit.DAYS);

        String sql = "UPDATE PrestamosMaterial SET fechaDevolucionReal = ?, activo = ?, completado = ? WHERE id = ?;";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbManager.getConnection();
            conn.setAutoCommit(false); // Iniciar transacción

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, fechaDevolucionReal.toString());
            pstmt.setBoolean(2, false); // Desactivar el préstamo
            pstmt.setBoolean(3, true);  // Marcar como completado
            pstmt.setInt(4, prestamo.getId());
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                // Actualizar el objeto en memoria
                prestamo.setFechaDevolucionReal(fechaDevolucionReal);
                prestamo.setActivo(false); // Se marcó en el setter de fechaDevolucionReal

                conn.commit(); // Confirmar transacción
                
                // Incrementar la cantidad disponible del material
                Material material = servicioMaterial.getMaterialById(prestamo.getMaterial().getId());
                if (material != null) {
                    material.incrementarCantidadDisponible(1);
                    servicioMaterial.updateMaterial(material); // Actualizar en la base de datos
                } else {
                    System.err.println("Advertencia: Material asociado al préstamo no encontrado. Cantidad no actualizada.");
                }

                if (diasRetraso > 0) {
                    System.out.println("Préstamo de '" + prestamo.getMaterial().getTitulo() + "' devuelto con " + diasRetraso + " días de retraso.");
                    // Aplicar sanción
                    servicioSancion.aplicarSancionPorRetraso(prestamo, diasRetraso);
                } else {
                    System.out.println("Préstamo de '" + prestamo.getMaterial().getTitulo() + "' devuelto a tiempo.");
                }
                
                return true;
            } else {
                System.out.println("No se pudo registrar la devolución. Préstamo no encontrado.");
                conn.rollback();
                return false;
            }
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback(); // Revertir transacción en caso de error
            } catch (SQLException ex) {
                System.err.println("Error al hacer rollback: " + ex.getMessage());
            }
            System.err.println("Error al registrar devolución de material: " + e.getMessage());
            return false;
        } finally {
            dbManager.closeConnection(conn);
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { /* ignore */ }
        }
    }

    public boolean reservarRecurso(Usuario usuario, Recurso recurso, LocalDateTime fechaHoraInicio, LocalDateTime fechaHoraFin, int participantes) {
        if (usuario == null || recurso == null) {
            System.err.println("Error: Usuario o recurso no pueden ser nulos.");
            return false;
        }
        if (servicioSancion.usuarioEstaSancionado(usuario)) {
            System.out.println("El usuario " + usuario.getNombreCompleto() + " está sancionado y no puede realizar reservas.");
            return false;
        }
        if (!recurso.isDisponible()) {
            System.out.println("El recurso '" + recurso.getCodigo() + "' no está disponible para reserva.");
            return false;
        }
        if (fechaHoraInicio.isAfter(fechaHoraFin) || fechaHoraInicio.isBefore(LocalDateTime.now())) {
            System.out.println("Fechas de reserva inválidas.");
            return false;
        }
        if (recurso instanceof SalaEstudio) {
            SalaEstudio sala = (SalaEstudio) recurso;
            if (!sala.puedeReservar(participantes)) {
                System.out.println("La sala de estudio '" + sala.getCodigo() + "' no puede acomodar " + participantes + " participantes o no está disponible.");
                return false;
            }
        } else if (participantes > 0) {
            System.out.println("Advertencia: Los participantes solo son relevantes para salas de estudio.");
            participantes = 0; // Resetear participantes para otros recursos
        }

        // Verificar si el recurso ya está reservado para el mismo período
        if (isRecursoReservadoEnPeriodo(recurso, fechaHoraInicio, fechaHoraFin)) {
            System.out.println("El recurso '" + recurso.getCodigo() + "' ya está reservado en el período solicitado.");
            return false;
        }

        String sql = "INSERT INTO ReservasRecurso (usuarioId, recursoId, fechaHoraInicio, fechaHoraFin, participantes, activo) VALUES (?, ?, ?, ?, ?, ?);";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbManager.getConnection();
            conn.setAutoCommit(false); // Iniciar transacción

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, usuario.getId());
            pstmt.setInt(2, recurso.getId());
            pstmt.setString(3, fechaHoraInicio.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            pstmt.setString(4, fechaHoraFin.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            pstmt.setInt(5, participantes);
            pstmt.setBoolean(6, true); // Activo
            pstmt.executeUpdate();

            if (!(recurso instanceof SalaEstudio)) {
                recurso.setDisponible(false);
                servicioRecurso.updateRecurso(recurso);
            }

            conn.commit(); // Confirmar transacción
            System.out.println("Reserva de '" + recurso.getCodigo() + "' registrada para " + usuario.getNombreCompleto() + " desde " +
                               fechaHoraInicio.format(DateTimeFormatter.ofPattern("dd/MM HH:mm")) + " hasta " +
                               fechaHoraFin.format(DateTimeFormatter.ofPattern("dd/MM HH:mm")) + ".");
            return true;
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback(); // Revertir transacción en caso de error
            } catch (SQLException ex) {
                System.err.println("Error al hacer rollback: " + ex.getMessage());
            }
            System.err.println("Error al reservar recurso: " + e.getMessage());
            return false;
        } finally {
            dbManager.closeConnection(conn);
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { /* ignore */ }
        }
    }

    public boolean liberarRecurso(ReservaRecurso reserva) {
        if (reserva == null || !reserva.isActivo()) {
            System.out.println("La reserva no es válida o ya ha sido liberada/finalizada.");
            return false;
        }

        String sql = "UPDATE ReservasRecurso SET activo = ?, completado = ? WHERE id = ?;";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbManager.getConnection();
            conn.setAutoCommit(false); // Iniciar transacción

            pstmt = conn.prepareStatement(sql);
            pstmt.setBoolean(1, false); // Desactivar la reserva
            pstmt.setBoolean(2, true);  // Marcar como completada
            pstmt.setInt(3, reserva.getId());
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                // Actualizar el objeto en memoria
                reserva.setActivo(false); // Se marcó en el setter de activo

                // Marcar el recurso como disponible si no es una Sala de Estudio
                Recurso recurso = servicioRecurso.getRecursoById(reserva.getRecurso().getId());
                if (recurso != null && !(recurso instanceof SalaEstudio)) {
                    recurso.setDisponible(true);
                    servicioRecurso.updateRecurso(recurso); // Actualizar en la base de datos
                }

                conn.commit(); // Confirmar transacción
                System.out.println("Reserva de '" + reserva.getRecurso().getCodigo() + "' liberada exitosamente.");
                return true;
            } else {
                System.out.println("No se pudo liberar la reserva. ID no encontrado.");
                conn.rollback();
                return false;
            }
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback(); // Revertir transacción en caso de error
            } catch (SQLException ex) {
                System.err.println("Error al hacer rollback: " + ex.getMessage());
            }
            System.err.println("Error al liberar reserva de recurso: " + e.getMessage());
            return false;
        } finally {
            dbManager.closeConnection(conn);
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { /* ignore */ }
        }
    }

    public List<PrestamoMaterial> getPrestamosPorUsuario(Usuario usuario) {
        List<PrestamoMaterial> prestamos = new ArrayList<>();
        String sql = "SELECT id, usuarioId, materialId, fechaPrestamo, fechaDevolucionEstimada, fechaDevolucionReal, esReserva, activo, completado FROM PrestamosMaterial WHERE usuarioId = ? ORDER BY activo DESC, fechaDevolucionEstimada DESC;";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, usuario.getId());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                PrestamoMaterial prestamo = createPrestamoMaterialFromResultSet(rs, usuario);
                if (prestamo != null) {
                    prestamos.add(prestamo);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener préstamos por usuario: " + e.getMessage());
        } finally {
            dbManager.closeConnection(conn);
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { /* ignore */ }
            if (rs != null) try { rs.close(); } catch (SQLException e) { /* ignore */ }
        }
        return prestamos;
    }

    public List<PrestamoMaterial> getAllPrestamosMaterial() {
        List<PrestamoMaterial> prestamos = new ArrayList<>();
        String sql = "SELECT id, usuarioId, materialId, fechaPrestamo, fechaDevolucionEstimada, fechaDevolucionReal, esReserva, activo, completado FROM PrestamosMaterial ORDER BY activo DESC, fechaDevolucionEstimada DESC;";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                // Necesitamos el usuario para construir el objeto PrestamoMaterial
                Usuario prestamoUsuario = servicioSancion.getUsuarioById(rs.getInt("usuarioId")); // Reutilizar el método de servicioSancion para obtener usuario
                PrestamoMaterial prestamo = createPrestamoMaterialFromResultSet(rs, prestamoUsuario);
                if (prestamo != null) {
                    prestamos.add(prestamo);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los préstamos de material: " + e.getMessage());
        } finally {
            dbManager.closeConnection(conn);
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { /* ignore */ }
            if (rs != null) try { rs.close(); } catch (SQLException e) { /* ignore */ }
        }
        return prestamos;
    }


    public List<ReservaRecurso> getAllReservasRecurso() {
        List<ReservaRecurso> reservas = new ArrayList<>();
        String sql = "SELECT id, usuarioId, recursoId, fechaHoraInicio, fechaHoraFin, participantes, activo, completado FROM ReservasRecurso ORDER BY activo DESC, fechaHoraFin DESC;";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                // Necesitamos el usuario para construir el objeto ReservaRecurso
                Usuario reservaUsuario = servicioSancion.getUsuarioById(rs.getInt("usuarioId"));
                ReservaRecurso reserva = createReservaRecursoFromResultSet(rs, reservaUsuario);
                if (reserva != null) {
                    reservas.add(reserva);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todas las reservas de recurso: " + e.getMessage());
        } finally {
            dbManager.closeConnection(conn);
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { /* ignore */ }
            if (rs != null) try { rs.close(); } catch (SQLException e) { /* ignore */ }
        }
        return reservas;
    }

    public ReservaRecurso getReservaRecursoById(int id) {
        String sql = "SELECT id, usuarioId, recursoId, fechaHoraInicio, fechaHoraFin, participantes, activo, completado FROM ReservasRecurso WHERE id = ?;";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ReservaRecurso reserva = null;

        try {
            conn = dbManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                Usuario usuario = servicioSancion.getUsuarioById(rs.getInt("usuarioId"));
                reserva = createReservaRecursoFromResultSet(rs, usuario);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener reserva de recurso por ID: " + e.getMessage());
        } finally {
            dbManager.closeConnection(conn);
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { /* ignore */ }
            if (rs != null) try { rs.close(); } catch (SQLException e) { /* ignore */ }
        }
        return reserva;
    }


    public PrestamoMaterial getPrestamoMaterialById(int id) {
        String sql = "SELECT id, usuarioId, materialId, fechaPrestamo, fechaDevolucionEstimada, fechaDevolucionReal, esReserva, activo, completado FROM PrestamosMaterial WHERE id = ?;";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        PrestamoMaterial prestamo = null;

        try {
            conn = dbManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                Usuario usuario = servicioSancion.getUsuarioById(rs.getInt("usuarioId"));
                prestamo = createPrestamoMaterialFromResultSet(rs, usuario);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener préstamo de material por ID: " + e.getMessage());
        } finally {
            dbManager.closeConnection(conn);
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { /* ignore */ }
            if (rs != null) try { rs.close(); } catch (SQLException e) { /* ignore */ }
        }
        return prestamo;
    }


    public boolean extenderPrestamoMaterial(PrestamoMaterial prestamo) {
        if (prestamo == null || !prestamo.isActivo() || prestamo.estaVencido()) {
            System.out.println("No se puede extender un préstamo nulo, inactivo o vencido.");
            return false;
        }

        // Extender por 7 días adicionales
        LocalDate nuevaFechaFin = prestamo.getFechaFinPrevista().plusDays(7);
        String sql = "UPDATE PrestamosMaterial SET fechaDevolucionEstimada = ? WHERE id = ?;";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nuevaFechaFin.toString());
            pstmt.setInt(2, prestamo.getId());
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                prestamo.extenderFechaDevolucion(nuevaFechaFin); // Actualizar el objeto en memoria
                System.out.println("Préstamo de '" + prestamo.getMaterial().getTitulo() + "' extendido hasta " + nuevaFechaFin + ".");
                return true;
            } else {
                System.out.println("No se pudo extender el préstamo. ID no encontrado.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error al extender préstamo de material: " + e.getMessage());
            return false;
        } finally {
            dbManager.closeConnection(conn);
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { /* ignore */ }
        }
    }

    public boolean extenderReservaRecurso(ReservaRecurso reserva, LocalDateTime nuevaFechaFin) {
        if (reserva == null || !reserva.isActivo() || reserva.estaVencido() || nuevaFechaFin.isBefore(reserva.getFechaHoraFin())) {
            System.out.println("No se puede extender una reserva nula, inactiva, vencida o a una fecha anterior.");
            return false;
        }

        // Verificar que la nueva franja horaria no se solape con otras reservas del mismo recurso
        if (isRecursoReservadoEnPeriodo(reserva.getRecurso(), reserva.getFechaHoraInicio(), nuevaFechaFin, reserva.getId())) {
             System.out.println("La extensión de la reserva para '" + reserva.getRecurso().getCodigo() + "' se solapa con otra reserva existente.");
             return false;
         }

        String sql = "UPDATE ReservasRecurso SET fechaHoraFin = ? WHERE id = ?;";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nuevaFechaFin.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            pstmt.setInt(2, reserva.getId());
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                reserva.setFechaHoraFin(nuevaFechaFin); // Actualizar el objeto en memoria
                System.out.println("Reserva de '" + reserva.getRecurso().getCodigo() + "' extendida hasta " + nuevaFechaFin.format(DateTimeFormatter.ofPattern("dd/MM HH:mm")) + ".");
                return true;
            } else {
                System.out.println("No se pudo extender la reserva. ID no encontrado.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error al extender reserva de recurso: " + e.getMessage());
            return false;
        } finally {
            dbManager.closeConnection(conn);
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { /* ignore */ }
        }
    }


    private boolean isRecursoReservadoEnPeriodo(Recurso recurso, LocalDateTime inicio, LocalDateTime fin) {
        return isRecursoReservadoEnPeriodo(recurso, inicio, fin, 0); // 0 indica que no es una reserva existente a excluir
    }

    private boolean isRecursoReservadoEnPeriodo(Recurso recurso, LocalDateTime inicio, LocalDateTime fin, int excludeReservaId) {
        String sql = "SELECT COUNT(*) FROM ReservasRecurso WHERE recursoId = ? AND activo = TRUE AND id <> ? AND (" +
                     "(fechaHoraInicio < ? AND fechaHoraFin > ?) OR " + // Nueva reserva envuelve una existente
                     "(fechaHoraInicio >= ? AND fechaHoraInicio < ?) OR " + // Nueva reserva empieza durante una existente
                     "(fechaHoraFin > ? AND fechaHoraFin <= ?)" +          // Nueva reserva termina durante una existente
                     ");";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, recurso.getId());
            pstmt.setInt(2, excludeReservaId);
            pstmt.setString(3, fin.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            pstmt.setString(4, inicio.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            pstmt.setString(5, inicio.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            pstmt.setString(6, fin.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            pstmt.setString(7, inicio.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            pstmt.setString(8, fin.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar disponibilidad del recurso: " + e.getMessage());
        } finally {
            dbManager.closeConnection(conn);
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { /* ignore */ }
            if (rs != null) try { rs.close(); } catch (SQLException e) { /* ignore */ }
        }
        return false;
    }


    private PrestamoMaterial createPrestamoMaterialFromResultSet(ResultSet rs, Usuario usuario) throws SQLException {
        int id = rs.getInt("id");
        int materialId = rs.getInt("materialId");
        LocalDate fechaPrestamo = LocalDate.parse(rs.getString("fechaPrestamo"));
        LocalDate fechaDevolucionEstimada = LocalDate.parse(rs.getString("fechaDevolucionEstimada"));
        String fechaDevolucionRealStr = rs.getString("fechaDevolucionReal");
        LocalDate fechaDevolucionReal = (fechaDevolucionRealStr != null) ? LocalDate.parse(fechaDevolucionRealStr) : null;
        boolean esReserva = rs.getBoolean("esReserva");
        boolean activo = rs.getBoolean("activo");
        boolean completado = rs.getBoolean("completado"); // Para PrestamoBase

        Material material = servicioMaterial.getMaterialById(materialId); // Obtener el material asociado

        if (material != null && usuario != null) {
            PrestamoMaterial prestamo = new PrestamoMaterial(id, usuario, material, fechaPrestamo, fechaDevolucionEstimada, esReserva);
            prestamo.setFechaDevolucionReal(fechaDevolucionReal); // Esto también setea activo a false y completado a true si fechaDevolucionReal no es null
            prestamo.setActivo(activo); // Asegurar que el estado activo sea el de la DB
            prestamo.setCompletado(completado); // Asegurar que el estado completado sea el de la DB
            return prestamo;
        }
        return null;
    }

    private ReservaRecurso createReservaRecursoFromResultSet(ResultSet rs, Usuario usuario) throws SQLException {
        int id = rs.getInt("id");
        int recursoId = rs.getInt("recursoId");
        LocalDateTime fechaHoraInicio = LocalDateTime.parse(rs.getString("fechaHoraInicio"));
        LocalDateTime fechaHoraFin = LocalDateTime.parse(rs.getString("fechaHoraFin"));
        int participantes = rs.getInt("participantes");
        boolean activo = rs.getBoolean("activo");
        boolean completado = rs.getBoolean("completado"); // Para PrestamoBase

        Recurso recurso = servicioRecurso.getRecursoById(recursoId); // Obtener el recurso asociado

        if (recurso != null && usuario != null) {
            ReservaRecurso reserva;
            if (recurso instanceof SalaEstudio) {
                reserva = new ReservaRecurso(id, usuario, recurso, fechaHoraInicio, fechaHoraFin, participantes);
            } else {
                reserva = new ReservaRecurso(id, usuario, recurso, fechaHoraInicio, fechaHoraFin);
            }
            reserva.setActivo(activo); // Asegurar que el estado activo sea el de la DB
            reserva.setCompletado(completado); // Asegurar que el estado completado sea el de la DB
            return reserva;
        }
        return null;
    }
}
