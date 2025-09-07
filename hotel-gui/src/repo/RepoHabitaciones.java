package repo;

import model.Habitacion;
import java.util.*;

public class RepoHabitaciones {
    private final Map<Integer, Habitacion> porNumero = new LinkedHashMap<>();

    public List<Habitacion> listar() {
        return new ArrayList<>(porNumero.values());
    }

    public void agregar(Habitacion h) {
        if (porNumero.containsKey(h.getNumero())) {
            throw new IllegalArgumentException("Ya existe una habitación con ese número");
        }
        porNumero.put(h.getNumero(), h);
    }

    public void eliminar(int numero) {
        porNumero.remove(numero);
    }
}
