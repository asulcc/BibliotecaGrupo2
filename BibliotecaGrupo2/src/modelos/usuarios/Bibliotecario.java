package modelos.usuarios;

public class Bibliotecario extends Usuario {
    public Bibliotecario(int id, String nombreUsuario, String contrasena, String nombreCompleto, String email) {
        super(id, nombreUsuario, contrasena, nombreCompleto, email, Rol.BIBLIOTECARIO);
    }

    // Métodos específicos del Operador
    public void registrarPrestamo() {
        System.out.println("Operador " + nombreCompleto + " registrando un préstamo.");
        // Lógica para registrar un préstamo
    }

    public void registrarDevolucion() {
        System.out.println("Operador " + nombreCompleto + " registrando una devolución.");
        // Lógica para registrar una devolución
    }

    public void aplicarSancion() {
        System.out.println("Operador " + nombreCompleto + " aplicando una sanción.");
        // Lógica para aplicar una sanción
    }

    @Override
    public void mostrarInformacion() {
        System.out.println("--- Información del Operador ---");
        System.out.println("ID: " + getId());
        System.out.println("Nombre de Usuario: " + getNombreUsuario());
        System.out.println("Nombre Completo: " + getNombreCompleto());
        System.out.println("Correo Electrónico: " + getEmail());
        System.out.println("Rol: " + getRol().name());
        System.out.println("--------------------------------");
    }
}
