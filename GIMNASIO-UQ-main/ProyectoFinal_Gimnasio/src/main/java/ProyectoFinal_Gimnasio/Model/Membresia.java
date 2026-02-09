package ProyectoFinal_Gimnasio.Model;

import java.io.Serializable; // Importar Serializable

public abstract class Membresia implements Serializable { // Implementa Serializable

    private static final long serialVersionUID = 1L; // Añadir serialVersionUID

    protected String nombre;

    public Membresia(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Métodos abstractos para definir las características de cada tipo de membresía
    public abstract String getDescripcion();
    public abstract boolean tieneAccesoMaquinas();
    public abstract boolean tieneAccesoClasesGrupales();
    public abstract boolean tieneAccesoSpa();
    public abstract boolean tieneEntrenadorPersonal();

    @Override
    public String toString() {
        return nombre;
    }
}
