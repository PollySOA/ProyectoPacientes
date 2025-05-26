package com.example.example.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

@Data // Lombok genera automáticamente los métodos básicos como getters, setters, equals y hashCode
@NoArgsConstructor // Lombok crea un constructor vacío por defecto
@EqualsAndHashCode(exclude = "medicamentos") // Evita recursión infinita en equals/hashCode por la relación bidireccional
@Entity
@Table(name = "paciente")
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_paciente")
    private int id; // Identificador único para cada paciente

    private String nombre; // Nombre del paciente
    private int nivelGlucosa; // Nivel de glucosa en sangre
    private int nivelHierroSangre; // Nivel de hierro en sangre

    // Relación muchos a muchos con medicamentos.
    // Se usa una tabla intermedia llamada 'paciente_medicamento' para gestionar la relación.
    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER)
    @JoinTable(
        name = "paciente_medicamento",
        joinColumns = @JoinColumn(name = "idpaciente"),
        inverseJoinColumns = @JoinColumn(name = "idmedicamento")
    )
    private Set<Medicamento> medicamentos = new HashSet<>();

    // Constructor para crear un paciente con nombre y niveles iniciales
    public Paciente(String nombre, int nivelGlucosa, int nivelHierroSangre) {
        this.nombre = nombre;
        this.nivelGlucosa = nivelGlucosa;
        this.nivelHierroSangre = nivelHierroSangre;
    }

    // para asociar un medicamento a este paciente y actualizar la relación en el medicamento
    public void agregarMedicamento(Medicamento m) {
        medicamentos.add(m);
        m.getPacientes().add(this);
    }

   
    public void quitarMedicamento(Medicamento m) {
        medicamentos.remove(m);
        m.getPacientes().remove(this);
    }
}
