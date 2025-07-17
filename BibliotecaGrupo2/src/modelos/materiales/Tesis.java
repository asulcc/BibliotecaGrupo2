package modelos.materiales;

public class Tesis extends Material {
    private String universidad;
    private String gradoAcademico;
    private String palabrasClave;

    public Tesis(int id, String titulo, String autor, int anioPublicacion, String descripcion, 
            String idioma, CategoriaMaterial categoria, int cantidadTotal, String tipoMaterial,
            String universidad, String gradoAcademico, String palabrasClave) {
        super(id, titulo, autor, anioPublicacion, descripcion, idioma, categoria.TESIS, cantidadTotal, tipoMaterial);
        this.gradoAcademico = gradoAcademico;
        this.universidad = universidad;
        this.palabrasClave = palabrasClave;
    }

    // Getters y Setters
    public String getUniversidad() {
        return universidad;
    }

    public String getGradoAcademico() {
        return gradoAcademico;
    }

    public String getPalabrasClave() {
        return palabrasClave;
    }

    public void setUniversidad(String universidad) {
        this.universidad = universidad;
    }

    public void setGradoAcademico(String gradoAcademico) {
        this.gradoAcademico = gradoAcademico;
    }

    public void setPalabrasClave(String palabrasClave) {
        this.palabrasClave = palabrasClave;
    }
    

    @Override
    public void mostrarDetalles() {
        System.out.println("--- Detalles de la Tesis ---");
        System.out.println("ID: " + getId());
        System.out.println("Título: " + getTitulo());
        System.out.println("Autor: " + getAutor());
        System.out.println("Año de Publicación: " + getAnioPublicacion());
        System.out.println("Descripción: " + getDescripcion());
        System.out.println("Idioma: " + getIdioma());
        System.out.println("Categoría: " + getCategoria().name());
        System.out.println("Institución: " + getUniversidad());
        System.out.println("Grado Académico: " + getGradoAcademico());
        System.out.println("Palabras Claves: " + getPalabrasClave());
        System.out.println("Cantidad Disponible: " + getCantidadDisponible() + "/" + getCantidadTotal());
        System.out.println("----------------------------");
    }
}
