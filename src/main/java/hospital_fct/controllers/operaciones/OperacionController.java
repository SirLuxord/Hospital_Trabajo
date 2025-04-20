package hospital_fct.controllers.operaciones;

import hospital_fct.CreateDataBase;
import hospital_fct.QR_Email.EmailSender;
import hospital_fct.QR_Email.QRGenerator;
import hospital_fct.controllers.SearchFechaDialog;
import hospital_fct.controllers.citas.SearchCitaDialog;
import hospital_fct.controllers.doctores.CreateDoctorDialog;
import hospital_fct.controllers.doctores.SearchDoctorDialog;
import hospital_fct.controllers.operaciones.doctoresOpe.TablaDoctoresController;
import hospital_fct.models.Doctor;
import hospital_fct.models.Operacion;
import hospital_fct.models.Sectores;
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

public class OperacionController implements Initializable {

    // Model

    private ListProperty<Operacion> listaOperacion = new SimpleListProperty<>(FXCollections.observableArrayList());
    private ObjectProperty<Operacion> selectedOperacion = new SimpleObjectProperty<>();
    private ModifiedOperacionController modifiedOperacionController = new ModifiedOperacionController(this);
    private TablaDoctoresController tablaDoctoresController = new TablaDoctoresController(this);
    private boolean modificar = false;
    private boolean tablaDoctores = false;

    // View

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Bindings

        selectedOperacion.bind(operacionTable.getSelectionModel().selectedItemProperty());

        // Cell bindings

        idOperacionColumn.setCellValueFactory(v -> v.getValue().idOperacionProperty());
        idPacienteColumn.setCellValueFactory(v -> v.getValue().idPacienteProperty());
        idSectorColumn.setCellValueFactory(v -> v.getValue().idSectorProperty());
        operacionColumn.setCellValueFactory(v -> v.getValue().operacionProperty());
        fechaOperacionColumn.setCellValueFactory(v -> v.getValue().fechaProperty());
        horaOperacionColumn.setCellValueFactory(v -> v.getValue().horaProperty());
        //doctorOperacionColumn.setCellValueFactory(v -> v.getValue().doctoresProperty());
        pacienteOperacionColumn.setCellValueFactory(v -> v.getValue().nombrePacienteProperty());
        sectorOperacionColumn.setCellValueFactory(v -> v.getValue().nombreSectorProperty());

        // Table binding

        operacionTable.itemsProperty().bind(listaOperacion);

        // Listener de selectedCita

        selectedOperacion.addListener((o, ov, nv) -> {
            if (nv != null) {
                modifiedOperacionController.getOperacionModify().setFecha(nv.getFecha());
                modifiedOperacionController.getOperacionModify().setHora(nv.getHora());
                modifiedOperacionController.getOperacionModify().setIdPaciente(nv.getIdPaciente());
                modifiedOperacionController.getOperacionModify().setIdSector(nv.getIdSector());
                modifiedOperacionController.getOperacionModify().setIdOperacion(nv.getIdOperacion());
                modifiedOperacionController.getOperacionModify().setOperacion(nv.getOperacion());
                modifiedOperacionController.getOperacionModify().setNombrePaciente(nv.getNombrePaciente());
                modifiedOperacionController.getOperacionModify().setDoctores(nv.getDoctores());
                modifiedOperacionController.getSectorComboBox().getSelectionModel().select(Sectores.valueOf(nv.getNombreSector().replace(" ", "_")));

                tablaDoctoresController.getOperacionAsistente().setIdOperacion(nv.getIdOperacion());
                tablaDoctoresController.buscarDoctor();
                if (!tablaDoctores){
                    splitModificar.getItems().add(tablaDoctoresController.getRoot());
                    tablaDoctores = true;
                }
            }
        });

        // Buttons listener

        selectedOperacion.addListener((o , ov ,nv) -> {
            if (!modificar) {
                modifyButton.setDisable(nv == null);
                deleteButton.setDisable(nv == null);
                emailButton.setDisable(nv == null);
            }
        });

        modifyButton.setDisable(true);
        deleteButton.setDisable(true);
        emailButton.setDisable(true);

        operacionTable.getColumns().remove(idPacienteColumn);
        operacionTable.getColumns().remove(idOperacionColumn);
        operacionTable.getColumns().remove(idSectorColumn);

