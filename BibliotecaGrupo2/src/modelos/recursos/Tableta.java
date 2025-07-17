package modelos.recursos;

public class Tableta extends Recurso {
    private String marca;
    private String modelo;

    public Tableta(int id, String codigo, String ubicacion, TipoRecurso tipo, String marca, String modelo) {
        super(id, codigo, ubicacion, tipo.TABLETA);
        this.marca = marca;
        this.modelo = modelo;
    }

    // Getters y Setters específicos
    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    @Override
    public void mostrarDetalles() {
        System.out.println("--- Detalles de la Tableta ---");
        System.out.println("ID: " + getId());
        System.out.println("Codigo: " + getCodigo());
        System.out.println("Ubicación: " + getUbicacion());
        System.out.println("Marca: " + getMarca());
        System.out.println("Modelo: " + getModelo());
        System.out.println("Disponible: " + (isDisponible() ? "Sí" : "No"));
        System.out.println("----------------------------");
    }
}

