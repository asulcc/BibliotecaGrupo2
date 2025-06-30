package modelos.recursos;

public class SalaEstudio extends Recurso {
    private final int capacidad;
    private final boolean tieneProyector;

    public SalaEstudio(String id, String nombre, String ubicacion, int capacidad, boolean tieneProyector) {
        super(id, nombre, ubicacion);
        if (capacidad <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser mayor que cero");
        }
        this.capacidad = capacidad;
        this.tieneProyector = tieneProyector;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public boolean hasProyector() {
        return tieneProyector;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(", capacidad=%d, proyector=%b", capacidad, tieneProyector);
    }
}

