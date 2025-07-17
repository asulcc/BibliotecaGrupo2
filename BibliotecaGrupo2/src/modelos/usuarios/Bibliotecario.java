package modelos.usuarios;
import java.util.List;
public class Bibliotecario extends Usuario {
    private List<String> permisos;
    public Bibliotecario(String idUsuario, String nombreUsuario, String contrasena,
                    String nombreCompleto, String email, List<String> rol, List<String> permisos) {
        super(idUsuario, nombreUsuario, contrasena, nombreCompleto, email, rol);
        this.permisos = permisos;
    }
    public String getIdUsuario() {
        return idUsuario;
    }
    public String getNombreUsuario() {
        return nombreUsuario;
    }
    public String getContrasena() {
        return contrasena;
    }
    public String getNombreCompleto() {
        return nombreCompleto;
    }
    public String getEmail() {
        return email;
    }
    public List<String> getRol() {
        return rol;
    }
    public boolean isActivo() {
        return activo;
    }
    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setRol(List<String> rol) {
        this.rol = rol;
    }
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    public boolean tienePermiso(String permiso) {
        return permisos != null && permisos.contains(permiso);
    }
    public boolean autenticar(String contrasena) {
        return this.contrasena != null && this.contrasena.equals(contrasena);
    }
    public List<String> getPermisos() {
        return permisos;
    }
    public void setPermisos(List<String> permisos) {
        this.permisos = permisos;
    }
}