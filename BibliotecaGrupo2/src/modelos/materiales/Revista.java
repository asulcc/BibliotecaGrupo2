package Materiales;

import java.util.List;

public class Revista extends Material<Revista> {
    private int numero;
    private int volumen;

    public Revista(int id, String titulo, String autor, int añoPublicacion, List<String> categorias, int numero, int volumen) {
        super(id, titulo, autor, añoPublicacion, categorias);
        this.numero = numero;
        this.volumen = volumen;
    }

    @Override
    public void mostrarDetalles() {
        System.out.println("Revista: " + titulo + " (" + añoPublicacion + ")");
        System.out.println("Autor: " + autor + " | Número: " + numero + " | Volumen: " + volumen);
    }
}