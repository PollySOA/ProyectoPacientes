package com.example;

import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.example.dao.MedicamentoDAO;
import com.example.dao.PacienteDAO;
import com.example.example.model.Medicamento;
import com.example.example.model.Paciente;
import com.example.util.HibernateUtil;

public class Main {

    static void mostrarMenu() {
        System.out.println("-------------- Menu: GESTION Pacientes---------------\n");
        System.out.println("******SELECCION OPCION********\n");
        System.out.println("1. Mostrar Pacientes");
        System.out.println("2. Insertar Pacientes");
        System.out.println("3. Actualizar Pacientes");
        System.out.println("4. Eliminar Pacientes");
        System.out.println("--------------------------------------------------------\n");
        System.out.println("5. Mostrar Medicamentos");
        System.out.println("6. Insertar Medicamentos");
        System.out.println("7. Actualizar Medicamentos");
        System.out.println("8. Eliminar Medicamentos");
        System.out.println("--------------------------------------------------------\n");
        System.out.println("9. Asociar Paciente a Medicamento");
        System.out.println("10. Quitar Paciente a Medicamento");
        System.out.println("11. Mostrar Pacientes con Medicamentos");
        System.out.println("0. salir");
        System.out.println("--------------------------------------------------------");
    }

    public static void main(String[] args) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Scanner s = new Scanner(System.in);

        PacienteDAO pDAO = new PacienteDAO();
        MedicamentoDAO mDAO = new MedicamentoDAO();

