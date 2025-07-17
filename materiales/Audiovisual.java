/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Materiales;
import java.util.List;

public class Audiovisual extends material {
    private int duracionMinutos;
    private String formato;

    public Audiovisual(int id, String titulo, String autor, int añoPublicacion, List<String> categoria, int duracionMinutos, String formato) {
        this.duracionMinutos = duracionMinutos;
        this.formato = formato;
    }

    public int getDuracionMinutos() { return duracionMinutos; }
    public String getFormato() { return formato; }

    public void setDuracionMinutos(int duracionMinutos) { this.duracionMinutos = duracionMinutos; }
    public void setFormato(String formato) { this.formato = formato; }

    public void mostrarDetalles(String titulo, String autor) {
        String añoPublicacion = null;
        System.out.println("Audiovisual: " + titulo + " (" + añoPublicacion + ")");
        System.out.println("Autor: " + autor + " | Duración: " + duracionMinutos + " min | Formato: " + formato);
    }
}
