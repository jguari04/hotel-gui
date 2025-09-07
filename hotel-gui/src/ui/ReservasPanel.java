package ui;

import model.Cliente;
import model.Habitacion;
import model.Reserva;
import repo.RepoClientes;
import repo.RepoHabitaciones;
import repo.RepoReservas;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;

public class ReservasPanel extends JPanel {
    private final RepoClientes repoClientes;
    private final RepoHabitaciones repoHabitaciones;
    private final RepoReservas repoReservas;

    private final JComboBox<Cliente> cmbClientes = new JComboBox<>();
    private final JComboBox<Habitacion> cmbHabitaciones = new JComboBox<>();
    private final JTextField txtDesde = new JTextField(10);
    private final JTextField txtHasta = new JTextField(10);

    private final DefaultTableModel modelTabla = new DefaultTableModel(
            new Object[] { "Cliente", "Habitación", "Desde", "Hasta" }, 0);
    private final JTable tabla = new JTable(modelTabla);

    public ReservasPanel(RepoClientes rc, RepoHabitaciones rh, RepoReservas rr) {
        this.repoClientes = rc;
        this.repoHabitaciones = rh;
        this.repoReservas = rr;

        setLayout(new BorderLayout(8, 8));
        add(crearFormulario(), BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        refrescarCombos();
    }

    private JPanel crearFormulario() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        p.add(new JLabel("Cliente:"));
        p.add(cmbClientes);
        p.add(new JLabel("Habitación:"));
        p.add(cmbHabitaciones);
        p.add(new JLabel("Desde (YYYY-MM-DD):"));
        p.add(txtDesde);
        p.add(new JLabel("Hasta (YYYY-MM-DD):"));
        p.add(txtHasta);

        JButton btnAgregar = new JButton("Reservar");
        p.add(btnAgregar);
        btnAgregar.addActionListener(e -> onReservar());
        return p;
    }

    private void onReservar() {
        try {
            Cliente cliente = (Cliente) cmbClientes.getSelectedItem();
            Habitacion habitacion = (Habitacion) cmbHabitaciones.getSelectedItem();
            LocalDate desde = LocalDate.parse(txtDesde.getText().trim());
            LocalDate hasta = LocalDate.parse(txtHasta.getText().trim());

            Reserva r = new Reserva(cliente, habitacion, desde, hasta);
            repoReservas.agregar(r);
            refrescarTabla();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refrescarCombos() {
        cmbClientes.removeAllItems();
        for (Cliente c : repoClientes.listar())
            cmbClientes.addItem(c);

        cmbHabitaciones.removeAllItems();
        for (Habitacion h : repoHabitaciones.listar())
            cmbHabitaciones.addItem(h);
    }

    private void refrescarTabla() {
        modelTabla.setRowCount(0);
        for (Reserva r : repoReservas.listar()) {
            modelTabla.addRow(new Object[] {
                    r.getCliente().getNombre(),
                    r.getHabitacion().getNumero(),
                    r.getDesde(),
                    r.getHasta()
            });
        }
    }
}
