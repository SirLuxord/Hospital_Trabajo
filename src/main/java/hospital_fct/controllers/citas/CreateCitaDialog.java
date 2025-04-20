package hospital_fct.controllers.citas;

import hospital_fct.controllers.doctores.DoctorController;
import hospital_fct.controllers.pacientes.PacienteController;
import hospital_fct.models.Cita;
import hospital_fct.models.Doctor;
import hospital_fct.models.Estado;
import hospital_fct.models.Paciente;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Dialog;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.io.IOException;

import java.net.URL;
import java.util.ResourceBundle;

public class CreateCitaDialog extends Dialog<Cita> implements Initializable {

    // Model

    private CitaController citaController = new CitaController();
    private ObjectProperty<Cita> cita = new SimpleObjectProperty<>(new Cita());
    private PacienteController pacienteController = new PacienteController();
    private DoctorController doctorController = new DoctorController();



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Bindings

        asuntoTextField.textProperty().bindBidirectional(cita.get().asuntoProperty());
        fechaDatePicker.valueProperty().bindBidirectional(cita.get().fechaCitaProperty());
        horaTextField.textProperty().bindBidirectional(cita.get().horaProperty());
        pacienteTextField.textProperty().bindBidirectional(cita.get().nombrePacienteProperty());
        doctorTextField.textProperty().bindBidirectional(cita.get().nombreDoctorProperty());
        estadoComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                cita.get().setEstado(newVal.toString());
            }
        });
        cita.get().estadoProperty().addListener((obs, oldVal, newVal) -> {
            estadoComboBox.setValue(Estado.valueOf(newVal));
        });

        // Rellenamos la comboBox
        estadoComboBox.getItems().addAll(Estado.values());

        setTitle("Crear");
        setHeaderText("Introduzca los datos de la cita a crear:");
        getDialogPane().setContent(root);
        getDialogPane().getButtonTypes().setAll(
                new ButtonType("Crear", ButtonBar.ButtonData.OK_DONE),
                ButtonType.CANCEL
        );
        setResultConverter(this::onResult);

    }

    public CreateCitaDialog() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/citas/createCitaView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private TextField asuntoTextField;

    @FXML
    private TextField doctorTextField;

    @FXML
    private ComboBox<Estado> estadoComboBox;

    @FXML
    private DatePicker fechaDatePicker;

    @FXML
    private TextField horaTextField;

    @FXML
    private TextField pacienteTextField;

    @FXML
    private GridPane root;

    @FXML
    void onSelectDoctorAction(ActionEvent event) {
        Doctor doctor = doctorController.seleccionarDoctor();
        if (doctor != null) {
            doctorTextField.setText(doctor.getNombreDoctor());
            cita.get().setIdDoctor(doctor.getIdDoctor());
        }
    }

    @FXML
    void onSelectPacienteAction(ActionEvent event) {
        Paciente paciente = pacienteController.seleccionarPaciente();
        if (paciente != null) {
            pacienteTextField.setText(paciente.getNombrePaciente());
            cita.get().setIdPaciente(paciente.getIdPaciente());
        }
    }

    public GridPane getRoot() {
        return root;
    }

    // Pendiente de este método

    private Cita onResult(ButtonType buttonType) {
        if (buttonType.getButtonData() == ButtonBar.ButtonData.OK_DONE){
            return cita.get();
        }
        return null;
    }


}
