package hospital_fct.controllers.doctores;

import hospital_fct.CreateDataBase;
import hospital_fct.controllers.citas.CreateCitaDialog;
import hospital_fct.controllers.citas.SearchCitaDialog;
import hospital_fct.models.Cita;
import hospital_fct.models.Doctor;
import hospital_fct.models.Especialidad;
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
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static hospital_fct.controllers.HospitalController.hospital;

public class DoctorController implements Initializable {

    // Model

    private ListProperty<Doctor> listaDoctor = new SimpleListProperty<>(FXCollections.observableArrayList());
    private ObjectProperty<Doctor> selectedDoctor = new SimpleObjectProperty<>();
    private ModifiedDoctorController modifiedDoctorController = new ModifiedDoctorController(this);
    private boolean modificar = false;

    // View

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Bindings

        selectedDoctor.bind(doctorTable.getSelectionModel().selectedItemProperty());

        // Cell bindings
        idDoctorColumn.setCellValueFactory(v -> v.getValue().idDoctorProperty());
        idSectorColumn.setCellValueFactory(v -> v.getValue().idSectorProperty());
        nombreDoctorColumn.setCellValueFactory(v -> v.getValue().nombreDoctorProperty());
        apellidoDoctorColumn.setCellValueFactory(v -> v.getValue().apellidoDoctorProperty());
        emailDoctorColumn.setCellValueFactory(v -> v.getValue().emailDoctorProperty());
        especialidadColumn.setCellValueFactory(v -> v.getValue().especialidadProperty());
        seccionDoctorColumn.setCellValueFactory(v -> v.getValue().sectorProperty());

        // Table binding

        doctorTable.itemsProperty().bind(listaDoctor);

        // Listener de selectedCita

        selectedDoctor.addListener((o, ov, nv) -> {
            if (nv != null) {
                modifiedDoctorController.getDoctorModify().setNombreDoctor(nv.getNombreDoctor());
                modifiedDoctorController.getDoctorModify().setApellidoDoctor(nv.getApellidoDoctor());
                modifiedDoctorController.getDoctorModify().setEmailDoctor(nv.getEmailDoctor());
                modifiedDoctorController.getDoctorModify().setEspecialidad(nv.getEspecialidad());
                modifiedDoctorController.getDoctorModify().setSector(nv.getSector());
                modifiedDoctorController.getDoctorModify().setIdDoctor(nv.getIdDoctor());
                modifiedDoctorController.getDoctorModify().setIdSector(nv.getIdSector());
                modifiedDoctorController.getSectorComboBox().getSelectionModel().select(Sectores.valueOf(nv.getSector()));
                modifiedDoctorController.getEspecialidadComboBox().getSelectionModel().select(Especialidad.valueOf(nv.getEspecialidad().replace(" ", "_")));
                System.out.println(selectedDoctor.get().getIdDoctor());
            }
        });

        // Buttons listener

        selectedDoctor.addListener((o , ov ,nv) -> {
            if (!modificar) {
                modifyButton.setDisable(nv == null);
                deleteButton.setDisable(nv == null);
            }
        });

        modifyButton.setDisable(true);
        deleteButton.setDisable(true);


        doctorTable.getColumns().remove(idDoctorColumn);
        doctorTable.getColumns().remove(idSectorColumn);

