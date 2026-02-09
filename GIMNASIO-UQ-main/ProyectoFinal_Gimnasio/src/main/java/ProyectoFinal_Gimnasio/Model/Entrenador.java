package ProyectoFinal_Gimnasio.Model;

import java.io.Serializable; // Importar Serializable

public class Entrenador extends Empleado implements Serializable { // Implementa Serializable

    private static final long serialVersionUID = 1L; // Añadir serialVersionUID

    private String especialidad;

    public Entrenador(String nombre, String identificacion, String telefono, double salario, String cargo, String especialidad) {
        super(nombre, identificacion, telefono, salario, cargo);
        this.especialidad = especialidad;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }
}
