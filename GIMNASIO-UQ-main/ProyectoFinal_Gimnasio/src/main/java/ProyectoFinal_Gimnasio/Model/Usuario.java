package ProyectoFinal_Gimnasio.Model;

import java.io.Serializable;

public class Usuario extends Persona implements Serializable {

    private static final long serialVersionUID = 1L;

    private int edad;
    private Suscripcion suscripcion;
    private String rutaImagen; // Nuevo campo para la ruta de la imagen

    public Usuario(String nombre, String identificacion, String telefono, int edad, Suscripcion suscripcion, String rutaImagen) {
        super(nombre, identificacion, telefono);
        this.edad = edad;
        this.suscripcion = suscripcion;
        this.rutaImagen = rutaImagen; // Inicializar en el constructor
    }

    // Constructor existente, adaptado para no requerir rutaImagen si no se proporciona
    public Usuario(String nombre, String identificacion, String telefono, int edad, Suscripcion suscripcion) {
        this(nombre, identificacion, telefono, edad, suscripcion, null); // Llama al constructor completo con rutaImagen nula por defecto
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public Suscripcion getSuscripcion() {
        return suscripcion;
    }

    public void setSuscripcion(Suscripcion suscripcion) {
        this.suscripcion = suscripcion;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }
}