        SplitPane.setResizableWithParent(modifiedDoctorController.getRoot() , false);
        buscarDoctor("", "");
    }

    public DoctorController() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/doctor/doctorView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private TableColumn<Doctor, String> apellidoDoctorColumn;

    @FXML
    private Button createButton;

    @FXML
    private Button deleteButton;

    @FXML
    private TableView<Doctor> doctorTable;

    @FXML
    private TableColumn<Doctor, Number> idDoctorColumn;

    @FXML
    private TableColumn<Doctor, Number> idSectorColumn;

    @FXML
    private TableColumn<Doctor, String> emailDoctorColumn;

    @FXML
    private TableColumn<Doctor, String> especialidadColumn;

    @FXML
    private Button modifyButton;

    @FXML
    private TableColumn<Doctor, String> nombreDoctorColumn;

    @FXML
    private BorderPane root;

    @FXML
    private TableColumn<Doctor, String> seccionDoctorColumn;

    @FXML
    private SplitPane splitModificar;

    @FXML
    void onCreateDoctorAction(ActionEvent event) {
        CreateDoctorDialog createDialog = new CreateDoctorDialog();
        Optional<Doctor> result = createDialog.showAndWait();
        result.ifPresent(this::insetarDoctor);
        buscarDoctor("", "");
    }

    @FXML
    void onDeleteDoctorAction(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Eliminar Doctor");
        alert.setHeaderText("¿Está seguro de que desea eliminar el doctor?");
        alert.setContentText("Esta acción no se puede deshacer y afectará a otras tablas como operaciones/citas.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Doctor doctor = selectedDoctor.get();
            if (doctor != null) {
                String query = "DELETE FROM Doctores WHERE IdDoctor = ?";
                try (Connection connection = DriverManager.getConnection(CreateDataBase.url);
                     PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setInt(1, doctor.getIdDoctor());
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                listaDoctor.remove(doctor);
            }
        }
    }

    @FXML
    void onModifiedDoctorAction(ActionEvent event) {
        modificar = true;
        createButton.setDisable(true);
        modifyButton.setDisable(true);
        deleteButton.setDisable(true);

        splitModificar.getItems().add(modifiedDoctorController.getRoot());
    }

    @FXML
    void onSearchAllDoctoresAction(ActionEvent event) {
        listaDoctor.clear();
        listaDoctor.addAll(buscarDoctor("", ""));
    }

    @FXML
    void onSearchDoctorAction(ActionEvent event) {
        SearchDoctorDialog searchDialog = new SearchDoctorDialog();
        Optional<String> campo = searchDialog.showAndWait();
        if (!campo.get().equals("")){
            TextInputDialog nameDialog = new TextInputDialog();
            nameDialog.setHeaderText("Introduzca el " + campo.get());
            nameDialog.setContentText(campo.get() + ": ");
            Optional<String> result = nameDialog.showAndWait();
            result.ifPresent(value -> {
                listaDoctor.clear();
                listaDoctor.addAll(buscarDoctor(campo.get(), "%" + value + "%"));
            });
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


    // Métodos

    public List<Doctor> buscarDoctor(String opcion, String parametro) {
        List<Doctor> doctores = new ArrayList<>();

        String query = getString(opcion);

        try (Connection connection = DriverManager.getConnection(CreateDataBase.url);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            if (opcion != null && !opcion.isEmpty() && parametro != null && !parametro.isEmpty()) {
                preparedStatement.setString(1, parametro);
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Doctor doctor = new Doctor();
                doctor.setIdDoctor(resultSet.getInt("IdDoctor"));
                doctor.setIdSector(resultSet.getInt("IdSector"));
                doctor.setNombreDoctor(resultSet.getString("NombreDoctor"));
                doctor.setApellidoDoctor(resultSet.getString("Apellido"));
                doctor.setEmailDoctor(resultSet.getString("Email"));
                doctor.setEspecialidad(resultSet.getString("Especialidad"));
                doctor.setSector(resultSet.getString("NombreSector"));
                doctores.add(doctor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return doctores;
    }

    public static String getString(String opcion) {
        String condicion = switch (opcion) {
            case "Nombre" -> "WHERE Doctores.Nombre LIKE ?";
            case "Apellido" -> "WHERE Apellido LIKE ?";
            case "Especialidad" -> "WHERE Especialidad LIKE ?";
            case "Sector" -> "WHERE Sectores_Hospital.NombreSeccion LIKE ?";
            case "Email" -> "WHERE Email LIKE ?";
            default -> "WHERE 1 = 1";
        };
        String query = "SELECT IdDoctor, Doctores.IdSector AS IdSector, Doctores.Nombre AS NombreDoctor, Apellido, Especialidad, Email, Sectores_Hospital.NombreSeccion AS NombreSector " +
                "FROM Doctores " +
                "JOIN Sectores_Hospital ON Sectores_Hospital.IdSector = Doctores.IdSector " +
                "JOIN Hospital ON Sectores_Hospital.IdHospital = Hospital.IdHospital " +
                condicion + hospital;

        return query;
    }

    public Doctor seleccionarDoctor() {

        // Crea la nueva ventana
        Stage secondaryStage = new Stage();
        secondaryStage.initModality(Modality.APPLICATION_MODAL); // Bloquea la ventana principal mientras esta está abierta

        SelectDoctorController selectDoctorController = new SelectDoctorController();

        // Configura la escena con el contenido de la ventana secundaria
        Scene scene = new Scene(selectDoctorController.getRoot());
        secondaryStage.setScene(scene);

        // Muestra la ventana secundaria y espera hasta que se cierre
        secondaryStage.showAndWait();

        // Obtener el doctor seleccionado
        Doctor doctorseleccionado = selectDoctorController.getSelectedDoctor();
        if (doctorseleccionado != null) {
            // Añadir el doctor a la lista
            return doctorseleccionado;
        } else {
            System.out.println("No se ha seleccionado ningún doctor.");
            return null;
        }
    }

    public void actualizarListaDoctores() {
        listaDoctor.clear();
        listaDoctor.addAll(buscarDoctor("", ""));
    }

    public void insetarDoctor(Doctor doctor) {
        String query = "INSERT INTO Doctores (IdSector, Nombre, Apellido, Email, Especialidad) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(CreateDataBase.url);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, doctor.getIdSector());
            preparedStatement.setString(2, doctor.getNombreDoctor());
            preparedStatement.setString(3, doctor.getApellidoDoctor());
            preparedStatement.setString(4, doctor.getEmailDoctor());
            preparedStatement.setString(5, doctor.getEspecialidad());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int getIdSector(Connection connection, String sector) throws SQLException {
        String getIdPacienteQuery = "SELECT IdSector FROM Sectores_Hospital WHERE NombreSeccion = ?";

        try (PreparedStatement stmt = connection.prepareStatement(getIdPacienteQuery)) {
            stmt.setString(1, sector);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("IdSector");
            } else {
                System.err.println("No se encontró el sector = " + sector);
                return 1;
            }
        }
    }
}
