package modelos.usuarios;
import java.util.List;
public class Profesor {
    private int id;
    private String nombreUsuario;
    private String contrasena;
    private String nombreCompleto;
    private String departamento;
    private List<String> permisos;
    public Profesor(int id, String nombreUsuario, String contrasena, String nombreCompleto, String departamento) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.nombreCompleto = nombreCompleto;
        this.departamento = departamento;
    }
    public String getDepartamento() {
        return departamento;
    }
    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }
    public boolean tienePermiso(String permiso) {
        return permisos != null && permisos.contains(permiso);
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
