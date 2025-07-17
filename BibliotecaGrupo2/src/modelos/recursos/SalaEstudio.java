package modelos.recursos;

public class SalaEstudio extends Recurso {
    private int capacidadMaxima;
    private int capacidadActual; // Número de participantes actualmente en la sala (o reservados para el mismo bloque)

    public SalaEstudio(int id, String codigo, String ubicacion, TipoRecurso tipo, int capacidadMaxima) {
        super(id, codigo, ubicacion, tipo.SALAESTUDIO);
        if (capacidadMaxima < 3 || capacidadMaxima > 5) {
            throw new IllegalArgumentException("La capacidad máxima de una sala de estudio debe ser entre 3 y 5 participantes.");
        }
        this.capacidadMaxima = capacidadMaxima;
        this.capacidadActual = 0; // Inicialmente la sala está vacía
    }

    // Getters y Setters específicos
    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public void setCapacidadMaxima(int capacidadMaxima) {
        if (capacidadMaxima >= 3 && capacidadMaxima <= 5) {
            this.capacidadMaxima = capacidadMaxima;
        } else {
            System.err.println("Advertencia: La capacidad máxima debe ser entre 3 y 5.");
        }
    }

    public int getCapacidadActual() {
        return capacidadActual;
    }

    public void setCapacidadActual(int capacidadActual) {
        if (capacidadActual >= 0 && capacidadActual <= capacidadMaxima) {
            this.capacidadActual = capacidadActual;
        } else {
            System.err.println("Advertencia: Capacidad actual inválida. Debe ser entre 0 y " + capacidadMaxima);
        }
    }

    
    public boolean puedeReservar(int numParticipantes) {
        return isDisponible() && numParticipantes >= 3 && numParticipantes <= capacidadMaxima;
    }

    @Override
    public void mostrarDetalles() {
        System.out.println("--- Detalles de la Sala de Estudio ---");
        System.out.println("ID: " + getId());
        System.out.println("Codigo: " + getCodigo());
        System.out.println("Ubicación: " + getUbicacion());
        System.out.println("Capacidad Máxima: " + getCapacidadMaxima() + " participantes");
        System.out.println("Participantes Actuales: " + getCapacidadActual());
        System.out.println("Disponible: " + (isDisponible() ? "Sí" : "No"));
        System.out.println("------------------------------------");
    }
}

