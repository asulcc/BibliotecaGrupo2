/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Materiales;
import java.util.List;

public class Revista extends material {
    private int numero;
    private int volumen;

    public Revista(int id, String titulo, String autor, int anioPublicacion, List<String> categoria, int numero, int volumen) {
        super(id, titulo, autor, anioPublicacion, categoria);
        this.numero = numero;
        this.volumen = volumen;
    }

    public int getNumero() { return numero; }
    public int getVolumen() { return volumen; }

    public void setNumero(int numero) { this.numero = numero; }
    public void setVolumen(int volumen) { this.volumen = volumen; }

    @Override
    public void mostrarDetalles() {
        System.out.println("Revista: " + titulo + " (" + añoPublicacion + ")");
        System.out.println("Autor: " + autor + " | Número: " + numero + " | Volumen: " + volumen);
    }
}
