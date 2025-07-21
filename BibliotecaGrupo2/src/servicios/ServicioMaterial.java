package servicios;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelos.materiales.Audiovisual;
import modelos.materiales.CategoriaMaterial;
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
                + "titulo, autor, anioPublicacion, descripcion, idioma, categoria, "
                + "cantidadDisponible, cantidadTotal, tipoMaterial, editorial, isbn, "
                + "numeroPaginas, genero, productora, duracion, formato, universidad, "
                + "grado, palabrasClave, volumen, numero, fechaPublicacion) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ///
    // getNextId

        try {
            conn = dbManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, material.getTitulo());
            pstmt.setString(2, material.getAutor());
            pstmt.setInt(3, material.getAnioPublicacion());
            pstmt.setString(4, material.getDescripcion());
            pstmt.setString(5, material.getIdioma());
            pstmt.setString(6, material.getCategoria().name());
            pstmt.setInt(7, material.getCantidadDisponible());
            pstmt.setInt(8, material.getCantidadTotal());
//            pstmt.setString(9, material.getTipoMaterial());

            
            if (material instanceof Libro) {
                Libro libro = (Libro) material;
                pstmt.setString(9, "LIBRO");
                pstmt.setString(10, libro.getEditorial());
                pstmt.setString(11, libro.getIsbn());
                pstmt.setInt(12, libro.getNumeroPaginas());
                pstmt.setString(13, libro.getGenero());
                pstmt.setNull(14, java.sql.Types.VARCHAR);
                pstmt.setNull(15, java.sql.Types.INTEGER);
                pstmt.setNull(16, java.sql.Types.VARCHAR);
                pstmt.setNull(17, java.sql.Types.VARCHAR);
                pstmt.setNull(18, java.sql.Types.VARCHAR);
                pstmt.setNull(19, java.sql.Types.VARCHAR);
                pstmt.setNull(20, java.sql.Types.INTEGER);
                pstmt.setNull(21, java.sql.Types.INTEGER);
                pstmt.setNull(22, java.sql.Types.VARCHAR);
            } else if (material instanceof Revista) {
                Revista revista = (Revista) material;
                pstmt.setString(9, "REVISTA");
                pstmt.setNull(10, java.sql.Types.VARCHAR);
                pstmt.setNull(11, java.sql.Types.VARCHAR);
                pstmt.setNull(12, java.sql.Types.INTEGER);
                pstmt.setNull(13, java.sql.Types.VARCHAR);
                pstmt.setNull(14, java.sql.Types.VARCHAR);
                pstmt.setNull(15, java.sql.Types.INTEGER);
                pstmt.setNull(16, java.sql.Types.VARCHAR);
                pstmt.setNull(17, java.sql.Types.VARCHAR);
                pstmt.setNull(18, java.sql.Types.VARCHAR);
                pstmt.setNull(19, java.sql.Types.VARCHAR);
                pstmt.setInt(20, revista.getVolumen());
                pstmt.setInt(21, revista.getNumeroEdicion());
                pstmt.setString(22, revista.getFechaPublicacion());
            } else if (material instanceof Tesis) {
                Tesis tesis = (Tesis) material;
                pstmt.setString(9, "TESIS");
                pstmt.setNull(10, java.sql.Types.VARCHAR);
                pstmt.setNull(11, java.sql.Types.VARCHAR);
                pstmt.setNull(12, java.sql.Types.INTEGER);
                pstmt.setNull(13, java.sql.Types.VARCHAR);
                pstmt.setNull(14, java.sql.Types.VARCHAR);
                pstmt.setNull(15, java.sql.Types.INTEGER);
                pstmt.setNull(16, java.sql.Types.VARCHAR);
                pstmt.setString(17, tesis.getUniversidad());
                pstmt.setString(18, tesis.getGradoAcademico());
                pstmt.setString(19, tesis.getPalabrasClave());
                pstmt.setNull(20, java.sql.Types.INTEGER);
                pstmt.setNull(21, java.sql.Types.INTEGER);
                pstmt.setNull(22, java.sql.Types.VARCHAR);
            } else if (material instanceof Audiovisual) {
                Audiovisual audiovisual = (Audiovisual) material;
                pstmt.setString(9, "AUDIOVISUAL");
                pstmt.setNull(10, java.sql.Types.VARCHAR);
                pstmt.setNull(11, java.sql.Types.VARCHAR);
                pstmt.setNull(12, java.sql.Types.INTEGER);
                pstmt.setString(13, audiovisual.getGenero());
                pstmt.setString(14, audiovisual.getProductora());
                pstmt.setInt(15, audiovisual.getDuracion());
                pstmt.setString(16, audiovisual.getFormato());
                pstmt.setNull(17, java.sql.Types.VARCHAR);
                pstmt.setNull(18, java.sql.Types.VARCHAR);
                pstmt.setNull(19, java.sql.Types.VARCHAR);
                pstmt.setNull(20, java.sql.Types.INTEGER);
                pstmt.setNull(21, java.sql.Types.INTEGER);
                pstmt.setNull(22, java.sql.Types.VARCHAR);
            } else {
                pstmt.setString(9, "MATERIAL"); // Tipo genérico si no coincide con ninguno
                pstmt.setNull(10, java.sql.Types.VARCHAR);
                pstmt.setNull(11, java.sql.Types.VARCHAR);
                pstmt.setNull(12, java.sql.Types.INTEGER);
                pstmt.setNull(13, java.sql.Types.VARCHAR);
                pstmt.setNull(14, java.sql.Types.VARCHAR);
                pstmt.setNull(15, java.sql.Types.INTEGER);
                pstmt.setNull(16, java.sql.Types.VARCHAR);
                pstmt.setNull(17, java.sql.Types.VARCHAR);
                pstmt.setNull(18, java.sql.Types.VARCHAR);
                pstmt.setNull(19, java.sql.Types.VARCHAR);
                pstmt.setNull(20, java.sql.Types.INTEGER);
                pstmt.setNull(21, java.sql.Types.INTEGER);
                pstmt.setNull(22, java.sql.Types.VARCHAR);
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
        int anioPublicacion = rs.getInt("anioPublicacion");
        String descripcion = rs.getString("descripcion");
        String idioma = rs.getString("idioma");
        String categoriaStr = rs.getString("categoria");
        CategoriaMaterial categoria = CategoriaMaterial.valueOf(categoriaStr.toUpperCase());
        int cantidadDisponible = rs.getInt("cantidadDisponible");
        int cantidadTotal = rs.getInt("cantidadTotal");
        String tipoMaterial = rs.getString("tipoMaterial");
        String genero;

        Material material = null;
        switch (tipoMaterial) {
            case "LIBRO":
                String editorial = rs.getString("editorial");
                String isbn = rs.getString("isbn");
                int numeroPaginas = rs.getInt("numeroPaginas");
                genero = rs.getString("genero");
                material = new Libro(id, titulo, autor, anioPublicacion, descripcion, idioma, categoria, cantidadTotal,
                        tipoMaterial, editorial, isbn, numeroPaginas, genero);
                break;
            case "REVISTA":
                int volumen = rs.getInt("volumen");
                int numero = rs.getInt("numero");
                String fechaPublicacion = rs.getString("fechaPublicacion");
                material = new Revista(id, titulo, autor, anioPublicacion, descripcion, idioma, categoria, cantidadTotal,
                        tipoMaterial, volumen, numero, fechaPublicacion);
                break;
            case "TESIS":
                String universidad = rs.getString("universidad");
                String gradoAcademico = rs.getString("gradoAcademico");
                String palabrasClave = rs.getString("palabrasClave");
                material = new Tesis(id, titulo, autor, anioPublicacion, descripcion, idioma, categoria, cantidadTotal,
                        tipoMaterial, universidad, gradoAcademico, palabrasClave);
                break;
            case "AUDIOVISUAL":
                genero = rs.getString("genero");
                String productora = rs.getString("productora");
                String formato = rs.getString("formato");
                int duracion = rs.getInt("duracion");
                material = new Audiovisual(id, titulo, autor, anioPublicacion, descripcion, idioma, categoria, cantidadTotal,
                        tipoMaterial, genero, productora, formato, duracion);
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
        String sql = "UPDATE Materiales SET titulo = ?, autor = ?, anioPublicacion = ?, descripcion = ?, "
                + "idioma = ?, categoria = ?, cantidadDisponible = ?, cantidadTotal = ?, tipoMaterial = ?, "
                + "editorial = ?, isbn = ?, numeroPaginas = ?, genero = ?, productora = ?, duracion = ?, "
                + "formato = ?, universidad = ?, grado = ?, palabrasClave = ?, volumen = ?, "
                + "numero = ?, fechaPublicacion = ? WHERE id = ?;";
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = dbManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, material.getTitulo());
            pstmt.setString(2, material.getAutor());
            pstmt.setInt(3, material.getAnioPublicacion());
            pstmt.setString(4, material.getDescripcion());
            pstmt.setString(5, material.getIdioma());
            pstmt.setString(6, material.getCategoria().name());
            pstmt.setInt(7, material.getCantidadDisponible());
            pstmt.setInt(8, material.getCantidadTotal());
            pstmt.setString(9, material.getTipoMaterial());
            
            String tipoMaterial = material.getTipoMaterial();
            String editorial = null;
            String isbn = null;
            int numeroPaginas = 0;
            String genero = null;
            String productora = null;
            int duracion = 0;
            String formato = null;
            String universidad = null;
            String grado = null;
            String palabrasClave = null;
            int volumen = 0;
            int numero = 0;
            String fechaPublicacion = null;
            
            switch (tipoMaterial) {
                case "LIBRO":
                    Libro libro = (Libro) material;
                    editorial = libro.getEditorial();
                    isbn = libro.getIsbn();
                    numeroPaginas = libro.getNumeroPaginas();
                    genero = libro.getGenero();
                    break;
                case "REVISTA":
                    Revista revista = (Revista) material;
                    volumen = revista.getVolumen();
                    numero = revista.getNumeroEdicion();
                    fechaPublicacion = revista.getFechaPublicacion();
                    break;
                case "TESIS":
                    Tesis tesis = (Tesis) material;
                    universidad = tesis.getUniversidad();
                    grado = tesis.getGradoAcademico();
                    palabrasClave = tesis.getPalabrasClave();
                    break;
                case "AUDIOVISUAL":
                    Audiovisual audiovisual = (Audiovisual) material;
                    genero = audiovisual.getGenero();
                    productora = audiovisual.getProductora();
                    formato = audiovisual.getFormato();
                    duracion = audiovisual.getDuracion();
                    break;
                default:
                    throw new IllegalArgumentException("Tipo de material no soportado.");
            }
            
            pstmt.setObject(10, editorial);
            pstmt.setObject(11, isbn);
            pstmt.setObject(12, numeroPaginas);
            pstmt.setObject(13, genero);
            pstmt.setObject(14, productora);
            pstmt.setObject(15, duracion);
            pstmt.setObject(16, formato);
            pstmt.setObject(17, universidad);
            pstmt.setObject(18, grado);
            pstmt.setObject(19, palabrasClave);
            pstmt.setObject(20, volumen);
            pstmt.setObject(21, numero);
            pstmt.setObject(22, fechaPublicacion);
            pstmt.setInt(23, material.getId());
            
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
