package hospital_fct.controllers.pacientes;

import hospital_fct.CreateDataBase;
import hospital_fct.controllers.doctores.CreateDoctorDialog;
import hospital_fct.controllers.doctores.SelectDoctorController;
import hospital_fct.controllers.operaciones.SearchOperacionDialog;
import hospital_fct.controllers.pacientes.alergias.TablaAlergiasController;
import hospital_fct.models.Doctor;
import hospital_fct.models.Operacion;
import hospital_fct.models.Paciente;
import hospital_fct.models.Sangre;
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
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class PacienteController implements Initializable {

    // Model

    private ListProperty<Paciente> listaPaciente = new SimpleListProperty<>(FXCollections.observableArrayList());
    private ObjectProperty<Paciente> selectedPaciente = new SimpleObjectProperty<>();
    private ModifiedPacienteController modifiedPacienteController = new ModifiedPacienteController(this);
    private TablaAlergiasController tablaAlergiasController = new TablaAlergiasController(this);
    private boolean modificar = false;
    private boolean tablaAlergias = false;

    // View

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Bindings

        selectedPaciente.bind(pacienteTable.getSelectionModel().selectedItemProperty());

        // Cell bindings

        idPacienteColumn.setCellValueFactory(v -> v.getValue().idPacienteProperty());
        nombrePacienteColumn.setCellValueFactory(v -> v.getValue().nombrePacienteProperty());
        apellidoPacienteColumn.setCellValueFactory(v -> v.getValue().apellidoPacienteProperty());
        emailPacienteColumn.setCellValueFactory(v -> v.getValue().emailPacienteProperty());
        telefonoPacienteColumn.setCellValueFactory(v -> v.getValue().telefonoProperty());
        sangrePacienteColumn.setCellValueFactory(v -> v.getValue().tipoSangreProperty());
//        alergiaPacienteColumn.setCellValueFactory(v -> v.getValue().alergiaProperty());

        // Table binding

        pacienteTable.itemsProperty().bind(listaPaciente);

        // Listener de selectedCita

        selectedPaciente.addListener((o, ov, nv) -> {
            if (nv != null) {
                modifiedPacienteController.getPacienteModify().setIdPaciente(nv.getIdPaciente());
                modifiedPacienteController.getPacienteModify().setNombrePaciente(nv.getNombrePaciente());
                modifiedPacienteController.getPacienteModify().setApellidoPaciente(nv.getApellidoPaciente());
                modifiedPacienteController.getPacienteModify().setEmailPaciente(nv.getEmailPaciente());
                modifiedPacienteController.getPacienteModify().setTelefono(nv.getTelefono());
                modifiedPacienteController.getPacienteModify().setTipoSangre(nv.getTipoSangre());
                modifiedPacienteController.getPacienteModify().setAlergia(nv.getAlergia());
                modifiedPacienteController.getSangreComboBox().getSelectionModel().select(Sangre.fromEtiqueta(nv.getTipoSangre()));
                tablaAlergiasController.getPacienteTabla().setIdPaciente(nv.getIdPaciente());
                tablaAlergiasController.buscarAlergias();
                if (!tablaAlergias){
                    splitModificar.getItems().add(tablaAlergiasController.getRoot());
                    tablaAlergias = true;
                }
            }
        });

        // Buttons listener

        selectedPaciente.addListener((o , ov ,nv) -> {
            if (!modificar) {
                modifyButton.setDisable(nv == null);
                deleteButton.setDisable(nv == null);
            }
        });

        modifyButton.setDisable(true);
        deleteButton.setDisable(true);

        idPacienteColumn.setVisible(false);

        SplitPane.setResizableWithParent(modifiedPacienteController.getRoot() , false);
    }

    public PacienteController() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/pacientes/pacienteView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private TableColumn<Paciente, String> alergiaPacienteColumn;

    @FXML
    private TableColumn<Paciente, String> apellidoPacienteColumn;

    @FXML
    private Button createButton;

    @FXML
    private Button deleteButton;

    @FXML
    private TableColumn<Paciente, String> emailPacienteColumn;

    @FXML
    private Button modifyButton;

    @FXML
    private TableColumn<Paciente, String> nombrePacienteColumn;

    @FXML
    private TableColumn<Paciente, Number> idPacienteColumn;

    @FXML
    private TableView<Paciente> pacienteTable;

    @FXML
    private BorderPane root;

    @FXML
    private TableColumn<Paciente, String> sangrePacienteColumn;

    @FXML
    private SplitPane splitModificar;

    @FXML
    private TableColumn<Paciente, String> telefonoPacienteColumn;

    @FXML
    void onCreatePacienteAction(ActionEvent event) {
        CreatePacienteDialog createDialog = new CreatePacienteDialog();
        Optional<Paciente> result = createDialog.showAndWait();
        result.ifPresent(paciente -> {
            insertarPaciente(paciente);
        });
    }

    @FXML
    void onDeletePacienteAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Eliminar Paciente");
        alert.setHeaderText("¿Está seguro de que desea eliminar al paciente?");
        alert.setContentText("Esta acción no se puede deshacer y afectará a otras tablas como operaciones/citas.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String query = "DELETE FROM Pacientes WHERE IdPaciente = ?";
            try (Connection connection = DriverManager.getConnection(CreateDataBase.url);
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setInt(1, selectedPaciente.get().getIdPaciente());
                preparedStatement.executeUpdate();
                listaPaciente.remove(selectedPaciente.get());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void onModifiedPacienteAction(ActionEvent event) {
        modificar = true;
        createButton.setDisable(true);
        modifyButton.setDisable(true);
        deleteButton.setDisable(true);

        splitModificar.getItems().add(modifiedPacienteController.getRoot());
    }

    @FXML
    void onSearchAllPacientesAction(ActionEvent event) {
        listaPaciente.clear();
        listaPaciente.addAll(buscarPaciente("", ""));
    }

    @FXML
    void onSearchPacienteAction(ActionEvent event) {
        SearchPacienteDialog searchDialog = new SearchPacienteDialog();
        Optional<String> campo = searchDialog.showAndWait();
        if (!campo.get().equals("")){
            TextInputDialog nameDialog = new TextInputDialog();
            nameDialog.setHeaderText("Introduzca el " + campo.get());
            nameDialog.setContentText(campo.get() + ": ");
            Optional<String> result = nameDialog.showAndWait();
            result.ifPresent(value -> {
                listaPaciente.clear();
                listaPaciente.addAll(buscarPaciente(campo.get(), "%" + value + "%"));
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

    public boolean isTablaAlergias() {
        return tablaAlergias;
    }

    public void setTablaAlergias(boolean tablaAlergias) {
        this.tablaAlergias = tablaAlergias;
    }

    public List<Paciente> buscarPaciente(String opcion, String parametro) {
        List<Paciente> pacientes = FXCollections.observableArrayList();

        String query = getString(opcion);

        try (Connection connection = DriverManager.getConnection(CreateDataBase.url);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            if (opcion != null && !opcion.isEmpty() && parametro != null && !parametro.isEmpty()) {
                preparedStatement.setString(1, parametro);
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Paciente paciente = new Paciente();
                paciente.setIdPaciente(resultSet.getInt("IdPaciente"));
                paciente.setNombrePaciente(resultSet.getString("Nombre"));
                paciente.setApellidoPaciente(resultSet.getString("Apellido"));
                paciente.setEmailPaciente(resultSet.getString("Email"));
                paciente.setTelefono(resultSet.getString("Telefono"));
                paciente.setTipoSangre(resultSet.getString("TipoSangre"));
                //paciente.setAlergia(resultSet.getString("Alergias"));
                pacientes.add(paciente);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pacientes;
    }

    private String getString(String opcion) {
        String condicion = switch (opcion) {
            case "Nombre" -> "WHERE Nombre LIKE ?";
            case "Apellido" -> "WHERE Apellido LIKE ?";
            case "Sangre" -> "WHERE TipoSangre LIKE ?";
            case "Alergias" -> "WHERE Alergias LIKE ?";
            case "Email" -> "WHERE Email LIKE ?";
            case "Telefono" -> "WHERE Telefono LIKE ?";
            default -> "WHERE 1 = 1";
        };
        String query = "SELECT Pacientes.* FROM Pacientes " + // , GROUP_CONCAT(Alergias.Nombre, ', ') AS Alergias
                //"JOIN Alergias_Pacientes ON Alergias_Pacientes.IdPaciente = Pacientes.IdPaciente " +
               /* "JOIN Alergias ON Alergias.IdAlergia = Alergias_Pacientes.IdAlergia " + */ condicion + " " +
                "GROUP BY Pacientes.IdPaciente";

        return query;
    }


    public Paciente seleccionarPaciente() {

        // Crea la nueva ventana
        Stage secondaryStage = new Stage();
        secondaryStage.initModality(Modality.APPLICATION_MODAL); // Bloquea la ventana principal mientras esta está abierta

        SelectPacienteController selectPacienteController = new SelectPacienteController();

        // Configura la escena con el contenido de la ventana secundaria
        Scene scene = new Scene(selectPacienteController.getRoot());
        secondaryStage.setScene(scene);

        // Muestra la ventana secundaria y espera hasta que se cierre
        secondaryStage.showAndWait();

        // Obtener el doctor seleccionado
        Paciente pacienteSeleccionado = selectPacienteController.getSelectedPaciente();
        if (pacienteSeleccionado != null) {
            // Añadir el doctor a la lista
            return pacienteSeleccionado;
        } else {
            System.out.println("No se ha seleccionado ningún paciente.");
            return null;
        }
    }

    public void insertarPaciente(Paciente paciente) {
        String query = "INSERT INTO Pacientes (Nombre, Apellido, Email, Telefono, TipoSangre) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(CreateDataBase.url);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, paciente.getNombrePaciente());
            preparedStatement.setString(2, paciente.getApellidoPaciente());
            preparedStatement.setString(3, paciente.getEmailPaciente());
            preparedStatement.setString(4, paciente.getTelefono());
            preparedStatement.setString(5, paciente.getTipoSangre());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int getAlergia(Connection connection, String sector) throws SQLException {
        String getIdAlergiaQuery = "SELECT IdAlergia FROM Alergias WHERE Nombre = ?";

        try (PreparedStatement stmt = connection.prepareStatement(getIdAlergiaQuery)) {
            stmt.setString(1, sector);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("IdAlergia");
            } else {
                System.err.println("No se encontró la alergia = " + sector);
                return 1;
            }
        }
    }
}