        try {
            int opc = 0;
            do {
                mostrarMenu();
                opc = s.nextInt();
                s.nextLine(); // Limpiar buffer

                switch (opc) {
                    case 1:
                        List<Paciente> pacientes = pDAO.selectAllPacientes(session);
                        for (Paciente p : pacientes) {
                            System.out.println("Paciente: " + p.getNombre() + "| Id: " + p.getId());
                            System.out.println("Medicamentos: ");
                            for (Medicamento m : p.getMedicamentos()) {
                                System.out.println("| Medicamento:  " + m.getNombre());
                            }
                        }
                        break;

                    case 2:
                        // Insertar paciente
                        System.out.println("Ingrese el nombre del paciente:");
                        String nombre = s.nextLine();
                        System.out.println("Ingrese el nivel de glucosa del paciente:");
                        int nivelGlucosa = s.nextInt();
                        s.nextLine();
                        System.out.println("Ingrese el nivel de hierro en la sangre del paciente:");
                        int nivelHierroSangre = s.nextInt();
                        s.nextLine();
                        Paciente nuevoPaciente = new Paciente(nombre, nivelGlucosa, nivelHierroSangre);
                        Transaction txa = session.beginTransaction();
                        try {
                            pDAO.insertPaciente(session, nuevoPaciente);
                            txa.commit();
                            System.out.println("Paciente insertado correctamente.");
                        } catch (Exception e) {
                            txa.rollback();
                            System.out.println("Error al insertar paciente: " + e.getMessage());
                        }
                        break;

                    case 3:
                        // Actualizar paciente
                        System.out.println("Ingrese el ID del paciente a actualizar:");
                        int idPaciente = s.nextInt();
                        s.nextLine();
                        Paciente paciente = pDAO.selectPacienteById(session, idPaciente);
                        if (paciente != null) {
                            System.out.println("Ingrese el nuevo nombre del paciente:");
                            String nuevoNombre = s.nextLine();
                            System.out.println("Ingrese el nuevo nivel de glucosa del paciente:");
                            int nuevoNivelGlucosa = s.nextInt();
                            s.nextLine();
                            System.out.println("Ingrese el nuevo nivel de hierro en la sangre del paciente:");
                            int nuevoNivelHierroSangre = s.nextInt();
                            s.nextLine();
                            paciente.setNombre(nuevoNombre);
                            paciente.setNivelGlucosa(nuevoNivelGlucosa);
                            paciente.setNivelHierroSangre(nuevoNivelHierroSangre);
                            Transaction txu = session.beginTransaction();
                            try {
                                pDAO.updatePaciente(session, paciente);
                                txu.commit();
                                System.out.println("Paciente actualizado correctamente.");
                            } catch (Exception e) {
                                txu.rollback();
                                System.out.println("Error al actualizar paciente: " + e.getMessage());
                            }
                        } else {
                            System.out.println("Paciente no encontrado.");
                        }
                        break;

                    case 4:
                        System.out.println("Ingrese el ID del paciente a eliminar:");
                        int idEliminar = s.nextInt();
                        s.nextLine();
                        Paciente pacienteEliminar = pDAO.selectPacienteById(session, idEliminar);
                        if (pacienteEliminar != null) {
                            for (Medicamento m : pacienteEliminar.getMedicamentos()) {
                                pacienteEliminar.quitarMedicamento(m);
                            }
                            Transaction txd = session.beginTransaction();
                            try {
                                pDAO.deletePaciente(session, idEliminar);
                                txd.commit();
                                System.out.println("Paciente eliminado.");
                            } catch (Exception e) {
                                txd.rollback();
                                System.out.println("Error al eliminar paciente: " + e.getMessage());
                            }
                        } else {
                            System.out.println("Paciente no encontrado.");
                        }
                        break;

                    case 5:
                        List<Medicamento> medicamentos = mDAO.selectAllMedicamentos(session);
                        for (Medicamento m : medicamentos) {
                            System.out.println("Medicamento: " + m.getNombre() + ", ID: " + m.getId());
                            System.out.println("Pacientes asociados: ");
                            for (Paciente p : m.getPacientes()) {
                                System.out.println("- " + p.getNombre());
                            }
                        }
                        break;

                    case 6:
                        // Insertar medicamento
                        System.out.println("Ingrese el nombre del medicamento:");
                        String nombreMedicamento = s.nextLine();
                        Medicamento nuevoMedicamento = new Medicamento(nombreMedicamento);
                        Transaction txm = session.beginTransaction();
                        try {
                            mDAO.insertMedicamento(session, nuevoMedicamento);
                            txm.commit();
                            System.out.println("Medicamento insertado correctamente.");
                        } catch (Exception e) {
                            txm.rollback();
                            System.out.println("Error al insertar medicamento: " + e.getMessage());
                        }
                        break;

                    case 7:
                        // Actualizar medicamento
                        System.out.println("Ingrese el ID del medicamento a actualizar:");
                        int idMedicamento = s.nextInt();
                        s.nextLine();
                        Medicamento medicamento = mDAO.selectMedicamentoById(session, idMedicamento);
                        if (medicamento != null) {
                            System.out.println("Ingrese el nuevo nombre del medicamento:");
                            String nuevoNombreMedicamento = s.nextLine();
                            medicamento.setNombre(nuevoNombreMedicamento);
                            Transaction txum = session.beginTransaction();
                            try {
                                mDAO.updateMedicamento(session, medicamento);
                                txum.commit();
                                System.out.println("Medicamento actualizado correctamente.");
                            } catch (Exception e) {
                                txum.rollback();
                                System.out.println("Error al actualizar medicamento: " + e.getMessage());
                            }
                        } else {
                            System.out.println("Medicamento no encontrado.");
                        }
                        break;

                    case 8:
                        // Eliminar medicamento
                        System.out.println("Ingrese el ID del medicamento a eliminar:");
                        int idMedicamentoEliminar = s.nextInt();
                        s.nextLine();
                        Medicamento medicamentoEliminar = mDAO.selectMedicamentoById(session, idMedicamentoEliminar);
                        if (medicamentoEliminar != null) {
                            Transaction txdm = session.beginTransaction();
                            try {
                                mDAO.deleteMedicamento(session, idMedicamentoEliminar);
                                txdm.commit();
                                System.out.println("Medicamento eliminado.");
                            } catch (Exception e) {
                                txdm.rollback();
                                System.out.println("Error al eliminar medicamento: " + e.getMessage());
                            }
                        } else {
                            System.out.println("Medicamento no encontrado.");
                        }
                        break;

                    case 9:
                        // PASO 1: Pedimos al usuario el ID del paciente que quiere asociar
                        System.out.println("Ingrese el ID del paciente a asociar:");
                        int idPacienteAsociar = s.nextInt(); // Leemos el número que escribe el usuario
                        s.nextLine(); // Limpiamos el buffer del teclado

                        // PASO 2: Pedimos el ID del medicamento al que lo vamos a asociar
                        System.out.println("Ingrese el ID del medicamento al que desea asociar al paciente:");
                        int idMedicamentoAsociar = s.nextInt();
                        s.nextLine();

                        // PASO 3: Buscamos en la base de datos...
                        // - Buscamos al paciente usando su ID (como buscar por DNI)
                        Paciente pac = pDAO.selectPacienteById(session, idPacienteAsociar);
                        // - Buscamos el medicamento usando su ID (como buscar por código de barras)
                        Medicamento medic = mDAO.selectMedicamentoById(session, idMedicamentoAsociar);

                        // PASO 4: Comprobamos si existen ambos
                        if (pac == null) {
                            System.out.println("¡Error! No existe ningún paciente con ese ID");
                        } else if (medic == null) {
                            System.out.println("¡Error! No existe ningún medicamento con ese ID");
                        } else {
                            // PASO 5: Verificamos si YA están asociados
                            // Miramos en la lista de pacientes de ese medicamento
                            Set<Paciente> pacientesDelMedicamento = medic.getPacientes();

                            // Bandera para saber si ya estaban juntos
                            boolean yaEstabanJuntos = false;

                            // Recorremos la lista como mirando nombres en una hoja de cálculo
                            for (Paciente p : pacientesDelMedicamento) {
                                if (p.equals(pac)) { // Comparamos paciente a paciente
                                    yaEstabanJuntos = true;
                                    break; // Si lo encontramos, dejamos de buscar
                                }
                            }

                            // PASO 6: Empezamos la transacción (como "empezar a anotar en lápiz")
                            Transaction tx9 = session.beginTransaction();
                            try {
                                if (yaEstabanJuntos) {
                                    // PASO 7a: Si ya estaban asociados, avisamos
                                    System.out.println("Este paciente YA toma este medicamento");
                                } else {
                                    // PASO 7b: Si NO estaban asociados, los unimos
                                    medic.agregarPaciente(pac); // Añadimos a la lista
                                    mDAO.updateMedicamento(session, medic); // Guardamos el cambio
                                    System.out.println("Asociación correcta:");
                                    System.out.println("Paciente: " + pac.getNombre());
                                    System.out.println("Medicamento: " + medic.getNombre());
                                }
                                tx9.commit(); // PASO 8: Confirmamos los cambios ("pasamos a tinta")
                            } catch (Exception e) {
                                tx9.rollback(); // Si algo falla, borramos lo anotado a lápiz
                                System.out.println("Error: " + e.getMessage());
                            }
                        }
                        break;

                    case 10:
                        // (Explicación similar al case 9 pero para DESVINCULAR)
                        // PASO 1: Pedimos IDs
                        System.out.println("Ingrese el ID del paciente a desvincular:");
                        int idPacienteQuitar = s.nextInt();
                        s.nextLine();
                        System.out.println("Ingrese el ID del medicamento:");
                        int idMedicamentoQuitar = s.nextInt();
                        s.nextLine();

                        // PASO 2: Buscamos ambos
                        Paciente pacienteQuitar = pDAO.selectPacienteById(session, idPacienteQuitar);
                        Medicamento medicamentoQuitar = mDAO.selectMedicamentoById(session, idMedicamentoQuitar);

                        // PASO 3: Verificamos existencia
                        if (pacienteQuitar == null) {
                            System.out.println("¡Error! Paciente no encontrado");
                        } else if (medicamentoQuitar == null) {
                            System.out.println("¡Error! Medicamento no encontrado");
                        } else {
                            // PASO 4: Buscamos si están asociados
                            boolean encontrado = false;
                            for (Paciente p : medicamentoQuitar.getPacientes()) {
                                if (p.equals(pacienteQuitar)) {
                                    encontrado = true;
                                    break;
                                }
                            }

                            // PASO 5: Transacción
                            Transaction tx10 = session.beginTransaction();
                            try {
                                if (encontrado) {
                                    // PASO 6a: Si están asociados, los separamos
                                    medicamentoQuitar.quitarPaciente(pacienteQuitar);
                                    mDAO.updateMedicamento(session, medicamentoQuitar);
                                    System.out.println(" Desvinculación correcta");
                                } else {
                                    // PASO 6b: Si NO estaban asociados, avisamos
                                    System.out.println(" Este paciente NO tomaba este medicamento");
                                }
                                tx10.commit();
                            } catch (Exception e) {
                                tx10.rollback();
                                System.out.println(" Error: " + e.getMessage());
                            }
                        }
                        break;

                    case 11:
                        List<Paciente> pacientesConMedicamentos = pDAO.selectAllPacientes(session);
                        for (Paciente p : pacientesConMedicamentos) {
                            System.out.println("Paciente: " + p.getNombre() + ", ID: " + p.getId());
                            System.out.println("Medicamentos asociados: ");
                            for (Medicamento m : p.getMedicamentos()) {
                                System.out.println("- " + m.getNombre());
                            }
                        }
                        break;

                    case 0:
                        System.out.println("Saliendo del programa...");
                        break;

                    default:
                        System.out.println("Opción no válida. Por favor, elija una opción del menú.");
                        break;
                }

            } while (opc != 0);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
            s.close();
        }
    }
}