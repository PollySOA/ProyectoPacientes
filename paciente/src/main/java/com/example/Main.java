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

    static void mostrarMenu() {// metodo para mostrar el menu de opciones

        System.out.println("-------------- Menu: GESTION Pacientes---------------\n");
        System.out.println("******SELECCION OPCION********\n");
        System.out.println("1. Mostrar Pacientes");
        System.out.println("2. Insertar Pacientes");
        System.out.println("3. Actualizar Pacientes");
        System.out.println("4. Eliminar Pacientes");
        //
        System.out.println("--------------------------------------------------------\n");
        System.out.println("5. Mostrar Medicamentos");
        System.out.println("6. Insertar Medicamentos");
        System.out.println("7. Actualizar Medicamentos");
        System.out.println("8. Eliminar Medicamentos");
        System.out.println("--------------------------------------------------------\n");
        System.out.println("9. Asociar Paciente a Medicamento");
        System.out.println("10. Quitar Paciente a Medicamento");
        System.out.println("11. Mostrar Pacientes con Medicamentos"); // con el for anidado que hemos hecho
        System.out.println("0. salir");
        System.out.println("--------------------------------------------------------");

    }

    public static void main(String[] args) {
        // Abrimos una sesión de Hibernate y comenzamos una transacción
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        // Instanciamos los DAOs para manejar pacientes y medicamentos
        Scanner s = new Scanner(System.in);

        PacienteDAO pDAO = new PacienteDAO();
        MedicamentoDAO mDAO = new MedicamentoDAO();

        try {

            int opc = 0;
            do {
                mostrarMenu();

                opc = s.nextInt();

                switch (opc) {
                    case 1: // mostrar paciente
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
                        Transaction txa = session.beginTransaction();
                        try {
                            System.out.println("Ingrese el nombre del paciente:");
                            String nombre = s.nextLine();

                            System.out.println("Ingrese el nivel de glucosa del paciente:");
                            int nivelGlucosa = s.nextInt();

                            System.out.println("Ingrese el nivel de hierro en la sangre del paciente:");
                            int nivelHierroSangre = s.nextInt();

                            Paciente nuevoPaciente = new Paciente(nombre, nivelGlucosa, nivelHierroSangre);
                            pDAO.insertPaciente(session, nuevoPaciente);
                            tx.commit();
                            System.out.println("Paciente insertado correctamente.");
                        } catch (Exception e) {
                            if (txa != null)
                                tx.rollback();
                            System.out.println("Error al insertar paciente: " + e.getMessage());
                        }
                        break;

                    case 3:
                        // Actualizar paciente
                        System.out.println("Ingrese el ID del paciente a actualizar:");
                        int idPaciente = s.nextInt();
                        Paciente paciente = pDAO.selectPacienteById(session, idPaciente);
                        if (paciente != null) {
                            System.out.println("Ingrese el nuevo nombre del paciente:");
                            String nuevoNombre = s.nextLine();
                            System.out.println("Ingrese el nuevo nivel de glucosa del paciente:");
                            int nuevoNivelGlucosa = s.nextInt();
                            System.out.println("Ingrese el nuevo nivel de hierro en la sangre del paciente:");
                            int nuevoNivelHierroSangre = s.nextInt();
                            paciente.setNombre(nuevoNombre);
                            paciente.setNivelGlucosa(nuevoNivelGlucosa);
                            paciente.setNivelHierroSangre(nuevoNivelHierroSangre);
                            pDAO.updatePaciente(session, paciente);
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
                            // Eliminar referencias bidireccionales primero
                            for (Medicamento m : pacienteEliminar.getMedicamentos()) {
                                pacienteEliminar.quitarMedicamento(m);
                            }
                            pDAO.deletePaciente(session, idEliminar);
                            System.out.println("Paciente eliminado.");
                        } else {
                            System.out.println("Paciente no encontrado.");
                        }
                        break;
                    case 5: // mostrar medicamentos
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
                        Transaction t = session.beginTransaction();
                        try {
                            System.out.println("Ingrese el nombre del medicamento:");
                            String nombreMedicamento = s.nextLine();

                            Medicamento nuevoMedicamento = new Medicamento(nombreMedicamento);
                            mDAO.insertMedicamento(session, nuevoMedicamento);
                            tx.commit();
                            System.out.println("Medicamento insertado correctamente.");
                        } catch (Exception e) {
                            if (t != null)
                                tx.rollback();
                            System.out.println("Error al insertar medicamento: " + e.getMessage());
                        }
                        break;

                    case 7:
                        // Actualizar medicamento
                        System.out.println("Ingrese el ID del medicamento a actualizar:");
                        int idMedicamento = s.nextInt();

                        Medicamento medicamento = mDAO.selectMedicamentoById(session, idMedicamento);
                        if (medicamento != null) {
                            System.out.println("Ingrese el nuevo nombre del medicamento:");
                            String nuevoNombreMedicamento = s.nextLine();
                            medicamento.setNombre(nuevoNombreMedicamento);
                            mDAO.updateMedicamento(session, medicamento);
                        } else {
                            System.out.println("Medicamento no encontrado.");
                        }
                        break;
                    case 8:
                        // Eliminar medicamento
                        System.out.println("Ingrese el ID del medicamento a eliminar:");
                        int idMedicamentoEliminar = s.nextInt();
                        Medicamento medicamentoEliminar = mDAO.selectMedicamentoById(session, idMedicamentoEliminar);
                        if (medicamentoEliminar != null) {
                            mDAO.deleteMedicamento(session, idMedicamentoEliminar);
                            System.out.println("Medicamento eliminado.");
                        } else {
                            System.out.println("Medicamento no encontrado.");
                        }
                        mDAO.updateMedicamento(session, medicamentoEliminar);

                        break;

                    case 9:
                        // Asociar todos los pacientes al medicamento "Vitaminas"
                        System.out.println("Asociando todos los pacientes al medicamento 'Vitaminas'...");
                        // Obtenemos todos los medicamentos y mostramos sus pacientes
                        List<Medicamento> medicamentosA = mDAO.selectAllMedicamentos(session);
                        for (Medicamento m : medicamentosA) {
                            System.out.println("Medicamento: " + m.getNombre());
                            // Si el medicamento es "Vitaminas", lo asociamos a todos los pacientes
                            if (m.getNombre().equals("Vitaminas")) {// si el medicamento es vitaminas

                                Set<Paciente> pacientesConVitaminas = m.getPacientes();// Recordar que utilizamos Set,
                                                                                       // me devuelve
                                                                                       // el conjunto de pacientes

                                for (Paciente p : pacientesConVitaminas) {
                                    m.agregarPaciente(p);
                                }

                            }
                            mDAO.updateMedicamento(session, m);

                        }

                        // Mostrar mensaje de éxito
                        System.out.println("Todos los pacientes fueron asociados con Vitaminas.");
                        break;

                    case 10:
                        // Obtenemos todos los medicamentos y mostramos sus pacientes
                        List<Medicamento> medicamentosQ = mDAO.selectAllMedicamentos(session);
                        for (Medicamento m : medicamentosQ) {
                            System.out.println("Medicamento: " + m.getNombre());
                            // Si el medicamento es "Vitaminas", lo eliminamos de los pacientes que tenga
                            // vitaminas
                            if (m.getNombre().equals("Vitaminas")) {// si el medicamento es vitaminas

                                Set<Paciente> pacientesConVitaminas = m.getPacientes();// Recordar que utilizamos Set,
                                                                                       // me devuelve
                                                                                       // el conjunto de pacientes

                                for (Paciente p : pacientesConVitaminas) {
                                    m.quitarPaciente(p);
                                }

                            }
                            mDAO.updateMedicamento(session, m);

                        }

                        // Mostrar mensaje de éxito
                        System.out.println("Todos los pacientes quitados de Vitaminas.");
                        break;

                    case 11:
                        // Mostrar pacientes con medicamentos
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
                        // Salir del programa
                        System.out.println("Saliendo del programa...");
                        break;

                    default:
                        System.out.println("Opción no válida. Por favor, elija una opción del menú.");
                        break;
                }

            } while (opc != 0);
            tx.commit();
            session.clear();
        } catch (Exception e) {

            // Si ocurre un error, revertimos la transacción y mostramos el error
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            // Cerramos la sesión de Hibernate
            session.close();

        }
    }
}

