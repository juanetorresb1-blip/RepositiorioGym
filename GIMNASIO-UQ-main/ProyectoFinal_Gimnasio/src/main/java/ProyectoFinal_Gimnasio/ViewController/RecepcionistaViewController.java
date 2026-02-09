package ProyectoFinal_Gimnasio.ViewController;

import ProyectoFinal_Gimnasio.Factory.ModelFactory;
import ProyectoFinal_Gimnasio.Model.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RecepcionistaViewController {

    // --- Pestaña Registro ---
    @FXML private TextField nombreField;
    @FXML private TextField idField;
    @FXML private TextField edadField;
    @FXML private TextField telefonoField;
    @FXML private ComboBox<String> membresiaBox;
    @FXML private ComboBox<DuracionPlan> duracionPlanBox;
    @FXML private ComboBox<String> tipoUsuarioBox;
    @FXML private Label descuentoLabel;
    @FXML private Label beneficioEspecialLabel;
    @FXML private TextField rutaImagenField;
    @FXML private TableView<Usuario> usuariosTable;
    @FXML private TableColumn<Usuario, String> nombreColumn;
    @FXML private TableColumn<Usuario, String> idColumn;
    @FXML private TableColumn<Usuario, Integer> edadColumn;
    @FXML private TableColumn<Usuario, String> telefonoColumn;
    @FXML private TableColumn<Usuario, String> membresiaColumn;
    @FXML private Label receptionistNameLabel;

    // --- Pestaña Reservas ---
    @FXML private ComboBox<Usuario> reservaUsuarioBox;
    @FXML private ComboBox<Clase> reservaClaseBox;
    @FXML private Label claseInfoLabel;

    // --- Pestaña Control de Acceso ---
    @FXML private TextField accesoIdField;
    @FXML private Label accesoResultadoLabel;

    private Gimnasio gimnasio = ModelFactory.getInstance().getGimnasio();
    private ModelFactory modelFactory = ModelFactory.getInstance();

    @FXML
    public void initialize() {
        membresiaBox.getItems().addAll("Membresía Básica", "Membresía Premium", "Membresía VIP");
        duracionPlanBox.getItems().setAll(DuracionPlan.values());
        tipoUsuarioBox.getItems().addAll("Estudiante", "Trabajador UQ", "Externo");

        tipoUsuarioBox.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            descuentoLabel.setVisible(false);
            beneficioEspecialLabel.setVisible(false);
            if ("Estudiante".equals(newSelection)) {
                descuentoLabel.setVisible(true);
            } else if ("Trabajador UQ".equals(newSelection)) {
                beneficioEspecialLabel.setVisible(true);
            }
        });

        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("identificacion"));
        edadColumn.setCellValueFactory(new PropertyValueFactory<>("edad"));
        telefonoColumn.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        membresiaColumn.setCellValueFactory(cellData -> {
            Suscripcion s = cellData.getValue().getSuscripcion();
            return new javafx.beans.property.SimpleStringProperty(s != null ? s.getMembresia().getNombre() : "Sin suscripción");
        });
        actualizarTablaUsuarios();

        usuariosTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                nombreField.setText(newSelection.getNombre());
                idField.setText(newSelection.getIdentificacion());
                telefonoField.setText(newSelection.getTelefono());
                edadField.setText(String.valueOf(newSelection.getEdad()));
                rutaImagenField.setText(newSelection.getRutaImagen());
                
                Suscripcion s = newSelection.getSuscripcion();
                if (s != null) {
                    membresiaBox.setValue(s.getMembresia().getNombre());
                    duracionPlanBox.setValue(s.getDuracionPlan());
                } else {
                    membresiaBox.setValue(null);
                    duracionPlanBox.setValue(null);
                }

                if (newSelection instanceof Estudiante) {
                    tipoUsuarioBox.setValue("Estudiante");
                    descuentoLabel.setVisible(true);
                    beneficioEspecialLabel.setVisible(false);
                } else if (newSelection instanceof TrabajadorUQ) {
                    tipoUsuarioBox.setValue("Trabajador UQ");
                    beneficioEspecialLabel.setVisible(true);
                    descuentoLabel.setVisible(false);
                } else {
                    tipoUsuarioBox.setValue("Externo");
                    descuentoLabel.setVisible(false);
                    beneficioEspecialLabel.setVisible(false);
                }
                idField.setEditable(false);
            } else {
                limpiarCamposRegistro();
                idField.setEditable(true);
            }
        });

        actualizarComboBoxReserva();
        reservaUsuarioBox.setConverter(createUsuarioConverter());
        reservaClaseBox.setConverter(createClaseConverter());

        reservaClaseBox.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                int inscritos = newSelection.getUsuariosInscritos().size();
                int cupo = newSelection.getCupoMaximo();
                String info = String.format("Clase: %s\nEntrenador: %s\nHorario: %s\nInscritos: %d / %d",
                    newSelection.getNombre(),
                    newSelection.getEntrenador().getNombre(),
                    newSelection.getHorario().toString(),
                    inscritos,
                    cupo);
                claseInfoLabel.setText(info);
            } else {
                claseInfoLabel.setText("Seleccione una clase para ver sus detalles...");
            }
        });
    }

    @FXML
    void registrarUsuario(ActionEvent event) {
        String nombre = nombreField.getText();
        String id = idField.getText();
        String edadStr = edadField.getText();
        String telefono = telefonoField.getText();
        String tipoPlanMembresia = membresiaBox.getValue();
        DuracionPlan duracionPlan = duracionPlanBox.getValue();
        String tipoUsuario = tipoUsuarioBox.getValue();
        String rutaImagen = rutaImagenField.getText();

        if (nombre.isEmpty() || id.isEmpty() || edadStr.isEmpty() || telefono.isEmpty() || tipoPlanMembresia == null || duracionPlan == null || tipoUsuario == null) {
            mostrarAlerta("Error de Validación", "Todos los campos son obligatorios.", Alert.AlertType.ERROR);
            return;
        }

        int edad;
        try {
            edad = Integer.parseInt(edadStr);
        } catch (NumberFormatException e) {
            mostrarAlerta("Error de Formato", "La edad debe ser un número válido.", Alert.AlertType.ERROR);
            return;
        }

        boolean idExiste = gimnasio.getUsuarios().stream().anyMatch(u -> u.getIdentificacion().equals(id));
        if (idExiste) {
            mostrarAlerta("Error", "Ya existe un usuario con esa identificación. Use 'Modificar' si desea actualizarlo.", Alert.AlertType.ERROR);
            return;
        }

        Membresia planMembresia;
        switch (tipoPlanMembresia) {
            case "Membresía Básica": planMembresia = modelFactory.crearMembresiaBasica(); break;
            case "Membresía Premium": planMembresia = modelFactory.crearMembresiaPremium(); break;
            case "Membresía VIP": planMembresia = modelFactory.crearMembresiaVip(); break;
            default:
                mostrarAlerta("Error", "Tipo de membresía no válido.", Alert.AlertType.ERROR);
                return;
        }

        double costoBase = 0;
        switch (tipoPlanMembresia) {
            case "Membresía Básica": costoBase = 50.0; break;
            case "Membresía Premium": costoBase = 100.0; break;
            case "Membresía VIP": costoBase = 150.0; break;
        }

        double costoFinal = costoBase;
        int mesesDuracion = 0;
        switch (duracionPlan) {
            case MENSUAL: mesesDuracion = 1; break;
            case TRIMESTRAL: mesesDuracion = 3; costoFinal *= 0.95; break;
            case ANUAL: mesesDuracion = 12; costoFinal *= 0.90; break;
        }

        Date fechaInicio = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(fechaInicio);
        cal.add(Calendar.MONTH, mesesDuracion);
        Date fechaVencimiento = cal.getTime();
        
        Suscripcion nuevaSuscripcion = modelFactory.crearSuscripcion(
            planMembresia, 
            costoFinal, 
            fechaInicio, 
            fechaVencimiento, 
            EstadoSuscripcion.ACTIVA, 
            duracionPlan
        );

        Usuario nuevoUsuario = null;
        switch (tipoUsuario) {
            case "Estudiante":
                nuevoUsuario = modelFactory.crearEstudiante(nombre, id, telefono, edad, nuevaSuscripcion, rutaImagen, 0.15);
                break;
            case "Trabajador UQ":
                nuevoUsuario = modelFactory.crearTrabajadorUQ(nombre, id, telefono, edad, nuevaSuscripcion, rutaImagen, "Acceso a casillero gratuito");
                break;
            case "Externo":
                nuevoUsuario = modelFactory.crearExterno(nombre, id, telefono, edad, nuevaSuscripcion, rutaImagen);
                break;
            default:
                mostrarAlerta("Error", "Tipo de usuario no válido.", Alert.AlertType.ERROR);
                return;
        }
        
        if (nuevoUsuario != null) {
            gimnasio.getUsuarios().add(nuevoUsuario);
            actualizarTablaUsuarios();
            actualizarComboBoxReserva();
            limpiarCamposRegistro();
            mostrarAlerta("Registro Exitoso", "El usuario ha sido registrado correctamente.", Alert.AlertType.INFORMATION);
            modelFactory.enviarNotificacionCorreo(nuevoUsuario, "Bienvenido a Gimnasio UQ Fit", "¡Hola " + nuevoUsuario.getNombre() + "! Te damos la bienvenida a nuestro gimnasio. Tu suscripción está activa.");
        }
    }

    @FXML
    void modificarUsuario(ActionEvent event) {
        Usuario usuarioSeleccionado = usuariosTable.getSelectionModel().getSelectedItem();
        if (usuarioSeleccionado == null) {
            mostrarAlerta("Error", "Debe seleccionar un usuario de la tabla para modificar.", Alert.AlertType.ERROR);
            return;
        }

        String nombre = nombreField.getText();
        String id = idField.getText();
        String edadStr = edadField.getText();
        String telefono = telefonoField.getText();
        String tipoPlanMembresia = membresiaBox.getValue();
        DuracionPlan duracionPlan = duracionPlanBox.getValue();
        String tipoUsuario = tipoUsuarioBox.getValue();
        String rutaImagen = rutaImagenField.getText();

        if (nombre.isEmpty() || id.isEmpty() || edadStr.isEmpty() || telefono.isEmpty() || tipoPlanMembresia == null || duracionPlan == null || tipoUsuario == null) {
            mostrarAlerta("Error de Validación", "Todos los campos son obligatorios.", Alert.AlertType.ERROR);
            return;
        }

        int edad;
        try {
            edad = Integer.parseInt(edadStr);
        } catch (NumberFormatException e) {
            mostrarAlerta("Error de Formato", "La edad debe ser un número válido.", Alert.AlertType.ERROR);
            return;
        }

        usuarioSeleccionado.setNombre(nombre);
        usuarioSeleccionado.setTelefono(telefono);
        usuarioSeleccionado.setEdad(edad);
        usuarioSeleccionado.setRutaImagen(rutaImagen);

        Membresia planMembresia;
        switch (tipoPlanMembresia) {
            case "Membresía Básica": planMembresia = modelFactory.crearMembresiaBasica(); break;
            case "Membresía Premium": planMembresia = modelFactory.crearMembresiaPremium(); break;
            case "Membresía VIP": planMembresia = modelFactory.crearMembresiaVip(); break;
            default:
                mostrarAlerta("Error", "Tipo de membresía no válido.", Alert.AlertType.ERROR);
                return;
        }

        double costoBase = 0;
        switch (tipoPlanMembresia) {
            case "Membresía Básica": costoBase = 50.0; break;
            case "Membresía Premium": costoBase = 100.0; break;
            case "Membresía VIP": costoBase = 150.0; break;
        }

        double costoFinal = costoBase;
        int mesesDuracion = 0;
        switch (duracionPlan) {
            case MENSUAL: mesesDuracion = 1; break;
            case TRIMESTRAL: mesesDuracion = 3; costoFinal *= 0.95; break;
            case ANUAL: mesesDuracion = 12; costoFinal *= 0.90; break;
        }

        Date fechaInicio = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(fechaInicio);
        cal.add(Calendar.MONTH, mesesDuracion);
        Date fechaVencimiento = cal.getTime();
        
        Suscripcion nuevaSuscripcion = modelFactory.crearSuscripcion(
            planMembresia, 
            costoFinal, 
            fechaInicio, 
            fechaVencimiento, 
            EstadoSuscripcion.ACTIVA, 
            duracionPlan
        );
        usuarioSeleccionado.setSuscripcion(nuevaSuscripcion);

        if (usuarioSeleccionado instanceof Estudiante && "Estudiante".equals(tipoUsuario)) {
            ((Estudiante) usuarioSeleccionado).setDescuento(0.15);
        } else if (usuarioSeleccionado instanceof TrabajadorUQ && "Trabajador UQ".equals(tipoUsuario)) {
            ((TrabajadorUQ) usuarioSeleccionado).setBeneficioEspecial("Acceso a casillero gratuito");
        } 

        actualizarTablaUsuarios();
        actualizarComboBoxReserva();
        limpiarCamposRegistro();
        mostrarAlerta("Éxito", "Usuario modificado correctamente.", Alert.AlertType.INFORMATION);
        modelFactory.enviarNotificacionCorreo(usuarioSeleccionado, "Actualización de Suscripción en Gimnasio UQ Fit", "¡Hola " + usuarioSeleccionado.getNombre() + "! Tu suscripción ha sido actualizada.");
    }

    @FXML
    void eliminarUsuario(ActionEvent event) {
        Usuario usuarioSeleccionado = usuariosTable.getSelectionModel().getSelectedItem();
        if (usuarioSeleccionado == null) {
            mostrarAlerta("Error", "Debe seleccionar un usuario de la tabla para eliminar.", Alert.AlertType.ERROR);
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar Eliminación");
        confirmacion.setHeaderText("¿Está seguro de que desea eliminar a " + usuarioSeleccionado.getNombre() + "?");
        confirmacion.setContentText("Esta acción no se puede deshacer.");
        Optional<javafx.scene.control.ButtonType> result = confirmacion.showAndWait();

        if (result.isPresent() && result.get() == javafx.scene.control.ButtonType.OK) {
            gimnasio.getUsuarios().remove(usuarioSeleccionado);
            actualizarTablaUsuarios();
            actualizarComboBoxReserva();
            limpiarCamposRegistro();
            mostrarAlerta("Éxito", "Usuario eliminado correctamente.", Alert.AlertType.INFORMATION);
            modelFactory.enviarNotificacionCorreo(usuarioSeleccionado, "Baja de Suscripción en Gimnasio UQ Fit", "¡Hola " + usuarioSeleccionado.getNombre() + "! Lamentamos informarte que tu suscripción ha sido dada de baja.");
        }
    }

    @FXML
    void generarReporteUsuariosActivos(ActionEvent event) {
        List<Usuario> usuariosActivos = gimnasio.getUsuarios().stream()
                .filter(u -> u.getSuscripcion() != null && u.getSuscripcion().estaActiva())
                .collect(Collectors.toList());

        StringBuilder reporte = new StringBuilder("Usuarios con suscripción activa:\n\n");
        if (usuariosActivos.isEmpty()) {
            reporte.append("No hay usuarios activos en este momento.");
        } else {
            for (Usuario u : usuariosActivos) {
                reporte.append("- ").append(u.getNombre()).append(" (ID: ").append(u.getIdentificacion())
                       .append(" - Plan: ").append(u.getSuscripcion().getMembresia().getNombre()).append(")\n");
            }
        }
        mostrarAlerta("Reporte de Usuarios Activos", reporte.toString(), Alert.AlertType.INFORMATION);
    }

    @FXML
    void generarReporteVencimientos(ActionEvent event) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 30);
        Date fechaLimite = cal.getTime();

        List<Usuario> usuariosProximosAVencer = gimnasio.getUsuarios().stream()
                .filter(u -> u.getSuscripcion() != null && 
                             u.getSuscripcion().getEstado() == EstadoSuscripcion.ACTIVA &&
                             u.getSuscripcion().getFechaVencimiento() != null &&
                             u.getSuscripcion().getFechaVencimiento().before(fechaLimite))
                .collect(Collectors.toList());

        StringBuilder reporte = new StringBuilder("Usuarios con suscripción próxima a vencer (30 días):\n\n");
        if (usuariosProximosAVencer.isEmpty()) {
            reporte.append("No hay suscripciones próximas a vencer.");
        } else {
            for (Usuario u : usuariosProximosAVencer) {
                reporte.append("- ").append(u.getNombre()).append(" (ID: ").append(u.getIdentificacion())
                       .append(" - Vence: ").append(u.getSuscripcion().getFechaVencimiento()).append(")\n");
                modelFactory.enviarNotificacionCorreo(u, "Recordatorio de Vencimiento de Suscripción", "¡Hola " + u.getNombre() + "! Tu suscripción está próxima a vencer. Por favor, renuévala para continuar disfrutando de nuestros servicios.");
            }
        }
        mostrarAlerta("Reporte de Vencimientos", reporte.toString(), Alert.AlertType.INFORMATION);
    }

    @FXML
    void inscribirUsuarioEnClase(ActionEvent event) {
        Usuario usuario = reservaUsuarioBox.getValue();
        Clase clase = reservaClaseBox.getValue();

        if (usuario == null || clase == null) {
            mostrarAlerta("Error", "Debe seleccionar un usuario y una clase.", Alert.AlertType.ERROR);
            return;
        }
        
        if (usuario.getSuscripcion() == null || !usuario.getSuscripcion().estaActiva()) {
            mostrarAlerta("Error", "El usuario no tiene una suscripción activa.", Alert.AlertType.ERROR);
            return;
        }
        if (!usuario.getSuscripcion().getMembresia().tieneAccesoClasesGrupales()) {
            mostrarAlerta("Error", "El plan de membresía del usuario no incluye acceso a clases grupales.", Alert.AlertType.ERROR);
            return;
        }

        if (clase.inscribirUsuario(usuario)) {
            mostrarAlerta("Éxito", "Usuario " + usuario.getNombre() + " inscrito en " + clase.getNombre() + ".", Alert.AlertType.INFORMATION);
            modelFactory.enviarNotificacionCorreo(usuario, "Confirmación de Inscripción a Clase", "¡Hola " + usuario.getNombre() + "! Has sido inscrito exitosamente en la clase de " + clase.getNombre() + ".");
            reservaClaseBox.getSelectionModel().select(null);
            reservaClaseBox.getSelectionModel().select(clase);
        } else {
            mostrarAlerta("Error", "No hay cupo disponible en la clase " + clase.getNombre() + ".", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void validarAcceso(ActionEvent event) {
        String idAcceso = accesoIdField.getText();
        if (idAcceso.isEmpty()) {
            accesoResultadoLabel.setText("Estado: Ingrese una identificación.");
            return;
        }

        Optional<Usuario> usuarioOpt = gimnasio.getUsuarios().stream()
                                            .filter(u -> u.getIdentificacion().equals(idAcceso))
                                            .findFirst();

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            if (usuario.getSuscripcion() != null && usuario.getSuscripcion().estaActiva()) {
                accesoResultadoLabel.setText("Estado: Acceso Permitido para " + usuario.getNombre() + " (Plan: " + usuario.getSuscripcion().getMembresia().getNombre() + ")");
                accesoResultadoLabel.setStyle("-fx-text-fill: green;");
            } else {
                accesoResultadoLabel.setText("Estado: Acceso Denegado para " + usuario.getNombre() + " (Suscripción inactiva o vencida)");
                accesoResultadoLabel.setStyle("-fx-text-fill: red;");
            }
        } else {
            accesoResultadoLabel.setText("Estado: Usuario no encontrado.");
            accesoResultadoLabel.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    void cerrarSesion(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginView.fxml"));
            Parent root = loader.load();
            
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Gimnasio UQ Fit - Login");
            stage.show();

            Stage currentStage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo cargar la pantalla de inicio de sesión.", Alert.AlertType.ERROR);
        }
    }

    private void actualizarTablaUsuarios() {
        usuariosTable.setItems(FXCollections.observableArrayList(gimnasio.getUsuarios()));
    }
    
    private void actualizarComboBoxReserva() {
        reservaUsuarioBox.setItems(FXCollections.observableArrayList(gimnasio.getUsuarios()));
        reservaClaseBox.setItems(FXCollections.observableArrayList(gimnasio.getClases()));
    }

    private void limpiarCamposRegistro() {
        nombreField.clear();
        idField.clear();
        edadField.clear();
        telefonoField.clear();
        membresiaBox.setValue(null);
        duracionPlanBox.setValue(null);
        tipoUsuarioBox.setValue(null);
        rutaImagenField.clear();
        descuentoLabel.setVisible(false);
        beneficioEspecialLabel.setVisible(false);
        idField.setEditable(true);
        usuariosTable.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private StringConverter<Usuario> createUsuarioConverter() {
        return new StringConverter<>() {
            @Override
            public String toString(Usuario usuario) {
                return usuario == null ? null : usuario.getNombre() + " (" + usuario.getIdentificacion() + ")";
            }
            @Override
            public Usuario fromString(String string) { return null; }
        };
    }

    private StringConverter<Clase> createClaseConverter() {
        return new StringConverter<>() {
            @Override
            public String toString(Clase clase) {
                return clase == null ? null : clase.getNombre();
            }
            @Override
            public Clase fromString(String string) { return null; }
        };
    }
}
