package modelos.recursos;

public class SalaEstudio extends Recurso {
    private int capacidad;
    private boolean tieneProyector;

    public SalaEstudio(int id, String nombre, String ubicacion, int capacidad, boolean tieneProyector) {
        super(id, nombre, ubicacion);
        this.capacidad = capacidad;
        this.tieneProyector = tieneProyector;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public boolean isTieneProyector() {
        return tieneProyector;
    }

    public void setTieneProyector(boolean tieneProyector) {
        this.tieneProyector = tieneProyector;
    }

    @Override
    public void mostrarDetalles() {
        System.out.println("Sala de Estudio - ID: " + id +
                           ", Nombre: " + nombre +
                           ", Ubicación: " + ubicacion +
                           ", Disponible: " + disponible +
                           ", Capacidad: " + capacidad +
                           ", Tiene Proyector: " + (tieneProyector ? "Sí" : "No"));
    }
}
