package recursos;

public abstract class Recurso {
    protected int id;
    protected String codigo;
    protected String ubicacion;
    protected boolean disponible; // Indica si el recurso está actualmente disponible para uso/reserva
   public Recurso(int id, String codigo, String ubicacion) {
        this.id = id;
        this.codigo = codigo;
        this.ubicacion = ubicacion;
        this.disponible = true; // Por defecto, un recurso se crea como disponible
    }
// Getters
    public int getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public boolean isDisponible() {
        return disponible;
    }

    // Setters
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    // Método abstracto que debe ser implementado por las subclases para mostrar detalles específicos
    public abstract void mostrarDetalles();

}