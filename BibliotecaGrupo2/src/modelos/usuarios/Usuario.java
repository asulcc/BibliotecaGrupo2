package modelos.usuarios;
import java.util.List;
public abstract class Usuario {
    protected String idUsuario;
    protected String nombreUsuario;
    protected String contrasena;
    protected String nombreCompleto;
    protected String email;
    protected List<String> rol;  
    protected boolean activo;
    public Usuario(String idUsuario, String nombreUsuario, String contrasena, 
                   String nombreCompleto, String email, List<String> rol) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.nombreCompleto = nombreCompleto;
        this.email = email;
        this.rol = rol;
        this.activo = true; 
    }
    public abstract String getIdUsuario();
    public abstract String getNombreUsuario();
    public abstract String getContrasena();
    public abstract String getNombreCompleto();
    public abstract String getEmail();
    public abstract List<String> getRol();
    public abstract boolean isActivo();
    public abstract void setIdUsuario(String idUsuario);
    public abstract void setNombreUsuario(String nombreUsuario);
    public abstract void setContrasena(String contrasena);
    public abstract void setNombreCompleto(String nombreCompleto);
    public abstract void setEmail(String email);
    public abstract void setRol(List<String> rol);
    public abstract void setActivo(boolean activo);
}
