package hospital_fct.controllers.pacientes;

import hospital_fct.controllers.doctores.SearchDoctorDialog;
import hospital_fct.models.Doctor;
import hospital_fct.models.Paciente;
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
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class SelectPacienteController implements Initializable {

    // Model

    private ListProperty<Paciente> listaPaciente = new SimpleListProperty<>(FXCollections.observableArrayList());
    private ObjectProperty<Paciente> selectedPaciente = new SimpleObjectProperty<>();
    private PacienteController pacienteController = new PacienteController();

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
        alergiaPacienteColumn.setCellValueFactory(v -> v.getValue().alergiaProperty());

        // Table binding
        pacienteTable.itemsProperty().bind(listaPaciente);

        idPacienteColumn.setVisible(false);

        listaPaciente.addAll(pacienteController.buscarPaciente("",""));
    }

    public SelectPacienteController() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/pacientes/selectPacienteView.fxml"));
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
    private TableColumn<Paciente, String> emailPacienteColumn;

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
    private TableColumn<Paciente, String> telefonoPacienteColumn;

    @FXML
    void onCancelAction(ActionEvent event) {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }

    @FXML
    void onSearchAllPacientesAction(ActionEvent event) {
        listaPaciente.addAll(pacienteController.buscarPaciente("",""));
    }

    @FXML
    void onSearchPacienteAction(ActionEvent event) {
        SearchDoctorDialog searchDialog = new SearchDoctorDialog();
        Optional<String> campo = searchDialog.showAndWait();
        if (!campo.get().equals("")){
            TextInputDialog nameDialog = new TextInputDialog();
            nameDialog.setHeaderText("Introduzca el " + campo.get());
            nameDialog.setContentText(campo.get() + ": ");
            Optional<String> result = nameDialog.showAndWait();
            result.ifPresent(value -> {
                listaPaciente.clear();
                listaPaciente.addAll(pacienteController.buscarPaciente(campo.get(), "%" + value + "%"));
            });
        }
    }

    @FXML
    void onSelectAction(ActionEvent event) {
        Paciente paciente = selectedPaciente.get();

        paciente.setIdPaciente(selectedPaciente.get().getIdPaciente());
        paciente.setNombrePaciente(selectedPaciente.get().getNombrePaciente() + " " + selectedPaciente.get().getApellidoPaciente());

        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }

    public BorderPane getRoot() {
        return root;
    }

    public Paciente getSelectedPaciente() {
        return selectedPaciente.get();
    }

    public ObjectProperty<Paciente> selectedPacienteProperty() {
        return selectedPaciente;
    }

    public void setSelectedPaciente(Paciente selectedPaciente) {
        this.selectedPaciente.set(selectedPaciente);
    }
}
