package com.example.Gui;

import com.example.dao.MedicamentoDAO;
import com.example.dao.PacienteDAO;
import com.example.model.Medicamento;
import com.example.model.Paciente;
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
import java.util.ArrayList;
import java.util.List;
// imports de las clases helper
// En tu clase GestionPacientesGUI, agrega estos imports adicionales
import com.example.Gui.helper_classes.*;

public class GestionPacientesGUI extends JFrame {

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

    private JTextField txtIdPaciente, txtNombrePaciente, txtGlucosa, txtHierro;
    private JTextField txtIdMedicamento, txtNombreMedicamento;
    private JTextField txtAsociarPaciente, txtAsociarMedicamento;

    public GestionPacientesGUI() {
        // Configuración inicial de la ventana
        setTitle("Gestión de Pacientes y Medicamentos");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Inicialización de Hibernate y DAOs
        session = HibernateUtil.getSessionFactory().openSession();
        pDAO = new PacienteDAO();
        mDAO = new MedicamentoDAO();

        // Cargar fuente personalizada (si existe)
        Font customFont = CustomFontLoader.loadFont("fuentes/roboto.ttf", 12f);
        if (customFont == null) {
            customFont = new Font("Arial", Font.PLAIN, 12);
        }

        // Panel principal con fondo
        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(new Color(240, 245, 250));

        // ============ SECCIÓN PACIENTES ============
        JLabel lblTituloPacientes = new JLabel("PACIENTES");
        lblTituloPacientes.setBounds(20, 20, 200, 25);
        lblTituloPacientes.setFont(CustomFontLoader.loadFont("fonts/roboto-bold.ttf", 16f)); // Fuente personalizada
        lblTituloPacientes.setForeground(new Color(0, 102, 204)); // Color azul
        mainPanel.add(lblTituloPacientes);

        // Tabla de pacientes (muestra todos los pacientes)
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
                int filaSeleccionada = tablePacientes.getSelectedRow();
                if (filaSeleccionada >= 0) {
                    txtIdPaciente.setText(modelPacientes.getValueAt(filaSeleccionada, 0).toString());
                    txtNombrePaciente.setText(modelPacientes.getValueAt(filaSeleccionada, 1).toString());
                    txtGlucosa.setText(modelPacientes.getValueAt(filaSeleccionada, 2).toString());
                    txtHierro.setText(modelPacientes.getValueAt(filaSeleccionada, 3).toString());
                    // Al seleccionar paciente, cargar su ID en campo de asociación
                    txtAsociarPaciente.setText(modelPacientes.getValueAt(filaSeleccionada, 0).toString());
                }
            }
        });

        JScrollPane scrollPacientes = new JScrollPane(tablePacientes);
        scrollPacientes.setBounds(20, 50, 550, 150);
        mainPanel.add(scrollPacientes);

        // Panel de controles para pacientes
        JPanel panelControlesPacientes = new JPanel(null);
        panelControlesPacientes.setBounds(20, 210, 550, 180);
        panelControlesPacientes.setBorder(BorderFactory.createTitledBorder("Gestión de Pacientes"));
        panelControlesPacientes.setBackground(new Color(240, 240, 240));

        // Campos de texto para pacientes
        JLabel lblIdPaciente = new JLabel("ID:");
        lblIdPaciente.setBounds(20, 30, 80, 25);
        panelControlesPacientes.add(lblIdPaciente);

        txtIdPaciente = new JTextField();
        txtIdPaciente.setBounds(100, 30, 150, 25);
        txtIdPaciente.setEditable(false);
        panelControlesPacientes.add(txtIdPaciente);

        JLabel lblNombrePaciente = new JLabel("Nombre:");
        lblNombrePaciente.setBounds(20, 70, 80, 25);
        panelControlesPacientes.add(lblNombrePaciente);

        txtNombrePaciente = new JTextField();
        txtNombrePaciente.setBounds(100, 70, 150, 25);
        panelControlesPacientes.add(txtNombrePaciente);

        JLabel lblGlucosa = new JLabel("Glucosa:");
        lblGlucosa.setBounds(20, 110, 80, 25);
        panelControlesPacientes.add(lblGlucosa);

        txtGlucosa = new JTextField();
        txtGlucosa.setBounds(100, 110, 150, 25);
        panelControlesPacientes.add(txtGlucosa);

        JLabel lblHierro = new JLabel("Hierro:");
        lblHierro.setBounds(270, 110, 80, 25);
        panelControlesPacientes.add(lblHierro);

        txtHierro = new JTextField();
        txtHierro.setBounds(320, 110, 150, 25);
        panelControlesPacientes.add(txtHierro);

        // Botones para pacientes
        JButton btnGuardarPaciente = new JButton("Guardar");
        btnGuardarPaciente.setBounds(320, 70, 100, 25);
        btnGuardarPaciente.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nombre = txtNombrePaciente.getText().trim();
                String glucosa = txtGlucosa.getText().trim();
                String hierro = txtHierro.getText().trim();

                if (nombre.isEmpty() || glucosa.isEmpty() || hierro.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No puede haber campos vacíos", "Error",
                            JOptionPane.ERROR_MESSAGE);
                } else if (!nombre.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$")) {
                    JOptionPane.showMessageDialog(null, "El nombre solo puede contener letras y espacios", "Error",
                            JOptionPane.ERROR_MESSAGE);
                } else if (!glucosa.matches("^\\d+$")) {
                    JOptionPane.showMessageDialog(null, "El nivel de glucosa debe ser un número entero", "Error",
                            JOptionPane.ERROR_MESSAGE);
                } else if (!hierro.matches("^\\d+$")) {
                    JOptionPane.showMessageDialog(null, "El nivel de hierro debe ser un número entero", "Error",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    try {
                        Paciente nuevoPaciente = new Paciente(
                                nombre,
                                Integer.parseInt(glucosa),
                                Integer.parseInt(hierro));

                        Transaction tx = session.beginTransaction();
                        try {
                            pDAO.insertPaciente(session, nuevoPaciente);
                            tx.commit();
                            cargarDatosPacientes();
                            cargarDatosPacientesMedicamentos();
                            limpiarCamposPaciente();
                            JOptionPane.showMessageDialog(null, "Paciente guardado correctamente");
                        } catch (Exception ex) {
                            tx.rollback();
                            JOptionPane.showMessageDialog(null, "Error al guardar: " + ex.getMessage(), "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Error en los valores numéricos", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        panelControlesPacientes.add(btnGuardarPaciente);

        JButton btnActualizarPaciente = new JButton("Actualizar");
        btnActualizarPaciente.setBounds(430, 30, 100, 25);
        btnActualizarPaciente.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nombre = txtNombrePaciente.getText().trim();
                String glucosa = txtGlucosa.getText().trim();
                String hierro = txtHierro.getText().trim();

                if (txtIdPaciente.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Seleccione un paciente", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (nombre.isEmpty() || glucosa.isEmpty() || hierro.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No puede haber campos vacíos", "Error",
                            JOptionPane.ERROR_MESSAGE);
                } else if (!nombre.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$")) {
                    JOptionPane.showMessageDialog(null, "El nombre solo puede contener letras y espacios", "Error",
                            JOptionPane.ERROR_MESSAGE);
                } else if (!glucosa.matches("^\\d+$")) {
                    JOptionPane.showMessageDialog(null, "El nivel de glucosa debe ser un número entero", "Error",
                            JOptionPane.ERROR_MESSAGE);
                } else if (!hierro.matches("^\\d+$")) {
                    JOptionPane.showMessageDialog(null, "El nivel de hierro debe ser un número entero", "Error",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    Paciente paciente = pDAO.selectPacienteById(session, Integer.parseInt(txtIdPaciente.getText()));
                    if (paciente == null) {
                        JOptionPane.showMessageDialog(null, "Paciente no encontrado", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        paciente.setNombre(nombre);
                        try {
                            paciente.setNivelGlucosa(Integer.parseInt(glucosa));
                            paciente.setNivelHierroSangre(Integer.parseInt(hierro));

                            Transaction tx = session.beginTransaction();
                            try {
                                pDAO.updatePaciente(session, paciente);
                                tx.commit();
                                cargarDatosPacientes();
                                cargarDatosPacientesMedicamentos();
                                limpiarCamposPaciente();
                                JOptionPane.showMessageDialog(null, "Paciente actualizado");
                            } catch (Exception ex) {
                                tx.rollback();
                                JOptionPane.showMessageDialog(null, "Error al actualizar: " + ex.getMessage(), "Error",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null, "Error en los valores numéricos", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });
        panelControlesPacientes.add(btnActualizarPaciente);

        JButton btnEliminarPaciente = new JButton("Eliminar");
        btnEliminarPaciente.setBounds(430, 70, 100, 25);
        btnEliminarPaciente.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (txtIdPaciente.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Seleccione un paciente", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    int confirm = JOptionPane.showConfirmDialog(null,
                            "¿Está seguro que desea eliminar este paciente y todas sus asociaciones?",
                            "Confirmar eliminación",
                            JOptionPane.YES_NO_OPTION);

                    if (confirm == JOptionPane.YES_OPTION) {
                        Transaction tx = session.beginTransaction();
                        try {
                            Paciente paciente = pDAO.selectPacienteById(session,
                                    Integer.parseInt(txtIdPaciente.getText()));
                            if (paciente == null) {
                                JOptionPane.showMessageDialog(null, "Paciente no encontrado", "Error",
                                        JOptionPane.ERROR_MESSAGE);
                            } else {
                                // Eliminar asociaciones de medicamentos
                                //
                                for (Medicamento m : paciente.getMedicamentos()) {
                                    // Eliminar este paciente de cada medicamento asociado
                                    m.getPacientes().remove(paciente);
                                    // Actualizar el medicamento en la base de datos
                                    mDAO.updateMedicamento(session, m);
                                }

                                // Ahora eliminar el paciente
                                pDAO.deletePaciente(session, paciente.getId());
                                tx.commit();

                                cargarDatosPacientes();
                                cargarDatosMedicamentos();
                                cargarDatosPacientesMedicamentos();
                                limpiarCamposPaciente();
                                JOptionPane.showMessageDialog(null,
                                        "Paciente y sus asociaciones eliminados correctamente");
                            }
                        } catch (Exception ex) {
                            tx.rollback();
                            JOptionPane.showMessageDialog(null, "Error al eliminar: " + ex.getMessage(), "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });
        panelControlesPacientes.add(btnEliminarPaciente);

        mainPanel.add(panelControlesPacientes);

        // ============ SECCIÓN MEDICAMENTOS ============
        JLabel lblTituloMedicamentos = new JLabel("MEDICAMENTOS");
        lblTituloMedicamentos.setBounds(600, 20, 200, 25);
        lblTituloMedicamentos.setFont(CustomFontLoader.loadFont("fonts/roboto-bold.ttf", 16f)); // Fuente personalizada
        lblTituloMedicamentos.setForeground(new Color(0, 102, 204)); // Color azul
        mainPanel.add(lblTituloMedicamentos);

        // Tabla de medicamentos
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
                int filaSeleccionada = tableMedicamentos.getSelectedRow();
                if (filaSeleccionada >= 0) {
                    txtIdMedicamento.setText(modelMedicamentos.getValueAt(filaSeleccionada, 0).toString());
                    txtNombreMedicamento.setText(modelMedicamentos.getValueAt(filaSeleccionada, 1).toString());

                    // Al seleccionar medicamento, cargar su ID en campo de asociación
                    txtAsociarMedicamento.setText(modelMedicamentos.getValueAt(filaSeleccionada, 0).toString());
                }
            }
        });

        JScrollPane scrollMedicamentos = new JScrollPane(tableMedicamentos);
        scrollMedicamentos.setBounds(600, 50, 550, 150);
        mainPanel.add(scrollMedicamentos);

        // Panel de controles para medicamentos
        JPanel panelControlesMedicamentos = new JPanel(null);
        panelControlesMedicamentos.setBounds(600, 210, 550, 180);
        panelControlesMedicamentos.setBorder(BorderFactory.createTitledBorder("Gestión de Medicamentos"));
        panelControlesMedicamentos.setBackground(new Color(240, 240, 240));

        // Campos de texto para medicamentos
        JLabel lblIdMedicamento = new JLabel("ID:");
        lblIdMedicamento.setBounds(20, 30, 80, 25);
        panelControlesMedicamentos.add(lblIdMedicamento);

        txtIdMedicamento = new JTextField();
        txtIdMedicamento.setBounds(100, 30, 150, 25);
        txtIdMedicamento.setEditable(false);
        panelControlesMedicamentos.add(txtIdMedicamento);

        JLabel lblNombreMedicamento = new JLabel("Nombre:");
        lblNombreMedicamento.setBounds(20, 70, 80, 25);
        panelControlesMedicamentos.add(lblNombreMedicamento);

        txtNombreMedicamento = new JTextField();
        txtNombreMedicamento.setBounds(100, 70, 150, 25);
        panelControlesMedicamentos.add(txtNombreMedicamento);

        // Botones para medicamentos
        JButton btnGuardarMedicamento = new JButton("Guardar");
        btnGuardarMedicamento.setBounds(320, 70, 100, 25);
        btnGuardarMedicamento.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nombre = txtNombreMedicamento.getText().trim();
                if (nombre.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "El nombre del medicamento no puede estar vacío", "Error",
                            JOptionPane.ERROR_MESSAGE);
                } else if (!nombre.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$")) {
                    JOptionPane.showMessageDialog(null,
                            "El nombre del medicamento solo puede contener letras y espacios", "Error",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    Medicamento nuevoMedicamento = new Medicamento(nombre);

                    Transaction tx = session.beginTransaction();
                    try {
                        mDAO.insertMedicamento(session, nuevoMedicamento);
                        tx.commit();
                        cargarDatosMedicamentos();
                        cargarDatosPacientesMedicamentos();
                        limpiarCamposMedicamento();
                        JOptionPane.showMessageDialog(null, "Medicamento guardado");
                    } catch (Exception ex) {
                        tx.rollback();
                        JOptionPane.showMessageDialog(null, "Error al guardar: " + ex.getMessage(), "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        panelControlesMedicamentos.add(btnGuardarMedicamento);

        JButton btnActualizarMedicamento = new JButton("Actualizar");
        btnActualizarMedicamento.setBounds(430, 30, 100, 25);
        btnActualizarMedicamento.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (txtIdMedicamento.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Seleccione un medicamento", "Error",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    String nombre = txtNombreMedicamento.getText().trim();
                    if (nombre.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "El nombre del medicamento no puede estar vacío", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    } else if (!nombre.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$")) {
                        JOptionPane.showMessageDialog(null,
                                "El nombre del medicamento solo puede contener letras y espacios", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        Medicamento medicamento = mDAO.selectMedicamentoById(session,
                                Integer.parseInt(txtIdMedicamento.getText()));
                        if (medicamento == null) {
                            JOptionPane.showMessageDialog(null, "Medicamento no encontrado", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        } else {
                            medicamento.setNombre(nombre);

                            Transaction tx = session.beginTransaction();
                            try {
                                mDAO.updateMedicamento(session, medicamento);
                                tx.commit();
                                cargarDatosMedicamentos();
                                cargarDatosPacientesMedicamentos();
                                limpiarCamposMedicamento();
                                JOptionPane.showMessageDialog(null, "Medicamento actualizado");
                            } catch (Exception ex) {
                                tx.rollback();
                                JOptionPane.showMessageDialog(null, "Error al actualizar: " + ex.getMessage(), "Error",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                }
            }
        });
        panelControlesMedicamentos.add(btnActualizarMedicamento);

        JButton btnEliminarMedicamento = new JButton("Eliminar");
        btnEliminarMedicamento.setBounds(430, 70, 100, 25);
        btnEliminarMedicamento.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (txtIdMedicamento.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Seleccione un medicamento", "Error",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    int confirm = JOptionPane.showConfirmDialog(null,
                            "¿Está seguro que desea eliminar este medicamento y todas sus asociaciones?",
                            "Confirmar eliminación",
                            JOptionPane.YES_NO_OPTION);

                    if (confirm == JOptionPane.YES_OPTION) {
                        Transaction tx = session.beginTransaction();
                        try {
                            Medicamento medicamento = mDAO.selectMedicamentoById(session,
                                    Integer.parseInt(txtIdMedicamento.getText()));
                            if (medicamento == null) {
                                JOptionPane.showMessageDialog(null, "Medicamento no encontrado", "Error",
                                        JOptionPane.ERROR_MESSAGE);
                            } else {
                                // Primero quito este medicamento de todos los pacientes que lo usan
                                for (Paciente p : medicamento.getPacientes()) {
                                    p.getMedicamentos().remove(medicamento);
                                    pDAO.updatePaciente(session, p);
                                }

                                // Ahora sí elimino el medicamento
                                mDAO.deleteMedicamento(session, medicamento.getId());
                                tx.commit();

                                // Actualizo todas las tablascargarDatosMedicamentos();
                                cargarDatosPacientes();
                                cargarDatosPacientesMedicamentos();
                                limpiarCamposMedicamento();
                                JOptionPane.showMessageDialog(null,
                                        "Medicamento y sus asociaciones eliminados correctamente");
                            }
                        } catch (Exception ex) {
                            tx.rollback();
                            JOptionPane.showMessageDialog(null, "Error al eliminar: " + ex.getMessage(), "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });
        panelControlesMedicamentos.add(btnEliminarMedicamento);

        mainPanel.add(panelControlesMedicamentos);

        // ============ SECCIÓN ASOCIACIONES ============
        JLabel lblTituloAsociaciones = new JLabel("ASOCIACIONES PACIENTES-MEDICAMENTOS");
        lblTituloAsociaciones.setBounds(20, 400, 400, 25);
        lblTituloAsociaciones.setFont(CustomFontLoader.loadFont("fonts/roboto-bold.ttf", 16f)); // Fuente personalizada
        lblTituloAsociaciones.setForeground(new Color(0, 102, 204)); // Color azul
        mainPanel.add(lblTituloAsociaciones);

        // Tabla de pacientes con medicamentos
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
        tablePacientesMedicamentos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int filaSeleccionada = tablePacientesMedicamentos.getSelectedRow();
                if (filaSeleccionada >= 0) {
                    // Cargar IDs en campos de asociación al seleccionar en esta tabla
                    txtAsociarPaciente.setText(modelPacientesMedicamentos.getValueAt(filaSeleccionada, 0).toString());
                }
            }
        });
        tablePacientesMedicamentos.setSelectionBackground(new Color(180, 210, 240));

        JScrollPane scrollPacientesMed = new JScrollPane(tablePacientesMedicamentos);
        scrollPacientesMed.setBounds(20, 430, 1130, 150);
        mainPanel.add(scrollPacientesMed);

        // Panel de controles para asociaciones
        JPanel panelControlesAsociaciones = new JPanel(null);
        panelControlesAsociaciones.setBounds(20, 590, 1130, 100);
        panelControlesAsociaciones.setBorder(BorderFactory.createTitledBorder("Gestión de Asociaciones"));
        panelControlesAsociaciones.setBackground(new Color(240, 240, 240));

        // Campos para asociar/desasociar
        JLabel lblPacienteAsociar = new JLabel("ID Paciente:");
        lblPacienteAsociar.setBounds(20, 30, 100, 25);
        panelControlesAsociaciones.add(lblPacienteAsociar);

        txtAsociarPaciente = new JTextField();
        txtAsociarPaciente.setBounds(120, 30, 100, 25);
        panelControlesAsociaciones.add(txtAsociarPaciente);

        JLabel lblMedicamentoAsociar = new JLabel("ID Medicamento:");
        lblMedicamentoAsociar.setBounds(250, 30, 110, 25); // Ajustado para mejor alineación
        panelControlesAsociaciones.add(lblMedicamentoAsociar);

        txtAsociarMedicamento = new JTextField();
        txtAsociarMedicamento.setBounds(370, 30, 100, 25); // Ajustado para mejor alineación
        panelControlesAsociaciones.add(txtAsociarMedicamento);

        // Botón para asociar
        JButton btnAsociar = new JButton("Asociar");
        btnAsociar.setBounds(490, 30, 100, 25);
        btnAsociar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (txtAsociarPaciente.getText().isEmpty() || txtAsociarMedicamento.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Ingrese ambos IDs", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    try {
                        int idPaciente = Integer.parseInt(txtAsociarPaciente.getText());
                        int idMedicamento = Integer.parseInt(txtAsociarMedicamento.getText());

                        Transaction tx = session.beginTransaction();
                        try {
                            Paciente paciente = pDAO.selectPacienteById(session, idPaciente);
                            Medicamento medicamento = mDAO.selectMedicamentoById(session, idMedicamento);

                            if (paciente == null || medicamento == null) {
                                JOptionPane.showMessageDialog(null, "Paciente o medicamento no encontrado", "Error",
                                        JOptionPane.ERROR_MESSAGE);
                            } else {
                                paciente.agregarMedicamento(medicamento);
                                pDAO.updatePaciente(session, paciente);
                                tx.commit();
                                cargarDatosPacientesMedicamentos();
                                txtAsociarPaciente.setText("");
                                txtAsociarMedicamento.setText("");
                                JOptionPane.showMessageDialog(null, "Asociación realizada");
                            }
                        } catch (Exception ex) {
                            tx.rollback();
                            JOptionPane.showMessageDialog(null, "Error al asociar: " + ex.getMessage(), "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "IDs deben ser números", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        panelControlesAsociaciones.add(btnAsociar);

        // Botón para desasociar
        JButton btnDesasociar = new JButton("Desasociar");
        btnDesasociar.setBounds(600, 30, 100, 25);
        btnDesasociar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (txtAsociarPaciente.getText().isEmpty() || txtAsociarMedicamento.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Ingrese ambos IDs", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    try {
                        int idPaciente = Integer.parseInt(txtAsociarPaciente.getText());
                        int idMedicamento = Integer.parseInt(txtAsociarMedicamento.getText());

                        Transaction tx = session.beginTransaction();
                        try {
                            Paciente paciente = pDAO.selectPacienteById(session, idPaciente);
                            Medicamento medicamento = mDAO.selectMedicamentoById(session, idMedicamento);

                            if (paciente == null || medicamento == null) {
                                JOptionPane.showMessageDialog(null, "Paciente o medicamento no encontrado", "Error",
                                        JOptionPane.ERROR_MESSAGE);
                            } else {
                                paciente.quitarMedicamento(medicamento);
                                pDAO.updatePaciente(session, paciente);
                                tx.commit();

                                cargarDatosPacientesMedicamentos();
                                txtAsociarPaciente.setText("");
                                txtAsociarMedicamento.setText("");
                                JOptionPane.showMessageDialog(null, "Desasociación realizada");
                            }
                        } catch (Exception ex) {
                            tx.rollback();
                            JOptionPane.showMessageDialog(null, "Error al desasociar: " + ex.getMessage(), "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "IDs deben ser números", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        panelControlesAsociaciones.add(btnDesasociar);

        mainPanel.add(panelControlesAsociaciones);

        // Cargar datos iniciales
        cargarDatosPacientes();
        cargarDatosMedicamentos();
        cargarDatosPacientesMedicamentos();

        add(mainPanel);
    }

    // Métodos para limpiar campos
    private void limpiarCamposPaciente() {
        txtIdPaciente.setText("");
        txtNombrePaciente.setText("");
        txtGlucosa.setText("");
        txtHierro.setText("");
    }

    private void limpiarCamposMedicamento() {
        txtIdMedicamento.setText("");
        txtNombreMedicamento.setText("");
    }

    // Métodos para cargar datos en las tablas
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
        // Limpio tabla
        modelPacientesMedicamentos.setRowCount(0);

        // Obtengo todos los pacientes
        List<Paciente> pacientes = pDAO.selectAllPacientes(session);

        // Recojo lista de pacientes y
        for (Paciente p : pacientes) { // Para cada paciente

            for (Medicamento m : p.getMedicamentos()) {// Recorremos medicamentos de cada paciente
                // Crea UNA fila por CADA relación paciente-medicamento
                Object[] row = new Object[3];
                row[0] = p.getId();
                row[1] = p.getNombre();
                row[2] = m.getNombre(); // Mostramos nombre en lugar de ID

                modelPacientesMedicamentos.addRow(row);
            }

            // Si no hay medicamentos asociados : mostrar pacientes sin medicamentos
            if (p.getMedicamentos().isEmpty()) {
                Object[] row = new Object[3];
                row[0] = p.getId();
                row[1] = p.getNombre();
                row[2] = "Sin medicamentos";
                modelPacientesMedicamentos.addRow(row);
            }

            // Imprmir por consola
            System.out.println("Paciente: " + p.getNombre() + " (ID: " + p.getId() + ")");// Para cada paciente
            // Imprimir medicamentos asociados
            for (Medicamento m : p.getMedicamentos()) {
                System.out.println("  - Medicamento: " + m.getNombre() + " (ID: " + m.getId() + ")");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GestionPacientesGUI gui = new GestionPacientesGUI();
            gui.setVisible(true);
        });
    }
}