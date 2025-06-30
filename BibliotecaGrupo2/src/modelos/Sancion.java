import java.time.LocalDate;

public class Sancion {
    private int id;
    private Usuario usuario;
    private PrestamoMaterial prestamo;
    private LocalDate fechaSancion;
    private LocalDate finalSancion;
    private String descripcion;
    private boolean estaActiva;
    private int diasRetraso;
    private double montoMulta;
//constructor
    public Sancion(int id, Usuario usuario, PrestamoMaterial prestamo, LocalDate fechaSancion,
                   int diasRetraso, double montoMulta) {
        this.id = id;
        this.usuario = usuario;
        this.prestamo = prestamo;
        this.fechaSancion = fechaSancion;
        this.diasRetraso = diasRetraso;
        this.montoMulta = montoMulta;
        this.finalSancion = fechaSancion.plusDays(diasRetraso);
        this.estaActiva = true; //inicia como true automaticamente cuando hay una sancion
    }
//geters
    public int getId() { return id; }
    public Usuario getUsuario() { return usuario; }
    public PrestamoMaterial getPrestamo() { return prestamo; }
    public LocalDate getFechaSancion() { return fechaSancion; }
    public LocalDate getFinalSancion() { return finalSancion; }
    public String getDescripcion() { return descripcion; }
    public boolean isEstaActiva() { return estaActiva; }
//seters
    public void setId(int id) { this.id = id; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public void setPrestamo(PrestamoMaterial prestamo) { this.prestamo = prestamo; }
    public void setFechaSancion(LocalDate fechaSancion) { this.fechaSancion = fechaSancion; }
    public void setFinalSancion(LocalDate finalSancion) { this.finalSancion = finalSancion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setEstaActiva(boolean estaActiva) { this.estaActiva = estaActiva; }
}
