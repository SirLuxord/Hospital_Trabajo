package hospital_fct.controllers.doctores;

import hospital_fct.models.Doctor;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class SelectDoctorController implements Initializable {

    // Model

    private ListProperty<Doctor> listaDoctor = new SimpleListProperty<>(FXCollections.observableArrayList());
    private ObjectProperty<Doctor> selectedDoctor = new SimpleObjectProperty<>();
    private DoctorController doctorController = new DoctorController();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Bindings

        selectedDoctor.bind(doctorTable.getSelectionModel().selectedItemProperty());

        // Cell bindings
        idDoctorColumn.setCellValueFactory(v -> v.getValue().idDoctorProperty());
        nombreDoctorColumn.setCellValueFactory(v -> v.getValue().nombreDoctorProperty());
        apellidoDoctorColumn.setCellValueFactory(v -> v.getValue().apellidoDoctorProperty());
        emailDoctorColumn.setCellValueFactory(v -> v.getValue().emailDoctorProperty());
        especialidadColumn.setCellValueFactory(v -> v.getValue().especialidadProperty());
        seccionDoctorColumn.setCellValueFactory(v -> v.getValue().sectorProperty());

        // Table binding
        doctorTable.itemsProperty().bind(listaDoctor);

        listaDoctor.addAll(doctorController.buscarDoctor("",""));
    }

    public SelectDoctorController() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/doctor/selectDoctorView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private TableColumn<Doctor, String> apellidoDoctorColumn;

    @FXML
    private TableView<Doctor> doctorTable;

    @FXML
    private TableColumn<Doctor, Number> idDoctorColumn;

    @FXML
    private TableColumn<Doctor, String> emailDoctorColumn;

    @FXML
    private TableColumn<Doctor, String> especialidadColumn;

    @FXML
    private TableColumn<Doctor, String> nombreDoctorColumn;

    @FXML
    private BorderPane root;

    @FXML
    private TableColumn<Doctor, String> seccionDoctorColumn;

    @FXML
    void onCancelAction(ActionEvent event) {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }

    @FXML
    void onSearchAllDoctoresAction(ActionEvent event) {
        listaDoctor.addAll(doctorController.buscarDoctor("",""));
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
                listaDoctor.addAll(doctorController.buscarDoctor(campo.get(), "%" + value + "%"));
            });
        }
    }

    @FXML
    void onSelectAction(ActionEvent event) {
        Doctor doctor = selectedDoctor.get();

        doctor.setIdDoctor(selectedDoctor.get().getIdDoctor());
        doctor.setNombreDoctor(selectedDoctor.get().getNombreDoctor() + " " + selectedDoctor.get().getApellidoDoctor());

        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }

    public BorderPane getRoot() {
        return root;
    }

    public Doctor getSelectedDoctor() {
        return selectedDoctor.get();
    }

    public ObjectProperty<Doctor> selectedDoctorProperty() {
        return selectedDoctor;
    }

    public void setSelectedDoctor(Doctor selectedDoctor) {
        this.selectedDoctor.set(selectedDoctor);
    }
}
