/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Materiales;
import java.util.List;

public class Libro extends material {
    private String isbn;
    private String editorial;

    public Libro(int id, String titulo, String autor, int anioPublicacion, List<String> categoria, String isbn, String editorial) {
        super(id, titulo, autor, anioPublicacion, categoria);
        this.isbn = isbn;
        this.editorial = editorial;
    }

    public String getIsbn() { return isbn; }
    public String getEditorial() { return editorial; }

    public void setIsbn(String isbn) { this.isbn = isbn; }
    public void setEditorial(String editorial) { this.editorial = editorial; }

    @Override
    public void mostrarDetalles() {
        System.out.println("Libro: " + titulo + " (" + añoPublicacion + ")");
        System.out.println("Autor: " + autor + " | ISBN: " + isbn + " | Editorial: " + editorial);
    }
}
