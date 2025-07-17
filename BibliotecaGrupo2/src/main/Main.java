package main;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import modelos.Sancion;
import modelos.materiales.Audiovisual;
import modelos.materiales.Libro;
import modelos.materiales.Material;
import modelos.materiales.PrestamoMaterial;
import modelos.materiales.Revista;
import modelos.materiales.Tesis;
import modelos.recursos.PC;
import modelos.recursos.Recurso;
import modelos.recursos.ReservaRecurso;
import modelos.recursos.SalaEstudio;
import modelos.recursos.Tableta;
import modelos.usuarios.Usuario;
import servicios.BaseDatos;
import servicios.ServicioAutenticacion;
import servicios.ServicioMaterial;
import servicios.ServicioPrestamo;
import servicios.ServicioRecurso;
import servicios.ServicioSancion;

public class Main {
    private final Scanner scanner;
    private BaseDatos dbManager;
    private ServicioAutenticacion autenticacionService;
    private ServicioMaterial materialService;
    private ServicioRecurso recursoService;
    private ServicioPrestamo prestamoService;
    private ServicioSancion sancionService;

     // El usuario que inicia sesión
    private Usuario usuarioActual;

    
    public Main() {
        scanner = new Scanner(System.in);
        dbManager = new BaseDatos();

        // Inicializar servicios, inyectando dependencias
        autenticacionService = new ServicioAutenticacion(dbManager);
        materialService = new ServicioMaterial(dbManager);
        recursoService = new ServicioRecurso(dbManager);
        sancionService = new ServicioSancion(dbManager);
        // PrestamoService depende de MaterialService, RecursoService y SancionService
        prestamoService = new ServicioPrestamo(dbManager, materialService, recursoService, sancionService);
    }

    public void start() {
        System.out.println("¡Bienvenido al Sistema de Gestión de Biblioteca!");
        mostrarMenuPrincipal();
    }
    
    // Método del menú principal
    private void mostrarMenuPrincipal() {
        int opcion;
        do {
            System.out.println("\n--- MENÚ PRINCIPAL ---");
            System.out.println("1. Iniciar Sesión");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = leerEntero();

            switch (opcion) {
                case 1 -> iniciarSesion();
                case 0 -> System.out.println("Saliendo del sistema. ¡Hasta pronto!");
                default -> System.out.println("Opción inválida. Intente de nuevo.");
            }
        } while (opcion != 0);
    }
    
    // proceso de inicio de sesión
    private void iniciarSesion() {
        System.out.print("Nombre de usuario: ");
        String nombreUsuario = scanner.nextLine();
        System.out.print("Contraseña: ");
        String contrasena = scanner.nextLine();

        usuarioActual = autenticacionService.login(nombreUsuario, contrasena);

        if (usuarioActual != null) {
            System.out.println("¡Inicio de sesión exitoso! Bienvenido, " 
                    + usuarioActual.getNombreCompleto() 
                    + " (" + usuarioActual.getRol().name() + ").");
            mostrarMenuPorRol();
        } else {
            System.out.println("Nombre de usuario o contraseña incorrectos.");
        }
    }
    
