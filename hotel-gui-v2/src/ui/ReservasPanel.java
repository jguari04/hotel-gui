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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

public class ReservasPanel extends JPanel {
    private final RepoClientes repoClientes;
    private final RepoHabitaciones repoHabitaciones;
    private final RepoReservas repoReservas;

    private final JComboBox<Cliente> cmbClientes = new JComboBox<>();
    private final JComboBox<Habitacion> cmbHabitaciones = new JComboBox<>();
    private final JTextField txtDesde = new JTextField(10); // YYYY-MM-DD
    private final JTextField txtHasta = new JTextField(10); // YYYY-MM-DD

    private final JLabel lblNoches = new JLabel("Noches: -");
    private final JLabel lblTotal = new JLabel("Total: $ -");

    private final DefaultTableModel modelTabla = new DefaultTableModel(
            new Object[] { "ID", "Cliente", "Hab", "Desde", "Hasta", "Noches", "Total" }, 0);
    private final JTable tabla = new JTable(modelTabla);

    public ReservasPanel(RepoClientes rc, RepoHabitaciones rh, RepoReservas rr) {
        System.out.println(">>> ReservasPanel v2 (GridBagLayout, con Hasta/Noches/Total/Reservar)");
        this.repoClientes = rc;
        this.repoHabitaciones = rh;
        this.repoReservas = rr;

        setLayout(new BorderLayout(8, 8));
        add(crearFormulario(), BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        cargarCombos();

        // Fechas por defecto + primer cálculo
        txtDesde.setText(LocalDate.now().toString());
        txtHasta.setText(LocalDate.now().plusDays(1).toString());
        recalcular();

        refrescarTabla();
    }

    private JPanel crearFormulario() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.gridy = 0;
        c.gridx = 0;
        c.anchor = GridBagConstraints.WEST;

        // Fila 1: Cliente | Habitación | Desde | Hasta
        p.add(new JLabel("Cliente:"), c);
        c.gridx++;
        p.add(cmbClientes, c);

        c.gridx++;
        p.add(new JLabel("Habitación:"), c);
        c.gridx++;
        p.add(cmbHabitaciones, c);

        c.gridx++;
        p.add(new JLabel("Desde (YYYY-MM-DD):"), c);
        c.gridx++;
        p.add(txtDesde, c);

        c.gridx++;
        p.add(new JLabel("Hasta (YYYY-MM-DD):"), c);
        c.gridx++;
        p.add(txtHasta, c);

        // Fila 2: Noches | Total | Recalcular | Reservar
        c.gridy = 1;
        c.gridx = 0;

        p.add(lblNoches, c);
        c.gridx++;
        p.add(lblTotal, c);

        JButton btnRecalcular = new JButton("Recalcular");
        JButton btnReservar = new JButton("Reservar");

        c.gridx++;
        p.add(btnRecalcular, c);
        c.gridx++;
        p.add(btnReservar, c);

        // Listeners
        cmbHabitaciones.addActionListener(e -> recalcular());
        txtDesde.getDocument().addDocumentListener(SimpleDocListener.of(this::recalcular));
        txtHasta.getDocument().addDocumentListener(SimpleDocListener.of(this::recalcular));
        btnRecalcular.addActionListener(e -> recalcular());
        btnReservar.addActionListener(e -> onReservar());

        return p;
    }

    private void cargarCombos() {
        cmbClientes.removeAllItems();
        for (Cliente c : repoClientes.listar())
            cmbClientes.addItem(c);

        cmbHabitaciones.removeAllItems();
        for (Habitacion h : repoHabitaciones.listar())
            cmbHabitaciones.addItem(h);
    }

    private void onReservar() {
        try {
            Cliente c = (Cliente) cmbClientes.getSelectedItem();
            Habitacion h = (Habitacion) cmbHabitaciones.getSelectedItem();
            LocalDate desde = LocalDate.parse(txtDesde.getText().trim());
            LocalDate hasta = LocalDate.parse(txtHasta.getText().trim());

            int nextId = repoReservas.listar().size() + 1;
            Reserva r = new Reserva(nextId, c, h, desde, hasta);
            repoReservas.agregar(r); // valida solapamiento

            limpiarCampos();
            refrescarTabla();
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de fecha inválido (use YYYY-MM-DD).",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        txtDesde.setText(LocalDate.now().toString());
        txtHasta.setText(LocalDate.now().plusDays(1).toString());
        lblNoches.setText("Noches: -");
        lblTotal.setText("Total: $ -");
    }

    private void refrescarTabla() {
        modelTabla.setRowCount(0);
        repoReservas.listar().forEach(r -> modelTabla.addRow(new Object[] {
                r.getIdReserva(),
                r.getCliente().getNombre(),
                r.getHabitacion().getNumero(),
                r.getDesde(),
                r.getHasta(),
                r.getNoches(),
                r.getTotal()
        }));
    }

    private void recalcular() {
        try {
            Habitacion hab = (Habitacion) cmbHabitaciones.getSelectedItem();
            if (hab == null) {
                lblNoches.setText("Noches: -");
                lblTotal.setText("Total: $ -");
                return;
            }

            LocalDate desde = LocalDate.parse(txtDesde.getText().trim());
            LocalDate hasta = LocalDate.parse(txtHasta.getText().trim());

            long noches = ChronoUnit.DAYS.between(desde, hasta); // check-in incl, check-out excl
            if (noches <= 0) {
                lblNoches.setText("Noches: -");
                lblTotal.setText("Total: $ -");
                return;
            }

            lblNoches.setText("Noches: " + noches);

            BigDecimal total = BigDecimal.valueOf(hab.getPrecioBase())
                    .multiply(BigDecimal.valueOf(noches));
            lblTotal.setText("Total: $ " + total);
        } catch (Exception ex) {
            lblNoches.setText("Noches: -");
            lblTotal.setText("Total: $ -");
        }
    }
}

/** Listener simple para reaccionar a cambios en JTextField sin boilerplate. */
class SimpleDocListener implements javax.swing.event.DocumentListener {
    private final Runnable cb;

    private SimpleDocListener(Runnable cb) {
        this.cb = cb;
    }

    public static SimpleDocListener of(Runnable cb) {
        return new SimpleDocListener(cb);
    }

    public void insertUpdate(javax.swing.event.DocumentEvent e) {
        cb.run();
    }

    public void removeUpdate(javax.swing.event.DocumentEvent e) {
        cb.run();
    }

    public void changedUpdate(javax.swing.event.DocumentEvent e) {
        cb.run();
    }
}
