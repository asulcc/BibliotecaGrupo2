package main;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import modelos.Sancion;
import modelos.materiales.Audiovisual;
import modelos.materiales.Libro;
import modelos.materiales.Material;
import modelos.materiales.Material.CategoriaMaterial;
import modelos.materiales.PrestamoMaterial;
import modelos.materiales.Revista;
import modelos.materiales.Tesis;
import modelos.recursos.PC;
import modelos.recursos.Recurso;
import modelos.recursos.ReservaRecurso;
import modelos.recursos.SalaEstudio;
import modelos.recursos.Tableta;
import modelos.usuarios.Usuario;
import modelos.usuarios.Usuario.Rol;
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
    
// --- Métodos de Gestión de Materiales (CRUD) ---
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
        int nuevoId = dbManager.getNextId("Materiales"); // Obtener el siguiente ID disponible

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

    private void searchMateriales() {
        System.out.println("\n--- BUSCAR MATERIALES ---");
        System.out.println("Buscar por:");
        System.out.println("1. Título");
        System.out.println("2. Autor");
        System.out.println("3. Género");
        System.out.println("4. Categoría");
        System.out.print("Seleccione una opción: ");
        int opcion = leerEntero();
        scanner.nextLine(); // Consumir nueva línea

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

    private void updateMaterial() {
        System.out.println("\n--- ACTUALIZAR MATERIAL ---");
        System.out.print("Ingrese el ID del material a actualizar: ");
        int id = leerEntero();
        scanner.nextLine(); // Consumir nueva línea

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
                if (nuevaCantidadTotal >= material.getCantidadDisponible()) { // No se puede bajar la total por debajo de la disponible
                    material.setCantidadTotal(nuevaCantidadTotal);
                } else {
                    System.out.println("Advertencia: La nueva cantidad total no puede ser menor que la cantidad disponible actual.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Cantidad total inválida. Se mantiene la anterior.");
            }
        }
        // La cantidad disponible se ajusta automáticamente si se modifica la total.
        // Si se necesita ajustar la disponible directamente, habría que añadir un campo también.

        materialService.updateMaterial(material);
    }

    private void deleteMaterial() {
        System.out.println("\n--- ELIMINAR MATERIAL ---");
        System.out.print("Ingrese el ID del material a eliminar: ");
        int id = leerEntero();
        scanner.nextLine(); // Consumir nueva línea
        materialService.deleteMaterial(id);
    }

    // --- Métodos de Gestión de Recursos (CRUD) ---
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
        int nuevoId = dbManager.getNextId("Recursos"); // Obtener el siguiente ID disponible

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

    private void updateRecurso() {
        System.out.println("\n--- ACTUALIZAR RECURSO ---");
        System.out.print("Ingrese el ID del recurso a actualizar: ");
        int id = leerEntero();
        scanner.nextLine(); // Consumir nueva línea

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

    private void deleteRecurso() {
        System.out.println("\n--- ELIMINAR RECURSO ---");
        System.out.print("Ingrese el ID del recurso a eliminar: ");
        int id = leerEntero();
        scanner.nextLine(); // Consumir nueva línea
        recursoService.deleteRecurso(id);
    }

    // --- Métodos de Gestión de Préstamos y Reservas ---
    private void menuGestionPrestamosReservas() {
        int opcion;
        do {
            System.out.println("\n--- GESTIÓN DE PRÉSTAMOS Y RESERVAS ---");
            System.out.println("1. Realizar Préstamo de Material");
            System.out.println("2. Registrar Devolución de Material");
            System.out.println("3. Extender Préstamo de Material");
            System.out.println("4. Realizar Reserva de Recurso");
            System.out.println("5. Liberar Reserva de Recurso");
            System.out.println("6. Extender Reserva de Recurso");
            System.out.println("7. Ver Todos los Préstamos de Material");
            System.out.println("8. Ver Todas las Reservas de Recurso");
            System.out.println("0. Volver al Menú Principal");
            System.out.print("Seleccione una opción: ");
            opcion = leerEntero();

            switch (opcion) {
                case 1:
                    realizarPrestamoMaterial();
                    break;
                case 2:
                    registrarDevolucionMaterial();
                    break;
                case 3:
                    extenderPrestamoMaterial();
                    break;
                case 4:
                    realizarReservaRecurso();
                    break;
                case 5:
                    liberarReservaRecurso();
                    break;
                case 6:
                    extenderReservaRecurso();
                    break;
                case 7:
                    verTodosLosPrestamosMaterial();
                    break;
                case 8:
                    verTodasLasReservasRecurso();
                    break;
                case 0:
                    System.out.println("Volviendo...");
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

    private void realizarPrestamoMaterial() {
        System.out.println("\n--- REALIZAR PRÉSTAMO DE MATERIAL ---");
        System.out.print("Ingrese el ID del usuario: ");
        int idUsuario = leerEntero();
//        scanner.nextLine(); // Consumir nueva línea
        Usuario usuario = sancionService.getUsuarioById(idUsuario); // Reutilizamos el método de SancionService
        if (usuario == null) {
            System.out.println("Usuario no encontrado.");
            return;
        }

        System.out.print("Ingrese el ID del material: ");
        int idMaterial = leerEntero();
//        scanner.nextLine(); // Consumir nueva línea
        Material material = materialService.getMaterialById(idMaterial);
        if (material == null) {
            System.out.println("Material no encontrado.");
            return;
        }

        System.out.print("¿Es una reserva (s/n)? ");
        boolean esReserva = scanner.nextLine().equalsIgnoreCase("s");

        prestamoService.prestarMaterial(usuario, material, esReserva);
    }

    private void registrarDevolucionMaterial() {
        System.out.println("\n--- REGISTRAR DEVOLUCIÓN DE MATERIAL ---");
        System.out.print("Ingrese el ID del préstamo a devolver: ");
        int idPrestamo = leerEntero();
//        scanner.nextLine(); // Consumir nueva línea

        PrestamoMaterial prestamo = prestamoService.getPrestamoMaterialById(idPrestamo);
        if (prestamo == null) {
            System.out.println("Préstamo no encontrado con ID: " + idPrestamo);
            return;
        }

        prestamoService.devolverMaterial(prestamo);
    }

    private void extenderPrestamoMaterial() {
        System.out.println("\n--- EXTENDER PRÉSTAMO DE MATERIAL ---");
        System.out.print("Ingrese el ID del préstamo a extender: ");
        int idPrestamo = leerEntero();
        scanner.nextLine(); // Consumir nueva línea

        PrestamoMaterial prestamo = prestamoService.getPrestamoMaterialById(idPrestamo);
        if (prestamo == null) {
            System.out.println("Préstamo no encontrado con ID: " + idPrestamo);
            return;
        }

        if (prestamo.isActivo() && !prestamo.estaVencido()) {
            prestamoService.extenderPrestamoMaterial(prestamo);
        } else {
            System.out.println("El préstamo no está activo o ya está vencido, no se puede extender.");
        }
    }

    private void realizarReservaRecurso() {
        System.out.println("\n--- REALIZAR RESERVA DE RECURSO ---");
        System.out.print("Ingrese el ID del usuario: ");
        int idUsuario = leerEntero();
        scanner.nextLine(); // Consumir nueva línea
        Usuario usuario = sancionService.getUsuarioById(idUsuario);
        if (usuario == null) {
            System.out.println("Usuario no encontrado.");
            return;
        }

        System.out.print("Ingrese el ID del recurso: ");
        int idRecurso = leerEntero();
        scanner.nextLine(); // Consumir nueva línea
        Recurso recurso = recursoService.getRecursoById(idRecurso);
        if (recurso == null) {
            System.out.println("Recurso no encontrado.");
            return;
        }

        LocalDateTime fechaHoraInicio = leerFechaHora("Fecha y Hora de Inicio (YYYY-MM-DD HH:MM): ");
        if (fechaHoraInicio == null) {
            return;
        }
        LocalDateTime fechaHoraFin = leerFechaHora("Fecha y Hora de Fin (YYYY-MM-DD HH:MM): ");
        if (fechaHoraFin == null) {
            return;
        }

        int participantes = 0;
        if (recurso instanceof SalaEstudio) {
            System.out.print("Número de participantes (3-5): ");
            participantes = leerEntero();
            scanner.nextLine();
        }

        prestamoService.reservarRecurso(usuario, recurso, fechaHoraInicio, fechaHoraFin, participantes);
    }

    private void liberarReservaRecurso() {
        System.out.println("\n--- LIBERAR RESERVA DE RECURSO ---");
        System.out.print("Ingrese el ID de la reserva a liberar: ");
        int idReserva = leerEntero();
        scanner.nextLine(); // Consumir nueva línea

        ReservaRecurso reserva = prestamoService.getReservaRecursoById(idReserva);
        if (reserva == null) {
            System.out.println("Reserva no encontrada con ID: " + idReserva);
            return;
        }

        prestamoService.liberarRecurso(reserva);
    }

    private void extenderReservaRecurso() {
        System.out.println("\n--- EXTENDER RESERVA DE RECURSO ---");
        System.out.print("Ingrese el ID de la reserva a extender: ");
        int idReserva = leerEntero();
        scanner.nextLine(); // Consumir nueva línea

        ReservaRecurso reserva = prestamoService.getReservaRecursoById(idReserva);
        if (reserva == null) {
            System.out.println("Reserva no encontrada con ID: " + idReserva);
            return;
        }

        if (reserva.isActivo() && !reserva.estaVencido()) {
            System.out.println("Fecha y hora de fin actual: " + reserva.getFechaHoraFin().format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
            LocalDateTime nuevaFechaFin = leerFechaHora("Nueva Fecha y Hora de Fin (YYYY-MM-DD HH:MM): ");
            if (nuevaFechaFin == null) {
                return;
            }

            prestamoService.extenderReservaRecurso(reserva, nuevaFechaFin);
        } else {
            System.out.println("La reserva no está activa o ya está vencida, no se puede extender.");
        }
    }

    private void verTodosLosPrestamosMaterial() {
        System.out.println("\n--- TODOS LOS PRÉSTAMOS DE MATERIAL ---");
        List<PrestamoMaterial> prestamos = prestamoService.getAllPrestamosMaterial();
        if (prestamos.isEmpty()) {
            System.out.println("No hay préstamos de materiales registrados.");
        } else {
            prestamos.forEach(System.out::println);
        }
        System.out.println("Presione Enter para continuar...");
        scanner.nextLine();
    }

    private void verTodasLasReservasRecurso() {
        System.out.println("\n--- TODAS LAS RESERVAS DE RECURSO ---");
        List<ReservaRecurso> reservas = prestamoService.getAllReservasRecurso();
        if (reservas.isEmpty()) {
            System.out.println("No hay reservas de recursos registradas.");
        } else {
            reservas.forEach(System.out::println);
        }
        System.out.println("Presione Enter para continuar...");
        scanner.nextLine();
    }

    // --- Métodos de Gestión de Sanciones ---
    private void menuGestionSanciones() {
        int opcion;
        do {
            System.out.println("\n--- GESTIÓN DE SANCIONES ---");
            System.out.println("1. Ver Sanciones por Usuario");
            System.out.println("2. Levantar Sanción");
            System.out.println("0. Volver al Menú Principal");
            System.out.print("Seleccione una opción: ");
            opcion = leerEntero();

            switch (opcion) {
                case 1:
                    verSancionesPorUsuario();
                    break;
                case 2:
                    levantarSancion();
                    break;
                case 0:
                    System.out.println("Volviendo...");
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

    private void verSancionesPorUsuario() {
        System.out.println("\n--- VER SANCIONES POR USUARIO ---");
        System.out.print("Ingrese el ID del usuario: ");
        int idUsuario = leerEntero();
        scanner.nextLine(); // Consumir nueva línea

        Usuario usuario = sancionService.getUsuarioById(idUsuario);
        if (usuario == null) {
            System.out.println("Usuario no encontrado.");
            return;
        }

        System.out.println("\n--- SANCIONES PARA: " + usuario.getNombreCompleto() + " ---");
        List<Sancion> sanciones = sancionService.getSancionesPorUsuario(usuario);
        if (sanciones.isEmpty()) {
            System.out.println("El usuario " + usuario.getNombreCompleto() + " no tiene sanciones registradas.");
        } else {
            sanciones.forEach(System.out::println);
            if (sancionService.usuarioEstaSancionado(usuario)) {
                System.out.println("\nEl usuario " + usuario.getNombreCompleto() + " ESTÁ SANCIONADO actualmente.");
            } else {
                System.out.println("\nEl usuario " + usuario.getNombreCompleto() + " NO ESTÁ SANCIONADO actualmente.");
            }
        }
        System.out.println("Presione Enter para continuar...");
        scanner.nextLine();
    }

    private void levantarSancion() {
        System.out.println("\n--- LEVANTAR SANCIÓN ---");
        System.out.print("Ingrese el ID de la sanción a levantar: ");
        int idSancion = leerEntero();
        scanner.nextLine(); // Consumir nueva línea

        sancionService.levantarSancion(idSancion);
    }

    // --- Métodos de Utilidad para la UI ---
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

    private LocalDateTime leerFechaHora(String mensaje) {
        System.out.print(mensaje + " ");
        String fechaHoraStr = scanner.nextLine();
        try {
            return LocalDateTime.parse(fechaHoraStr, java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        } catch (DateTimeParseException e) {
            System.out.println("Formato de fecha y hora inválido. Use YYYY-MM-DD HH:MM.");
            return null;
        }
    }

    private CategoriaMaterial seleccionarCategoriaMaterial() {
        System.out.println("Seleccione Categoría de Material:");
        CategoriaMaterial[] categorias = CategoriaMaterial.values();
        for (int i = 0; i < categorias.length; i++) {
            System.out.println((i + 1) + ". " + categorias[i].name());
        }
        while (true) {
            System.out.print("Opción: ");
            int opcion = leerEntero();
            if (opcion > 0 && opcion <= categorias.length) {
                return categorias[opcion - 1];
            } else {
                System.out.println("Opción inválida. Intente de nuevo.");
            }
        }
    }

    // Método principal para ejecutar la aplicación
    public static void main(String[] args) {
        Main ui = new Main();
        ui.start();
    }
}
