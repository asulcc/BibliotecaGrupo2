package modelos.materiales;

public abstract class Material {
    protected int id;
    protected String titulo;
    protected String autor;
    protected int anioPublicacion;
    protected String descripcion;
    protected String idioma;
    protected CategoriaMaterial categoria;
    protected int cantidadDisponible;
    protected int cantidadTotal;
    protected String tipoMaterial;

    public Material(int id, String titulo, String autor, int anioPublicacion, String descripcion, String idioma, 
                CategoriaMaterial categoria, int cantidadTotal,String tipoMaterial) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.anioPublicacion = anioPublicacion;
        this.descripcion = descripcion;
        this.idioma = idioma;
        this.categoria = categoria;
        this.cantidadTotal = cantidadTotal;
        this.cantidadDisponible = cantidadTotal;
        this.tipoMaterial = tipoMaterial;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public int getAnioPublicacion() {
        return anioPublicacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getIdioma() {
        return idioma;
    }

    public CategoriaMaterial getCategoria() {
        return categoria;
    }

    public int getCantidadDisponible() {
        return cantidadDisponible;
    }

    public int getCantidadTotal() {
        return cantidadTotal;
    }

    public String getTipoMaterial() {
        return tipoMaterial;
    }

    // Setters (para atributos que pueden cambiar, o para actualizar la disponibilidad)
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public void setAnioPublicacion(int anioPublicacion) {
        this.anioPublicacion = anioPublicacion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public void setCategoria(CategoriaMaterial categoria) {
        this.categoria = categoria;
    }

    public void setCantidadTotal(Integer cantidadTotal) {
        this.cantidadTotal = cantidadTotal;
    }

    public void setCantidadDisponible(Integer cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }

    public void setTipoMaterial(String tipoMaterial) {
        this.tipoMaterial = tipoMaterial;
    }
    
    public void setCantidadDisponible(int cantidadDisponible) {
        if (cantidadDisponible >= 0 && cantidadDisponible <= cantidadTotal) {
            this.cantidadDisponible = cantidadDisponible;
        } else {
            System.err.println("Advertencia: Cantidad disponible inválida. Debe ser entre 0 y " + cantidadTotal);
        }
    }

    public void incrementarCantidadDisponible(int cantidad) {
        if (this.cantidadDisponible + cantidad <= this.cantidadTotal) {
            this.cantidadDisponible += cantidad;
        } else {
            this.cantidadDisponible = this.cantidadTotal; // No se puede superar la cantidad total
        }
    }

    public void decrementarCantidadDisponible(int cantidad) {
        if (this.cantidadDisponible - cantidad >= 0) {
            this.cantidadDisponible -= cantidad;
        } else {
            this.cantidadDisponible = 0; // No puede ser negativa
        }
    }

    // Método abstracto que debe ser implementado por las subclases
    public abstract void mostrarDetalles();
}
