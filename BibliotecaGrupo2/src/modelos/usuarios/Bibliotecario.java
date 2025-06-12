package modelos.usuarios;
import java.util.List;
public class Bibliotecario extends Usuario {
    private List<String> permisos;
    public Bibliotecario(String idUsuario, String nombreUsuario, String contrasena,
                    String nombreCompleto, String email, List<String> rol, List<String> permisos) {
        super(idUsuario, nombreUsuario, contrasena, nombreCompleto, email, rol);
        this.permisos = permisos;
    }
    @Override
    public String getIdUsuario() {
        return idUsuario;
    }
    @Override
    public String getNombreUsuario() {
        return nombreUsuario;
    }
    @Override
    public String getContrasena() {
        return contrasena;
    }
    @Override
    public String getNombreCompleto() {
        return nombreCompleto;
    }
    @Override
    public String getEmail() {
        return email;
    }
    @Override
    public List<String> getRol() {
        return rol;
    }
    @Override
    public boolean isActivo() {
        return activo;
    }
    @Override
    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }
    @Override
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
    @Override
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
    @Override
    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }
    @Override
    public void setEmail(String email) {
        this.email = email;
    }
    @Override
    public void setRol(List<String> rol) {
        this.rol = rol;
    }
    @Override
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