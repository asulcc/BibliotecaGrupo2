package Materiales;

import java.util.List;

public abstract class Material<T> {
    protected int id;
    protected String titulo;
    protected String autor;
    protected int añoPublicacion;
    protected boolean disponible = true;
    protected int cantidadDisponible = 1;
    protected int cantidadTotal = 1;
    protected List<String> categorias;

    public Material(int id, String titulo, String autor, int añoPublicacion, List<String> categorias) {
        if (categorias == null || categorias.isEmpty()) {
            throw new IllegalArgumentException("Debe tener al menos una categoría.");
        }
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.añoPublicacion = añoPublicacion;
        this.categorias = categorias;
    }

    // Getters y Setters
    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public int getAñoPublicacion() { return añoPublicacion; }
    public List<String> getCategorias() { return categorias; }

    public void setDisponible(boolean disponible) { this.disponible = disponible; }
    public void setCategorias(List<String> categorias) { this.categorias = categorias; }

    public void incrementarDisponible(int cantidad) {
        cantidadDisponible += cantidad;
        cantidadTotal += cantidad;
    }

    public void decrementarDisponible(int cantidad) {
        if (cantidadDisponible >= cantidad) {
            cantidadDisponible -= cantidad;
        }
    }

    public abstract void mostrarDetalles();
}
