package modelos.recursos;

public abstract class Recurso {
    public enum TipoRecurso {
        PC,
        TABLETA,
        SALAESTUDIO
    }
    protected int id;
    protected String codigo;
    protected String ubicacion;
    protected boolean disponible; // Indica si el recurso está actualmente disponible para uso/reserva
    protected TipoRecurso tipo;
    
    public Recurso(int id, String codigo, String ubicacion, TipoRecurso tipo) {
        this.id = id;
        this.codigo = codigo;
        this.ubicacion = ubicacion;
        this.disponible = true; // Por defecto, un recurso se crea como disponible
        this.tipo = tipo;
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
    
    public TipoRecurso getTipo() {
        return tipo;
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