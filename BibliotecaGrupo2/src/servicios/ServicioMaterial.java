package servicios;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelos.materiales.Audiovisual;
import modelos.materiales.Libro;
import modelos.materiales.Material;
import modelos.materiales.Revista;
import modelos.materiales.Tesis;

public class ServicioMaterial {
    private BaseDatos dbManager;
    
    public ServicioMaterial(BaseDatos dbManager) {
        this.dbManager = dbManager;
    }
    
    public void addMaterial(Material material) {
        String sql = "INSERT INTO Materiales ("
                + "titulo, autor, anoPublicacion, genero, categoria, cantidadDisponible, cantidadTotal, "
                + "tipoMaterial, isbn, numeroPaginas, editorial, numeroEdicion, institucion, "
                + "gradoAcademico, formato, duracion) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, material.getTitulo());
            pstmt.setString(2, material.getAutor());
            pstmt.setString(3, material.getAnoPublicacion());
            pstmt.setString(4, material.getGenero());
            pstmt.setString(5, material.getCategoria().name());
            pstmt.setInt(6, material.getCantidadDisponible());
            pstmt.setInt(7, material.getCantidadTotal());

            
            if (material instanceof Libro) {
                Libro libro = (Libro) material;
                pstmt.setString(8, "LIBRO");
                pstmt.setString(9, libro.getIsbn());
                pstmt.setInt(10, libro.getNumeroPaginas());
                pstmt.setNull(11, java.sql.Types.VARCHAR);
                pstmt.setNull(12, java.sql.Types.INTEGER);
                pstmt.setNull(13, java.sql.Types.VARCHAR);
                pstmt.setNull(14, java.sql.Types.VARCHAR);
                pstmt.setNull(15, java.sql.Types.VARCHAR);
                pstmt.setNull(16, java.sql.Types.VARCHAR);
            } else if (material instanceof Revista) {
                Revista revista = (Revista) material;
                pstmt.setString(8, "REVISTA");
                pstmt.setNull(9, java.sql.Types.VARCHAR);
                pstmt.setNull(10, java.sql.Types.INTEGER);
                pstmt.setString(11, revista.getEditorial());
                pstmt.setInt(12, revista.getNumeroEdicion());
                pstmt.setNull(13, java.sql.Types.VARCHAR);
                pstmt.setNull(14, java.sql.Types.VARCHAR);
                pstmt.setNull(15, java.sql.Types.VARCHAR);
                pstmt.setNull(16, java.sql.Types.VARCHAR);
            } else if (material instanceof Tesis) {
                Tesis tesis = (Tesis) material;
                pstmt.setString(8, "TESIS");
                pstmt.setNull(9, java.sql.Types.VARCHAR);
                pstmt.setNull(10, java.sql.Types.INTEGER);
                pstmt.setNull(11, java.sql.Types.VARCHAR);
                pstmt.setNull(12, java.sql.Types.INTEGER);
                pstmt.setString(13, tesis.getInstitucion());
                pstmt.setString(14, tesis.getGradoAcademico());
                pstmt.setNull(15, java.sql.Types.VARCHAR);
                pstmt.setNull(16, java.sql.Types.VARCHAR);
            } else if (material instanceof Audiovisual) {
                Audiovisual audiovisual = (Audiovisual) material;
                pstmt.setString(8, "AUDIOVISUAL");
                pstmt.setNull(9, java.sql.Types.VARCHAR);
                pstmt.setNull(10, java.sql.Types.INTEGER);
                pstmt.setNull(11, java.sql.Types.VARCHAR);
                pstmt.setNull(12, java.sql.Types.INTEGER);
                pstmt.setNull(13, java.sql.Types.VARCHAR);
                pstmt.setNull(14, java.sql.Types.VARCHAR);
                pstmt.setString(15, audiovisual.getFormato());
                pstmt.setString(16, audiovisual.getDuracion());
            } else {
                pstmt.setString(8, "MATERIAL"); // Tipo genérico si no coincide con ninguno
                pstmt.setNull(9, java.sql.Types.VARCHAR);
                pstmt.setNull(10, java.sql.Types.INTEGER);
                pstmt.setNull(11, java.sql.Types.VARCHAR);
                pstmt.setNull(12, java.sql.Types.INTEGER);
                pstmt.setNull(13, java.sql.Types.VARCHAR);
                pstmt.setNull(14, java.sql.Types.VARCHAR);
                pstmt.setNull(15, java.sql.Types.VARCHAR);
                pstmt.setNull(16, java.sql.Types.VARCHAR);
            }

            pstmt.executeUpdate();
            System.out.println("Material agregado exitosamente: " + material.getTitulo());
        } catch (SQLException e) {
            System.err.println("Error al agregar material: " + e.getMessage());
        } finally {
            dbManager.closeConnection(conn);
            if (pstmt != null) {
                try { pstmt.close();
                } catch (SQLException e) { 
                        /* Se captura la excepcion pero no hace nada */ 
                }
            }
        }
    }
    
    public List<Material> getAllMateriales() {
        String sql = "SELECT * FROM Materiales;";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Material> materiales = new ArrayList<>();

        try {
            conn = dbManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                materiales.add(createMaterialFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los materiales: " + e.getMessage());
        } finally {
            dbManager.closeConnection(conn);
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { /* ignore */ }
            if (rs != null) try { rs.close(); } catch (SQLException e) { /* ignore */ }
        }
        return materiales;
    }
    
    public Material getMaterialById(int id) {
        String sql = "SELECT * FROM Materiales WHERE id = ?;";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Material material = null;

        try {
            conn = dbManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                material = createMaterialFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener material por ID: " + e.getMessage());
        } finally {
            dbManager.closeConnection(conn);
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { /* ignore */ }
            if (rs != null) try { rs.close(); } catch (SQLException e) { /* ignore */ }
        }
        return material;
    }
    
    public List<Material> buscarMateriales(String query, String tipoBusqueda) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Material> materiales = new ArrayList<>();
        
        String sql = "SELECT * FROM Materiales WHERE ";
        switch (tipoBusqueda.toLowerCase()) {
            case "titulo":
                sql += "titulo LIKE ?";
                break;
            case "autor":
                sql += "autor LIKE ?";
                break;
            case "genero":
                sql += "genero LIKE ?";
                break;
            case "categoria":
                sql += "categoria LIKE ?";
                break;
            default:
                System.err.println("Tipo de búsqueda inválido: " + tipoBusqueda);
                return new ArrayList<>();
        }
        sql += ";";

        try {
            conn = dbManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + query + "%");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                materiales.add(createMaterialFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar materiales: " + e.getMessage());
        } finally {
            dbManager.closeConnection(conn);
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { /* ignore */ }
            if (rs != null) try { rs.close(); } catch (SQLException e) { /* ignore */ }
        }
        return materiales;
    }
    
    public Material createMaterialFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String titulo = rs.getString("titulo");
        String autor = rs.getString("autor");
        String anoPublicacion = rs.getString("anoPublicacion");
        String genero = rs.getString("genero");
        String categoria = rs.getString("categoria");
        int cantidadDisponible = rs.getInt("cantidadDisponible");
        int cantidadTotal = rs.getInt("cantidadTotal");
        String tipoMaterial = rs.getString("tipoMaterial");

        Material material = null;
        switch (tipoMaterial) {
            case "LIBRO":
                String isbn = rs.getString("isbn");
                int numeroPaginas = rs.getInt("numeroPaginas");
                material = new Libro(id, titulo, autor, anoPublicacion, genero, categoria, cantidadTotal, isbn, numeroPaginas);
                break;
            case "REVISTA":
                String editorial = rs.getString("editorial");
                int numeroEdicion = rs.getInt("numeroEdicion");
                material = new Revista(id, titulo, autor, anoPublicacion, genero, categoria, cantidadTotal, editorial, numeroEdicion);
                break;
            case "TESIS":
                String institucion = rs.getString("institucion");
                String gradoAcademico = rs.getString("gradoAcademico");
                material = new Tesis(id, titulo, autor, anoPublicacion, genero, categoria, cantidadTotal, institucion, gradoAcademico);
                break;
            case "AUDIOVISUAL":
                String formato = rs.getString("formato");
                String duracion = rs.getString("duracion");
                material = new Audiovisual(id, titulo, autor, anoPublicacion, genero, categoria, cantidadTotal, formato, duracion);
                break;
            default:
                System.err.println("Tipo de material desconocido: " + tipoMaterial);
                break;
        }
        if (material != null) {
            material.setCantidadDisponible(cantidadDisponible); 
        }
        return material;
    }
    
    public void updateMaterial(Material material) {
        String sql = "UPDATE Materiales SET titulo = ?, autor = ?, anoPublicacion = ?, "
                + "genero = ?, categoria = ?, cantidadDisponible = ?, cantidadTotal = ? WHERE id = ?;";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, material.getTitulo());
            pstmt.setString(2, material.getAutor());
            pstmt.setString(3, material.getAnoPublicacion());
            pstmt.setString(4, material.getGenero());
            pstmt.setString(5, material.getCategoria().name());
            pstmt.setInt(6, material.getCantidadDisponible());
            pstmt.setInt(7, material.getCantidadTotal());
            pstmt.setInt(8, material.getId());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Material actualizado exitosamente: " + material.getTitulo());
            } else {
                System.out.println("No se pudo actualizar el material. ID no encontrado.");
            }
        } catch (SQLException e) {
            System.err.println("Error al actualizar material: " + e.getMessage());
        } finally {
            dbManager.closeConnection(conn);
            if (pstmt != null) {
                try { 
                    pstmt.close(); 
                } catch (SQLException e) {
                    /* Solo captura la excepcion */ 
                }
            }
        }
    }
    
    public void deleteMaterial(int id) {
        String sql = "DELETE FROM Materiales WHERE id = ?;";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Material con ID " + id + " eliminado exitosamente.");
            } else {
                System.out.println("No se pudo eliminar el material. ID no encontrado.");
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar material: " + e.getMessage());
        } finally {
            dbManager.closeConnection(conn);
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { /* ignore */ }
        }
    }
}
