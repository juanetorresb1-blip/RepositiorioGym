package ProyectoFinal_Gimnasio.ViewController;

import ProyectoFinal_Gimnasio.Factory.ModelFactory;
import ProyectoFinal_Gimnasio.Model.Clase;
import ProyectoFinal_Gimnasio.Model.Entrenador;
import ProyectoFinal_Gimnasio.Model.Gimnasio;
import ProyectoFinal_Gimnasio.Model.TipoClase;
import ProyectoFinal_Gimnasio.Model.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea; // Importar TextArea
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List; // Asegurarse de que esta importación esté presente
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class AdminViewController {

    // --- Pestaña Entrenadores ---
    @FXML private TextField nombreField;
    @FXML private TextField idField;
    @FXML private TextField telefonoField;
    @FXML private TextField salarioField;
    @FXML private TextField especialidadField;
    @FXML private TableView<Entrenador> entrenadoresTable;
    @FXML private TableColumn<Entrenador, String> nombreColumn;
    @FXML private TableColumn<Entrenador, String> idColumn;
    @FXML private TableColumn<Entrenador, String> telefonoColumn;
    @FXML private TableColumn<Entrenador, Double> salarioColumn;
    @FXML private TableColumn<Entrenador, String> especialidadColumn;

    // --- Pestaña Clases ---
    @FXML private TextField claseNombreField;
    @FXML private ComboBox<TipoClase> claseTipoBox;
    @FXML private TextField claseHorarioField;
    @FXML private TextField claseCupoField;
    @FXML private ComboBox<Entrenador> claseEntrenadorBox;
    @FXML private TableView<Clase> clasesTable;
    @FXML private TableColumn<Clase, String> claseNombreColumn;
    @FXML private TableColumn<Clase, TipoClase> claseTipoColumn;
    @FXML private TableColumn<Clase, LocalTime> claseHorarioColumn;
    @FXML private TableColumn<Clase, Integer> claseCupoColumn;
    @FXML private TableColumn<Clase, String> claseEntrenadorColumn;

    // --- Pestaña Reportes Avanzados ---
    @FXML private TextArea reporteTextArea;

    private Gimnasio gimnasio = ModelFactory.getInstance().getGimnasio();
    private ModelFactory modelFactory = ModelFactory.getInstance();
    private ObservableList<Entrenador> listaEntrenadores;
    private ObservableList<Clase> listaClases;

    @FXML
    public void initialize() {
        // --- Inicialización Pestaña Entrenadores ---
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("identificacion"));
        telefonoColumn.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        salarioColumn.setCellValueFactory(new PropertyValueFactory<>("salario"));
        especialidadColumn.setCellValueFactory(new PropertyValueFactory<>("especialidad"));
        actualizarTablaEntrenadores();

        entrenadoresTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                nombreField.setText(newSelection.getNombre());
                idField.setText(newSelection.getIdentificacion());
                telefonoField.setText(newSelection.getTelefono());
                salarioField.setText(String.valueOf(newSelection.getSalario()));
                especialidadField.setText(newSelection.getEspecialidad());
                idField.setEditable(false);
            } else {
                limpiarCamposEntrenador();
                idField.setEditable(true);
            }
        });

        // --- Inicialización Pestaña Clases ---
        claseTipoBox.getItems().setAll(TipoClase.values());
        claseEntrenadorBox.setItems(FXCollections.observableArrayList(gimnasio.getEntrenadores()));
        claseEntrenadorBox.setConverter(new StringConverter<Entrenador>() {
            @Override
            public String toString(Entrenador entrenador) {
                return entrenador == null ? "" : entrenador.getNombre();
            }
            @Override
            public Entrenador fromString(String string) {
                return null;
            }
        });

        claseNombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        claseTipoColumn.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        claseHorarioColumn.setCellValueFactory(new PropertyValueFactory<>("horario"));
        claseCupoColumn.setCellValueFactory(new PropertyValueFactory<>("cupoMaximo"));
        claseEntrenadorColumn.setCellValueFactory(cellData -> {
            Entrenador e = cellData.getValue().getEntrenador();
            return new javafx.beans.property.SimpleStringProperty(e != null ? e.getNombre() : "Sin asignar");
        });
        actualizarTablaClases();

        clasesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                claseNombreField.setText(newSelection.getNombre());
                claseTipoBox.setValue(newSelection.getTipo());
                claseHorarioField.setText(newSelection.getHorario().toString());
                claseCupoField.setText(String.valueOf(newSelection.getCupoMaximo()));
                claseEntrenadorBox.setValue(newSelection.getEntrenador());
                claseNombreField.setEditable(false);
            } else {
                limpiarCamposClase();
                claseNombreField.setEditable(true);
            }
        });
    }

    @FXML
    void registrarEntrenador(ActionEvent event) {
        String nombre = nombreField.getText();
        String id = idField.getText();
        String telefono = telefonoField.getText();
        String salarioStr = salarioField.getText();
        String especialidad = especialidadField.getText();

        if (nombre.isEmpty() || id.isEmpty() || telefono.isEmpty() || salarioStr.isEmpty() || especialidad.isEmpty()) {
            mostrarAlerta("Error", "Todos los campos son obligatorios.", Alert.AlertType.ERROR);
            return;
        }

        double salario;
        try {
            salario = Double.parseDouble(salarioStr);
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El salario debe ser un número válido.", Alert.AlertType.ERROR);
            return;
        }

        boolean idExiste = gimnasio.getEntrenadores().stream().anyMatch(e -> e.getIdentificacion().equals(id));
        if (idExiste) {
            mostrarAlerta("Error", "Ya existe un entrenador con esa identificación. Use 'Modificar' si desea actualizarlo.", Alert.AlertType.ERROR);
            return;
        }

        Entrenador nuevoEntrenador = modelFactory.crearEntrenador(nombre, id, telefono, salario, "Entrenador", especialidad);
        gimnasio.getEntrenadores().add(nuevoEntrenador);

        actualizarTablaEntrenadores();
        claseEntrenadorBox.setItems(FXCollections.observableArrayList(gimnasio.getEntrenadores()));
        limpiarCamposEntrenador();
        mostrarAlerta("Éxito", "Entrenador registrado correctamente.", Alert.AlertType.INFORMATION);
    }

    @FXML
    void modificarEntrenador(ActionEvent event) {
        Entrenador entrenadorSeleccionado = entrenadoresTable.getSelectionModel().getSelectedItem();
        if (entrenadorSeleccionado == null) {
            mostrarAlerta("Error", "Debe seleccionar un entrenador de la tabla para modificar.", Alert.AlertType.ERROR);
            return;
        }

        String nombre = nombreField.getText();
        String id = idField.getText();
        String telefono = telefonoField.getText();
        String salarioStr = salarioField.getText();
        String especialidad = especialidadField.getText();

        if (nombre.isEmpty() || id.isEmpty() || telefono.isEmpty() || salarioStr.isEmpty() || especialidad.isEmpty()) {
            mostrarAlerta("Error", "Todos los campos son obligatorios.", Alert.AlertType.ERROR);
            return;
        }

        double salario;
        try {
            salario = Double.parseDouble(salarioStr);
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El salario debe ser un número válido.", Alert.AlertType.ERROR);
            return;
        }

        entrenadorSeleccionado.setNombre(nombre);
        entrenadorSeleccionado.setTelefono(telefono);
        entrenadorSeleccionado.setSalario(salario);
        entrenadorSeleccionado.setEspecialidad(especialidad);

        actualizarTablaEntrenadores();
        claseEntrenadorBox.setItems(FXCollections.observableArrayList(gimnasio.getEntrenadores()));
        limpiarCamposEntrenador();
        mostrarAlerta("Éxito", "Entrenador modificado correctamente.", Alert.AlertType.INFORMATION);
    }

    @FXML
    void eliminarEntrenador(ActionEvent event) {
        Entrenador entrenadorSeleccionado = entrenadoresTable.getSelectionModel().getSelectedItem();
        if (entrenadorSeleccionado == null) {
            mostrarAlerta("Error", "Debe seleccionar un entrenador de la tabla para eliminar.", Alert.AlertType.ERROR);
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar Eliminación");
        confirmacion.setHeaderText("¿Está seguro de que desea eliminar a " + entrenadorSeleccionado.getNombre() + "?");
        confirmacion.setContentText("Esta acción no se puede deshacer.");
        Optional<javafx.scene.control.ButtonType> result = confirmacion.showAndWait();

        if (result.isPresent() && result.get() == javafx.scene.control.ButtonType.OK) {
            gimnasio.getEntrenadores().remove(entrenadorSeleccionado);
            actualizarTablaEntrenadores();
            claseEntrenadorBox.setItems(FXCollections.observableArrayList(gimnasio.getEntrenadores()));
            limpiarCamposEntrenador();
            mostrarAlerta("Éxito", "Entrenador eliminado correctamente.", Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    void registrarClase(ActionEvent event) {
        String nombre = claseNombreField.getText();
        TipoClase tipo = claseTipoBox.getValue();
        String horarioStr = claseHorarioField.getText();
        String cupoStr = claseCupoField.getText();
        Entrenador entrenador = claseEntrenadorBox.getValue();

        if (nombre.isEmpty() || tipo == null || horarioStr.isEmpty() || cupoStr.isEmpty() || entrenador == null) {
            mostrarAlerta("Error", "Todos los campos son obligatorios.", Alert.AlertType.ERROR);
            return;
        }

        LocalTime horario;
        try {
            horario = LocalTime.parse(horarioStr);
        } catch (DateTimeParseException e) {
            mostrarAlerta("Error", "El formato del horario debe ser HH:mm (ej. 14:30).", Alert.AlertType.ERROR);
            return;
        }

        int cupo;
        try {
            cupo = Integer.parseInt(cupoStr);
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El cupo debe ser un número válido.", Alert.AlertType.ERROR);
            return;
        }

        boolean nombreClaseExiste = gimnasio.getClases().stream().anyMatch(c -> c.getNombre().equals(nombre));
        if (nombreClaseExiste) {
            mostrarAlerta("Error", "Ya existe una clase con ese nombre. Use 'Modificar' si desea actualizarla.", Alert.AlertType.ERROR);
            return;
        }

        Clase nuevaClase = modelFactory.crearClase(nombre, tipo, horario, cupo, entrenador);
        gimnasio.getClases().add(nuevaClase);

        actualizarTablaClases();
        limpiarCamposClase();
        mostrarAlerta("Éxito", "Clase registrada correctamente.", Alert.AlertType.INFORMATION);
    }

    @FXML
    void modificarClase(ActionEvent event) {
        Clase claseSeleccionada = clasesTable.getSelectionModel().getSelectedItem();
        if (claseSeleccionada == null) {
            mostrarAlerta("Error", "Debe seleccionar una clase de la tabla para modificar.", Alert.AlertType.ERROR);
            return;
        }

        String nombre = claseNombreField.getText();
        TipoClase tipo = claseTipoBox.getValue();
        String horarioStr = claseHorarioField.getText();
        String cupoStr = claseCupoField.getText();
        Entrenador entrenador = claseEntrenadorBox.getValue();

        if (nombre.isEmpty() || tipo == null || horarioStr.isEmpty() || cupoStr.isEmpty() || entrenador == null) {
            mostrarAlerta("Error", "Todos los campos son obligatorios.", Alert.AlertType.ERROR);
            return;
        }

        LocalTime horario;
        try {
            horario = LocalTime.parse(horarioStr);
        } catch (DateTimeParseException e) {
            mostrarAlerta("Error", "El formato del horario debe ser HH:mm (ej. 14:30).", Alert.AlertType.ERROR);
            return;
        }

        int cupo;
        try {
            cupo = Integer.parseInt(cupoStr);
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El cupo debe ser un número válido.", Alert.AlertType.ERROR);
            return;
        }

        claseSeleccionada.setTipo(tipo);
        claseSeleccionada.setHorario(horario);
        claseSeleccionada.setCupoMaximo(cupo);
        claseSeleccionada.setEntrenador(entrenador);

        actualizarTablaClases();
        limpiarCamposClase();
        mostrarAlerta("Éxito", "Clase modificada correctamente.", Alert.AlertType.INFORMATION);
    }

    @FXML
    void eliminarClase(ActionEvent event) {
        Clase claseSeleccionada = clasesTable.getSelectionModel().getSelectedItem();
        if (claseSeleccionada == null) {
            mostrarAlerta("Error", "Debe seleccionar una clase de la tabla para eliminar.", Alert.AlertType.ERROR);
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar Eliminación");
        confirmacion.setHeaderText("¿Está seguro de que desea eliminar la clase " + claseSeleccionada.getNombre() + "?");
        confirmacion.setContentText("Esta acción no se puede deshacer.");
        Optional<javafx.scene.control.ButtonType> result = confirmacion.showAndWait();

        if (result.isPresent() && result.get() == javafx.scene.control.ButtonType.OK) {
            gimnasio.getClases().remove(claseSeleccionada);
            actualizarTablaClases();
            limpiarCamposClase();
            mostrarAlerta("Éxito", "Clase eliminada correctamente.", Alert.AlertType.INFORMATION);
        }
    }

    // --- Métodos de Reportes Avanzados ---
    @FXML
    void generarReporteAsistencia(ActionEvent event) {
        StringBuilder reporte = new StringBuilder("--- Reporte de Asistencia a Clases ---\n\n");
        if (gimnasio.getClases().isEmpty()) {
            reporte.append("No hay clases registradas.");
        } else {
            for (Clase clase : gimnasio.getClases()) {
                reporte.append("Clase: ").append(clase.getNombre()).append(" (").append(clase.getTipo()).append(")\n");
                reporte.append("  Entrenador: ").append(clase.getEntrenador().getNombre()).append("\n");
                reporte.append("  Horario: ").append(clase.getHorario()).append("\n");
                reporte.append("  Cupo: ").append(clase.getUsuariosInscritos().size()).append(" / ").append(clase.getCupoMaximo()).append("\n");
                if (!clase.getUsuariosInscritos().isEmpty()) {
                    reporte.append("  Inscritos:\n");
                    clase.getUsuariosInscritos().forEach(u -> reporte.append("    - ").append(u.getNombre()).append(" (ID: ").append(u.getIdentificacion()).append(")\n"));
                } else {
                    reporte.append("  No hay usuarios inscritos.\n");
                }
                reporte.append("\n");
            }
        }
        reporteTextArea.setText(reporte.toString());
    }

    @FXML
    void generarReporteIngresos(ActionEvent event) {
        double ingresosTotales = gimnasio.getUsuarios().stream()
                .filter(u -> u.getSuscripcion() != null)
                .mapToDouble(u -> u.getSuscripcion().getCosto())
                .sum();

        StringBuilder reporte = new StringBuilder("--- Reporte de Ingresos por Membresías ---\n\n");
        reporte.append(String.format("Ingresos totales por suscripciones: $%.2f\n\n", ingresosTotales));
        
        // Desglose por tipo de membresía
        Map<String, Double> ingresosPorTipo = gimnasio.getUsuarios().stream()
                .filter(u -> u.getSuscripcion() != null)
                .collect(Collectors.groupingBy(u -> u.getSuscripcion().getMembresia().getNombre(),
                                               Collectors.summingDouble(u -> u.getSuscripcion().getCosto())));
        
        reporte.append("Ingresos por tipo de membresía:\n");
        ingresosPorTipo.forEach((tipo, ingreso) -> reporte.append(String.format("  - %s: $%.2f\n", tipo, ingreso)));

        reporteTextArea.setText(reporte.toString());
    }

    @FXML
    void generarReporteClasesPopulares(ActionEvent event) {
        StringBuilder reporte = new StringBuilder("--- Reporte de Clases Más Populares ---\n\n");

        if (gimnasio.getClases().isEmpty()) {
            reporte.append("No hay clases registradas.");
        } else {
            // Ordenar clases por número de inscritos (descendente)
            List<Clase> clasesOrdenadas = gimnasio.getClases().stream()
                    .sorted(Comparator.comparingInt((Clase c) -> c.getUsuariosInscritos().size()).reversed())
                    .collect(Collectors.toList());

            reporte.append("Clases por popularidad (más inscritos primero):\n");
            clasesOrdenadas.forEach(clase -> 
                reporte.append(String.format("  - %s (%s) - Inscritos: %d/%d\n", 
                    clase.getNombre(), 
                    clase.getTipo(), 
                    clase.getUsuariosInscritos().size(), 
                    clase.getCupoMaximo()))
            );
        }
        reporteTextArea.setText(reporte.toString());
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

    private void actualizarTablaEntrenadores() {
        listaEntrenadores = FXCollections.observableArrayList(gimnasio.getEntrenadores());
        entrenadoresTable.setItems(listaEntrenadores);
    }

    private void limpiarCamposEntrenador() {
        nombreField.clear();
        idField.clear();
        telefonoField.clear();
        salarioField.clear();
        especialidadField.clear();
        idField.setEditable(true);
        entrenadoresTable.getSelectionModel().clearSelection();
    }

    private void actualizarTablaClases() {
        listaClases = FXCollections.observableArrayList(gimnasio.getClases());
        clasesTable.setItems(listaClases);
    }

    private void limpiarCamposClase() {
        claseNombreField.clear();
        claseTipoBox.setValue(null);
        claseHorarioField.clear();
        claseCupoField.clear();
        claseEntrenadorBox.setValue(null);
        claseNombreField.setEditable(true);
        clasesTable.getSelectionModel().clearSelection();
    }
    
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
