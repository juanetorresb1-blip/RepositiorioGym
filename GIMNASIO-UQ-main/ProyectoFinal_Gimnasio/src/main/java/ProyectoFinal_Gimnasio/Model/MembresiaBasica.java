package ProyectoFinal_Gimnasio.Model;

import java.io.Serializable; // Importar Serializable

public class MembresiaBasica extends Membresia implements Serializable { // Implementa Serializable

    private static final long serialVersionUID = 1L; // Añadir serialVersionUID

    public MembresiaBasica() {
        super("Membresía Básica");
    }

    @Override
    public String getDescripcion() {
        return "Acceso general a máquinas.";
    }

    @Override
    public boolean tieneAccesoMaquinas() {
        return true;
    }

    @Override
    public boolean tieneAccesoClasesGrupales() {
        return false;
    }

    @Override
    public boolean tieneAccesoSpa() {
        return false;
    }

    @Override
    public boolean tieneEntrenadorPersonal() {
        return false;
    }
}
