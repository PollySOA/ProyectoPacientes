import javax.swing.*;
import java.awt.Color;
import helper_classes.*;

public class WindowBuilder {
  public static void main(String[] args) {

    JFrame frame = new JFrame("My Awesome Window");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(824, 444);
    JPanel panel = new JPanel();
    panel.setLayout(null);
    panel.setBackground(Color.decode("#f4c064"));

    JButton BtnInsertarPaciente = new JButton("Insertar");
    BtnInsertarPaciente.setBounds(246, 211, 106, 29);
    BtnInsertarPaciente.setBackground(Color.decode("#bca8e4"));
    BtnInsertarPaciente.setForeground(Color.decode("#000"));
    BtnInsertarPaciente.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 14));
    BtnInsertarPaciente.setBorder(new RoundedBorder(4, Color.decode("#3d364a"), 1));
    BtnInsertarPaciente.setFocusPainted(false);
    OnClickEventHelper.setOnClickColor(BtnInsertarPaciente, Color.decode("#7c6f97"), Color.decode("#bca8e4"));
    panel.add(BtnInsertarPaciente);

    JButton BtnActualizarPaciente = new JButton("Actualizar ");
    BtnActualizarPaciente.setBounds(249, 261, 106, 29);
    BtnActualizarPaciente.setBackground(Color.decode("#bca8e4"));
    BtnActualizarPaciente.setForeground(Color.decode("#000"));
    BtnActualizarPaciente.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 14));
    BtnActualizarPaciente.setBorder(new RoundedBorder(4, Color.decode("#3d364a"), 1));
    BtnActualizarPaciente.setFocusPainted(false);
    OnClickEventHelper.setOnClickColor(BtnActualizarPaciente, Color.decode("#7c6f97"), Color.decode("#bca8e4"));
    panel.add(BtnActualizarPaciente);

    JButton BtnEliminarPaciente = new JButton("Eliminar");
    BtnEliminarPaciente.setBounds(250, 309, 106, 29);
    BtnEliminarPaciente.setBackground(Color.decode("#bca8e4"));
    BtnEliminarPaciente.setForeground(Color.decode("#000"));
    BtnEliminarPaciente.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 14));
    BtnEliminarPaciente.setBorder(new RoundedBorder(4, Color.decode("#3d364a"), 1));
    BtnEliminarPaciente.setFocusPainted(false);
    OnClickEventHelper.setOnClickColor(BtnEliminarPaciente, Color.decode("#7c6f97"), Color.decode("#bca8e4"));
    panel.add(BtnEliminarPaciente);

    JTextField NombrePaciente = new JTextField("");
    NombrePaciente.setBounds(57, 213, 106, 21);
    NombrePaciente.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 14));
    NombrePaciente.setBackground(Color.decode("#ffe7bf"));
    NombrePaciente.setForeground(Color.decode("#73664e"));
    NombrePaciente.setBorder(new RoundedBorder(2, Color.decode("#000"), 1));
    OnFocusEventHelper.setOnFocusText(NombrePaciente, "", Color.decode("#000"), Color.decode("#73664e"));
    panel.add(NombrePaciente);

    JTextField element5 = new JTextField(""); // Glucosa
    element5.setBounds(56, 246, 106, 21);
    element5.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 14));
    element5.setBackground(Color.decode("#ffe7bf"));
    element5.setForeground(Color.decode("#73664e"));
    element5.setBorder(new RoundedBorder(2, Color.decode("#000"), 1));
    OnFocusEventHelper.setOnFocusText(element5, "", Color.decode("#000"), Color.decode("#73664e"));
    panel.add(element5);

    JTextField element6 = new JTextField(""); // Hierro
    element6.setBounds(56, 278, 106, 21);
    element6.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 14));
    element6.setBackground(Color.decode("#ffe7bf"));
    element6.setForeground(Color.decode("#73664e"));
    element6.setBorder(new RoundedBorder(2, Color.decode("#000"), 1));
    OnFocusEventHelper.setOnFocusText(element6, "", Color.decode("#000"), Color.decode("#73664e"));
    panel.add(element6);

    JTextField element7 = new JTextField(""); // Otro campo
    element7.setBounds(52, 309, 106, 21);
    element7.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 14));
    element7.setBackground(Color.decode("#ffe7bf"));
    element7.setForeground(Color.decode("#73664e"));
    element7.setBorder(new RoundedBorder(2, Color.decode("#000"), 1));
    OnFocusEventHelper.setOnFocusText(element7, "", Color.decode("#000"), Color.decode("#73664e"));
    panel.add(element7);

    JButton BtnEliminarMedicamentos = new JButton("Eliminar");
    BtnEliminarMedicamentos.setBounds(700, 306, 106, 29);
    BtnEliminarMedicamentos.setBackground(Color.decode("#bca8e4"));
    BtnEliminarMedicamentos.setForeground(Color.decode("#000"));
    BtnEliminarMedicamentos.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 14));
    BtnEliminarMedicamentos.setBorder(new RoundedBorder(4, Color.decode("#3d364a"), 1));
    BtnEliminarMedicamentos.setFocusPainted(false);
    OnClickEventHelper.setOnClickColor(BtnEliminarMedicamentos, Color.decode("#7c6f97"), Color.decode("#bca8e4"));
    panel.add(BtnEliminarMedicamentos);

    JButton BtnActualizarMedicamento = new JButton("Actualizar");
    BtnActualizarMedicamento.setBounds(700, 255, 106, 29);
    BtnActualizarMedicamento.setBackground(Color.decode("#bca8e4"));
    BtnActualizarMedicamento.setForeground(Color.decode("#000"));
    BtnActualizarMedicamento.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 14));
    BtnActualizarMedicamento.setBorder(new RoundedBorder(4, Color.decode("#3d364a"), 1));
    BtnActualizarMedicamento.setFocusPainted(false);
    OnClickEventHelper.setOnClickColor(BtnActualizarMedicamento, Color.decode("#7c6f97"), Color.decode("#bca8e4"));
    panel.add(BtnActualizarMedicamento);

    JButton BtnInsertarMedicamento = new JButton("Insertar");
    BtnInsertarMedicamento.setBounds(698, 206, 106, 29);
    BtnInsertarMedicamento.setBackground(Color.decode("#bca8e4"));
    BtnInsertarMedicamento.setForeground(Color.decode("#000"));
    BtnInsertarMedicamento.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 14));
    BtnInsertarMedicamento.setBorder(new RoundedBorder(4, Color.decode("#3d364a"), 1));
    BtnInsertarMedicamento.setFocusPainted(false);
    OnClickEventHelper.setOnClickColor(BtnInsertarMedicamento, Color.decode("#7c6f97"), Color.decode("#bca8e4"));
    panel.add(BtnInsertarMedicamento);

    JTextField TxtInsertar = new JTextField(""); // Nombre medicamento
    TxtInsertar.setBounds(555, 247, 106, 21);
    TxtInsertar.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 14));
    TxtInsertar.setBackground(Color.decode("#ffe7bf"));
    TxtInsertar.setForeground(Color.decode("#73664e"));
    TxtInsertar.setBorder(new RoundedBorder(2, Color.decode("#000"), 1));
    OnFocusEventHelper.setOnFocusText(TxtInsertar, " ", Color.decode("#000"), Color.decode("#73664e"));
    panel.add(TxtInsertar);

    JTextField TxtActualizarMedicamento = new JTextField("");
    TxtActualizarMedicamento.setBounds(556, 281, 106, 21);
    TxtActualizarMedicamento.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 14));
    TxtActualizarMedicamento.setBackground(Color.decode("#ffe7bf"));
    TxtActualizarMedicamento.setForeground(Color.decode("#73664e"));
    TxtActualizarMedicamento.setBorder(new RoundedBorder(2, Color.decode("#000"), 1));
    OnFocusEventHelper.setOnFocusText(TxtActualizarMedicamento, "", Color.decode("#000"), Color.decode("#73664e"));
    panel.add(TxtActualizarMedicamento);

    JTextField TxtID = new JTextField("");
    TxtID.setBounds(553, 212, 106, 21);
    TxtID.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 14));
    TxtID.setBackground(Color.decode("#ffe7bf"));
    TxtID.setForeground(Color.decode("#73664e"));
    TxtID.setBorder(new RoundedBorder(2, Color.decode("#000"), 1));
    OnFocusEventHelper.setOnFocusText(TxtID, "", Color.decode("#000"), Color.decode("#73664e"));
    panel.add(TxtID);

    JTextField TxtEliminar = new JTextField("");
    TxtEliminar.setBounds(555, 314, 106, 21);
    TxtEliminar.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 14));
    TxtEliminar.setBackground(Color.decode("#ffe7bf"));
    TxtEliminar.setForeground(Color.decode("#73664e"));
    TxtEliminar.setBorder(new RoundedBorder(2, Color.decode("#000"), 1));
    OnFocusEventHelper.setOnFocusText(TxtEliminar, "", Color.decode("#000"), Color.decode("#73664e"));
    panel.add(TxtEliminar);

    // VALIDACIÓN PARA INSERTAR PACIENTE
    BtnInsertarPaciente.addActionListener(e -> {
      String nombre = NombrePaciente.getText().trim();
      String glucosa = element5.getText().trim();
      String hierro = element6.getText().trim();

      if (nombre.isEmpty() || glucosa.isEmpty() || hierro.isEmpty()) {
        JOptionPane.showMessageDialog(frame, "No puede haber campos vacíos.", "Error", JOptionPane.ERROR_MESSAGE);
      } else if (!nombre.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$")) {
        JOptionPane.showMessageDialog(frame, "El nombre solo puede contener letras y espacios.", "Error",
            JOptionPane.ERROR_MESSAGE);
      } else if (!glucosa.matches("^\\d+$")) {
        JOptionPane.showMessageDialog(frame, "El nivel de glucosa debe ser un número entero.", "Error",
            JOptionPane.ERROR_MESSAGE);
      } else if (!hierro.matches("^\\d+$")) {
        JOptionPane.showMessageDialog(frame, "El nivel de hierro debe ser un número entero.", "Error",
            JOptionPane.ERROR_MESSAGE);
      } else {
        JOptionPane.showMessageDialog(frame, "Paciente insertado correctamente.", "Éxito",
            JOptionPane.INFORMATION_MESSAGE);
        // Aquí puedes agregar el código para insertar el paciente en la base de datos
      }
    });

    // VALIDACIÓN PARA ACTUALIZAR PACIENTE
    BtnActualizarPaciente.addActionListener(e -> {
      String nombre = NombrePaciente.getText().trim();
      String glucosa = element5.getText().trim();
      String hierro = element6.getText().trim();

      if (nombre.isEmpty() || glucosa.isEmpty() || hierro.isEmpty()) {
        JOptionPane.showMessageDialog(frame, "No puede haber campos vacíos para actualizar.", "Error",
            JOptionPane.ERROR_MESSAGE);
      } else if (!nombre.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$")) {
        JOptionPane.showMessageDialog(frame, "El nombre solo puede contener letras y espacios.", "Error",
            JOptionPane.ERROR_MESSAGE);
      } else if (!glucosa.matches("^\\d+$")) {
        JOptionPane.showMessageDialog(frame, "El nivel de glucosa debe ser un número entero.", "Error",
            JOptionPane.ERROR_MESSAGE);
      } else if (!hierro.matches("^\\d+$")) {
        JOptionPane.showMessageDialog(frame, "El nivel de hierro debe ser un número entero.", "Error",
            JOptionPane.ERROR_MESSAGE);
      } else {
        JOptionPane.showMessageDialog(frame, "Paciente actualizado correctamente.", "Éxito",
            JOptionPane.INFORMATION_MESSAGE);
        // Aquí puedes agregar el código para actualizar el paciente en la base de datos
      }
    });

    // VALIDACIÓN PARA ELIMINAR PACIENTE
    BtnEliminarPaciente.addActionListener(e -> {
      String nombre = NombrePaciente.getText().trim();

      if (nombre.isEmpty()) {
        JOptionPane.showMessageDialog(frame, "Debe ingresar el nombre del paciente a eliminar.", "Error",
            JOptionPane.ERROR_MESSAGE);
      } else if (!nombre.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$")) {
        JOptionPane.showMessageDialog(frame, "El nombre solo puede contener letras y espacios.", "Error",
            JOptionPane.ERROR_MESSAGE);
      } else {
        JOptionPane.showMessageDialog(frame, "Paciente eliminado correctamente.", "Éxito",
            JOptionPane.INFORMATION_MESSAGE);
        // Aquí puedes agregar el código para eliminar el paciente en la base de datos
      }
    });

    // VALIDACIÓN PARA INSERTAR MEDICAMENTO
    BtnInsertarMedicamento.addActionListener(e -> {
      String nombreMedicamento = TxtInsertar.getText().trim();

      if (nombreMedicamento.isEmpty()) {
        JOptionPane.showMessageDialog(frame, "El nombre del medicamento no puede estar vacío.", "Error",
            JOptionPane.ERROR_MESSAGE);
      } else if (!nombreMedicamento.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$")) {
        JOptionPane.showMessageDialog(frame, "El nombre del medicamento solo puede contener letras y espacios.",
            "Error", JOptionPane.ERROR_MESSAGE);
      } else {
        JOptionPane.showMessageDialog(frame, "Medicamento insertado correctamente.", "Éxito",
            JOptionPane.INFORMATION_MESSAGE);
        // Aquí puedes agregar el código para insertar el medicamento en la base de
        // datos
      }
    });

    // VALIDACIÓN PARA ACTUALIZAR MEDICAMENTO
    BtnActualizarMedicamento.addActionListener(e -> {
      String nombreMedicamento = TxtActualizarMedicamento.getText().trim();

      if (nombreMedicamento.isEmpty()) {
        JOptionPane.showMessageDialog(frame, "El nombre del medicamento a actualizar no puede estar vacío.", "Error",
            JOptionPane.ERROR_MESSAGE);
      } else if (!nombreMedicamento.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$")) {
        JOptionPane.showMessageDialog(frame, "El nombre del medicamento solo puede contener letras y espacios.",
            "Error", JOptionPane.ERROR_MESSAGE);
      } else {
        JOptionPane.showMessageDialog(frame, "Medicamento actualizado correctamente.", "Éxito",
            JOptionPane.INFORMATION_MESSAGE);
        // Aquí puedes agregar el código para actualizar el medicamento en la base de
        // datos
      }
    });

    // VALIDACIÓN PARA ELIMINAR MEDICAMENTO
    BtnEliminarMedicamentos.addActionListener(e -> {
      String nombreMedicamento = TxtEliminar.getText().trim();

      if (nombreMedicamento.isEmpty()) {
        JOptionPane.showMessageDialog(frame, "Debe ingresar el nombre del medicamento a eliminar.", "Error",
            JOptionPane.ERROR_MESSAGE);
      } else if (!nombreMedicamento.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$")) {
        JOptionPane.showMessageDialog(frame, "El nombre del medicamento solo puede contener letras y espacios.",
            "Error", JOptionPane.ERROR_MESSAGE);
      } else {
        JOptionPane.showMessageDialog(frame, "Medicamento eliminado correctamente.", "Éxito",
            JOptionPane.INFORMATION_MESSAGE);
        // Aquí puedes agregar el código para eliminar el medicamento en la base de
        // datos
      }
    });

    frame.add(panel);
    frame.setVisible(true);

  }
}