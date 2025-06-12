/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicios;

/**
 *
 * @author Patrick
 */
public class PrestamoMaterial extends PrestamoBase {
    private Material material;
    private boolean estaActivo;
    private boolean esReserva;

    public PrestamoMaterial(int id, Usuario usuario, Material material, LocalDate fechaPrestamo, 
            LocalDate fechaDevolucionPrevista, LocalDate fechaDevolucionReal, boolean extendido, 
            boolean estaActivo, boolean esReserva) {
        super(id, usuario, fechaPrestamo, fechaDevolucionPrevista, fechaDevolucionReal, extendido);
        this.material = material;
        this.estaActivo = estaActivo;
        this.esReserva = esReserva;
    }

    public Material getMaterial() { return material; }
    public boolean isEstaActivo() { return estaActivo; }
    public boolean isEsReserva() { return esReserva; }

    public void setMaterial(Material material) { this.material = material; }
    public void setEstaActivo(boolean estaActivo) { this.estaActivo = estaActivo; }
    public void setEsReserva(boolean esReserva) { this.esReserva = esReserva; }

    @Override
    public long calcularDiasRetraso() {
        return fechaDevolucionReal != null && fechaDevolucionReal.isAfter(fechaDevolucionPrevista)
                ? fechaDevolucionPrevista.until(fechaDevolucionReal).getDays()
                : 0;
    }

    @Override
    public boolean estaVencido() {
        return fechaDevolucionReal == null && LocalDate.now().isAfter(fechaDevolucionPrevista);
    }
}

