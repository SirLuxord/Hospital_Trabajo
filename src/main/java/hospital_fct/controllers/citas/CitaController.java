package hospital_fct.controllers.citas;

import hospital_fct.CreateDataBase;
import hospital_fct.QR_Email.EmailSender;
import hospital_fct.QR_Email.QRGenerator;
import hospital_fct.controllers.SearchFechaDialog;
import hospital_fct.models.Cita;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.Normalizer;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static hospital_fct.controllers.operaciones.OperacionController.obtenerEmailPaciente;


public class CitaController implements Initializable {

    // Model

    private ListProperty<Cita> listaCita = new SimpleListProperty<>(FXCollections.observableArrayList());
    private ObjectProperty<Cita> selectedCita = new SimpleObjectProperty<>();
    private ModifiedCitaController modifiedCitaController = new ModifiedCitaController(this);
    private boolean modificar = false;

    // View

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Bindings

        selectedCita.bind(citaTable.getSelectionModel().selectedItemProperty());

        // Cell bindings

        idCitaColumn.setCellValueFactory(v -> v.getValue().idCitaProperty());
        idDoctorColumn.setCellValueFactory(v -> v.getValue().idDoctorProperty());
        idPacienteColumn.setCellValueFactory(v -> v.getValue().idPacienteProperty());
        asuntoColumn.setCellValueFactory(v -> v.getValue().asuntoProperty());
        doctorCitaColumn.setCellValueFactory(v -> v.getValue().nombreDoctorProperty());
        estadoColumn.setCellValueFactory(v -> v.getValue().estadoProperty());
        fechaCitaColumn.setCellValueFactory(v -> v.getValue().fechaCitaProperty());
        horaCitaColumn.setCellValueFactory(v -> v.getValue().horaProperty());
        pacienteCitaColumn.setCellValueFactory(v -> v.getValue().nombrePacienteProperty());

        // Table binding

        citaTable.itemsProperty().bind(listaCita);

        // Listener de selectedCita

        selectedCita.addListener((o, ov, nv) -> {
            if (nv != null) {
                modifiedCitaController.getCitaModify().setFechaCita(nv.getFechaCita());
                modifiedCitaController.getCitaModify().setAsunto(nv.getAsunto());
                modifiedCitaController.getCitaModify().setEstado(nv.getEstado());
                modifiedCitaController.getCitaModify().setHora(nv.getHora());
                modifiedCitaController.getCitaModify().setNombreDoctor(nv.getNombreDoctor());
                modifiedCitaController.getCitaModify().setNombrePaciente(nv.getNombrePaciente());
                modifiedCitaController.getCitaModify().setIdCita(nv.getIdCita());
                modifiedCitaController.getCitaModify().setIdDoctor(nv.getIdDoctor());
                modifiedCitaController.getCitaModify().setIdPaciente(nv.getIdPaciente());

                modifiedCitaController.getEstadoComboBox()
                        .getItems()
                        .stream()
                        .filter(estado -> estado.toString().equals(nv.getEstado()))
                        .findFirst()
                        .ifPresent(estado ->
                                modifiedCitaController.getEstadoComboBox().setValue(estado)
                        );
            }
        });

        // Buttons listener

        selectedCita.addListener((o , ov ,nv) -> {
            if (!modificar) {
                modifyButton.setDisable(nv == null);
                deleteButton.setDisable(nv == null);
                emailButton.setDisable(nv == null);
            }
        });

        modifyButton.setDisable(true);
        deleteButton.setDisable(true);
        emailButton.setDisable(true);

        citaTable.getColumns().remove(idCitaColumn);
        citaTable.getColumns().remove(idDoctorColumn);
        citaTable.getColumns().remove(idPacienteColumn);

