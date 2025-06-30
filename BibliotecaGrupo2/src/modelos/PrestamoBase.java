import java.time.LocalDate;

public abstract class PrestamoBase {
    protected int id;
    protected Usuario usuario;
    protected LocalDate fechaPrestamo;
    protected LocalDate fechaDevolucionPrevista;
    protected LocalDate fechaDevolucionReal;
    protected boolean extendido;
//constructo
    public PrestamoBase(int id, Usuario usuario, LocalDate fechaPrestamo,
                        LocalDate fechaDevolucionPrevista, LocalDate fechaDevolucionReal) {
        this.id = id;
        this.usuario = usuario;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucionPrevista = fechaDevolucionPrevista;
        this.fechaDevolucionReal = fechaDevolucionReal;
        this.extendido = false; //inicia como false pq aun no se sabe si el tiempo se va a extender
    }
//geters
    public int getId() { return id; }
    public Usuario getUsuario() { return usuario; }
    public LocalDate getFechaPrestamo() { return fechaPrestamo; }
    public LocalDate getFechaDevolucionPrevista() { return fechaDevolucionPrevista; }
    public LocalDate getFechaDevolucionReal() { return fechaDevolucionReal; }
    public boolean isExtendido() { return extendido; }
//seters
    public void setId(int id) { this.id = id; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public void setFechaPrestamo(LocalDate fechaPrestamo) { this.fechaPrestamo = fechaPrestamo; }
    public void setFechaDevolucionPrevista(LocalDate fechaDevolucionPrevista) { this.fechaDevolucionPrevista = fechaDevolucionPrevista; }
    public void setFechaDevolucionReal(LocalDate fechaDevolucionReal) { this.fechaDevolucionReal = fechaDevolucionReal; }
    public void setExtendido(boolean extendido) { this.extendido = extendido; }

    public abstract long calcularDiasRetraso();
    public abstract boolean estaVencido();
}

