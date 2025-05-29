package com.example.Gui;

import com.example.dao.MedicamentoDAO;
import com.example.dao.PacienteDAO;
import com.example.example.model.Medicamento;
import com.example.example.model.Paciente;
import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class PacientesGUI extends JFrame {

    private Session session;
    private PacienteDAO pDAO;
    private MedicamentoDAO mDAO;

    // Modelos de tablas
    private DefaultTableModel modelPacientes;
    private DefaultTableModel modelMedicamentos;
    private DefaultTableModel modelPacientesMedicamentos;

    // Componentes de la interfaz
    private JTable tablePacientes;
    private JTable tableMedicamentos;
    private JTable tablePacientesMedicamentos;

    private JTextField txtIdPaciente;
    private JTextField txtNombrePaciente;
    private JTextField txtGlucosa;
    private JTextField txtHierro;

    private JTextField txtIdMedicamento;
    private JTextField txtNombreMedicamento;

    public PacientesGUI() {
        session = HibernateUtil.getSessionFactory().openSession();
        pDAO = new PacienteDAO();
        mDAO = new MedicamentoDAO();

        setTitle("Gestión de Pacientes y Medicamentos");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        JTabbedPane tabbedPane = new JTabbedPane();

        // Panel de Pacientes
        JPanel panelPacientes = crearPanelPacientes();
        tabbedPane.addTab("Pacientes", panelPacientes);

        // Panel de Medicamentos
        JPanel panelMedicamentos = crearPanelMedicamentos();
        tabbedPane.addTab("Medicamentos", panelMedicamentos);

        // Panel de Asociaciones
        JPanel panelAsociaciones = crearPanelAsociaciones();
        tabbedPane.addTab("Asociaciones", panelAsociaciones);

        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        add(mainPanel);

        cargarDatosPacientes();
        cargarDatosMedicamentos();
        cargarDatosPacientesMedicamentos();
    }

    private JPanel crearPanelPacientes() {
        JPanel panel = new JPanel(null);
        panel.setBackground(Color.WHITE);

        // Modelo y tabla de pacientes
        modelPacientes = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modelPacientes.addColumn("ID");
        modelPacientes.addColumn("Nombre");
        modelPacientes.addColumn("Glucosa");
        modelPacientes.addColumn("Hierro");

        tablePacientes = new JTable(modelPacientes);
        tablePacientes.setSelectionBackground(new Color(180, 210, 240));
        tablePacientes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = tablePacientes.getSelectedRow();
                if (index >= 0) {
                    txtIdPaciente.setText(modelPacientes.getValueAt(index, 0).toString());
                    txtNombrePaciente.setText(modelPacientes.getValueAt(index, 1).toString());
                    txtGlucosa.setText(modelPacientes.getValueAt(index, 2).toString());
                    txtHierro.setText(modelPacientes.getValueAt(index, 3).toString());
                }
            }
        });

        JScrollPane scrollPacientes = new JScrollPane(tablePacientes);
        scrollPacientes.setBounds(20, 20, 600, 200);
        panel.add(scrollPacientes);

        // Panel de controles para pacientes
        JPanel controlPanel = new JPanel(null);
        controlPanel.setBounds(20, 240, 600, 300);
        controlPanel.setBackground(new Color(240, 240, 240));
        controlPanel.setBorder(BorderFactory.createTitledBorder("Gestión de Pacientes"));

        // Campos de texto
        JLabel lblId = new JLabel("ID:");
        lblId.setBounds(20, 30, 80, 25);
        controlPanel.add(lblId);

        txtIdPaciente = new JTextField();
        txtIdPaciente.setBounds(100, 30, 150, 25);
        txtIdPaciente.setEditable(false);
        controlPanel.add(txtIdPaciente);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(20, 70, 80, 25);
        controlPanel.add(lblNombre);

        txtNombrePaciente = new JTextField();
        txtNombrePaciente.setBounds(100, 70, 150, 25);
        controlPanel.add(txtNombrePaciente);

        JLabel lblGlucosa = new JLabel("Glucosa:");
        lblGlucosa.setBounds(20, 110, 80, 25);
        controlPanel.add(lblGlucosa);

        txtGlucosa = new JTextField();
        txtGlucosa.setBounds(100, 110, 150, 25);
        controlPanel.add(txtGlucosa);

        JLabel lblHierro = new JLabel("Hierro:");
        lblHierro.setBounds(20, 150, 80, 25);
        controlPanel.add(lblHierro);

        txtHierro = new JTextField();
        txtHierro.setBounds(100, 150, 150, 25);
        controlPanel.add(txtHierro);

        // Botones
        JButton btnNuevoPaciente = new JButton("Nuevo");
        btnNuevoPaciente.setBounds(300, 30, 100, 25);
        btnNuevoPaciente.addActionListener(e -> limpiarCamposPaciente());
        controlPanel.add(btnNuevoPaciente);

        JButton btnGuardarPaciente = new JButton("Guardar");
        btnGuardarPaciente.setBounds(300, 70, 100, 25);
        btnGuardarPaciente.addActionListener(e -> guardarPaciente());
        controlPanel.add(btnGuardarPaciente);

        JButton btnActualizarPaciente = new JButton("Actualizar");
        btnActualizarPaciente.setBounds(300, 110, 100, 25);
        btnActualizarPaciente.addActionListener(e -> actualizarPaciente());
        controlPanel.add(btnActualizarPaciente);

        JButton btnEliminarPaciente = new JButton("Eliminar");
        btnEliminarPaciente.setBounds(300, 150, 100, 25);
        btnEliminarPaciente.addActionListener(e -> eliminarPaciente());
        controlPanel.add(btnEliminarPaciente);

        panel.add(controlPanel);

        return panel;
    }

    private JPanel crearPanelMedicamentos() {
        JPanel panel = new JPanel(null);
        panel.setBackground(Color.WHITE);

        // Modelo y tabla de medicamentos
        modelMedicamentos = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modelMedicamentos.addColumn("ID");
        modelMedicamentos.addColumn("Nombre");

        tableMedicamentos = new JTable(modelMedicamentos);
        tableMedicamentos.setSelectionBackground(new Color(180, 210, 240));
        tableMedicamentos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = tableMedicamentos.getSelectedRow();
                if (index >= 0) {
                    txtIdMedicamento.setText(modelMedicamentos.getValueAt(index, 0).toString());
                    txtNombreMedicamento.setText(modelMedicamentos.getValueAt(index, 1).toString());
                }
            }
        });

        JScrollPane scrollMedicamentos = new JScrollPane(tableMedicamentos);
        scrollMedicamentos.setBounds(20, 20, 600, 200);
        panel.add(scrollMedicamentos);

        // Panel de controles para medicamentos
        JPanel controlPanel = new JPanel(null);
        controlPanel.setBounds(20, 240, 600, 200);
        controlPanel.setBackground(new Color(240, 240, 240));
        controlPanel.setBorder(BorderFactory.createTitledBorder("Gestión de Medicamentos"));

        // Campos de texto
        JLabel lblIdMed = new JLabel("ID:");
        lblIdMed.setBounds(20, 30, 80, 25);
        controlPanel.add(lblIdMed);

        txtIdMedicamento = new JTextField();
        txtIdMedicamento.setBounds(100, 30, 150, 25);
        txtIdMedicamento.setEditable(false);
        controlPanel.add(txtIdMedicamento);

        JLabel lblNombreMed = new JLabel("Nombre:");
        lblNombreMed.setBounds(20, 70, 80, 25);
        controlPanel.add(lblNombreMed);

        txtNombreMedicamento = new JTextField();
        txtNombreMedicamento.setBounds(100, 70, 150, 25);
        controlPanel.add(txtNombreMedicamento);

        // Botones
        JButton btnNuevoMedicamento = new JButton("Nuevo");
        btnNuevoMedicamento.setBounds(300, 30, 100, 25);
        btnNuevoMedicamento.addActionListener(e -> limpiarCamposMedicamento());
        controlPanel.add(btnNuevoMedicamento);

        JButton btnGuardarMedicamento = new JButton("Guardar");
        btnGuardarMedicamento.setBounds(300, 70, 100, 25);
        btnGuardarMedicamento.addActionListener(e -> guardarMedicamento());
        controlPanel.add(btnGuardarMedicamento);

        JButton btnActualizarMedicamento = new JButton("Actualizar");
        btnActualizarMedicamento.setBounds(300, 110, 100, 25);
        btnActualizarMedicamento.addActionListener(e -> actualizarMedicamento());
        controlPanel.add(btnActualizarMedicamento);

        JButton btnEliminarMedicamento = new JButton("Eliminar");
        btnEliminarMedicamento.setBounds(300, 150, 100, 25);
        btnEliminarMedicamento.addActionListener(e -> eliminarMedicamento());
        controlPanel.add(btnEliminarMedicamento);

        panel.add(controlPanel);

        return panel;
    }

    private JPanel crearPanelAsociaciones() {
        JPanel panel = new JPanel(null);
        panel.setBackground(Color.WHITE);

        // Modelo y tabla de pacientes con medicamentos
        modelPacientesMedicamentos = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modelPacientesMedicamentos.addColumn("ID Paciente");
        modelPacientesMedicamentos.addColumn("Nombre Paciente");
        modelPacientesMedicamentos.addColumn("Medicamentos");

        tablePacientesMedicamentos = new JTable(modelPacientesMedicamentos);
        tablePacientesMedicamentos.setSelectionBackground(new Color(180, 210, 240));

        JScrollPane scrollPacientesMed = new JScrollPane(tablePacientesMedicamentos);
        scrollPacientesMed.setBounds(20, 20, 600, 200);
        panel.add(scrollPacientesMed);

        // Panel de controles para asociaciones
        JPanel controlPanel = new JPanel(null);
        controlPanel.setBounds(20, 240, 600, 200);
        controlPanel.setBackground(new Color(240, 240, 240));
        controlPanel.setBorder(BorderFactory.createTitledBorder("Gestión de Asociaciones"));

        // Campos para asociar
        JLabel lblPacienteAsociar = new JLabel("ID Paciente:");
        lblPacienteAsociar.setBounds(20, 30, 100, 25);
        controlPanel.add(lblPacienteAsociar);

        JTextField txtPacienteAsociar = new JTextField();
        txtPacienteAsociar.setBounds(120, 30, 100, 25);
        controlPanel.add(txtPacienteAsociar);

        JLabel lblMedicamentoAsociar = new JLabel("ID Medicamento:");
        lblMedicamentoAsociar.setBounds(20, 70, 100, 25);
        controlPanel.add(lblMedicamentoAsociar);

        JTextField txtMedicamentoAsociar = new JTextField();
        txtMedicamentoAsociar.setBounds(120, 70, 100, 25);
        controlPanel.add(txtMedicamentoAsociar);

        // Botones
        JButton btnAsociar = new JButton("Asociar");
        btnAsociar.setBounds(250, 30, 100, 25);
        btnAsociar.addActionListener(e -> {
            int idPaciente = Integer.parseInt(txtPacienteAsociar.getText());
            int idMedicamento = Integer.parseInt(txtMedicamentoAsociar.getText());
            asociarPacienteMedicamento(idPaciente, idMedicamento);
        });
        controlPanel.add(btnAsociar);

        JButton btnDesasociar = new JButton("Desasociar");
        btnDesasociar.setBounds(250, 70, 100, 25);
        btnDesasociar.addActionListener(e -> {
            int idPaciente = Integer.parseInt(txtPacienteAsociar.getText());
            int idMedicamento = Integer.parseInt(txtMedicamentoAsociar.getText());
            desasociarPacienteMedicamento(idPaciente, idMedicamento);
        });
        controlPanel.add(btnDesasociar);

        panel.add(controlPanel);

        return panel;
    }

    // Métodos para cargar datos
    private void cargarDatosPacientes() {
        modelPacientes.setRowCount(0);
        List<Paciente> pacientes = pDAO.selectAllPacientes(session);
        for (Paciente p : pacientes) {
            modelPacientes.addRow(new Object[] {
                    p.getId(),
                    p.getNombre(),
                    p.getNivelGlucosa(),
                    p.getNivelHierroSangre()
            });
        }
    }

    private void cargarDatosMedicamentos() {
        modelMedicamentos.setRowCount(0);
        List<Medicamento> medicamentos = mDAO.selectAllMedicamentos(session);
        for (Medicamento m : medicamentos) {
            modelMedicamentos.addRow(new Object[] {
                    m.getId(),
                    m.getNombre()
            });
        }
    }

    private void cargarDatosPacientesMedicamentos() {
        modelPacientesMedicamentos.setRowCount(0);
        List<Paciente> pacientes = pDAO.selectAllPacientes(session);
        for (Paciente p : pacientes) {
            StringBuilder medicamentos = new StringBuilder();
            for (Medicamento m : p.getMedicamentos()) {
                medicamentos.append(m.getNombre()).append(", ");
            }
            // Eliminar la última coma y espacio
            String medicamentosStr = medicamentos.length() > 0 ? medicamentos.substring(0, medicamentos.length() - 2)
                    : "Ninguno";

            modelPacientesMedicamentos.addRow(new Object[] {
                    p.getId(),
                    p.getNombre(),
                    medicamentosStr
            });
        }
    }

    // Métodos para operaciones con pacientes
    private void limpiarCamposPaciente() {
        txtIdPaciente.setText("");
        txtNombrePaciente.setText("");
        txtGlucosa.setText("");
        txtHierro.setText("");
    }

    private void guardarPaciente() {
        String nombre = txtNombrePaciente.getText();
        int glucosa = Integer.parseInt(txtGlucosa.getText());
        int hierro = Integer.parseInt(txtHierro.getText());

        Paciente nuevoPaciente = new Paciente(nombre, glucosa, hierro);

        Transaction tx = session.beginTransaction();
        try {
            pDAO.insertPaciente(session, nuevoPaciente);
            tx.commit();
            cargarDatosPacientes();
            cargarDatosPacientesMedicamentos();
            JOptionPane.showMessageDialog(this, "Paciente guardado correctamente");
        } catch (Exception e) {
            tx.rollback();
            JOptionPane.showMessageDialog(this, "Error al guardar paciente: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarPaciente() {
        if (txtIdPaciente.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un paciente para actualizar");
            return;
        }

        int id = Integer.parseInt(txtIdPaciente.getText());
        String nombre = txtNombrePaciente.getText();
        int glucosa = Integer.parseInt(txtGlucosa.getText());
        int hierro = Integer.parseInt(txtHierro.getText());

        Paciente paciente = pDAO.selectPacienteById(session, id);
        if (paciente != null) {
            paciente.setNombre(nombre);
            paciente.setNivelGlucosa(glucosa);
            paciente.setNivelHierroSangre(hierro);

            Transaction tx = session.beginTransaction();
            try {
                pDAO.updatePaciente(session, paciente);
                tx.commit();
                cargarDatosPacientes();
                cargarDatosPacientesMedicamentos();
                JOptionPane.showMessageDialog(this, "Paciente actualizado correctamente");
            } catch (Exception e) {
                tx.rollback();
                JOptionPane.showMessageDialog(this, "Error al actualizar paciente: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void eliminarPaciente() {
        if (txtIdPaciente.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un paciente para eliminar");
            return;
        }

        int id = Integer.parseInt(txtIdPaciente.getText());

        Transaction tx = session.beginTransaction();
        try {
            Paciente paciente = pDAO.selectPacienteById(session, id);
            if (paciente != null) {
                // Eliminar referencias primero
                for (Medicamento m : paciente.getMedicamentos()) {
                    paciente.quitarMedicamento(m);
                }

                pDAO.deletePaciente(session, id);
                tx.commit();
                cargarDatosPacientes();
                cargarDatosPacientesMedicamentos();
                limpiarCamposPaciente();
                JOptionPane.showMessageDialog(this, "Paciente eliminado correctamente");
            }
        } catch (Exception e) {
            tx.rollback();
            JOptionPane.showMessageDialog(this, "Error al eliminar paciente: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Métodos para operaciones con medicamentos
    private void limpiarCamposMedicamento() {
        txtIdMedicamento.setText("");
        txtNombreMedicamento.setText("");
    }

    private void guardarMedicamento() {
        String nombre = txtNombreMedicamento.getText();

        Medicamento nuevoMedicamento = new Medicamento(nombre);

        Transaction tx = session.beginTransaction();
        try {
            mDAO.insertMedicamento(session, nuevoMedicamento);
            tx.commit();
            cargarDatosMedicamentos();
            cargarDatosPacientesMedicamentos();
            JOptionPane.showMessageDialog(this, "Medicamento guardado correctamente");
        } catch (Exception e) {
            tx.rollback();
            JOptionPane.showMessageDialog(this, "Error al guardar medicamento: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarMedicamento() {
        if (txtIdMedicamento.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un medicamento para actualizar");
            return;
        }

        int id = Integer.parseInt(txtIdMedicamento.getText());
        String nombre = txtNombreMedicamento.getText();

        Medicamento medicamento = mDAO.selectMedicamentoById(session, id);
        if (medicamento != null) {
            medicamento.setNombre(nombre);

            Transaction tx = session.beginTransaction();
            try {
                mDAO.updateMedicamento(session, medicamento);
                tx.commit();
                cargarDatosMedicamentos();
                cargarDatosPacientesMedicamentos();
                JOptionPane.showMessageDialog(this, "Medicamento actualizado correctamente");
            } catch (Exception e) {
                tx.rollback();
                JOptionPane.showMessageDialog(this, "Error al actualizar medicamento: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void eliminarMedicamento() {
        if (txtIdMedicamento.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un medicamento para eliminar");
            return;
        }

        int id = Integer.parseInt(txtIdMedicamento.getText());

        Transaction tx = session.beginTransaction();
        try {
            Medicamento medicamento = mDAO.selectMedicamentoById(session, id);
            if (medicamento != null) {
                // Eliminar referencias primero
                for (Paciente p : medicamento.getPacientes()) {
                    medicamento.quitarPaciente(p);
                }

                mDAO.deleteMedicamento(session, id);
                tx.commit();
                cargarDatosMedicamentos();
                cargarDatosPacientesMedicamentos();
                limpiarCamposMedicamento();
                JOptionPane.showMessageDialog(this, "Medicamento eliminado correctamente");
            }
        } catch (Exception e) {
            tx.rollback();
            JOptionPane.showMessageDialog(this, "Error al eliminar medicamento: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Métodos para operaciones de asociación
    private void asociarPacienteMedicamento(int idPaciente, int idMedicamento) {
        Transaction tx = session.beginTransaction();
        try {
            Paciente paciente = pDAO.selectPacienteById(session, idPaciente);
            Medicamento medicamento = mDAO.selectMedicamentoById(session, idMedicamento);

            if (paciente != null && medicamento != null) {
                medicamento.agregarPaciente(paciente);
                mDAO.updateMedicamento(session, medicamento);
                tx.commit();
                cargarDatosPacientesMedicamentos();
                JOptionPane.showMessageDialog(this, "Asociación realizada correctamente");
            } else {
                JOptionPane.showMessageDialog(this, "Paciente o medicamento no encontrado",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            tx.rollback();
            JOptionPane.showMessageDialog(this, "Error al asociar: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void desasociarPacienteMedicamento(int idPaciente, int idMedicamento) {
        Transaction tx = session.beginTransaction();
        try {
            Paciente paciente = pDAO.selectPacienteById(session, idPaciente);
            Medicamento medicamento = mDAO.selectMedicamentoById(session, idMedicamento);

            if (paciente != null && medicamento != null) {
                medicamento.quitarPaciente(paciente);
                mDAO.updateMedicamento(session, medicamento);
                tx.commit();
                cargarDatosPacientesMedicamentos();
                JOptionPane.showMessageDialog(this, "Desasociación realizada correctamente");
            } else {
                JOptionPane.showMessageDialog(this, "Paciente o medicamento no encontrado",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            tx.rollback();
            JOptionPane.showMessageDialog(this, "Error al desasociar: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PacientesGUI gui = new PacientesGUI();
            gui.setVisible(true);
        });
    }
}