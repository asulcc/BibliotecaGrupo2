package modelos.usuarios;
import java.util.List;
public class Alumno {
    private int id;
    private String nombreUsuario;
    private String contrasena;
    private String nombreCompleto;
    private String carrera;
    private List<String> permisos;
    public Alumno(int id, String nombreUsuario, String contrasena, String nombreCompleto, String carrera) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.nombreCompleto = nombreCompleto;
        this.carrera = carrera;
    }
    public String getCarrera() {
        return carrera;
    }
    public void setCarrera(String carrera) {
        this.carrera = carrera;
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
