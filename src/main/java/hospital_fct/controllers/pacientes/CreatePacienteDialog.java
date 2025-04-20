package hospital_fct.controllers.pacientes;

import hospital_fct.controllers.doctores.DoctorController;
import hospital_fct.models.Doctor;
import hospital_fct.models.Paciente;
import hospital_fct.models.Sangre;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CreatePacienteDialog extends Dialog<Paciente> implements Initializable {

    // Model

    private PacienteController pacienteController = new PacienteController();
    private ObjectProperty<Paciente> paciente = new SimpleObjectProperty<>(new Paciente());

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        sangreComboBox.getItems().addAll(Sangre.values());

        // Bindings

        nombreTextField.textProperty().bindBidirectional(paciente.get().nombrePacienteProperty());
        apellidoTextField.textProperty().bindBidirectional(paciente.get().apellidoPacienteProperty());
        telefonoTextField.textProperty().bindBidirectional(paciente.get().telefonoProperty());
        emailTextField.textProperty().bindBidirectional(paciente.get().emailPacienteProperty());
        sangreComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                paciente.get().setTipoSangre(newVal.toString());
            }
        });

        setTitle("Crear");
        setHeaderText("Introduzca los datos del paciente a crear:");
        getDialogPane().setContent(root);
        getDialogPane().getButtonTypes().setAll(
                new ButtonType("Crear", ButtonBar.ButtonData.OK_DONE),
                ButtonType.CANCEL
        );
        setResultConverter(this::onResult);
    }

    public CreatePacienteDialog() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/pacientes/createPacienteView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private TextField apellidoTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField nombreTextField;

    @FXML
    private GridPane root;

    @FXML
    private ComboBox<Sangre> sangreComboBox;

    @FXML
    private TextField telefonoTextField;

    public GridPane getRoot() {
        return root;
    }

    // Pendiente de este método

    private Paciente onResult(ButtonType buttonType) {
        if (buttonType.getButtonData() == ButtonBar.ButtonData.OK_DONE){
            return paciente.get();
        }
        return null;
    }
}
