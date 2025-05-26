package com.example;

import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.example.dao.MedicamentoDAO;
import com.example.dao.PacienteDAO;
import com.example.example.model.Medicamento;
import com.example.example.model.Paciente;
import com.example.util.HibernateUtil;

public class Main {

    public static void main(String[] args) {
        // Abrimos una sesión de Hibernate y comenzamos una transacción
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        try {
            // Instanciamos los DAOs para manejar pacientes y medicamentos
            PacienteDAO pDAO = new PacienteDAO();
            MedicamentoDAO mDAO = new MedicamentoDAO();

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

            // Insertamos los pacientes en la base de datos (los medicamentos se insertan en
            // cascada)
            pDAO.insertPaciente(session, juan);
            pDAO.insertPaciente(session, marta);
            pDAO.insertPaciente(session, pedro);

            // Quitamos un medicamento a Marta y actualizamos en la base de datos
            marta.quitarMedicamento(vitaminas);
            pDAO.updatePaciente(session, marta);

            // Agregamos un medicamento extra a Juan y actualizamos en la base de datos
            juan.agregarMedicamento(insulina);
            pDAO.updatePaciente(session, juan);

            // Eliminamos a Pedro de la base de datos
            // pDAO.deletePaciente(session, pedro.getId());

            /*
             * List<Medicamento> medicamentos = mDAO.selectAllMedicamentos(session);
             * for (Medicamento m : medicamentos) {
             * System.out.println("Medicamento: " + m.getNombre());
             * // Si el medicamento es "Vitaminas", lo eliminamos de los pacientes que tenga
             * // vitaminas
             * if (m.getNombre().equals("Vitaminas")) {
             * // Obtenemos todos los pacientes que tienen este medicamento
             * Set<Paciente> pacientesConVitaminas = m.getPacientes();// Recordar que
             * utilizamos Set, me devuelve el conjunto de pacientes
             * for (Paciente p : pacientesConVitaminas) {
             * m.quitarPaciente(p);
             * }
             * 
             * }
             * mDAO.updateMedicamento(session, m);
             * }
             */
            // Confirmamos los cambios realizados en la base de datos
            tx.commit();
            session.clear();

            // Obtenemos todos los pacientes y mostramos sus medicamentos
            List<Paciente> pacientes = pDAO.selectAllPacientes(session);

            for (Paciente p : pacientes) {
                System.out.print(p.getNombre() + ": ");
                for (Medicamento m : p.getMedicamentos()) {
                    System.out.print(m.getNombre() + " ");
                }
                System.out.println();
            }

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