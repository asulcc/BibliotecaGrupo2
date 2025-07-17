package modelos.recursos;

  public class PC extends Recurso {
    private final String procesador;
    private final int ramGB;

    public PC(String id, String nombre, String ubicacion, String procesador, int ramGB) {
        super(id, nombre, ubicacion);
        if (procesador == null || procesador.isBlank()) {
            throw new IllegalArgumentException("El procesador no puede estar vacío");
        }
        if (ramGB <= 0) {
            throw new IllegalArgumentException("La RAM debe ser mayor que cero");
        }
        this.procesador = procesador;
        this.ramGB = ramGB;
    }

    public String getProcesador() {
        return procesador;
    }

    public int getRamGB() {
        return ramGB;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(", procesador=%s, ram=%dGB", procesador, ramGB);
    }
}
    

