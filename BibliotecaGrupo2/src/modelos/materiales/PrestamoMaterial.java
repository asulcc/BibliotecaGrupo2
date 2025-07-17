package modelos.materiales;

import modelos.PrestamoBase;
import modelos.usuarios.Usuario;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class PrestamoMaterial extends PrestamoBase {
    private Material material;
    private LocalDate fechaDevolucionReal; // Puede ser null si aún no se ha devuelto
    private boolean esReserva; // True si es una reserva, false si es un préstamo directo
    private boolean activo; // Indica si el préstamo/reserva está en curso

    public PrestamoMaterial(int id, Usuario usuario, Material material, 
            LocalDate fechaPrestamo, LocalDate fechaDevolucionEstimada, 
            boolean esReserva) {
        super(id, usuario, fechaPrestamo, fechaDevolucionEstimada);
        this.material = material;
        this.esReserva = esReserva;
        this.fechaDevolucionReal = null; // Se establece al momento de la devolución
        this.activo = true; // Por defecto, el préstamo/reserva está activo al crearse
    }

    // Getters
    public Material getMaterial() {
        return material;
    }

    public LocalDate getFechaDevolucionReal() {
        return fechaDevolucionReal;
    }

    public boolean isEsReserva() {
        return esReserva;
    }

    public boolean isActivo() {
        return activo;
    }

    // Setters
    public void setFechaDevolucionReal(LocalDate fechaDevolucionReal) {
        this.fechaDevolucionReal = fechaDevolucionReal;
        // Cuando se registra la fecha de devolución real, el préstamo se considera completado
        setCompletado(true);
        this.activo = false; // El préstamo ya no está activo
    }

    public void setEsReserva(boolean esReserva) {
        this.esReserva = esReserva;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    /**
     * Verifica si el préstamo está vencido.
     * Un préstamo está vencido si la fecha actual es posterior a la fecha de devolución estimada
     * y el préstamo aún está activo (no se ha devuelto).
     * @return true si el préstamo está vencido, false en caso contrario.
     */
    @Override
    public boolean estaVencido() {
        return activo && LocalDate.now().isAfter(fechaFinPrevista);
    }

    /**
     * Calcula los días de retraso si el material ha sido devuelto fuera de tiempo.
     * @return Número de días de retraso, 0 si no hay retraso o si aún no se ha devuelto.
     */
    public int calcularDiasRetraso() {
        if (fechaDevolucionReal != null && fechaDevolucionReal.isAfter(fechaFinPrevista)) {
            return (int) ChronoUnit.DAYS.between(fechaFinPrevista, fechaDevolucionReal);
        }
        return 0;
    }

    /**
     * Extiende la fecha de devolución estimada.
     * @param nuevaFecha La nueva fecha de devolución.
     */
    public void extenderFechaDevolucion(LocalDate nuevaFecha) {
        this.fechaFinPrevista = nuevaFecha;
    }

    @Override
    public String toString() {
        String estado = activo ? (esReserva ? "RESERVADO" : "ACTIVO") : "FINALIZADO";
        String tipo = esReserva ? "Reserva" : "Préstamo";
        String devolucionReal = (fechaDevolucionReal != null) ? " - Devuelto: " + fechaDevolucionReal : "";
        return tipo + " ID: " + id +
               "\n  Usuario: " + getUsuario().getNombreCompleto() +
               "\n  Material: " + material.getTitulo() +
               "\n  Inicio: " + fechaInicio +
               "\n  Estimada: " + fechaFinPrevista +
               devolucionReal +
               "\n  Estado: " + estado +
               (estaVencido() && activo ? " (VENCIDO)" : "");
    }
}
