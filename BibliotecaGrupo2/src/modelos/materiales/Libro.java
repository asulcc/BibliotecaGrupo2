package Materiales;

import java.util.List;

public class Libro extends Material<Libro> {
    private String isbn;
    private String editorial;

    public Libro(int id, String titulo, String autor, int añoPublicacion, List<String> categorias, String isbn, String editorial) {
        super(id, titulo, autor, añoPublicacion, categorias);
        this.isbn = isbn;
        this.editorial = editorial;
    }

    @Override
    public void mostrarDetalles() {
        System.out.println("Libro: " + titulo + " (" + añoPublicacion + ")");
        System.out.println("Autor: " + autor + " | ISBN: " + isbn + " | Editorial: " + editorial);
    }
}