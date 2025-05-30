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
                        // Mostrar lista de pacientes disponibles
                        System.out.println("\n--- PACIENTES DISPONIBLES ---");
                        List<Paciente> pacientesLista = pDAO.selectAllPacientes(session);
                        for (Paciente p : pacientesLista) {
                            System.out.println("ID: " + p.getId() + " - " + p.getNombre());
                        }

                        // Mostrar lista de medicamentos disponibles
                        System.out.println("\n--- MEDICAMENTOS DISPONIBLES ---");
                        List<Medicamento> medicamentosLista = mDAO.selectAllMedicamentos(session);
                        for (Medicamento m : medicamentosLista) {
                            System.out.println("ID: " + m.getId() + " - " + m.getNombre());
                        }

                        // Pedir IDs para asociar
                        System.out.println("\nIngrese el ID del paciente a asociar:");
                        int idPac = s.nextInt();
                        s.nextLine();

                        System.out.println("Ingrese el ID del medicamento:");
                        int idMed = s.nextInt();
                        s.nextLine();

                        // Operación de asociación
                        Transaction txAsociar = session.beginTransaction();
                        try {
                            Paciente pac = pDAO.selectPacienteById(session, idPac);
                            Medicamento med = mDAO.selectMedicamentoById(session, idMed);

                            med.agregarPaciente(pac);
                            mDAO.updateMedicamento(session, med);

                            txAsociar.commit();
                            System.out.println("\n Asociación exitosa:");
                            System.out.println("   Paciente: " + pac.getNombre());
                            System.out.println("   Medicamento: " + med.getNombre());
                        } catch (Exception e) {
                            txAsociar.rollback();
                            System.out.println(" Error: " + e.getMessage());
                        }
                        break;

                    case 10:
                        // Mostrar pacientes con sus medicamentos
                        System.out.println("\n--- PACIENTES Y SUS MEDICAMENTOS ---");
                        List<Paciente> pacientesConMed = pDAO.selectAllPacientes(session);
                        for (Paciente p : pacientesConMed) {
                            System.out.println("\nPaciente ID: " + p.getId() + " - " + p.getNombre());
                            System.out.println("Medicamentos asignados:");
                            for (Medicamento m : p.getMedicamentos()) {
                                System.out.println("   ID: " + m.getId() + " - " + m.getNombre());
                            }
                        }

                        // Pedir IDs para desasociar
                        System.out.println("\nIngrese el ID del paciente a desasociar:");
                        int idPacQuitar = s.nextInt();
                        s.nextLine();

                        System.out.println("Ingrese el ID del medicamento a quitar:");
                        int idMedQuitar = s.nextInt();
                        s.nextLine();

                        // Operación de desasociación
                        Transaction txQuitar = session.beginTransaction();
                        try {
                            Paciente pacQuitar = pDAO.selectPacienteById(session, idPacQuitar);
                            Medicamento medQuitar = mDAO.selectMedicamentoById(session, idMedQuitar);

                            medQuitar.quitarPaciente(pacQuitar);
                            mDAO.updateMedicamento(session, medQuitar);

                            txQuitar.commit();
                            System.out.println("\n Desasociación exitosa:");
                            System.out.println("   Paciente: " + pacQuitar.getNombre());
                            System.out.println("   Medicamento: " + medQuitar.getNombre());
                        } catch (Exception e) {
                            txQuitar.rollback();
                            System.out.println("Error: " + e.getMessage());
                        }
                        break;
                    case 11:
                        // Mostrar pacientes con sus medicamentos (versión mejorada)
                        System.out.println("\n══════════════════════════════════════");
                        System.out.println("    LISTADO DE PACIENTES CON MEDICAMENTOS");
                        System.out.println("══════════════════════════════════════\n");

                        List<Paciente> pacientesConMedicamentos = pDAO.selectAllPacientes(session);

                        if (pacientesConMedicamentos.isEmpty()) {
                            System.out.println("No hay pacientes registrados.");
                        } else {
                            for (Paciente p : pacientesConMedicamentos) {
                                System.out.println("PACIENTE: " + p.getNombre() + " (ID: " + p.getId() + ")");
                                System.out.println("Nivel glucosa: " + p.getNivelGlucosa());
                                System.out.println("Nivel hierro: " + p.getNivelHierroSangre());

                                if (p.getMedicamentos().isEmpty()) {
                                    System.out.println(" No tiene medicamentos asignados");
                                } else {
                                    System.out.println("MEDICAMENTOS ASIGNADOS:");
                                    for (Medicamento m : p.getMedicamentos()) {
                                        System.out.println("- " + m.getNombre() + " (ID: " + m.getId() + ")");
                                    }
                                }
                                System.out.println("----------------------------------------");
                            }
                        }
                        System.out.println("\nTotal pacientes: " + pacientesConMedicamentos.size());
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