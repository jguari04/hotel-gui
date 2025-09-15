package model;

import java.math.BigDecimal;

public class DetalleReserva {
    private final String servicio; // p.ej. "Desayuno", "Spa"
    private final BigDecimal precioUnitario; // >= 0
    private final int cantidad; // >= 1

    public DetalleReserva(String servicio, BigDecimal precioUnitario, int cantidad) {
        if (servicio == null || servicio.isBlank())
            throw new IllegalArgumentException("Servicio requerido");
        if (precioUnitario == null || precioUnitario.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("Precio unitario inválido");
        if (cantidad <= 0)
            throw new IllegalArgumentException("Cantidad debe ser >= 1");

        this.servicio = servicio;
        this.precioUnitario = precioUnitario;
        this.cantidad = cantidad;
    }

    public String getServicio() {
        return servicio;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public int getCantidad() {
        return cantidad;
    }

    /** Subtotal derivado: precioUnitario × cantidad. */
    public BigDecimal getSubtotal() {
        return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }

    @Override
    public String toString() {
        return servicio + " x" + cantidad + " ($" + precioUnitario + ")";
    }
}
