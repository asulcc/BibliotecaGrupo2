package modelos.materiales;

public class Revista extends Material {
    private int volumen;
    private int numeroEdicion;
    private String fechaPublicacion;

    public Revista(int id, String titulo, String autor, int anioPublicacion, String descripcion, 
            String idioma, CategoriaMaterial categoria, int cantidadTotal, String tipoMaterial,
            int volumen, 
            int numeroEdicion,
            String fechaPublicacion) {
        super(id, titulo, autor, anioPublicacion, descripcion, idioma, categoria.REVISTA, cantidadTotal, tipoMaterial);
        this.volumen = volumen;
        this.numeroEdicion = numeroEdicion;
        this.fechaPublicacion = fechaPublicacion;
    }

    // Getters y Setters específicos
    public int getVolumen() { return volumen; }

    public int getNumeroEdicion() { return numeroEdicion; }

    public String getFechaPublicacion() { return fechaPublicacion; }

    public void setVolumen(int volumen) { this.volumen = volumen; }

    public void setNumeroEdicion(int numeroEdicion) { this.numeroEdicion = numeroEdicion; }

    public void setFechaPublicacion(String fechaPublicacion) { this.fechaPublicacion = fechaPublicacion; }
    
    
    @Override
    public void mostrarDetalles() {
        System.out.println("--- Detalles de la Revista ---");
        System.out.println("ID: " + getId());
        System.out.println("Título: " + getTitulo());
        System.out.println("Autor: " + getAutor());
        System.out.println("Año de Publicación: " + getAnioPublicacion());
        System.out.println("Descripción: " + getDescripcion());
        System.out.println("Idioma: " + getIdioma());
        System.out.println("Categoría: " + getCategoria().name());
        System.out.println("Volumen: " + getVolumen());
        System.out.println("Número de Edición: " + getNumeroEdicion());
        System.out.println("Fecha de publicación: " + getFechaPublicacion());
        System.out.println("Cantidad Disponible: " + getCantidadDisponible() + "/" + getCantidadTotal());
        System.out.println("-----------------------------");
    }
}
