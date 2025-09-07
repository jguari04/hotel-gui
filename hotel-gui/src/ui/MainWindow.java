package ui;

import javax.swing.*;
import java.awt.*;

import repo.RepoClientes;
import repo.RepoHabitaciones;
import repo.RepoReservas;

public class MainWindow extends JFrame {

    public MainWindow() {
        super("Reservas de Hotel - MVP");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 520);
        setLocationRelativeTo(null);

        // Repositorios (en memoria)
        var repoClientes = new RepoClientes();
        var repoHabitaciones = new RepoHabitaciones();
        var repoReservas = new RepoReservas();

        // Pestañas
        var tabs = new JTabbedPane();
        tabs.addTab("Clientes", new ClientesPanel(repoClientes));
        tabs.addTab("Habitaciones", new HabitacionesPanel(repoHabitaciones));
        tabs.addTab("Reservas", new ReservasPanel(repoClientes, repoHabitaciones, repoReservas));

        setContentPane(tabs);
    }

    // (Se deja por si querés usarlo de placeholder en alguna pestaña futura)
    private JPanel placeholder(String text) {
        var p = new JPanel(new BorderLayout());
        p.add(new JLabel(text, SwingConstants.CENTER), BorderLayout.CENTER);
        return p;
    }
}
