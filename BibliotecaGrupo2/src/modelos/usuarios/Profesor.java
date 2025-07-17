package modelos.usuarios;

public class Profesor extends Usuario {
    private String departamento;
    public Profesor(int id, String nombreUsuario, String contrasena, String nombreCompleto, String email, String departamento) {
        super(id, nombreUsuario, contrasena, nombreCompleto, email, Rol.PROFESOR);
        this.departamento = departamento;
    }
    // Getter y Setter específico
    public String getDepartamento() {
        return departamento;
    }
    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }
    @Override
    public void mostrarInformacion() {
        System.out.println("--- Información del Profesor ---");
        System.out.println("ID: " + getId());
        System.out.println("Nombre de Usuario: " + getNombreUsuario());
        System.out.println("Nombre Completo: " + getNombreCompleto());
        System.out.println("Correo Electrónico: " + getEmail());
        System.out.println("Rol: " + getRol().name());
        System.out.println("Departamento: " + getDepartamento());
        System.out.println("--------------------------------");
    }
}