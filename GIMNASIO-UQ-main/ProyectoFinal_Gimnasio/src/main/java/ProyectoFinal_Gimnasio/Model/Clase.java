package ProyectoFinal_Gimnasio.Model;

import java.io.Serializable; // Importar Serializable
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Clase implements Serializable { // Implementa Serializable

    private static final long serialVersionUID = 1L; // Añadir serialVersionUID

    private String nombre;
    private TipoClase tipo;
    private LocalTime horario;
    private int cupoMaximo;
    private Entrenador entrenador;
    private List<Usuario> usuariosInscritos;

    public Clase(String nombre, TipoClase tipo, LocalTime horario, int cupoMaximo, Entrenador entrenador) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.horario = horario;
        this.cupoMaximo = cupoMaximo;
        this.entrenador = entrenador;
        this.usuariosInscritos = new ArrayList<>();
    }

    public boolean inscribirUsuario(Usuario usuario) {
        if (usuariosInscritos.size() < cupoMaximo) {
            usuariosInscritos.add(usuario);
            return true; // Inscripción exitosa
        }
        return false; // No hay cupo
    }

    // --- Getters y Setters ---

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TipoClase getTipo() {
        return tipo;
    }

    public void setTipo(TipoClase tipo) {
        this.tipo = tipo;
    }

    public LocalTime getHorario() {
        return horario;
    }

    public void setHorario(LocalTime horario) {
        this.horario = horario;
    }

    public int getCupoMaximo() {
        return cupoMaximo;
    }

    public void setCupoMaximo(int cupoMaximo) {
        this.cupoMaximo = cupoMaximo;
    }

    public Entrenador getEntrenador() {
        return entrenador;
    }

    public void setEntrenador(Entrenador entrenador) {
        this.entrenador = entrenador;
    }


    public List<Usuario> getUsuariosInscritos() {
        return usuariosInscritos;
    }
}
