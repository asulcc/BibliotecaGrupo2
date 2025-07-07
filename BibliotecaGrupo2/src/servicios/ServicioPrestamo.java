package servicios;



public class ServicioPrestamo {
    private BaseDatos dbManager;
    private ServicioMaterial servicioMaterial; 
    private ServicioRecurso servicioRecurso;  
    private ServicioSancion servicioSancion;  

    public ServicioPrestamo(BaseDatos dbManager, ServicioMaterial servicioMaterial, ServicioRecurso servicioRecurso, ServicioSancion servicioSancion) {
        this.dbManager = dbManager;
        this.servicioMaterial = servicioMaterial;
        this.servicioRecurso = servicioRecurso;
        this.servicioSancion = servicioSancion;
    }
}
