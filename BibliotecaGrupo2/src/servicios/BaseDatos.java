package servicios;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BaseDatos {
    private static final String DB_URL = "jdbc:sqlite:./db/biblioteca.db";
    
    public BaseDatos() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("Error: Driver JDBC de SQLite no encontrado. " + e.getMessage());
            throw new RuntimeException("No se pudo cargar el driver de la base de datos.", e);
        }
    }
    
    public Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DB_URL);
            
        } catch (SQLException e) {
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
            throw new RuntimeException("No se pudo establecer la conexión a la base de datos.", e);
        }
        return connection;
    }
    
    public void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                // System.out.println("Conexión a la base de datos cerrada."); // Para depuración
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión a la base de datos: " + e.getMessage());
            }
        }
    }
    
    // Método para obtener el siguiente ID disponible en la base de datos.
    public int getNextId(String tableName) {
        String sql = "SELECT MAX(id) FROM " + tableName + ";";
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1) + 1;
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el siguiente ID para la tabla " + tableName + ": " + e.getMessage());
        }
        return 1; 
    }
}
