package ui;

import repo.RepoHabitaciones;
import model.Habitacion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class HabitacionesPanel extends JPanel {
    private final RepoHabitaciones repo;

    private final JTextField txtNumero = new JTextField(5);
    private final JTextField txtTipo = new JTextField(10);
    private final JTextField txtPrecio = new JTextField(7);

    private final DefaultTableModel modelTabla = new DefaultTableModel(new Object[] { "Número", "Tipo", "Precio" }, 0);
    private final JTable tabla = new JTable(modelTabla);

    public HabitacionesPanel(RepoHabitaciones repo) {
        this.repo = repo;
        setLayout(new BorderLayout(8, 8));
        add(crearFormulario(), BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        refrescarTabla();
    }

    private JPanel crearFormulario() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        p.add(new JLabel("Número:"));
        p.add(txtNumero);
        p.add(new JLabel("Tipo:"));
        p.add(txtTipo);
        p.add(new JLabel("Precio:"));
        p.add(txtPrecio);

        JButton btnAgregar = new JButton("Agregar");
        JButton btnEliminar = new JButton("Eliminar seleccionado");
        p.add(btnAgregar);
        p.add(btnEliminar);

        btnAgregar.addActionListener(e -> onAgregar());
        btnEliminar.addActionListener(e -> onEliminar());
        return p;
    }

    private void onAgregar() {
        try {
            int numero = Integer.parseInt(txtNumero.getText().trim());
            double precio = Double.parseDouble(txtPrecio.getText().trim());
            Habitacion h = new Habitacion(numero, txtTipo.getText().trim(), precio);
            repo.agregar(h);
            limpiarFormulario();
            refrescarTabla();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Número y precio deben ser numéricos", "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onEliminar() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccioná una fila", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int numero = (int) modelTabla.getValueAt(fila, 0);
        repo.eliminar(numero);
        refrescarTabla();
    }

    private void refrescarTabla() {
        modelTabla.setRowCount(0);
        for (Habitacion h : repo.listar()) {
            modelTabla.addRow(new Object[] { h.getNumero(), h.getTipo(), h.getPrecioBase() });
        }
    }

    private void limpiarFormulario() {
        txtNumero.setText("");
        txtTipo.setText("");
        txtPrecio.setText("");
        txtNumero.requestFocus();
    }
}
