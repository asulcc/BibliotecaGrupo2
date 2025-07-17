package modelos.recursos;

import modelos.PrestamoBase;
import modelos.usuarios.Usuario;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class ReservaRecurso extends PrestamoBase {
    private Recurso recurso;
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;
    private int participantes; // Solo relevante para SalaEstudio
    private boolean activo; // Indica si la reserva está en curso

    // Constructor para PC/Tableta (sin participantes)
    public ReservaRecurso(int id, Usuario usuario, Recurso recurso, LocalDateTime fechaHoraInicio, LocalDateTime fechaHoraFin) {
        // La clase padre PrestamoBase trabaja con LocalDate, así que extraemos la fecha de LocalDateTime
        super(id, usuario, fechaHoraInicio.toLocalDate(), fechaHoraFin.toLocalDate());
        this.recurso = recurso;
        this.fechaHoraInicio = fechaHoraInicio;
        this.fechaHoraFin = fechaHoraFin;
        this.participantes = 0; // Por defecto para PC/Tableta
        this.activo = true; // Por defecto, la reserva está activa al crearse
    }

    // Constructor para SalaEstudio (con participantes)
    public ReservaRecurso(int id, Usuario usuario, Recurso recurso, LocalDateTime fechaHoraInicio, LocalDateTime fechaHoraFin, int participantes) {
        super(id, usuario, fechaHoraInicio.toLocalDate(), fechaHoraFin.toLocalDate());
        this.recurso = recurso;
        this.fechaHoraInicio = fechaHoraInicio;
        this.fechaHoraFin = fechaHoraFin;
        this.participantes = participantes;
        this.activo = true;
    }

    // Getters
    public Recurso getRecurso() {
        return recurso;
    }

    public LocalDateTime getFechaHoraInicio() {
        return fechaHoraInicio;
    }

    public LocalDateTime getFechaHoraFin() {
        return fechaHoraFin;
    }

    public int getParticipantes() {
        return participantes;
    }

    public boolean isActivo() {
        return activo;
    }

    // Setters
    public void setFechaHoraInicio(LocalDateTime fechaHoraInicio) {
        this.fechaHoraInicio = fechaHoraInicio;
        // Actualizar la fecha de inicio en PrestamoBase si es necesario
        super.fechaInicio = fechaHoraInicio.toLocalDate();
    }

    public void setFechaHoraFin(LocalDateTime fechaHoraFin) {
        this.fechaHoraFin = fechaHoraFin;
        // Actualizar la fecha de fin prevista en PrestamoBase si es necesario
        super.fechaFinPrevista = fechaHoraFin.toLocalDate();
    }

    public void setParticipantes(int participantes) {
        this.participantes = participantes;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
        // Si la reserva se inactiva, se considera completada
        if (!activo) {
            setCompletado(true);
        }
    }

    /**
     * Verifica si la reserva ha vencido (si la hora actual es posterior a la hora de fin).
     * @return true si la reserva ha vencido y está activa, false en caso contrario.
     */
    @Override
    public boolean estaVencido() {
        // Se considera vencida si la fecha y hora actual es posterior a la fechaHoraFin
        return activo && LocalDateTime.now().isAfter(fechaHoraFin);
    }

    /**
     * Calcula la duración de la reserva en minutos.
     * @return La duración en minutos.
     */
    public long calcularDuracionEnMinutos() {
        return ChronoUnit.MINUTES.between(fechaHoraInicio, fechaHoraFin);
    }

    @Override
    public String toString() {
        String estado = activo ? "ACTIVA" : "FINALIZADA";
        String infoParticipantes = (recurso instanceof SalaEstudio) ? ", Participantes: " + participantes : "";
        return "Reserva Recurso ID: " + id +
               "\n  Usuario: " + getUsuario().getNombreCompleto() +
               "\n  Recurso: " + recurso.getCodigo() +
               "\n  Inicio: " + fechaHoraInicio.format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")) +
               "\n  Fin: " + fechaHoraFin.format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")) +
               infoParticipantes +
               "\n  Estado: " + estado +
               (estaVencido() && activo ? " (VENCIDA)" : "");
    }
}