    // Menú  de opciones generales
    private void mostrarMenuPorRol() {
        int opcion;
        do {
            System.out.println("\n--- MENÚ DE " + usuarioActual.getRol().name() + " ---");
            System.out.println("1. Gestión de Materiales (CRUD)");
            System.out.println("2. Gestión de Recursos (CRUD)");
            System.out.println("3. Gestión de Préstamos y Reservas");
            System.out.println("4. Gestión de Sanciones");
            System.out.println("5. Ver Mis Préstamos/Reservas/Sanciones");
            System.out.println("6. Cambiar Contraseña");
            System.out.println("0. Cerrar Sesión");
            System.out.print("Seleccione una opción: ");
            opcion = leerEntero();

            switch (opcion) {
                case 1:
                    if (autenticacionService.tienePermiso(usuarioActual, Rol.BIBLIOTECARIO)) {
                        menuGestionMateriales();
                    } else {
                        System.out.println("Permiso denegado. Solo Operadores y "
                                + "Administradores pueden gestionar materiales.");
                    }
                    break;
                case 2:
                    if (autenticacionService.tienePermiso(usuarioActual, Rol.BIBLIOTECARIO)) {
                        menuGestionRecursos();
                    } else {
                        System.out.println("Permiso denegado. Solo Operadores y "
                                + "Administradores pueden gestionar recursos.");
                    }
                    break;
                case 3:
                    if (autenticacionService.tienePermiso(usuarioActual, Rol.BIBLIOTECARIO)) {
                        menuGestionPrestamosReservas();
                    } else {
                        System.out.println("Permiso denegado. Solo Operadores y "
                                + "Administradores pueden gestionar préstamos y reservas.");
                    }
                    break;
                case 4:
                    if (autenticacionService.tienePermiso(usuarioActual, Rol.BIBLIOTECARIO)) {
                        menuGestionSanciones();
                    } else {
                        System.out.println("Permiso denegado. Solo Operadores y "
                                + "Administradores pueden gestionar sanciones.");
                    }
                    break;
                case 5:
                    verMisDatosUsuario();
                    break;
                case 6:
                    cambiarContrasena();
                    break;
                case 0:
                    autenticacionService.logout();
                    usuarioActual = null; // Limpiar el usuario actual
                    break;
                default:
                    System.out.println("Opción inválida. Intente de nuevo.");
            }
        } while (opcion != 0);
    }

    // Cmabiar contraseña de usuario actual
    private void cambiarContrasena() {
        System.out.print("Ingrese la nueva contraseña: ");
        String nuevaContrasena = scanner.nextLine();
        if (autenticacionService.cambiarContrasena(usuarioActual, nuevaContrasena)) {
            System.out.println("Contraseña actualizada con éxito.");
        } else {
            System.out.println("No se pudo actualizar la contraseña.");
        }
    }

    // Ver datos generales del usuario actaul
    private void verMisDatosUsuario() {
        System.out.println("\n--- MIS DATOS DE USUARIO ---");
        usuarioActual.mostrarInformacion();

        System.out.println("\n--- MIS PRÉSTAMOS DE MATERIAL ---");
        List<PrestamoMaterial> misPrestamos = prestamoService.getPrestamosPorUsuario(usuarioActual);
        if (misPrestamos.isEmpty()) {
            System.out.println("No tiene préstamos de materiales activos o pasados.");
        } else {
            misPrestamos.forEach(System.out::println);
        }

        System.out.println("\n--- MIS RESERVAS DE RECURSOS ---");
        List<ReservaRecurso> misReservas = prestamoService.getAllReservasRecurso().stream()
                .filter(r -> r.getUsuario().getId() == usuarioActual.getId())
                .toList();
        if (misReservas.isEmpty()) {
            System.out.println("No tiene reservas de recursos activas o pasadas.");
        } else {
            misReservas.forEach(System.out::println);
        }

        System.out.println("\n--- MIS SANCIONES ---");
        List<Sancion> misSanciones = sancionService.getSancionesPorUsuario(usuarioActual);
        if (misSanciones.isEmpty()) {
            System.out.println("No tiene sanciones.");
        } else {
            misSanciones.forEach(System.out::println);
            if (sancionService.usuarioEstaSancionado(usuarioActual)) {
                System.out.println("\n¡ADVERTENCIA: Usted está sancionado actualmente y "
                        + "no puede realizar préstamos/reservas!");
            }
        }
        System.out.println("Presione Enter para continuar...");
        scanner.nextLine();
    }
    
