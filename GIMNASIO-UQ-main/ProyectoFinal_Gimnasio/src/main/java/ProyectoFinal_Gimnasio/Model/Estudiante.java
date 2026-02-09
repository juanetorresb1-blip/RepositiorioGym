package ProyectoFinal_Gimnasio.Model;

import java.io.Serializable;

public class Estudiante extends Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    private double descuento;

    public Estudiante(String nombre, String identificacion, String telefono, int edad, Suscripcion suscripcion, String rutaImagen, double descuento) {
        super(nombre, identificacion, telefono, edad, suscripcion, rutaImagen); // Pasa rutaImagen al constructor de Usuario
        this.descuento = descuento;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }
}
