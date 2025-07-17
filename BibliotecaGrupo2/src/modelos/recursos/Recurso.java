package modelos.recursos;

public abstract class Recurso {
    private final String id;            // Identificador alfanumérico único
    private final String nombre;        // Nombre legible del recurso
    private String ubicacion;           // Ubicación actual
    private boolean disponible = true;  // true = libre, false = prestado

    /**
     * Constructor de Recurso.
     * @param id Identificador alfanumérico (solo letras y números, sin espacios)
     */
    public Recurso(String id, String nombre, String ubicacion) {
        if (id == null || !id.matches("[A-Za-z0-9]+")) {
            throw new IllegalArgumentException("El id debe ser alfanumérico (letras y números, sin espacios)");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (ubicacion == null || ubicacion.isBlank()) {
            throw new IllegalArgumentException("La ubicación no puede estar vacía");
        }
        this.id = id;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
    }

    // ---------- GETTERS ----------
    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public boolean getDisponible() {
        return disponible;
    }

    // ---------- COMPORTAMIENTO ----------
    public void moverA(String nuevaUbicacion) {
        if (nuevaUbicacion == null || nuevaUbicacion.isBlank()) {
            throw new IllegalArgumentException("La nueva ubicación no puede estar vacía");
        }
        this.ubicacion = nuevaUbicacion;
    }

    public void marcarPrestado() {
        disponible = false;
    }

    public void marcarDevuelto() {
        disponible = true;
    }

    @Override
    public String toString() {
        return String.format("%s[id=%s, nombre=%s, ubicacion=%s, disponible=%b]",
                             getClass().getSimpleName(), id, nombre, ubicacion, disponible);
    }
}
