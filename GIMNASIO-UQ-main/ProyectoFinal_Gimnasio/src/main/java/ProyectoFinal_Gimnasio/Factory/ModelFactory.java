package ProyectoFinal_Gimnasio.Factory;

import ProyectoFinal_Gimnasio.Model.*;

import java.io.*;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

public class ModelFactory {

    private static ModelFactory instance;
    private Gimnasio gimnasio;
    private final String FILE_PATH = "gimnasio_data.dat";

    private ModelFactory() {
        gimnasio = cargarDatos();

        if (gimnasio == null) {
            gimnasio = new Gimnasio();
            // Si no hay datos cargados, inicializar todos los datos de prueba
            inicializarDatosPruebaCompletos();
        } else {
            // Si se cargaron datos, asegurar que las credenciales de admin/recepcionista existan
            asegurarCredencialesIniciales();
        }
    }

    public static ModelFactory getInstance() {
        if (instance == null) {
            instance = new ModelFactory();
        }
        return instance;
    }

    public Gimnasio getGimnasio() {
        return gimnasio;
    }

    // --- Métodos de Persistencia ---

    public void guardarDatos(Gimnasio gimnasio) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(gimnasio);
            System.out.println("Datos del gimnasio guardados exitosamente.");
        } catch (IOException e) {
            System.err.println("Error al guardar los datos del gimnasio: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Gimnasio cargarDatos() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            Gimnasio loadedGimnasio = (Gimnasio) ois.readObject();
            System.out.println("Datos del gimnasio cargados exitosamente.");
            return loadedGimnasio;
        } catch (FileNotFoundException e) {
            System.out.println("Archivo de datos no encontrado. Se inicializará con datos de prueba.");
            return null;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al cargar los datos del gimnasio: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // Este método inicializa todos los datos de prueba si no hay archivo de persistencia
    private void inicializarDatosPruebaCompletos() {
        asegurarCredencialesIniciales(); // Asegurar admin y recep
        
        // Crear Entrenadores
        gimnasio.getEntrenadores().add(crearEntrenador("profe", "profe", "789", 35000, "Entrenador", "Yoga"));

        // Crear una Membresía (plan) y una Suscripción de prueba
        Membresia membresiaPlanVIP = crearMembresiaVip();
        
        Date fechaInicio = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(fechaInicio);
        cal.add(Calendar.YEAR, 1);
        Date fechaVencimiento = cal.getTime();

        Suscripcion suscripcionPrueba = crearSuscripcion(
            membresiaPlanVIP, 
            150.0,
            fechaInicio, 
            fechaVencimiento, 
            EstadoSuscripcion.ACTIVA, 
            DuracionPlan.ANUAL
        );

        gimnasio.getUsuarios().add(crearUsuario("user", "user", "111", 25, suscripcionPrueba, null));
    }

    // Este método asegura que el admin y el recepcionista de prueba siempre existan
    private void asegurarCredencialesIniciales() {
        // Asegurar Administrador
        Optional<Administrador> adminExistente = gimnasio.getAdministradores().stream()
                                                        .filter(a -> a.getIdentificacion().equals("admin"))
                                                        .findFirst();
        if (adminExistente.isEmpty()) {
            gimnasio.getAdministradores().add(crearAdministrador("admin", "admin", "123", 50000, "Administrador", "admin"));
        }

        // Asegurar Recepcionista
        Optional<Recepcionista> recepExistente = gimnasio.getRecepcionistas().stream()
                                                        .filter(r -> r.getIdentificacion().equals("recep"))
                                                        .findFirst();
        if (recepExistente.isEmpty()) {
            gimnasio.getRecepcionistas().add(crearRecepcionista("recep", "recep", "456", 25000, "Recepcionista"));
        }
    }

    // --- Métodos para crear objetos del modelo ---

    public Membresia crearMembresiaBasica() {
        return new MembresiaBasica();
    }

    public Membresia crearMembresiaPremium() {
        return new MembresiaPremium();
    }

    public Membresia crearMembresiaVip() {
        return new MembresiaVip();
    }

    public Suscripcion crearSuscripcion(Membresia membresia, double costo, Date fechaInicio, Date fechaVencimiento, EstadoSuscripcion estado, DuracionPlan duracionPlan) {
        return new Suscripcion(membresia, costo, fechaInicio, fechaVencimiento, estado, duracionPlan);
    }

    public Usuario crearUsuario(String nombre, String identificacion, String telefono, int edad, Suscripcion suscripcion, String rutaImagen) {
        return new Usuario(nombre, identificacion, telefono, edad, suscripcion, rutaImagen);
    }

    public Estudiante crearEstudiante(String nombre, String identificacion, String telefono, int edad, Suscripcion suscripcion, String rutaImagen, double descuento) {
        return new Estudiante(nombre, identificacion, telefono, edad, suscripcion, rutaImagen, descuento);
    }

    public TrabajadorUQ crearTrabajadorUQ(String nombre, String identificacion, String telefono, int edad, Suscripcion suscripcion, String rutaImagen, String beneficioEspecial) {
        return new TrabajadorUQ(nombre, identificacion, telefono, edad, suscripcion, rutaImagen, beneficioEspecial);
    }

    public Externo crearExterno(String nombre, String identificacion, String telefono, int edad, Suscripcion suscripcion, String rutaImagen) {
        return new Externo(nombre, identificacion, telefono, edad, suscripcion, rutaImagen);
    }

    public Entrenador crearEntrenador(String nombre, String identificacion, String telefono, double salario, String cargo, String especialidad) {
        return new Entrenador(nombre, identificacion, telefono, salario, cargo, especialidad);
    }

    public Recepcionista crearRecepcionista(String nombre, String identificacion, String telefono, double salario, String cargo) {
        return new Recepcionista(nombre, identificacion, telefono, salario, cargo);
    }

    public Administrador crearAdministrador(String nombre, String identificacion, String telefono, double salario, String cargo, String password) {
        return new Administrador(nombre, identificacion, telefono, salario, cargo, password);
    }

    public Clase crearClase(String nombre, TipoClase tipo, LocalTime horario, int cupoMaximo, Entrenador entrenador) {
        return new Clase(nombre, tipo, horario, cupoMaximo, entrenador);
    }

    // --- Placeholder para Notificaciones por Correo ---
    public void enviarNotificacionCorreo(Usuario usuario, String asunto, String mensaje) {
        if (usuario != null) {
            System.out.println("--- SIMULACIÓN DE ENVÍO DE CORREO ---");
            System.out.println("Para: " + usuario.getNombre() + " (ID: " + usuario.getIdentificacion() + ")");
            System.out.println("Asunto: " + asunto);
            System.out.println("Mensaje: " + mensaje);
            System.out.println("------------------------------------");
        } else {
            System.out.println("No se pudo enviar correo: Usuario nulo.");
        }
    }
}
