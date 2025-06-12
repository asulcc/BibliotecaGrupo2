package modelos.usuarios;
import java.util.List;
public class Administrador {
    private int id;
    private String nombreUsuario;
    private String contrasena;
    private String nombreCompleto;
    private List<String> permisos;
    public Administrador(int id, String nombreUsuario, String contrasena, String nombreCompleto) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.nombreCompleto = nombreCompleto;
    }
    public boolean tienePermiso(String permiso) {
        return permisos != null && permisos.contains(permiso);
    }
    public void gestionarUsuarios() {
        System.out.println("Gestionando usuarios...");
    }
    public void mostrarInformacion() {
        System.out.println("ID: " + id);
        System.out.println("Usuario: " + nombreUsuario);
        System.out.println("Nombre completo: " + nombreCompleto);
    }
    public boolean autenticar(String contrasena) {
        return this.contrasena.equals(contrasena);
    }
    public int getId() {
        return id;
    }
    public String getNombreUsuario() {
        return nombreUsuario;
    }
    public String getNombreCompleto() {
        return nombreCompleto;
    }
    public void setPermisos(List<String> permisos) {
        this.permisos = permisos;
    }
    public List<String> getPermisos() {
        return permisos;
    }
}
