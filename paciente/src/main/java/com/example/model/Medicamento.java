package com.example.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

@Data // Lombok genera automáticamente los métodos básicos como getters, setters, equals y hashCode
@NoArgsConstructor // Lombok crea un constructor vacío por defecto
@Entity
@Table(name = "medicamento")
public class Medicamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_medicamento")
    private int id; // Identificador único para cada medicamento

    private String nombre; // Nombre del medicamento

    // Aquí se define la relación muchos a muchos con los pacientes.
    // 'mappedBy' indica que la relación está gestionada por el atributo 'medicamentos' en la clase Paciente.
    @ManyToMany(mappedBy = "medicamentos", fetch = FetchType.EAGER)
    private Set<Paciente> pacientes = new HashSet<>();

    // Constructor que permite crear un medicamento indicando solo el nombre
    public Medicamento(String nombre) {
        this.nombre = nombre;
    }

    //  asocia un paciente a este medicamento y también actualiza la relación en el paciente.
    public void agregarPaciente(Paciente p) {
        pacientes.add(p);
        p.getMedicamentos().add(this);
    }

    
    public void quitarPaciente(Paciente p) {
        pacientes.remove(p);
        p.getMedicamentos().remove(this);
    }
}