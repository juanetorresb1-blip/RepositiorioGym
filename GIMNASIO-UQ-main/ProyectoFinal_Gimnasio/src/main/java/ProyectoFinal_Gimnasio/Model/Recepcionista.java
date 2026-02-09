package ProyectoFinal_Gimnasio.Model;

import java.io.Serializable; // Importar Serializable

public class Recepcionista extends Empleado implements Serializable { // Implementa Serializable

    private static final long serialVersionUID = 1L; // Añadir serialVersionUID

    public Recepcionista(String nombre, String identificacion, String telefono, double salario, String cargo) {
        super(nombre, identificacion, telefono, salario, cargo);
    }
}
