package ProyectoFinal_Gimnasio.Model;

import java.io.Serializable; // Importar Serializable

public class MembresiaVip extends Membresia implements Serializable { // Implementa Serializable

    private static final long serialVersionUID = 1L; // Añadir serialVersionUID

    public MembresiaVip() {
        super("Membresía VIP");
    }

    @Override
    public String getDescripcion() {
        return "Acceso ilimitado a todas las clases, área de spa y entrenador personal.";
    }

    @Override
    public boolean tieneAccesoMaquinas() {
        return true; // Asumo que VIP también incluye acceso a máquinas
    }

    @Override
    public boolean tieneAccesoClasesGrupales() {
        return true;
    }

    @Override
    public boolean tieneAccesoSpa() {
        return true;
    }

    @Override
    public boolean tieneEntrenadorPersonal() {
        return true;
    }
}
