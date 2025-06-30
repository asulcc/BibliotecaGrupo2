public class ReservaRecurso {
    private int id;
    private Usuario usuario;
    private Recurso recurso;
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;
    private int participantes;
    private boolean estaActivo;

    public ReservaRecurso(int id, Usuario usuario, Recurso recurso, LocalDateTime fechaHoraInicio, LocalDateTime fechaHoraFin, int participantes) {
        this.id = id;
        this.usuario = usuario;
        this.recurso = recurso;
        this.fechaHoraInicio = fechaHoraInicio;
        this.fechaHoraFin = fechaHoraFin;
        this.participantes = participantes;
        this.estaActivo = true;
    }

    public int getId() { return id; }
    public Usuario getUsuario() { return usuario; }
    public Recurso getRecurso() { return recurso; }
    public LocalDateTime getFechaHoraInicio() { return fechaHoraInicio; }
    public LocalDateTime getFechaHoraFin() { return fechaHoraFin; }
    public int getParticipantes() { return participantes; }
    public boolean isEstaActivo() { return estaActivo; }

    public void setId(int id) { this.id = id; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public void setRecurso(Recurso recurso) { this.recurso = recurso; }
    public void setFechaHoraInicio(LocalDateTime fechaHoraInicio) { this.fechaHoraInicio = fechaHoraInicio; }
    public void setFechaHoraFin(LocalDateTime fechaHoraFin) { this.fechaHoraFin = fechaHoraFin; }
    public void setEstaActivo(boolean estaActivo) { this.estaActivo = estaActivo; }

    public boolean estaVencida() {
        return LocalDateTime.now().isAfter(fechaHoraFin);
    }
}
