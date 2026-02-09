package ProyectoFinal_Gimnasio.Model;

import java.io.Serializable;

public class TrabajadorUQ extends Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    private String beneficioEspecial;

    public TrabajadorUQ(String nombre, String identificacion, String telefono, int edad, Suscripcion suscripcion, String rutaImagen, String beneficioEspecial) {
        super(nombre, identificacion, telefono, edad, suscripcion, rutaImagen); // Pasa rutaImagen al constructor de Usuario
        this.beneficioEspecial = beneficioEspecial;
    }

    public String getBeneficioEspecial() {
        return beneficioEspecial;
    }

    public void setBeneficioEspecial(String beneficioEspecial) {
        this.beneficioEspecial = beneficioEspecial;
    }
}
