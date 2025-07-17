package Materiales;

import java.util.List;

public class Audiovisual extends Material<Audiovisual> {
    private int duracionMinutos;
    private String formato;

    public Audiovisual(int id, String titulo, String autor, int añoPublicacion, List<String> categorias, int duracionMinutos, String formato) {
        super(id, titulo, autor, añoPublicacion, categorias);
        this.duracionMinutos = duracionMinutos;
        this.formato = formato;
    }

    @Override
    public void mostrarDetalles() {
        System.out.println("Audiovisual: " + titulo + " (" + añoPublicacion + ")");
        System.out.println("Autor: " + autor + " | Duración: " + duracionMinutos + " min | Formato: " + formato);
    }
}