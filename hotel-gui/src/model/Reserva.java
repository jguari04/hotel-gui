package model;

import java.time.LocalDate;

public class Reserva {
    private final Cliente cliente;
    private final Habitacion habitacion;
    private final LocalDate desde;
    private final LocalDate hasta;

    public Reserva(Cliente cliente, Habitacion habitacion, LocalDate desde, LocalDate hasta) {
        if (desde == null || hasta == null || !hasta.isAfter(desde)) {
            throw new IllegalArgumentException("Fechas inválidas");
        }
        this.cliente = cliente;
        this.habitacion = habitacion;
        this.desde = desde;
        this.hasta = hasta;
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

    @Override
    public String toString() {
        return cliente.getNombre() + " - Hab." + habitacion.getNumero()
                + " (" + desde + " a " + hasta + ")";
    }
}
