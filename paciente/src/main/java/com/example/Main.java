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
        System.out.println("9. Ascociar Paciente a Medicamento");
        System.out.println("10. Quitar Paciente a Medicamento");
        System.out.println("11. Mostrar Pacientes con Medicamentos"); // con el for anidado que hemos hecho
        System.out.println("0. salir");
        System.out.println("--------------------------------------------------------");

    }

    public static void main(String[] args) {
        // Abrimos una sesión de Hibernate y comenzamos una transacción
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        try {
            // Instanciamos los DAOs para manejar pacientes y medicamentos
            Scanner s = new Scanner(System.in);
            int opc;
            PacienteDAO pDAO = new PacienteDAO();
            MedicamentoDAO mDAO = new MedicamentoDAO();

            do {

                switch (opc) {
                    case 1: // mostrar paciente
                        List<Paciente> pacientes = pDAO.selectAllPacientes(session);
                        for (Paciente p : pacientes) {
                            System.out.println("Paciente: " + p.getNombre() + ", ID: " + p.getId());
                            System.out.println("Medicamentos: ");
                            for (Medicamento m : p.getMedicamentos()) {
                                System.out.println("- " + m.getNombre());
                            }
                        }

                    case 2:
                        // Insertar paciente
                        System.out.println("Ingrese el nombre del paciente:");
                        String nombre = s.nextLine();
                        System.out.println("Ingrese el nivel de glucosa del paciente:");
                        int nivelGlucosa = s.nextInt();
                        System.out.println("Ingrese  el nivel de hierro en la sangre del paciente:");
                        int nivelHierroSangre = s.nextInt();
                        s.nextLine(); // Limpiar el buffer del scanner
                        break;
                    case 3:
                        // Actualizar paciente
                        System.out.println("Ingrese el ID del paciente a actualizar:");
                        int idPaciente = s.nextInt();
                        s.nextLine();

                        break;

                    default:
                        break;
                }

            } while (opc != 0);

            // Creamos algunos pacientes con sus datos básicos
            Paciente pedro = new Paciente("Pedro", 120, 80);
            Paciente marta = new Paciente("Marta", 95, 70);
            Paciente juan = new Paciente("Juan", 110, 85);

            // Creamos algunos medicamentos
            Medicamento insulina = new Medicamento("Insulina");
            Medicamento vitaminas = new Medicamento("Vitaminas");
            Medicamento hierro = new Medicamento("Suplemento de Hierro");

            // Asignamos medicamentos a los pacientes
            pedro.agregarMedicamento(insulina);
            marta.agregarMedicamento(vitaminas);
            marta.agregarMedicamento(insulina);
            juan.agregarMedicamento(hierro);
            juan.agregarMedicamento(vitaminas);

            // Insertamos los pacientes en la base de datos (los medicamentos se insertan en
            // cascada)
            pDAO.insertPaciente(session, juan);
            pDAO.insertPaciente(session, marta);
            pDAO.insertPaciente(session, pedro);

            // Agregamos un medicamento extra a Juan y actualizamos en la base de datos
            juan.agregarMedicamento(insulina);
            pDAO.updatePaciente(session, juan);

            pedro.agregarMedicamento(vitaminas);
            pDAO.updatePaciente(session, pedro);

            // se quita todas las vitaminas de los demas pacientes menos de marta
            marta.agregarMedicamento(vitaminas);
            pDAO.updatePaciente(session, marta);

            pedro.quitarMedicamento(vitaminas);
            pDAO.updatePaciente(session, pedro);

            marta.quitarMedicamento(vitaminas);
            pDAO.updatePaciente(session, marta);

            juan.quitarMedicamento(insulina);
            pDAO.updatePaciente(session, juan);

            // Eliminamos a Pedro de la base de datos
            pDAO.deletePaciente(session, pedro.getId());
            // Obtenemos todos los medicamentos y mostramos sus pacientes
            List<Medicamento> medicamentos = mDAO.selectAllMedicamentos(session);
            for (Medicamento m : medicamentos) {
                System.out.println("Medicamento: " + m.getNombre());
                // Si el medicamento es "Vitaminas", lo eliminamos de los pacientes que tenga
                // vitaminas
                if (m.getNombre().equals("Vitaminas")) {// si el medicamento es vitaminas

                    Set<Paciente> pacientesConVitaminas = m.getPacientes();// Recordar que utilizamos Set, me devuelve
                                                                           // el conjunto de pacientes

                    for (Paciente p : pacientesConVitaminas) {
                        m.quitarPaciente(p);
                    }

                }
                mDAO.updateMedicamento(session, m);
            }

            // Obtenemos todos los pacientes y mostramos sus medicamentos
            List<Paciente> pacientes = pDAO.selectAllPacientes(session);

            for (Paciente p : pacientes) {
                System.out.print(p.getNombre() + ": ");
                for (Medicamento m : p.getMedicamentos()) {
                    System.out.print(m.getNombre() + " ");
                }
                System.out.println();
            }
            tx.commit();
            session.clear(); // Esto no es suficiente
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
