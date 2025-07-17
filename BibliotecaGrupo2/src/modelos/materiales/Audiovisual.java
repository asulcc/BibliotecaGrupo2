package modelos.materiales;

public class Audiovisual extends Material {
    private String genero;
    private String productora;
    private String formato; // Ej: DVD, Blu-ray, CD-Audio, MP4, etc.
    private int duracion; // Ej: "1h 30m", "45 min", "2h 15m"

    public Audiovisual(int id, String titulo, String autor, int anioPublicacion, String descripcion, 
            String idioma, CategoriaMaterial categoria, int cantidadTotal, String tipoMaterial,
            String genero, String productora, String formato, int duracion) {
        super(id, titulo, autor, anioPublicacion, descripcion, idioma, categoria.AUDIOVISUAL, cantidadTotal, tipoMaterial);
        this.genero = genero;
        this.productora = productora;
        this.formato = formato;
        this.duracion = duracion;
    }

    // Getters y Setters específicos
    public String getGenero() {
        return genero;
    }

    public String getProductora() {
        return productora;
    }

    public String getFormato() {
        return formato;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public void setProductora(String productora) {
        this.productora = productora;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    @Override
    public void mostrarDetalles() {
        System.out.println("--- Detalles del Material Audiovisual ---");
        System.out.println("ID: " + getId());
        System.out.println("Título: " + getTitulo());
        System.out.println("Productora: " + getProductora());
        System.out.println("Director: " + getAutor()); //Director
        System.out.println("Año de Publicación: " + getAnioPublicacion());
        System.out.println("Descripción: " + getDescripcion());
        System.out.println("Idioma: " + getIdioma());
        System.out.println("Categoría: " + getCategoria().name());
        System.out.println("Género: " + getGenero());
        System.out.println("Formato: " + getFormato());
        System.out.println("Duración: " + getDuracion());
        System.out.println("Cantidad Disponible: " + getCantidadDisponible() + "/" + getCantidadTotal());
        System.out.println("---------------------------------------");
    }
}