        SplitPane.setResizableWithParent(modifiedOperacionController.getRoot() , false);
    }

    public OperacionController() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/operaciones/operacionView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private Button createButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button emailButton;

    @FXML
    private TableColumn<Operacion, String> doctorOperacionColumn;

    @FXML
    private TableColumn<Operacion, LocalDate> fechaOperacionColumn;

    @FXML
    private TableColumn<Operacion, String> horaOperacionColumn;

    @FXML
    private TableColumn<Operacion, Number> idOperacionColumn;

    @FXML
    private TableColumn<Operacion, Number> idPacienteColumn;

    @FXML
    private TableColumn<Operacion, Number> idSectorColumn;

    @FXML
    private TableColumn<Operacion, String> sectorOperacionColumn;

    @FXML
    private Button modifyButton;

    @FXML
    private TableColumn<Operacion, String> operacionColumn;

    @FXML
    private TableView<Operacion> operacionTable;

    @FXML
    private TableColumn<Operacion, String> pacienteOperacionColumn;

    @FXML
    private BorderPane root;

    @FXML
    private SplitPane splitModificar;

    @FXML
    void onCreateOperacionAction(ActionEvent event) {
        CreateOperacionDialog createDialog = new CreateOperacionDialog();
        Optional<Operacion> result = createDialog.showAndWait();
        result.ifPresent(operacion -> {
            insertOperacion(operacion);
        });
    }

    @FXML
    void onDeleteOperacionAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Eliminar Operacion");
        alert.setHeaderText("¿Está seguro de que desea eliminar la operacion?");
        alert.setContentText("Esta acción no se puede deshacer.");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String deleteQuery = "DELETE FROM Operaciones WHERE IdOperacion = ?";
                try (Connection connection = DriverManager.getConnection(CreateDataBase.url);
                     PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {

                    preparedStatement.setInt(1, selectedOperacion.get().getIdOperacion());
                    preparedStatement.executeUpdate();
                    buscarOperacion("","", "");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    void onModifiedOperacionAction(ActionEvent event) {
        modificar = true;
        createButton.setDisable(true);
        modifyButton.setDisable(true);
        deleteButton.setDisable(true);
        emailButton.setDisable(true);

        splitModificar.getItems().add(modifiedOperacionController.getRoot());
    }

    @FXML
    void onSearchAllOperacionesAction(ActionEvent event) {
        buscarOperacion("","", "");
    }

    @FXML
    void onSearchOperacionAction(ActionEvent event) {
        SearchOperacionDialog searchDialog = new SearchOperacionDialog();
        Optional<String> campo = searchDialog.showAndWait();
        if (!campo.get().equals("") && !campo.get().equals("Fecha")) {
            TextInputDialog nameDialog = new TextInputDialog();
            nameDialog.setHeaderText("Introduzca el " + campo.get());
            nameDialog.setContentText(campo.get() + ": ");
            Optional<String> result = nameDialog.showAndWait();
            result.ifPresent(value -> buscarOperacion(campo.get(), "%" + value + "%", ""));
        } else if (campo.get().equals("Fecha")) {
            SearchFechaDialog fechaDialog = new SearchFechaDialog();
            Optional<List<LocalDate>> fechas = fechaDialog.showAndWait();

            fechas.ifPresent(rango -> {
                LocalDate inicio = rango.get(0);
                LocalDate fin = rango.get(1);
                buscarOperacion("Fecha", inicio.toString(), fin.toString());
            });
        }
    }

    @FXML
    void onEmailAction(ActionEvent event) {
        try {
            // Crear el contenido del QR
            String contenidoQR = "Operación: " + selectedOperacion.get().getOperacion() + "\n" +
                    "Fecha: " + selectedOperacion.get().getFecha() + "\n" +
                    "Hora: " + selectedOperacion.get().getHora() + "\n" +
                    "Sector: " + selectedOperacion.get().getNombreSector();

            // Normalizar el contenido (elimina acentos y caracteres especiales si es necesario)
            String contenidoNormalizado = Normalizer.normalize(contenidoQR, Normalizer.Form.NFD);
            contenidoNormalizado = contenidoNormalizado.replaceAll("[^\\p{ASCII}]", "");

            // Generar el código QR
            byte[] qrBytes = QRGenerator.generarQR(contenidoNormalizado, 250, 250);

            // Enviar el correo (puedes modificar el destinatario si lo tienes asociado al paciente)
            EmailSender.enviarCorreoConQR(
                    obtenerEmailPaciente(selectedOperacion.get().getIdPaciente()),
                    "Detalles de tu operación",
                    "Adjunto encontrarás tu código QR con los detalles de la operación.",
                    qrBytes
            );

            System.out.println("Correo enviado correctamente con el QR.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BorderPane getRoot() {
        return root;
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

    public SplitPane getSplitModificar() {
        return splitModificar;
    }

    public void setModificar(boolean modificar) {
        this.modificar = modificar;
    }

    public void setTablaDoctores(boolean tablaDoctores) {
        this.tablaDoctores = tablaDoctores;
    }

    public void buscarOperacion(String opcion, String parametro, String parametro2) {
        listaOperacion.clear();
        String query = getString(opcion);

        try (Connection connection = DriverManager.getConnection(CreateDataBase.url);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            if (opcion.equals("Fecha")) {
                preparedStatement.setString(1, parametro); // Primer parámetro (fecha de inicio)
                preparedStatement.setString(2, parametro2); // Segundo parámetro (fecha de fin)
            }  else if (opcion != null && !opcion.isEmpty() && parametro != null && !parametro.isEmpty()) {
                preparedStatement.setString(1, parametro);
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Operacion operacion = new Operacion();
                operacion.setIdPaciente(resultSet.getInt("IdPaciente"));
                operacion.setIdOperacion(resultSet.getInt("IdOperacion"));
                operacion.setIdSector(resultSet.getInt("IdSector"));
                operacion.setOperacion(resultSet.getString("Operacion"));
                String fechaString = resultSet.getString("Fecha");
                //operacion.setDoctores(resultSet.getString("Doctores"));
                operacion.setNombrePaciente(resultSet.getString("Paciente"));
                operacion.setNombreSector(resultSet.getString("NombreSeccion"));
                operacion.setFecha(LocalDate.parse(fechaString));
                operacion.setHora(resultSet.getString("Hora"));
                listaOperacion.add(operacion);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getString(String opcion) {
        String query = "";
        String condicion = switch (opcion) {
            case "Operacion" -> "WHERE Operacion LIKE ?";
            case "Doctor" -> "WHERE (Doctores.Nombre || ' ' || Doctores.Apellido) LIKE ?";
            case "Paciente" -> "WHERE (Pacientes.Nombre || ' ' || Pacientes.Apellido) LIKE ?";
            case "Hora" -> "WHERE Hora LIKE ?";
            case "Fecha" -> "WHERE Fecha BETWEEN ? AND ?";
            default -> "WHERE 1 = 1";
        };
        query = "SELECT Operaciones.*, " +
                "Pacientes.Nombre || ' ' || Pacientes.Apellido AS Paciente, Sectores_Hospital.NombreSeccion, " +
                "Pacientes.IdPaciente AS IdPaciente " +
                //"GROUP_CONCAT(Doctores.Nombre || ' ' || Doctores.Apellido, ', ') AS Doctores " +
                "FROM Operaciones " +
                "JOIN Sectores_Hospital ON Operaciones.IdSector = Sectores_Hospital.IdSector " +
                "JOIN Pacientes ON Operaciones.IdPaciente = Pacientes.IdPaciente " +
//                "JOIN Operaciones_Doctores ON Operaciones.IdOperacion = Operaciones_Doctores.IdOperacion " +
//                "JOIN Doctores ON Operaciones_Doctores.IdDoctor = Doctores.IdDoctor " +
                condicion + " " +
                "GROUP BY Operaciones.IdOperacion";
        return query;
    }

    public void insertOperacion(Operacion operacion) {
        String insertQuery = "INSERT INTO Operaciones (IdPaciente, IdSector, Operacion, Fecha, Hora) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(CreateDataBase.url);
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

            preparedStatement.setInt(1, operacion.getIdPaciente());
            preparedStatement.setInt(2, operacion.getIdSector());
            preparedStatement.setString(3, operacion.getOperacion());
            preparedStatement.setString(4, operacion.getFecha().toString());
            preparedStatement.setString(5, operacion.getHora());

            preparedStatement.executeUpdate();

            buscarOperacion("","", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String obtenerEmailPaciente(int idPaciente) {
        String email = null;

        String sql = "SELECT Email FROM Pacientes WHERE idPaciente = ?";

        try (Connection conn = DriverManager.getConnection(CreateDataBase.url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idPaciente);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                email = rs.getString("Email");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return email;
    }
}
