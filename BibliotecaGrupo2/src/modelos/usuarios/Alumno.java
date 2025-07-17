package modelos.usuarios;
public class Alumno extends Usuario {
    private String carrera;
    private String codigoUniversitario;
    public Alumno(int id, String nombreUsuario, String contrasena, String nombreCompleto, String email, String carrera, String codigoUniversitario) {
        super(id, nombreUsuario, contrasena, nombreCompleto, email, Rol.ALUMNO);
        this.carrera = carrera;
        this.codigoUniversitario = codigoUniversitario;
    }
    // Getters y Setters específicos
    public String getCarrera() {
        return carrera;
    }
    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }
    public String getCodigoUniversitario() {
        return codigoUniversitario;
    }
    public void setCodigoUniversitario(String codigoUniversitario) {
        this.codigoUniversitario = codigoUniversitario;
    }
    @Override
    public void mostrarInformacion() {
        System.out.println("--- Información del Alumno ---");
        System.out.println("ID: " + getId());
        System.out.println("Nombre de Usuario: " + getNombreUsuario());
        System.out.println("Nombre Completo: " + getNombreCompleto());
        System.out.println("Correo Electrónico: " + getEmail());
        System.out.println("Rol: " + getRol().name());
        System.out.println("Carrera: " + getCarrera());
        System.out.println("Código Universitario: " + getCodigoUniversitario());
        System.out.println("------------------------------");
    }
}