/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Materiales;
import java.util.List;

public abstract class material {
    protected int id;
    protected String titulo;
    protected String autor;
    protected int añoPublicacion;
    protected boolean disponible;
    protected int cantidadDisponible;
    protected int cantidadTotal;
    protected List<String> categoria;

    public material(int id, String titulo, String autor, int añoPublicacion, List<String> categoria) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.añoPublicacion = añoPublicacion;
        this.categoria = categoria;
        this.disponible = true;
        this.cantidadDisponible = 1;
        this.cantidadTotal = 1;
    }

    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public int getAnioPublicacion() { return añoPublicacion; }
    public List<String> getCategoria() { return categoria; }

    public void setId(int id) { this.id = id; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setAutor(String autor) { this.autor = autor; }
    public void setAñoPublicacion(int añoPublicacion) { this.añoPublicacion = añoPublicacion; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }
    public void setCategoria(List<String> categoria) { this.categoria = categoria; }

    public void incrementarDisponible(int cantidad) {
        this.cantidadDisponible += cantidad;
        this.cantidadTotal += cantidad;
    }

    public void decrementarDisponible(int cantidad) {
        if (cantidadDisponible >= cantidad) {
            this.cantidadDisponible -= cantidad;
        }
    }

    public abstract void mostrarDetalles();
}
