package ui;

import repo.RepoClientes;
import model.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ClientesPanel extends JPanel {
    private final RepoClientes repo;

    private final JTextField txtNombre = new JTextField(12);
    private final JTextField txtApellido = new JTextField(12);
    private final JTextField txtEdad = new JTextField(4);
    private final JTextField txtEmail = new JTextField(16);
    private final JTextField txtFiltro = new JTextField(12);

    private final DefaultTableModel modelTabla = new DefaultTableModel(
            new Object[] { "Nombre", "Apellido", "Edad", "Email" }, 0);
    private final JTable tabla = new JTable(modelTabla);

    public ClientesPanel(RepoClientes repo) {
        this.repo = repo;
        setLayout(new BorderLayout(8, 8));
        add(crearFormulario(), BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(crearPie(), BorderLayout.SOUTH);
        refrescarTabla(repo.listar());
    }

    private JPanel crearFormulario() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        p.add(new JLabel("Nombre:"));
        p.add(txtNombre);
        p.add(new JLabel("Apellido:"));
        p.add(txtApellido);
        p.add(new JLabel("Edad:"));
        p.add(txtEdad);
        p.add(new JLabel("Email:"));
        p.add(txtEmail);

        JButton btnAgregar = new JButton("Agregar");
        JButton btnEliminar = new JButton("Eliminar seleccionado");
        p.add(btnAgregar);
        p.add(btnEliminar);

        btnAgregar.addActionListener(e -> onAgregar());
        btnEliminar.addActionListener(e -> onEliminar());
        return p;
    }

    private JPanel crearPie() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        p.add(new JLabel("Filtrar por apellido:"));
        p.add(txtFiltro);
        JButton btnFiltrar = new JButton("Filtrar");
        JButton btnTodos = new JButton("Mostrar todos");
        p.add(btnFiltrar);
        p.add(btnTodos);

        btnFiltrar.addActionListener(e -> {
            String t = txtFiltro.getText().trim();
            refrescarTabla(repo.buscarPorApellido(t));
        });
        btnTodos.addActionListener(e -> {
            txtFiltro.setText("");
            refrescarTabla(repo.listar());
        });
        return p;
    }

    private void onAgregar() {
        try {
            int edad = Integer.parseInt(txtEdad.getText().trim());
            Cliente c = new Cliente(
                    txtNombre.getText().trim(),
                    txtApellido.getText().trim(),
                    edad,
                    txtEmail.getText().trim());
            repo.agregar(c);
            limpiarFormulario();
            refrescarTabla(repo.listar());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "La edad debe ser un entero", "Error", JOptionPane.ERROR_MESSAGE);
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
        String email = (String) modelTabla.getValueAt(fila, 3);
        repo.eliminarPorEmail(email);
        refrescarTabla(repo.listar());
    }

    private void refrescarTabla(java.util.List<Cliente> data) {
        modelTabla.setRowCount(0);
        for (Cliente c : data) {
            modelTabla.addRow(new Object[] { c.getNombre(), c.getApellido(), c.getEdad(), c.getEmail() });
        }
    }

    private void limpiarFormulario() {
        txtNombre.setText("");
        txtApellido.setText("");
        txtEdad.setText("");
        txtEmail.setText("");
        txtNombre.requestFocus();
    }
}
