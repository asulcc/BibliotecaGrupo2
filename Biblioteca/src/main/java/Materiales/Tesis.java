/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Materiales;
import java.util.List;

public class Tesis extends material {
    private String gradoAcademico;
    private String universidad;

    public Tesis(int id, String titulo, String autor, int anioPublicacion, List<String> categoria, String gradoAcademico, String universidad) {
        super(id, titulo, autor, anioPublicacion, categoria);
        this.gradoAcademico = gradoAcademico;
        this.universidad = universidad;
    }

    public String getGradoAcademico() { return gradoAcademico; }
    public String getUniversidad() { return universidad; }

    public void setGradoAcademico(String gradoAcademico) { this.gradoAcademico = gradoAcademico; }
    public void setUniversidad(String universidad) { this.universidad = universidad; }

    @Override
    public void mostrarDetalles() {
        System.out.println("Tesis: " + titulo + " (" + añoPublicacion + ")");
        System.out.println("Autor: " + autor + " | Grado: " + gradoAcademico + " | Universidad: " + universidad);
    }
}
