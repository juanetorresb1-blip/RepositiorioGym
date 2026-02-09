package ProyectoFinal_Gimnasio.Model;

import java.io.Serializable; // Importar Serializable

public class Empleado extends Persona implements Serializable { // Implementa Serializable

    private static final long serialVersionUID = 1L; // Añadir serialVersionUID

    private double salario;
    private String cargo;

    public Empleado(String nombre, String identificacion, String telefono, double salario, String cargo) {
        super(nombre, identificacion, telefono);
        this.salario = salario;
        this.cargo = cargo;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }
}
