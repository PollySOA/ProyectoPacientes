package com.example.dao;

import java.util.List;

import org.hibernate.Session;

import com.example.example.model.Paciente;

public class PacienteDAO {

    public void insertPaciente(Session session, Paciente p) {
        session.persist(p);
        System.out.println("Paciente insertado");
    }

    public void updatePaciente(Session session, Paciente p) {
        session.merge(p);
        System.out.println("Paciente actualizado");
    }

    public void deletePaciente(Session session, int id) {
        Paciente p = session.get(Paciente.class, id);
        if (p != null) {
            session.remove(p);
            System.out.println("Paciente borrado");
        }
    }

    public List<Paciente> selectAllPacientes(Session session) {
        return session.createQuery("FROM Paciente", Paciente.class).list();

    }
}
