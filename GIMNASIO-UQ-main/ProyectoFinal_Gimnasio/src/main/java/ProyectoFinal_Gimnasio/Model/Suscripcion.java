package ProyectoFinal_Gimnasio.Model;

import java.io.Serializable; // Importar Serializable
import java.util.Date;

public class Suscripcion implements Serializable { // Implementa Serializable

    private static final long serialVersionUID = 1L; // Añadir serialVersionUID

    private Membresia membresia; // El plan de membresía (Básica, Premium, VIP)
    private double costo;
    private Date fechaInicio;
    private Date fechaVencimiento;
    private EstadoSuscripcion estado;
    private DuracionPlan duracionPlan;

    public Suscripcion(Membresia membresia, double costo, Date fechaInicio, Date fechaVencimiento, EstadoSuscripcion estado, DuracionPlan duracionPlan) {
        this.membresia = membresia;
        this.costo = costo;
        this.fechaInicio = fechaInicio;
        this.fechaVencimiento = fechaVencimiento;
        this.estado = estado;
        this.duracionPlan = duracionPlan;
    }

    public Membresia getMembresia() {
        return membresia;
    }

    public void setMembresia(Membresia membresia) {
        this.membresia = membresia;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public EstadoSuscripcion getEstado() {
        return estado;
    }

    public void setEstado(EstadoSuscripcion estado) {
        this.estado = estado;
    }

    public DuracionPlan getDuracionPlan() {
        return duracionPlan;
    }

    public void setDuracionPlan(DuracionPlan duracionPlan) {
        this.duracionPlan = duracionPlan;
    }

    // Método para verificar si la suscripción está activa
    public boolean estaActiva() {
        // También se podría añadir una lógica para verificar si la fecha actual está dentro del rango
        return this.estado == EstadoSuscripcion.ACTIVA && (fechaVencimiento == null || new Date().before(this.fechaVencimiento));
    }
}
