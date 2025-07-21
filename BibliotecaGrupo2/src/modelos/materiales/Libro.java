package modelos.materiales;

public class Libro extends Material {
    private String editorial;
    private String isbn;
    private int numeroPaginas;
    private String genero;

    public Libro(int id, String titulo, String autor, int anioPublicacion, String descripcion, 
            String idioma, CategoriaMaterial categoria, int cantidadTotal, String tipoMaterial,
            String editorial, 
            String isbn, 
            int numeroPaginas, 
            String genero) {
        super(id, titulo, autor, anioPublicacion, descripcion, idioma, categoria, cantidadTotal, tipoMaterial);
        this.editorial = editorial;
        this.isbn = isbn;
        this.numeroPaginas = numeroPaginas;
        this.genero = genero;
    }

    // Getters y Setters específicos
    public String getEditorial() {
        return editorial;
    }
    
    public String getIsbn() {
        return isbn;
    }

    public int getNumeroPaginas() {
        return numeroPaginas;
    }

    public String getGenero() {
        return genero;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setNumeroPaginas(int numeroPaginas) {
        this.numeroPaginas = numeroPaginas;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    @Override
    public void mostrarDetalles() {
        System.out.println("--- Detalles del Libro ---");
        System.out.println("ID: " + getId());
        System.out.println("Título: " + getTitulo());
        System.out.println("Autor: " + getAutor());
        System.out.println("Año de Publicación: " + getAnioPublicacion());
        System.out.println("Descripción: " + getDescripcion());
        System.out.println("Idioma: " + getIdioma());
        System.out.println("Categoría: " + getCategoria().name());
        System.out.println("Género: " + getGenero());
        System.out.println("Editorial: " + getEditorial());
        System.out.println("ISBN: " + getIsbn());
        System.out.println("Número de Páginas: " + getNumeroPaginas());
        System.out.println("Cantidad Disponible: " + getCantidadDisponible() + "/" + getCantidadTotal());
        System.out.println("--------------------------");
    }
}
