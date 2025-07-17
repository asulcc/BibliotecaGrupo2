package modelos.usuarios;
public abstract class Usuario {
    //creacion de roles 
    public enum Rol {
        ADMINISTRADOR,
        BIBLIOTECARIO,
        PROFESOR,
        ALUMNO
    }
    protected int id;
    protected String nombreUsuario;
    protected String contrasena;
    protected String nombreCompleto;
    protected String email;
    protected Rol rol;
    public Usuario(int id, String nombreUsuario, String contrasena, String nombreCompleto, String email, Rol rol) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.nombreCompleto = nombreCompleto;
        this.email = email;
        this.rol = rol;
    }
    // Getters
    public int getId() {
        return id;
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
    public Rol getRol() {
        return rol;
    }
    
    // Setter
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }
    public void setemail(String email) {
        this.email = email;
    }
    
    // Método abstracto que debe ser implementado por las subclases
    public abstract void mostrarInformacion();
}

