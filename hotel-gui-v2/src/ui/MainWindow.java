package ui;

import repo.RepoClientes;
import repo.RepoHabitaciones;
import repo.RepoReservas;
import model.Cliente;
import model.Habitacion;

import javax.swing.*;

public class MainWindow extends JFrame {
    public MainWindow() {
        super("Reservas de Hotel - MVP");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 520);
        setLocationRelativeTo(null);

        // Repos compartidos (una sola instancia por app)
        RepoClientes repoClientes = new RepoClientes();
        RepoHabitaciones repoHabitaciones = new RepoHabitaciones();
        RepoReservas repoReservas = new RepoReservas();

        // Semilla de prueba (opcional)
        try {
            repoClientes.agregar(new Cliente("jose", "campos", 24, "example@gmail.com"));
        } catch (Exception ignore) {
        }
        try {
            repoHabitaciones.agregar(new Habitacion(101, "simple", 50.0));
        } catch (Exception ignore) {
        }

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Clientes", new ClientesPanel(repoClientes));
        tabs.addTab("Habitaciones", new HabitacionesPanel(repoHabitaciones));
        tabs.addTab("Reservas", new ReservasPanel(repoClientes, repoHabitaciones, repoReservas));

        setContentPane(tabs);
    }
}
