package repo;

import model.Reserva;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RepoReservas {
    private final List<Reserva> reservas = new ArrayList<>();

    public List<Reserva> listar() {
        return new ArrayList<>(reservas);
    }

    public void agregar(Reserva r) {
        for (Reserva ex : reservas) {
            boolean mismaHabitacion = ex.getHabitacion().getNumero() == r.getHabitacion().getNumero();
            if (mismaHabitacion && seSolapan(r.getDesde(), r.getHasta(), ex.getDesde(), ex.getHasta())) {
                throw new IllegalArgumentException("La habitación ya está reservada en esas fechas");
            }
        }
        reservas.add(r);
    }

    public void eliminarPorId(int idReserva) {
        reservas.removeIf(r -> r.getIdReserva() == idReserva);
    }

    /**
     * Intervalos [aIni,aFin) y [bIni,bFin) solapan sii: aIni < bFin && bIni < aFin
     */
    private boolean seSolapan(LocalDate aIni, LocalDate aFin, LocalDate bIni, LocalDate bFin) {
        return aIni.isBefore(bFin) && bIni.isBefore(aFin);
    }
}
