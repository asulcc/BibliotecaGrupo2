package modelos.usuarios;

public class Administrador extends Usuario{
    
    public Administrador(int id, String nombreUsuario, String contrasena, String nombreCompleto, String email) {
        super(id, nombreUsuario, contrasena, nombreCompleto, email, Rol.ADMINISTRADOR);
    }

    // Métodos específicos del Administrador
    public void gestionarUsuarios() {
        System.out.println("Administrador " + nombreCompleto + " gestionando usuarios del sistema.");
        // Lógica para CRUD de usuarios
    }

    public void generarReportes() {
        System.out.println("Administrador " + nombreCompleto + " generando reportes del sistema.");
        // Lógica para generar reportes
    }

    @Override
    public void mostrarInformacion() {
        System.out.println("--- Información del Administrador ---");
        System.out.println("ID: " + getId());
        System.out.println("Nombre de Usuario: " + getNombreUsuario());
        System.out.println("Nombre Completo: " + getNombreCompleto());
        System.out.println("Correo Electrónico: " + getEmail());
        System.out.println("Rol: " + getRol().name());
        System.out.println("------------------------------------");
    }
}