/*
 * // Creamos algunos pacientes con sus datos básicos
 * Paciente pedro = new Paciente("Pedro", 120, 80);
 * Paciente marta = new Paciente("Marta", 95, 70);
 * Paciente juan = new Paciente("Juan", 110, 85);
 * 
 * // Creamos algunos medicamentos
 * Medicamento insulina = new Medicamento("Insulina");
 * Medicamento vitaminas = new Medicamento("Vitaminas");
 * Medicamento hierro = new Medicamento("Suplemento de Hierro");
 * 
 * // Asignamos medicamentos a los pacientes
 * pedro.agregarMedicamento(insulina);
 * marta.agregarMedicamento(vitaminas);
 * marta.agregarMedicamento(insulina);
 * juan.agregarMedicamento(hierro);
 * juan.agregarMedicamento(vitaminas);
 * 
 * // Insertamos los pacientes en la base de datos (los medicamentos se insertan
 * en
 * // cascada)
 * pDAO.insertPaciente(session, juan);
 * pDAO.insertPaciente(session, marta);
 * pDAO.insertPaciente(session, pedro);
 * 
 * // Agregamos un medicamento extra a Juan y actualizamos en la base de datos
 * juan.agregarMedicamento(insulina);
 * pDAO.updatePaciente(session, juan);
 * 
 * pedro.agregarMedicamento(vitaminas);
 * pDAO.updatePaciente(session, pedro);
 * 
 * // se quita todas las vitaminas de los demas pacientes menos de marta
 * marta.agregarMedicamento(vitaminas);
 * pDAO.updatePaciente(session, marta);
 * 
 * pedro.quitarMedicamento(vitaminas);
 * pDAO.updatePaciente(session, pedro);
 * 
 * marta.quitarMedicamento(vitaminas);
 * pDAO.updatePaciente(session, marta);
 * 
 * juan.quitarMedicamento(insulina);
 * pDAO.updatePaciente(session, juan);
 * 
 * // Eliminamos a Pedro de la base de datos
 * pDAO.deletePaciente(session, pedro.getId());
 * // Obtenemos todos los medicamentos y mostramos sus pacientes
 * List<Medicamento> medicamentos = mDAO.selectAllMedicamentos(session);
 * for (Medicamento m : medicamentos) {
 * System.out.println("Medicamento: " + m.getNombre());
 * // Si el medicamento es "Vitaminas", lo eliminamos de los pacientes que tenga
 * // vitaminas
 * if (m.getNombre().equals("Vitaminas")) {// si el medicamento es vitaminas
 * 
 * Set<Paciente> pacientesConVitaminas = m.getPacientes();// Recordar que
 * utilizamos Set, me devuelve
 * // el conjunto de pacientes
 * 
 * for (Paciente p : pacientesConVitaminas) {
 * m.quitarPaciente(p);
 * }
 * 
 * }
 * mDAO.updateMedicamento(session, m);
 * }
 * 
 * // Obtenemos todos los pacientes y mostramos sus medicamentos
 * List<Paciente> pacientes = pDAO.selectAllPacientes(session);
 * 
 * for (Paciente p : pacientes) {
 * System.out.print(p.getNombre() + ": ");
 * for (Medicamento m : p.getMedicamentos()) {
 * System.out.print(m.getNombre() + " ");
 * }
 * System.out.println();
 * }
 * tx.commit();
 * session.clear(); // Esto no es suficiente
 * } catch (Exception e) {
 * 
 * // Si ocurre un error, revertimos la transacción y mostramos el error
 * if (tx != null) {
 * tx.rollback();
 * }
 * e.printStackTrace();
 * } finally {
 * // Cerramos la sesión de Hibernate
 * session.close();
 */
