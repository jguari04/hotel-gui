package repo;

import model.Cliente;
import java.util.*;

public class RepoClientes {
    // Clave única: email
    private final Map<String, Cliente> porEmail = new LinkedHashMap<>();

    public List<Cliente> listar() {
        return new ArrayList<>(porEmail.values());
    }

    public void agregar(Cliente c) {
        if (porEmail.containsKey(c.getEmail())) {
            throw new IllegalArgumentException("Ya existe un cliente con ese email");
        }
        porEmail.put(c.getEmail(), c);
    }

    public void eliminarPorEmail(String email) {
        porEmail.remove(email);
    }

    // Extra (útil para filtros): buscar por apellido (contiene, case-insensitive)
    public List<Cliente> buscarPorApellido(String texto) {
        String t = texto.toLowerCase();
        List<Cliente> out = new ArrayList<>();
        for (Cliente c : porEmail.values()) {
            if (c.getApellido().toLowerCase().contains(t))
                out.add(c);
        }
        return out;
    }
}
