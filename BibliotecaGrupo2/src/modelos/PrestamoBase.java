package modelos;

import modelos.usuarios.Usuario;
import java.time.LocalDate;

public abstract class PrestamoBase {
    protected int id;
    protected Usuario usuario;
    protected LocalDate fechaInicio;
    protected LocalDate fechaFinPrevista;
    protected boolean completado; // Indica si el préstamo/reserva ha sido finalizado/liberado

    public PrestamoBase(int id, Usuario usuario, LocalDate fechaInicio, LocalDate fechaFinPrevista) {
        this.id = id;
        this.usuario = usuario;
        this.fechaInicio = fechaInicio;
        this.fechaFinPrevista = fechaFinPrevista;
        this.completado = false; // Por defecto, no completado al crearse
    }

    // Getters
    public int getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFinPrevista() {
        return fechaFinPrevista;
    }

    public boolean isCompletado() {
        return completado;
    }

    // Setter
    public void setCompletado(boolean completado) {
        this.completado = completado;
    }

    // Método abstracto para verificar si el préstamo/reserva está vencido.
    public abstract boolean estaVencido();
}
