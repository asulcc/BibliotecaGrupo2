package servicios;

import modelos.recursos.Recurso;
import modelos.recursos.PC;
import modelos.recursos.Tableta;
import modelos.recursos.SalaEstudio;
import modelos.recursos.Recurso.TipoRecurso;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServicioRecurso {
    private BaseDatos dbManager;

    public ServicioRecurso(BaseDatos dbManager) {
        this.dbManager = dbManager;
    }

    public void addRecurso(Recurso recurso) {
        String sql = "INSERT INTO Recursos (codigo, ubicacion, disponible, tipoRecurso, "
                + "sistemaOperativo, especificaciones, marca, modelo, capacidadMaxima) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, recurso.getCodigo());
            pstmt.setString(2, recurso.getUbicacion());
            pstmt.setBoolean(3, recurso.isDisponible());

            // Determinar el tipo de recurso y setear los campos específicos
            if (recurso instanceof PC) {
                PC pc = (PC) recurso;
                pstmt.setString(4, "PC");
                pstmt.setString(5, pc.getSistemaOperativo());
                pstmt.setString(6, pc.getEspecificaciones());
                pstmt.setNull(7, java.sql.Types.VARCHAR); // marca
                pstmt.setNull(8, java.sql.Types.VARCHAR); // modelo
                pstmt.setNull(9, java.sql.Types.INTEGER); // capacidadMaxima
            } else if (recurso instanceof Tableta) {
                Tableta tableta = (Tableta) recurso;
                pstmt.setString(4, "TABLETA");
                pstmt.setNull(5, java.sql.Types.VARCHAR);
                pstmt.setNull(6, java.sql.Types.VARCHAR);
                pstmt.setString(7, tableta.getMarca());
                pstmt.setString(8, tableta.getModelo());
                pstmt.setNull(9, java.sql.Types.INTEGER);
            } else if (recurso instanceof SalaEstudio) {
                SalaEstudio sala = (SalaEstudio) recurso;
                pstmt.setString(4, "SALA_ESTUDIO");
                pstmt.setNull(5, java.sql.Types.VARCHAR);
                pstmt.setNull(6, java.sql.Types.VARCHAR);
                pstmt.setNull(7, java.sql.Types.VARCHAR);
                pstmt.setNull(8, java.sql.Types.VARCHAR);
                pstmt.setInt(9, sala.getCapacidadMaxima());
            } else {
                pstmt.setString(4, "RECURSO"); // Tipo genérico si no coincide con ninguno
                pstmt.setNull(5, java.sql.Types.VARCHAR);
                pstmt.setNull(6, java.sql.Types.VARCHAR);
                pstmt.setNull(7, java.sql.Types.VARCHAR);
                pstmt.setNull(8, java.sql.Types.VARCHAR);
                pstmt.setNull(9, java.sql.Types.INTEGER);
            }

            pstmt.executeUpdate();
            System.out.println("Recurso agregado exitosamente: " + recurso.getCodigo());
        } catch (SQLException e) {
            System.err.println("Error al agregar recurso: " + e.getMessage());
        } finally {
            dbManager.closeConnection(conn);
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { /* ignore */ }
        }
    }

    public Recurso getRecursoById(int id) {
        String sql = "SELECT * FROM Recursos WHERE id = ?;";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Recurso recurso = null;

        try {
            conn = dbManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                recurso = createRecursoFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener recurso por ID: " + e.getMessage());
        } finally {
            dbManager.closeConnection(conn);
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { /* ignore */ }
            if (rs != null) try { rs.close(); } catch (SQLException e) { /* ignore */ }
        }
        return recurso;
    }

    public List<Recurso> getAllRecursos() {
        String sql = "SELECT * FROM Recursos;";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Recurso> recursos = new ArrayList<>();

        try {
            conn = dbManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                recursos.add(createRecursoFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los recursos: " + e.getMessage());
        } finally {
            dbManager.closeConnection(conn);
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { /* ignore */ }
            if (rs != null) try { rs.close(); } catch (SQLException e) { /* ignore */ }
        }
        return recursos;
    }

    public List<Recurso> getRecursosDisponibles(String tipoRecurso) {
        String sql = "SELECT * FROM Recursos WHERE disponible = TRUE AND tipoRecurso = ?;";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Recurso> recursos = new ArrayList<>();

        try {
            conn = dbManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, tipoRecurso.toUpperCase());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                recursos.add(createRecursoFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener recursos disponibles por tipo: " + e.getMessage());
        } finally {
            dbManager.closeConnection(conn);
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { /* ignore */ }
            if (rs != null) try { rs.close(); } catch (SQLException e) { /* ignore */ }
        }
        return recursos;
    }

    public void updateRecurso(Recurso recurso) {
        String sql = "UPDATE Recursos SET codigo = ?, ubicacion = ?, disponible = ?, "
                + "sistemaOperativo = ?, especificaciones = ?, marca = ?, modelo = ?, "
                + "capacidadMaxima = ? WHERE id = ?;";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, recurso.getCodigo());
            pstmt.setString(2, recurso.getUbicacion());
            pstmt.setBoolean(3, recurso.isDisponible());

            // Setear los campos específicos según el tipo de recurso
            if (recurso instanceof PC) {
                PC pc = (PC) recurso;
                pstmt.setString(4, pc.getSistemaOperativo());
                pstmt.setString(5, pc.getEspecificaciones());
                pstmt.setNull(6, java.sql.Types.VARCHAR);
                pstmt.setNull(7, java.sql.Types.VARCHAR);
                pstmt.setNull(8, java.sql.Types.INTEGER);
            } else if (recurso instanceof Tableta) {
                Tableta tableta = (Tableta) recurso;
                pstmt.setNull(4, java.sql.Types.VARCHAR);
                pstmt.setNull(5, java.sql.Types.VARCHAR);
                pstmt.setString(6, tableta.getMarca());
                pstmt.setString(7, tableta.getModelo());
                pstmt.setNull(8, java.sql.Types.INTEGER);
            } else if (recurso instanceof SalaEstudio) {
                SalaEstudio sala = (SalaEstudio) recurso;
                pstmt.setNull(4, java.sql.Types.VARCHAR);
                pstmt.setNull(5, java.sql.Types.VARCHAR);
                pstmt.setNull(6, java.sql.Types.VARCHAR);
                pstmt.setNull(7, java.sql.Types.VARCHAR);
                pstmt.setInt(8, sala.getCapacidadMaxima());
            } else {
                pstmt.setNull(4, java.sql.Types.VARCHAR);
                pstmt.setNull(5, java.sql.Types.VARCHAR);
                pstmt.setNull(6, java.sql.Types.VARCHAR);
                pstmt.setNull(7, java.sql.Types.VARCHAR);
                pstmt.setNull(8, java.sql.Types.INTEGER);
            }
            pstmt.setInt(9, recurso.getId());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Recurso actualizado exitosamente: " + recurso.getCodigo());
            } else {
                System.out.println("No se pudo actualizar el recurso. ID no encontrado.");
            }
        } catch (SQLException e) {
            System.err.println("Error al actualizar recurso: " + e.getMessage());
        } finally {
            dbManager.closeConnection(conn);
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { /* ignore */ }
        }
    }

    public void deleteRecurso(int id) {
        String sql = "DELETE FROM Recursos WHERE id = ?;";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Recurso con ID " + id + " eliminado exitosamente.");
            } else {
                System.out.println("No se pudo eliminar el recurso. ID no encontrado.");
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar recurso: " + e.getMessage());
        } finally {
            dbManager.closeConnection(conn);
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { /* ignore */ }
        }
    }

    private Recurso createRecursoFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String codigo = rs.getString("codigo");
        String ubicacion = rs.getString("ubicacion");
        boolean disponible = rs.getBoolean("disponible");
        TipoRecurso tipoRecurso = TipoRecurso.valueOf(rs.getString("tipoRecurso"));

        Recurso recurso = null;
        switch (tipoRecurso) {
            case PC:
                String sistemaOperativo = rs.getString("sistemaOperativo");
                String especificaciones = rs.getString("especificaciones");
                recurso = new PC(id, codigo, ubicacion, tipoRecurso, sistemaOperativo, especificaciones);
                break;
            case TABLETA:
                String marca = rs.getString("marca");
                String modelo = rs.getString("modelo");
                recurso = new Tableta(id, codigo, ubicacion, tipoRecurso, marca, modelo);
                break;
            case SALAESTUDIO:
                int capacidadMaxima = rs.getInt("capacidadMaxima");
                recurso = new SalaEstudio(id, codigo, ubicacion, tipoRecurso, capacidadMaxima);
                break;
            default:
                System.err.println("Tipo de recurso desconocido: " + tipoRecurso);
                break;
        }
        if (recurso != null) {
            recurso.setDisponible(disponible); // Asegurar que la disponibilidad se carga correctamente
        }
        return recurso;
    }
}
