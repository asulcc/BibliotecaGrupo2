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
        this.isbn = isbn;
        this.editorial = editorial;
    }

    public String getIsbn() { return isbn; }
    public String getEditorial() { return editorial; }

    public void setIsbn(String isbn) { this.isbn = isbn; }
    public void setEditorial(String editorial) { this.editorial = editorial; }

    public void mostrarDetalles() {
        String añoPublicacion = null;
        String titulo = null;
        System.out.println("Libro: " + titulo + " (" + añoPublicacion + ")");
        String autor = null;
        System.out.println("Autor: " + autor + " | ISBN: " + isbn + " | Editorial: " + editorial);
    }
}