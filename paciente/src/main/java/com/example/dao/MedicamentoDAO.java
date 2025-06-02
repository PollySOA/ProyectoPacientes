package com.example.dao;

import java.util.List;
import org.hibernate.Session;

import com.example.model.Medicamento;

// Esta clase sirve para manejar todas las operaciones relacionadas con los medicamentos en la base de datos.
// Aquí puedes insertar, actualizar, borrar y consultar medicamentos usando Hibernate.
public class MedicamentoDAO {

    //guarda un nuevo medicamento en la base de datos.
    public void insertMedicamento(Session session, Medicamento m) {
        session.persist(m); // Guarda el medicamento usando la sesión de Hibernate.
        System.out.println("Medicamento insertado");
    }
    
    //actualiza los datos de un medicamento existente en la base de datos.
    public void updateMedicamento(Session session, Medicamento m) {
        session.merge(m); // Actualiza el medicamento con los nuevos datos.
        System.out.println("Medicamento actualizada");
    }
    
    //elimina un medicamento de la base de datos usando su id.
    public void deleteMedicamento(Session session, int id) {
        Medicamento m = session.get(Medicamento.class, id); // Busca el medicamento por id.
        if (m != null) {
            session.remove(m); // Si lo encuentra, lo elimina.
            System.out.println("Medicamento borrado");
        }
    }
    public Medicamento selectMedicamentoById(Session session, int id) {
        
        return session.get(Medicamento.class, id); // Busca y devuelve el medicamento por su id.
       
    }


    // devuelve una lista con todos los medicamentos que hay en la base de datos.
    public List<Medicamento> selectAllMedicamentos(Session session) {
        return session.createQuery("FROM Medicamento", Medicamento.class).list(); // Ejecuta una consulta para obtener todos los medicamentos.

    }
}