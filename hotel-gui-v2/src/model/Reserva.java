package model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Reserva {
    private final int idReserva;
    private final Cliente cliente;
    private final Habitacion habitacion;
    private final LocalDate desde; // check-in (incluido)
    private final LocalDate hasta; // check-out (exclusivo)
    private String estado = "confirmada";

    // === NUEVO: servicios/detalles asociados a la reserva ===
    private final List<DetalleReserva> detalles = new ArrayList<>();

    public Reserva(int idReserva, Cliente cliente, Habitacion habitacion, LocalDate desde, LocalDate hasta) {
        if (idReserva <= 0)
            throw new IllegalArgumentException("idReserva inválido");
        if (cliente == null)
            throw new IllegalArgumentException("Cliente requerido");
        if (habitacion == null)
            throw new IllegalArgumentException("Habitación requerida");
        if (desde == null || hasta == null)
            throw new IllegalArgumentException("Fechas requeridas");
        if (!hasta.isAfter(desde))
            throw new IllegalArgumentException("La fecha de fin debe ser posterior a la de inicio");

        this.idReserva = idReserva;
        this.cliente = cliente;
        this.habitacion = habitacion;
        this.desde = desde;
        this.hasta = hasta;
    }

    public int getIdReserva() {
        return idReserva;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Habitacion getHabitacion() {
        return habitacion;
    }

    public LocalDate getDesde() {
        return desde;
    }

    public LocalDate getHasta() {
        return hasta;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    /** Noches = días entre inicio (incl) y fin (excl). */
    public long getNoches() {
        return ChronoUnit.DAYS.between(desde, hasta);
    }

    /** Total de habitación (derivado) = precioBase × noches. */
    public BigDecimal getTotal() {
        BigDecimal precio = BigDecimal.valueOf(habitacion.getPrecioBase());
        return precio.multiply(BigDecimal.valueOf(getNoches()));
    }

    // ====== SOPORTE PARA DETALLES (SERVICIOS) ======
    /** Detalles solo lectura (no modificar desde afuera). */
    public List<DetalleReserva> getDetalles() {
        return Collections.unmodifiableList(detalles);
    }

    /** Agrega un ítem/servicio (p.ej., “Desayuno x2”). */
    public void agregarDetalle(DetalleReserva d) {
        if (d == null)
            throw new IllegalArgumentException("Detalle requerido");
        detalles.add(d);
    }

    /** Suma de subtotales de todos los servicios agregados. */
    public BigDecimal getTotalServicios() {
        return detalles.stream()
                .map(DetalleReserva::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Total general = Habitación + Servicios (útil si querés mostrar un total
     * final).
     */
    public BigDecimal getTotalGeneral() {
        return getTotal().add(getTotalServicios());
    }

    @Override
    public String toString() {
        return cliente.getNombre() + " - Hab." + habitacion.getNumero()
                + " (" + desde + " a " + hasta + ")";
    }
}
