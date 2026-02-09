package ProyectoFinal_Gimnasio.Model;

import java.io.Serializable; // Importar Serializable

public class Administrador extends Empleado implements Serializable { // Implementa Serializable

    private static final long serialVersionUID = 1L; // Añadir serialVersionUID

    private String password;

    public Administrador(String nombre, String identificacion, String telefono, double salario, String cargo, String password) {
        super(nombre, identificacion, telefono, salario, cargo);
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
