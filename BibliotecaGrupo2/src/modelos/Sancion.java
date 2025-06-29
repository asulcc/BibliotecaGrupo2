package modelos;

import modelos.usuarios.Usuario;
import modelos.materiales.PrestamoMaterial;
import java.time.LocalDate;

public class Sancion {
    private int id;
    private Usuario usuarioSancionado;
    private PrestamoMaterial prestamoAsociado; // Puede ser null si la sanción no es por un préstamo
    private String descripcion;
    private LocalDate fechaSancion;
    private LocalDate fechaFinSancion;
    private boolean activa;

    public Sancion(int id, Usuario usuarioSancionado, PrestamoMaterial prestamoAsociado, String descripcion, LocalDate fechaSancion, LocalDate fechaFinSancion) {
        this.id = id;
        this.usuarioSancionado = usuarioSancionado;
        this.prestamoAsociado = prestamoAsociado;
        this.descripcion = descripcion;
        this.fechaSancion = fechaSancion;
        this.fechaFinSancion = fechaFinSancion;
        this.activa = true; // Por defecto, la sanción está activa al crearse
    }

    // Getters
    public int getId() {
        return id;
    }

    public Usuario getUsuarioSancionado() {
        return usuarioSancionado;
    }

    public PrestamoMaterial getPrestamoAsociado() {
        return prestamoAsociado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public LocalDate getFechaSancion() {
        return fechaSancion;
    }

    public LocalDate getFechaFinSancion() {
        return fechaFinSancion;
    }

    public boolean isActiva() {
        return activa;
    }

    // Setter
    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    /**
     * Verifica si la sanción aún está vigente (la fecha actual es anterior o igual a la fecha de fin de sanción).
     * @return true si la sanción está vigente y activa, false en caso contrario.
     */
    public boolean estaVigente() {
        return activa && LocalDate.now().isBefore(fechaFinSancion.plusDays(1)); // plusDays(1) para incluir el día de fin
    }

    @Override
    public String toString() {
        String prestamoInfo = (prestamoAsociado != null) ? " (Préstamo ID: " + prestamoAsociado.getId() + ")" : "";
        return "Sanción ID: " + id +
               "\n  Usuario: " + usuarioSancionado.getNombreCompleto() +
               prestamoInfo +
               "\n  Descripción: " + descripcion +
               "\n  Fecha Inicio: " + fechaSancion +
               "\n  Fecha Fin: " + fechaFinSancion +
               "\n  Estado: " + (activa ? "Activa" : "Inactiva");
    }
}
