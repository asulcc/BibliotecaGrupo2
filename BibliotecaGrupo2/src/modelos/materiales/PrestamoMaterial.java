package Materiales;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class PrestamoMaterial {

    private final List<Material<?>> inventario = new ArrayList<>();
    private final List<Material<?>> prestados = new ArrayList<>();

    public PrestamoMaterial() {
        // Cargar materiales iniciales
        inventario.add(new Libro(1, "Cien años de soledad", "Gabriel García Márquez", 1967,
                Arrays.asList("Novela", "Realismo mágico"), "1234567890", "Sudamericana"));

        inventario.add(new Tesis(2, "Análisis de Algoritmos", "Ana Pérez", 2020,
                Arrays.asList("Ciencia", "Computación"), "Maestría", "UNAM"));

        inventario.add(new Audiovisual(3, "Documental del Amazonas", "Juan López", 2018,
                Arrays.asList("Naturaleza", "Educativo"), 90, "MP4"));

        inventario.add(new Revista(4, "National Geographic", "Editorial NG", 2023,
                Arrays.asList("Ciencia", "Fotografía"), 12, 45));
    }

    public void mostrarInventario() {
        System.out.println("=== INVENTARIO DISPONIBLE ===");
        for (Material<?> m : inventario) {
            m.mostrarDetalles();
            System.out.println("Disponible: " + m.cantidadDisponible);
            System.out.println();
        }
    }

    public void prestarMaterial(int id) {
        for (Material<?> m : inventario) {
            if (m.getId() == id) {
                if (m.cantidadDisponible > 0) {
                    m.decrementarDisponible(1);
                    prestados.add(m);
                    System.out.println("✅ Material prestado: " + m.getTitulo());
                } else {
                    System.out.println("❌ No hay ejemplares disponibles para préstamo.");
                }
                return;
            }
        }
        System.out.println("❌ No se encontró un material con ID: " + id);
    }

    public void devolverMaterial(int id) {
        for (Material<?> m : prestados) {
            if (m.getId() == id) {
                m.incrementarDisponible(1);
                prestados.remove(m);
                System.out.println("✅ Material devuelto: " + m.getTitulo());
                return;
            }
        }
        System.out.println("❌ El material no está registrado como prestado.");
    }

    public static void main(String[] args) {
        PrestamoMaterial sistema = new PrestamoMaterial();
        Scanner sc = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("\n=== SISTEMA DE PRÉSTAMOS ===");
            System.out.println("1. Ver materiales disponibles");
            System.out.println("2. Prestar material");
            System.out.println("3. Devolver material");
            System.out.println("4. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = sc.nextInt();

            switch (opcion) {
                case 1:
                    sistema.mostrarInventario();
                    break;
                case 2:
                    System.out.print("Ingrese el ID del material a prestar: ");
                    int idPrestamo = sc.nextInt();
                    sistema.prestarMaterial(idPrestamo);
                    break;
                case 3:
                    System.out.print("Ingrese el ID del material a devolver: ");
                    int idDevolucion = sc.nextInt();
                    sistema.devolverMaterial(idDevolucion);
                    break;
                case 4:
                    System.out.println("Saliendo del sistema...");
                    break;
                default:
                    System.out.println("❌ Opción no válida.");
            }
        } while (opcion != 4);
    }
}
