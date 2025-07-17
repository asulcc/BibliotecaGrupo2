package servicios;

import modelos.Sancion;
import modelos.materiales.PrestamoMaterial;
import modelos.usuarios.Usuario;
import modelos.usuarios.Administrador;
import modelos.usuarios.Bibliotecario;
import modelos.usuarios.Profesor;
import modelos.usuarios.Alumno;
import modelos.usuarios.Rol;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class ServicioSancion {
    private BaseDatos dbManager;

    public ServicioSancion(BaseDatos dbManager) {
        this.dbManager = dbManager;
    }

    public void aplicarSancion(Sancion sancion) {
        String sql = "INSERT INTO Sanciones (usuarioSancionadoId, prestamoAsociadoId, descripcion, fechaSancion, fechaFinSancion, activa) VALUES (?, ?, ?, ?, ?, ?);";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, sancion.getUsuarioSancionado().getId());
            if (sancion.getPrestamoAsociado() != null) {
                pstmt.setInt(2, sancion.getPrestamoAsociado().getId());
            } else {
                pstmt.setNull(2, java.sql.Types.INTEGER);
            }
            pstmt.setString(3, sancion.getDescripcion());
            pstmt.setString(4, sancion.getFechaSancion().toString());
            pstmt.setString(5, sancion.getFechaFinSancion().toString());
            pstmt.setBoolean(6, sancion.isActiva());
            pstmt.executeUpdate();
            System.out.println("Sanción aplicada exitosamente a " + sancion.getUsuarioSancionado().getNombreCompleto() + ".");
        } catch (SQLException e) {
            System.err.println("Error al aplicar sanción: " + e.getMessage());
        } finally {
            dbManager.closeConnection(conn);
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { /* ignore */ }
        }
    }

    public void aplicarSancionPorRetraso(PrestamoMaterial prestamo, int diasRetraso) {
        // Ejemplo: 1 día de sanción por cada día de retraso, mínimo 1 día.
        int diasSancion = Math.max(1, diasRetraso);
        LocalDate fechaSancion = LocalDate.now();
        LocalDate fechaFinSancion = fechaSancion.plusDays(diasSancion);
        String descripcion = "Retraso en la devolución del material: '" + prestamo.getMaterial().getTitulo() + "' por " + diasRetraso + " días.";

        Sancion nuevaSancion = new Sancion(0, prestamo.getUsuario(), prestamo, descripcion, fechaSancion, fechaFinSancion);
        aplicarSancion(nuevaSancion);
    }

    public List<Sancion> getSancionesPorUsuario(Usuario usuario) {
        List<Sancion> sanciones = new ArrayList<>();
        String sql = "SELECT id, usuarioSancionadoId, prestamoAsociadoId, descripcion, fechaSancion, fechaFinSancion, activa FROM Sanciones WHERE usuarioSancionadoId = ? ORDER BY activa DESC, fechaFinSancion DESC;";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, usuario.getId());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                sanciones.add(createSancionFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener sanciones por usuario: " + e.getMessage());
        } finally {
            dbManager.closeConnection(conn);
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { /* ignore */ }
            if (rs != null) try { rs.close(); } catch (SQLException e) { /* ignore */ }
        }
        return sanciones;
    }

    public Sancion getSancionById(int idSancion) {
        String sql = "SELECT id, usuarioSancionadoId, prestamoAsociadoId, descripcion, fechaSancion, fechaFinSancion, activa FROM Sanciones WHERE id = ?;";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Sancion sancion = null;

        try {
            conn = dbManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idSancion);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                sancion = createSancionFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener sanción por ID: " + e.getMessage());
        } finally {
            dbManager.closeConnection(conn);
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { /* ignore */ }
            if (rs != null) try { rs.close(); } catch (SQLException e) { /* ignore */ }
        }
        return sancion;
    }

    public boolean levantarSancion(int idSancion) {
        String sql = "UPDATE Sanciones SET activa = FALSE WHERE id = ?;";
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean exito = false;

        try {
            conn = dbManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idSancion);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Sanción con ID " + idSancion + " levantada exitosamente.");
                exito = true;
            } else {
                System.out.println("No se pudo levantar la sanción. ID no encontrado o ya inactiva.");
            }
        } catch (SQLException e) {
            System.err.println("Error al levantar sanción: " + e.getMessage());
        } finally {
            dbManager.closeConnection(conn);
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { /* ignore */ }
        }
        return exito;
    }

    public boolean usuarioEstaSancionado(Usuario usuario) {
        if (usuario == null) {
            return false;
        }
        String sql = "SELECT COUNT(*) FROM Sanciones WHERE usuarioSancionadoId = ? AND activa = TRUE AND fechaFinSancion >= ?;";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, usuario.getId());
            pstmt.setString(2, LocalDate.now().toString()); // Fecha actual
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar sanciones del usuario: " + e.getMessage());
        } finally {
            dbManager.closeConnection(conn);
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { /* ignore */ }
            if (rs != null) try { rs.close(); } catch (SQLException e) { /* ignore */ }
        }
        return false;
    }

    private Sancion createSancionFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int usuarioSancionadoId = rs.getInt("usuarioSancionadoId");
        int prestamoAsociadoId = rs.getInt("prestamoAsociadoId");
        String descripcion = rs.getString("descripcion");
        LocalDate fechaSancion = LocalDate.parse(rs.getString("fechaSancion"));
        LocalDate fechaFinSancion = LocalDate.parse(rs.getString("fechaFinSancion"));
        boolean activa = rs.getBoolean("activa");

        // Obtener el objeto Usuario sancionado
        Usuario usuarioSancionado = getUsuarioById(usuarioSancionadoId);
        if (usuarioSancionado == null) {
            System.err.println("Error: Usuario sancionado con ID " + usuarioSancionadoId + " no encontrado.");
            return null;
        }

        // Obtener el objeto PrestamoMaterial asociado (puede ser nulo)
        PrestamoMaterial prestamoAsociado = null;
        if (prestamoAsociadoId > 0) { // Un ID de 0 o negativo podría indicar que no hay préstamo asociado
            // Un ID de 0 o negativo podría indicar que no hay préstamo asociado
        }

        Sancion sancion = new Sancion(id, usuarioSancionado, null, descripcion, fechaSancion, fechaFinSancion);
        sancion.setActiva(activa); // Asegurar el estado de activa de la DB
        return sancion;
    }

    public Usuario getUsuarioById(int id) {
        String sql = "SELECT id, nombreUsuario, contrasena, nombreCompleto, correoElectronico, rol, carrera, codigoUniversitario, departamento FROM Usuarios WHERE id = ?;";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Usuario usuario = null;

        try {
            conn = dbManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                String nomUser = rs.getString("nombreUsuario");
                String pass = rs.getString("contrasena");
                String nomCompleto = rs.getString("nombreCompleto");
                String correo = rs.getString("correoElectronico");
                Rol rol = Rol.valueOf(rs.getString("rol"));

                switch (rol) {
                    case ADMINISTRADOR:
                        usuario = new Administrador(id, nomUser, pass, nomCompleto, correo);
                        break;
                    case BIBLIOTECARIO:
                        usuario = new Bibliotecario(id, nomUser, pass, nomCompleto, correo);
                        break;
                    case PROFESOR:
                        String departamento = rs.getString("departamento");
                        usuario = new Profesor(id, nomUser, pass, nomCompleto, correo, departamento);
                        break;
                    case ALUMNO:
                        String carrera = rs.getString("carrera");
                        String codigoUniversitario = rs.getString("codigoUniversitario");
                        usuario = new Alumno(id, nomUser, pass, nomCompleto, correo, carrera, codigoUniversitario);
                        break;
                    default:
                        System.err.println("Rol de usuario desconocido: " + rol);
                        break;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener usuario por ID para sanción: " + e.getMessage());
        } finally {
            dbManager.closeConnection(conn);
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { /* ignore */ }
            if (rs != null) try { rs.close(); } catch (SQLException e) { /* ignore */ }
        }
        return usuario;
    }
}