    /**
    * Métodos para la Gestión de Materiales (CRUD)
    * Materiales, libros, revistas, tesis, audiovisuales
    */
    // Menu de materiales
    private void menuGestionMateriales() {
        int opcion;
        do {
            System.out.println("\n--- GESTIÓN DE MATERIALES ---");
            System.out.println("1. Agregar Material");
            System.out.println("2. Ver Todos los Materiales");
            System.out.println("3. Buscar Materiales");
            System.out.println("4. Actualizar Material");
            System.out.println("5. Eliminar Material");
            System.out.println("0. Volver al Menú Principal");
            System.out.print("Seleccione una opción: ");
            opcion = leerEntero();

            switch (opcion) {
                case 1:
                    addMaterial();
                    break;
                case 2:
                    viewAllMateriales();
                    break;
                case 3:
                    searchMateriales();
                    break;
                case 4:
                    updateMaterial();
                    break;
                case 5:
                    deleteMaterial();
                    break;
                case 0:
                    System.out.println("Volviendo...");
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }
    
    // Método para añadir materiales nuevos al sistema
    private void addMaterial() {
        System.out.println("\n--- AGREGAR MATERIAL ---");
        System.out.print("Título: ");
        String titulo = scanner.nextLine();
        System.out.print("Autor: ");
        String autor = scanner.nextLine();
        System.out.print("Año de Publicación: ");
        int anioPublicacion = leerEntero();
        System.out.print("Descripción: ");
        String descripcion = scanner.nextLine();
        System.out.print("Idioma: ");
        String idioma = scanner.nextLine();
        CategoriaMaterial categoria = seleccionarCategoriaMaterial();
        System.out.print("Cantidad Total: ");
        int cantidadTotal = leerEntero();

        System.out.println("Seleccione tipo de material:");
        System.out.println("1. Libro");
        System.out.println("2. Revista");
        System.out.println("3. Tesis");
        System.out.println("4. Audiovisual");
        System.out.print("Opción: ");
        int tipo = leerEntero();
        
        String genero;
        String tipoMaterial;
                
        Material nuevoMaterial = null;
        // Obtener el siguiente ID disponible
        int nuevoId = dbManager.getNextId("Materiales"); 

        switch (tipo) {
            case 1: // Libro
                tipoMaterial = "LIBRO";
                System.out.print("Editorial: ");
                String editorial = scanner.nextLine();
                System.out.print("ISBN: ");
                String isbn = scanner.nextLine();
                System.out.print("Genero: ");
                genero = scanner.nextLine();
                System.out.print("Número de Páginas: ");
                int numeroPaginas = leerEntero();
                nuevoMaterial = new Libro(nuevoId, titulo, autor, anioPublicacion, descripcion, idioma, categoria, cantidadTotal,
                        tipoMaterial, editorial, isbn, numeroPaginas, genero);
                break;
            case 2: // Revista
                tipoMaterial = "REVISTA";
                System.out.print("Numero de Volumen: ");
                int volumen = leerEntero();
                System.out.print("Numero de Edición: ");
                int numero = leerEntero();
                System.out.print("Fecha de publicación (yy/mm/dd): ");
                String fechaPublicacion = scanner.nextLine();
                nuevoMaterial = new Revista(nuevoId, titulo, autor, anioPublicacion, descripcion, idioma, categoria, cantidadTotal,
                        tipoMaterial, volumen, numero, fechaPublicacion);
                break;
            case 3: // Tesis
                tipoMaterial = "TESIS";
                System.out.print("Universidad: ");
                String universidad = scanner.nextLine();
                System.out.print("Grado Académico: ");
                String gradoAcademico = scanner.nextLine();
                System.out.print("Palabras Claves: ");
                String palabrasClave = scanner.nextLine();
                nuevoMaterial = new Tesis(nuevoId, titulo, autor, anioPublicacion, descripcion, idioma, categoria, cantidadTotal,
                        tipoMaterial, universidad, gradoAcademico, palabrasClave);
                break;
            case 4: // Audiovisual
                tipoMaterial = "AUDIOVISUAL";
                System.out.print("Genero: ");
                genero = scanner.nextLine();
                System.out.print("Productora: ");
                String productora = scanner.nextLine();
                System.out.print("Formato (DVD, Blu-ray, MP4, etc.): ");
                String formato = scanner.nextLine();
                System.out.print("Duración (En minutos, Ej: 90, 45): ");
                int duracion = leerEntero();
                nuevoMaterial = new Audiovisual(nuevoId, titulo, autor, anioPublicacion, descripcion, idioma, categoria, cantidadTotal,
                        tipoMaterial, genero, productora, formato, duracion);
                break;
            default:
                System.out.println("Tipo de material inválido.");
                return;
        }

        if (nuevoMaterial != null) {
            materialService.addMaterial(nuevoMaterial);
        }
    }

    // Método para mostrar todos los materiales del sistema
    private void viewAllMateriales() {
        System.out.println("\n--- TODOS LOS MATERIALES ---");
        List<Material> materiales = materialService.getAllMateriales();
        if (materiales.isEmpty()) {
            System.out.println("No hay materiales registrados.");
        } else {
            materiales.forEach(Material::mostrarDetalles);
        }
        System.out.println("Presione Enter para continuar...");
        scanner.nextLine();
    }

    // Método para buscar materiales
    private void searchMateriales() {
        System.out.println("\n--- BUSCAR MATERIALES ---");
        System.out.println("Buscar por:");
        System.out.println("1. Título");
        System.out.println("2. Autor");
        System.out.println("3. Género");
        System.out.println("4. Categoría");
        System.out.print("Seleccione una opción: ");
        int opcion = leerEntero();

        String tipoBusqueda;
        switch (opcion) {
            case 1:
                tipoBusqueda = "titulo";
                break;
            case 2:
                tipoBusqueda = "autor";
                break;
            case 3:
                tipoBusqueda = "genero";
                break;
            case 4:
                tipoBusqueda = "categoria";
                break;
            default:
                System.out.println("Opción inválida.");
                return;
        }

        System.out.print("Ingrese la palabra clave para la búsqueda: ");
        String query = scanner.nextLine();

        List<Material> resultados = materialService.buscarMateriales(query, tipoBusqueda);
        if (resultados.isEmpty()) {
            System.out.println("No se encontraron materiales que coincidan con su búsqueda.");
        } else {
            System.out.println("\n--- RESULTADOS DE BÚSQUEDA ---");
            resultados.forEach(Material::mostrarDetalles);
        }
        System.out.println("Presione Enter para continuar...");
        scanner.nextLine();
    }

    // Método para Actualizar información de material por el ID
    private void updateMaterial() {
        System.out.println("\n--- ACTUALIZAR MATERIAL ---");
        System.out.print("Ingrese el ID del material a actualizar: ");
        int id = leerEntero();

        Material material = materialService.getMaterialById(id);
        if (material == null) {
            System.out.println("Material no encontrado con ID: " + id);
            return;
        }

        System.out.println("Material actual: " + material.getTitulo());
        System.out.println("Actualizar Título (" + material.getTitulo() + "): ");
        String titulo = scanner.nextLine();
        if (!titulo.isEmpty()) {
            material.setTitulo(titulo);
        }

        System.out.println("Actualizar Autor (" + material.getAutor() + "): ");
        String autor = scanner.nextLine();
        if (!autor.isEmpty()) {
            material.setAutor(autor);
        }

        System.out.println("Actualizar Año de Publicación (" + material.getAnioPublicacion() + "): ");
        int anioPublicacion = leerEntero();
        if (anioPublicacion >= 1500 && anioPublicacion < 2025) {
            material.setAnioPublicacion(anioPublicacion);
        } else {
            System.out.println("Año no valido! Se mantendrá el anterior.");
        }

        System.out.println("Actualizar Descripción (" + material.getDescripcion() + "): ");
        String descripcion = scanner.nextLine();
        if (!descripcion.isEmpty()) {
            material.setDescripcion(descripcion);
        }

        System.out.println("Actualizar Idioma (" + material.getIdioma() + "): ");
        String idioma = scanner.nextLine();
        if (!idioma.isEmpty()) {
            material.setIdioma(idioma);
        }

        System.out.println("Cambiar Categoría? (s/n): ");
        if (scanner.nextLine().equalsIgnoreCase("s")) {
            material.setCategoria(seleccionarCategoriaMaterial());
        }

        System.out.print("Actualizar Cantidad Total (" + material.getCantidadTotal() + "): ");
        String cantTotalStr = scanner.nextLine();
        if (!cantTotalStr.isEmpty()) {
            try {
                int nuevaCantidadTotal = Integer.parseInt(cantTotalStr);
                // No se puede bajar el total por debajo de la cantidad disponible
                if (nuevaCantidadTotal >= material.getCantidadDisponible()) { 
                    material.setCantidadTotal(nuevaCantidadTotal);
                } else {
                    System.out.println("Advertencia: La nueva cantidad total no puede "
                            + "ser menor que la cantidad disponible actual.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Cantidad total inválida. Se mantiene la anterior.");
            }
        }
        
        materialService.updateMaterial(material);
    }

    // Método para eliminar un material
    private void deleteMaterial() {
        System.out.println("\n--- ELIMINAR MATERIAL ---");
        System.out.print("Ingrese el ID del material a eliminar: ");
        int id = leerEntero();
        materialService.deleteMaterial(id);
    }
    
    
    /**
    * Métodos para la Gestión de Recursos (CRUD)
    * Pcs, Laptops, Salas de estudio
    */
    // Menu de Recursos
    private void menuGestionRecursos() {
        int opcion;
        do {
            System.out.println("\n--- GESTIÓN DE RECURSOS ---");
            System.out.println("1. Agregar Recurso");
            System.out.println("2. Ver Todos los Recursos");
            System.out.println("3. Actualizar Recurso");
            System.out.println("4. Eliminar Recurso");
            System.out.println("0. Volver al Menú Principal");
            System.out.print("Seleccione una opción: ");
            opcion = leerEntero();

            switch (opcion) {
                case 1:
                    addRecurso();
                    break;
                case 2:
                    viewAllRecursos();
                    break;
                case 3:
                    updateRecurso();
                    break;
                case 4:
                    deleteRecurso();
                    break;
                case 0:
                    System.out.println("Volviendo...");
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

    // Método para añadir un nuevo recurso
    private void addRecurso() {
        System.out.println("\n--- AGREGAR RECURSO ---");
        System.out.println("Seleccione tipo de recurso:");
        System.out.println("1. PC");
        System.out.println("2. Tableta");
        System.out.println("3. Sala de Estudio");
        System.out.print("Opción: ");
        int tipo = leerEntero();

        System.out.print("Codigo: ");
        String codigo = scanner.nextLine();
        System.out.print("Ubicación: ");
        String ubicacion = scanner.nextLine();

        Recurso nuevoRecurso = null;
        // Obtener el siguiente ID disponible
        int nuevoId = dbManager.getNextId("Recursos"); 

        switch (tipo) {
            case 1: // PC
                System.out.print("Sistema Operativo: ");
                String so = scanner.nextLine();
                System.out.print("Especificaciones: ");
                String especificaciones = scanner.nextLine();
                nuevoRecurso = new PC(nuevoId, codigo, ubicacion, so, especificaciones);
                break;
            case 2: // Tableta
                System.out.print("Marca: ");
                String marca = scanner.nextLine();
                System.out.print("Modelo: ");
                String modelo = scanner.nextLine();
                nuevoRecurso = new Tableta(nuevoId, codigo, ubicacion, marca, modelo);
                break;
            case 3: // Sala de Estudio
                System.out.print("Capacidad Máxima (3-5): ");
                int capacidad = leerEntero();
                try {
                    nuevoRecurso = new SalaEstudio(nuevoId, codigo, ubicacion, capacidad);
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: " + e.getMessage());
                    return;
                }
                break;
            default:
                System.out.println("Tipo de recurso inválido.");
                return;
        }

        if (nuevoRecurso != null) {
            recursoService.addRecurso(nuevoRecurso);
        }
    }

    // Método para mostrar todos los recursos
    private void viewAllRecursos() {
        System.out.println("\n--- TODOS LOS RECURSOS ---");
        List<Recurso> recursos = recursoService.getAllRecursos();
        if (recursos.isEmpty()) {
            System.out.println("No hay recursos registrados.");
        } else {
            recursos.forEach(Recurso::mostrarDetalles);
        }
        System.out.println("Presione Enter para continuar...");
        scanner.nextLine();
    }

    // Método para actualizar datos de un recurso
    private void updateRecurso() {
        System.out.println("\n--- ACTUALIZAR RECURSO ---");
        System.out.print("Ingrese el ID del recurso a actualizar: ");
        int id = leerEntero();

        Recurso recurso = recursoService.getRecursoById(id);
        if (recurso == null) {
            System.out.println("Recurso no encontrado con ID: " + id);
            return;
        }

        System.out.println("Recurso actual: " + recurso.getCodigo());
        System.out.print("Nuevo Codigo (" + recurso.getCodigo() + "): ");
        String codigo = scanner.nextLine();
        if (!codigo.isEmpty()) {
            recurso.setCodigo(codigo);
        }

        System.out.print("Nueva Ubicación (" + recurso.getUbicacion() + "): ");
        String ubicacion = scanner.nextLine();
        if (!ubicacion.isEmpty()) {
            recurso.setUbicacion(ubicacion);
        }

        System.out.print("¿Está Disponible? (s/n) (" + (recurso.isDisponible() ? "s" : "n") + "): ");
        String disponibleStr = scanner.nextLine();
        if (!disponibleStr.isEmpty()) {
            recurso.setDisponible(disponibleStr.equalsIgnoreCase("s"));
        }

        if (recurso instanceof PC pc) {
            System.out.print("Nuevo Sistema Operativo (" + pc.getSistemaOperativo() + "): ");
            String so = scanner.nextLine();
            if (!so.isEmpty()) {
                pc.setSistemaOperativo(so);
            }

            System.out.print("Nuevas Especificaciones (" + pc.getEspecificaciones() + "): ");
            String specs = scanner.nextLine();
            if (!specs.isEmpty()) {
                pc.setEspecificaciones(specs);
            }
        } else if (recurso instanceof Tableta tableta) {
            System.out.print("Nueva Marca (" + tableta.getMarca() + "): ");
            String marca = scanner.nextLine();
            if (!marca.isEmpty()) {
                tableta.setMarca(marca);
            }

            System.out.print("Nuevo Modelo (" + tableta.getModelo() + "): ");
            String modelo = scanner.nextLine();
            if (!modelo.isEmpty()) {
                tableta.setModelo(modelo);
            }
        } else if (recurso instanceof SalaEstudio sala) {
            System.out.print("Nueva Capacidad Máxima (" + sala.getCapacidadMaxima() + "): ");
            String capacidadStr = scanner.nextLine();
            if (!capacidadStr.isEmpty()) {
                try {
                    int nuevaCapacidad = Integer.parseInt(capacidadStr);
                    sala.setCapacidadMaxima(nuevaCapacidad);
                } catch (Exception e) {
                    System.out.println("Capacidad máxima inválida. " + e.getMessage());
                }
            }
        }

        recursoService.updateRecurso(recurso);
    }

    // Método para Eliminar un recurso
    private void deleteRecurso() {
        System.out.println("\n--- ELIMINAR RECURSO ---");
        System.out.print("Ingrese el ID del recurso a eliminar: ");
        int id = leerEntero();
        recursoService.deleteRecurso(id);
    }

    
    /**
    * Métodos para la Gestión de Recursos (CRUD)
    * Pcs, Laptops, Salas de estudio
    */
    
    
    
    // --- Métodos de Utilidad para Recibir un enterp por teclado ---
    private int leerEntero() {
        while (true) {
            try {
                int valor = scanner.nextInt();
                scanner.nextLine(); // Consumir la nueva línea pendiente
                return valor;
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, ingrese un número entero.");
                scanner.nextLine(); // Consumir la entrada incorrecta
            }
        }
    }
    
    public static void main(String[] args) {
        Main ui = new Main();
        ui.start();
    }
}
