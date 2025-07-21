package servicios;

import modelos.usuarios.Usuario;
import modelos.usuarios.Rol;
import modelos.usuarios.Administrador;
import modelos.usuarios.Bibliotecario;
import modelos.usuarios.Profesor;
import modelos.usuarios.Alumno;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ServicioAutenticacion<T extends Usuario> {
    private final BaseDatos dbManager;

    public ServicioAutenticacion(BaseDatos dbManager) {
        this.dbManager = dbManager;
    }

    public T login(String nombreUsuario, String contrasena) {
        String sql = "SELECT id, nombreUsuario, contrasena, nombreCompleto, correoElectronico, rol, carrera, codigoUniversitario, departamento FROM Usuarios WHERE nombreUsuario = ? AND contrasena = ?;";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        T usuario = null;

        try {
            conn = dbManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nombreUsuario);
            pstmt.setString(2, contrasena); // En un sistema real, se usaría hashing de contraseñas
            rs = pstmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String nomUser = rs.getString("nombreUsuario");
                String pass = rs.getString("contrasena");
                String nomCompleto = rs.getString("nombreCompleto");
                String correo = rs.getString("correoElectronico");
                Rol rol = Rol.valueOf(rs.getString("rol"));

                switch (rol) {
                    case ADMINISTRADOR:
                        usuario = (T) new Administrador(id, nomUser, pass, nomCompleto, correo);
                        break;
                    case BIBLIOTECARIO:
                        usuario = (T) new Bibliotecario(id, nomUser, pass, nomCompleto, correo);
                        break;
                    case PROFESOR:
                        String departamento = rs.getString("departamento");
                        usuario = (T) new Profesor(id, nomUser, pass, nomCompleto, correo, departamento);
                        break;
                    case ALUMNO:
                        String carrera = rs.getString("carrera");
                        String codigoUniversitario = rs.getString("codigoUniversitario");
                        usuario = (T) new Alumno(id, nomUser, pass, nomCompleto, correo, carrera, codigoUniversitario);
                        break;
                    default:
                        System.err.println("Rol de usuario desconocido: " + rol);
                        break;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al intentar login: " + e.getMessage());
        } finally {
            dbManager.closeConnection(conn);
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { /* ignore */ }
            if (rs != null) try { rs.close(); } catch (SQLException e) { /* ignore */ }
        }
        return usuario;
    }

    public boolean logout() {
        System.out.println("Sesión cerrada exitosamente.");
        return true;
    }

    public boolean cambiarContrasena(Usuario usuario, String nuevaContrasena) {
        String sql = "UPDATE Usuarios SET contrasena = ? WHERE id = ?;";
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean exito = false;

        try {
            conn = dbManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nuevaContrasena);
            pstmt.setInt(2, usuario.getId());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                usuario.setContrasena(nuevaContrasena); // Actualizar el objeto en memoria
                exito = true;
                System.out.println("Contraseña cambiada exitosamente para " + usuario.getNombreUsuario());
            } else {
                System.out.println("No se pudo cambiar la contraseña. Usuario no encontrado o sin cambios.");
            }
        } catch (SQLException e) {
            System.err.println("Error al cambiar contraseña: " + e.getMessage());
        } finally {
            dbManager.closeConnection(conn);
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { /* ignore */ }
        }
        return exito;
    }

    public boolean tienePermiso(T usuario, Rol rolRequerido) {
        if (usuario == null) {
            return false;
        }
        // Un administrador siempre tiene todos los permisos
        if (usuario.getRol() == Rol.ADMINISTRADOR) {
            return true;
        }
        // Un bibliotecario tiene los permisos de bibliotecario
        if (usuario.getRol() == Rol.BIBLIOTECARIO && (rolRequerido == Rol.BIBLIOTECARIO || rolRequerido == Rol.ALUMNO || rolRequerido == Rol.PROFESOR)) {
            return true;
        }
        // Otros roles deben coincidir exactamente
        return usuario.getRol() == rolRequerido;
    }
}
