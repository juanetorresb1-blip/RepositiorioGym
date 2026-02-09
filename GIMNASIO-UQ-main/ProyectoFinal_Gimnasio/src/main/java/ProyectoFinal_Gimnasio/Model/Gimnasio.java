package ProyectoFinal_Gimnasio.Model;

import java.io.Serializable; // Importar Serializable
import java.util.ArrayList;
import java.util.List;

public class Gimnasio implements Serializable { // Implementa Serializable

    private static final long serialVersionUID = 1L; // Añadir serialVersionUID

    private List<Usuario> usuarios;
    private List<Clase> clases;
    private List<Entrenador> entrenadores;
    private List<Recepcionista> recepcionistas;
    private List<Administrador> administradores;

    public Gimnasio() {
        usuarios = new ArrayList<>();
        clases = new ArrayList<>();
        entrenadores = new ArrayList<>();
        recepcionistas = new ArrayList<>();
        administradores = new ArrayList<>();
    }

    public Object login(String usuario, String password) {
        for (Administrador admin : administradores) {
            if (admin.getIdentificacion().equals(usuario) && admin.getPassword().equals(password)) {
                return admin;
            }
        }
        for (Recepcionista recep : recepcionistas) {
            if (recep.getIdentificacion().equals(usuario) && recep.getIdentificacion().equals(password)) {
                return recep;
            }
        }
        return null;
    }

    // --- Getters ---

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public List<Clase> getClases() {
        return clases;
    }

    public List<Entrenador> getEntrenadores() {
        return entrenadores;
    }

    public List<Recepcionista> getRecepcionistas() {
        return recepcionistas;
    }

    public List<Administrador> getAdministradores() {
        return administradores;
    }
}
