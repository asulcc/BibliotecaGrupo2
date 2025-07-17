package modelos.recursos;

public class PC extends Recurso {
    private String sistemaOperativo;
    private String especificaciones; // Ej: "Core i7, 16GB RAM, SSD 512GB"

    public PC(int id, String codigo, String ubicacion, TipoRecurso tipo, String sistemaOperativo, String especificaciones) {
        super(id, codigo, ubicacion, tipo.PC);
        this.sistemaOperativo = sistemaOperativo;
        this.especificaciones = especificaciones;
    }

    // Getters y Setters específicos
    public String getSistemaOperativo() {
        return sistemaOperativo;
    }

    public void setSistemaOperativo(String sistemaOperativo) {
        this.sistemaOperativo = sistemaOperativo;
    }

    public String getEspecificaciones() {
        return especificaciones;
    }

    public void setEspecificaciones(String especificaciones) {
        this.especificaciones = especificaciones;
    }

    @Override
    public void mostrarDetalles() {
        System.out.println("--- Detalles de la PC ---");
        System.out.println("ID: " + getId());
        System.out.println("Código: " + getCodigo());
        System.out.println("Ubicación: " + getUbicacion());
        System.out.println("Sistema Operativo: " + getSistemaOperativo());
        System.out.println("Especificaciones: " + getEspecificaciones());
        System.out.println("Disponible: " + (isDisponible() ? "Sí" : "No"));
        System.out.println("-------------------------");
    }
}

