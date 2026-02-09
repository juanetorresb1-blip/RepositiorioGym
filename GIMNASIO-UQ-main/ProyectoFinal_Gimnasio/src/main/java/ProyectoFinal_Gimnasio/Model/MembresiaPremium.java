package ProyectoFinal_Gimnasio.Model;

import java.io.Serializable; // Importar Serializable

public class MembresiaPremium extends Membresia implements Serializable { // Implementa Serializable

    private static final long serialVersionUID = 1L; // Añadir serialVersionUID

    public MembresiaPremium() {
        super("Membresía Premium");
    }

    @Override
    public String getDescripcion() {
        return "Acceso a máquinas y clases grupales.";
    }

    @Override
    public boolean tieneAccesoMaquinas() {
        return true;
    }

    @Override
    public boolean tieneAccesoClasesGrupales() {
        return true;
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
