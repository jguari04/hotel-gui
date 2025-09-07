package repo;

import model.Reserva;
import java.util.*;

public class RepoReservas {
    private final List<Reserva> reservas = new ArrayList<>();

    public List<Reserva> listar() {
        return new ArrayList<>(reservas);
    }

    public void agregar(Reserva r) {
        // validamos solapamiento
        for (Reserva existente : reservas) {
            if (existente.getHabitacion().getNumero() == r.getHabitacion().getNumero()) {
                boolean solapa = !(r.getHasta().isBefore(existente.getDesde()) ||
                        r.getDesde().isAfter(existente.getHasta()));
                if (solapa) {
                    throw new IllegalArgumentException("La habitación ya está reservada en esas fechas");
                }
            }
        }
        reservas.add(r);
    }
}
