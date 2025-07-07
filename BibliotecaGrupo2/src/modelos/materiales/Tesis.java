package Materiales;

import java.util.List;

public class Tesis extends Material<Tesis> {
    private String gradoAcademico;
    private String universidad;

    public Tesis(int id, String titulo, String autor, int añoPublicacion, List<String> categorias, String gradoAcademico, String universidad) {
        super(id, titulo, autor, añoPublicacion, categorias);
        this.gradoAcademico = gradoAcademico;
        this.universidad = universidad;
    }

    @Override
    public void mostrarDetalles() {
        System.out.println("Tesis: " + titulo + " (" + añoPublicacion + ")");
        System.out.println("Autor: " + autor + " | Grado: " + gradoAcademico + " | Universidad: " + universidad);
    }
}
