package model;

public class Habitacion {
    private final int numero;
    private final String tipo;
    private final double precioBase;

    public Habitacion(int numero, String tipo, double precioBase) {
        if (numero <= 0)
            throw new IllegalArgumentException("Número inválido");
        if (tipo == null || tipo.isBlank())
            throw new IllegalArgumentException("Tipo requerido");
        if (precioBase <= 0)
            throw new IllegalArgumentException("Precio debe ser positivo");

        this.numero = numero;
        this.tipo = tipo;
        this.precioBase = precioBase;
    }

    public int getNumero() {
        return numero;
    }

    public String getTipo() {
        return tipo;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    @Override
    public String toString() {
        return "Hab. " + numero + " (" + tipo + ") $" + precioBase;
    }
}
