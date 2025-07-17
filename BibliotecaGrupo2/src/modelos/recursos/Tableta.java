package modelos.recursos;

public class Tableta extends Recurso {
    private final String marca;
    private final int pulgadas;

    public Tableta(String id, String nombre, String ubicacion, String marca, int pulgadas) {
        super(id, nombre, ubicacion);
        if (marca == null || marca.isBlank()) {
            throw new IllegalArgumentException("La marca no puede estar vacía");
        }
        if (pulgadas <= 0) {
            throw new IllegalArgumentException("Las pulgadas deben ser mayores que cero");
        }
        this.marca = marca;
        this.pulgadas = pulgadas;
    }

    public String getMarca() {
        return marca;
    }

    public int getPulgadas() {
        return pulgadas;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(", marca=%s, pulgadas=%d", marca, pulgadas);
    }
}

