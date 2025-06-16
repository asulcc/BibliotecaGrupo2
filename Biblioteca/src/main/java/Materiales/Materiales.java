/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Materiales;

import java.util.Arrays;

public class Materiales {
    public static void main(String[] args) {
        Libro libro = new Libro(1, "Cien años de soledad", "Gabriel García Márquez", 1967, Arrays.asList("Novela", "Realismo mágico"), "1234567890", "Sudamericana");
        Tesis tesis = new Tesis(2, "Análisis de Algoritmos", "Ana Pérez", 2020, Arrays.asList("Ciencia", "Computación"), "Maestría", "UNAM");
        Audiovisual video = new Audiovisual(3, "Documental del Amazonas", "Juan López", 2018, Arrays.asList("Naturaleza", "Educativo"), 90, "MP4");
        Revista revista = new Revista(4, "National Geographic", "Editorial NG", 2023, Arrays.asList("Ciencia", "Fotografía"), 12, 45);

        libro.mostrarDetalles();
        System.out.println();

        tesis.mostrarDetalles();
        System.out.println();

        video.mostrarDetalles();
        System.out.println();

        revista.mostrarDetalles();
    }
}