        SplitPane.setResizableWithParent(modifiedCitaController.getRoot() , false);
    }

    public CitaController() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/citas/citaView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    private TableColumn<Cita, String> asuntoColumn;

    @FXML
    private TableView<Cita> citaTable;

    @FXML
    private Button createButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button emailButton;

    @FXML
    private TableColumn<Cita, String> doctorCitaColumn;

    @FXML
    private TableColumn<Cita, String> estadoColumn;

    @FXML
    private TableColumn<Cita, LocalDate> fechaCitaColumn;

    @FXML
    private TableColumn<Cita, String> horaCitaColumn;

    @FXML
    private TableColumn<Cita, Number> idCitaColumn;

    @FXML
    private TableColumn<Cita, Number> idDoctorColumn;

    @FXML
    private TableColumn<Cita, Number> idPacienteColumn;

    @FXML
    private Button modifyButton;

    @FXML
    private TableColumn<Cita, String> pacienteCitaColumn;

    @FXML
    private BorderPane root;

    @FXML
    private SplitPane splitModificar;

    @FXML
    void onCreateCitaAction(ActionEvent event) {
        CreateCitaDialog createDialog = new CreateCitaDialog();
        Optional<Cita> result = createDialog.showAndWait();
        result.ifPresent(this::insertarCita);
    }

    @FXML
    void onDeleteCitaAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Eliminar Cita");
        alert.setHeaderText("¿Está seguro de que desea eliminar la cita?");
        alert.setContentText("Esta acción no se puede deshacer.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Cita cita = selectedCita.get();
            String deleteQuery = "DELETE FROM Citas WHERE IdCita = ?";
            try (Connection connection = DriverManager.getConnection(CreateDataBase.url);
                 PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
                preparedStatement.setInt(1, cita.getIdCita());
                preparedStatement.executeUpdate();
                buscarCita("", "", "");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void onModifiedCitaAction(ActionEvent event) {
        modificar = true;
        createButton.setDisable(true);
        emailButton.setDisable(true);
        modifyButton.setDisable(true);
        deleteButton.setDisable(true);

        splitModificar.getItems().add(modifiedCitaController.getRoot());
    }

    @FXML
    void onSearchAllCitasAction(ActionEvent event) {
        buscarCita("","", "");
    }

    @FXML
    void onSearchCitaAction(ActionEvent event) {
        SearchCitaDialog searchDialog = new SearchCitaDialog();
        Optional<String> campo = searchDialog.showAndWait();
        if (!campo.get().equals("") && !campo.get().equals("Fecha")) {
            TextInputDialog nameDialog = new TextInputDialog();
            nameDialog.setHeaderText("Introduzca el " + campo.get());
            nameDialog.setContentText(campo.get() + ": ");
            Optional<String> result = nameDialog.showAndWait();
            result.ifPresent(value -> buscarCita(campo.get(), "%" + value + "%", ""));
        } else if (campo.get().equals("Fecha")) {
            SearchFechaDialog fechaDialog = new SearchFechaDialog();
            Optional<List<LocalDate>> fechas = fechaDialog.showAndWait();

            fechas.ifPresent(rango -> {
                LocalDate inicio = rango.get(0);
                LocalDate fin = rango.get(1);
                buscarCita("Fecha", inicio.toString(), fin.toString());
            });
        }
    }

    public BorderPane getRoot() {
        return root;
    }

    public SplitPane getSplitModificar() {
        return splitModificar;
    }

    public Button getCreateButton() {
        return createButton;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }


    public Button getModifyButton() {
        return modifyButton;
    }

    public void setModificar(boolean modificar) {
        this.modificar = modificar;
    }

    // Métodos

    public void buscarCita(String opcion, String parametro, String parametro2) {
        listaCita.clear();
        String query = getString(opcion);

        try (Connection connection = DriverManager.getConnection(CreateDataBase.url);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            if (opcion.equals("Fecha")) {
                // Si estás utilizando fechas en formato de cadena (YYYY-MM-DD)
                preparedStatement.setString(1, parametro); // Primer parámetro (fecha de inicio)
                preparedStatement.setString(2, parametro2); // Segundo parámetro (fecha de fin)
            }  else if (opcion != null && !opcion.isEmpty() && parametro != null && !parametro.isEmpty()) {
                preparedStatement.setString(1, parametro);
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Cita cita = new Cita();
                cita.setIdCita(resultSet.getInt("IdCita"));
                cita.setIdDoctor(resultSet.getInt("IdDoctor"));
                cita.setIdPaciente(resultSet.getInt("IdPaciente"));
                cita.setAsunto(resultSet.getString("Asunto"));
                cita.setNombreDoctor(resultSet.getString("NombreDoctor"));
                cita.setNombrePaciente(resultSet.getString("NombrePaciente"));
                cita.setEstado(resultSet.getString("Estado"));
                String fechaString = resultSet.getString("Fecha");
                cita.setFechaCita(LocalDate.parse(fechaString));
                cita.setHora(resultSet.getString("Hora"));
                listaCita.add(cita);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onEmailAction(ActionEvent event) {
        try {
            // Crear el contenido del QR
            String contenidoQR = "Asunto: " + selectedCita.get().getAsunto() + "\n" +
                    "Fecha: " + selectedCita.get().getFechaCita() + "\n" +
                    "Hora: " + selectedCita.get().getHora() + "\n";

            // Normalizar el contenido (elimina acentos y caracteres especiales si es necesario)
            String contenidoNormalizado = Normalizer.normalize(contenidoQR, Normalizer.Form.NFD);
            contenidoNormalizado = contenidoNormalizado.replaceAll("[^\\p{ASCII}]", "");

            // Generar el código QR
            byte[] qrBytes = QRGenerator.generarQR(contenidoNormalizado, 250, 250);

            // Enviar el correo (puedes modificar el destinatario si lo tienes asociado al paciente)
            EmailSender.enviarCorreoConQR(
                    obtenerEmailPaciente(selectedCita.get().getIdPaciente()),
                    "Detalles de tu operación",
                    "Adjunto encontrarás tu código QR con los detalles de la operación.",
                    qrBytes
            );

            System.out.println("Correo enviado correctamente con el QR.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getString(String opcion) {
        String condicion = switch (opcion) {
            case "Asunto" -> "WHERE Asunto LIKE ?";
            case "Doctor" -> "WHERE (Doctores.Nombre || ' ' || Doctores.Apellido) LIKE ?";
            case "Paciente" -> "WHERE (Pacientes.Nombre || ' ' || Pacientes.Apellido) LIKE ?";
            case "Hora" -> "WHERE Hora LIKE ?";
            case "Fecha" -> "WHERE Fecha BETWEEN ? AND ?";
            case "Estado" -> "WHERE Estado LIKE ?";
            default -> "WHERE 1 = 1";
        };
        String query = "SELECT Citas.IdCita, Citas.IdPaciente AS IdPaciente, Citas.IdDoctor AS IdDoctor, Citas.Asunto, Citas.Estado, Citas.Fecha, Citas.Hora, " +
                "Doctores.Nombre || ' ' || Doctores.Apellido AS NombreDoctor, " +
                "Pacientes.Nombre || ' ' || Pacientes.Apellido AS NombrePaciente " +
                "FROM Citas " +
                "JOIN Doctores ON Citas.IdDoctor = Doctores.IdDoctor " +
                "JOIN Pacientes ON Citas.IdPaciente = Pacientes.IdPaciente " +
                condicion;

        return query;
    }

    public void insertarCita(Cita cita) {
        String insertQuery = "INSERT INTO Citas (IdDoctor, IdPaciente, Asunto, Estado, Fecha, Hora) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(CreateDataBase.url);
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

            preparedStatement.setInt(1, cita.getIdDoctor());
            preparedStatement.setInt(2, cita.getIdPaciente());
            preparedStatement.setString(3, cita.getAsunto());
            preparedStatement.setString(4, cita.getEstado());
            //String fechaString = cita.getFechaCita().toString();
            preparedStatement.setString(5, cita.getFechaCita().toString());
            preparedStatement.setString(6, cita.getHora());

            preparedStatement.executeUpdate();

            // Opcional: volver a cargar la lista para actualizar la tabla
            buscarCita("", "", "");

        } catch (SQLException e) {
            e.printStackTrace(); // o muestra una alerta
        }
    }
}
