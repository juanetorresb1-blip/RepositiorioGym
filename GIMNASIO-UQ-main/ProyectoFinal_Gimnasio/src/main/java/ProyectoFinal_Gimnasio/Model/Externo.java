package ProyectoFinal_Gimnasio.Model;

import java.io.Serializable;

public class Externo extends Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    public Externo(String nombre, String identificacion, String telefono, int edad, Suscripcion suscripcion, String rutaImagen) {
        super(nombre, identificacion, telefono, edad, suscripcion, rutaImagen); // Pasa rutaImagen al constructor de Usuario
    }
}
