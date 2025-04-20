package hospital_fct.controllers.operaciones;

import hospital_fct.CreateDataBase;
import hospital_fct.controllers.doctores.DoctorController;
import hospital_fct.controllers.pacientes.PacienteController;
import hospital_fct.models.Doctor;
import hospital_fct.models.Operacion;
import hospital_fct.models.Paciente;
import hospital_fct.models.Sectores;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Dialog;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static hospital_fct.controllers.doctores.DoctorController.getIdSector;

public class CreateOperacionDialog extends Dialog<Operacion> implements Initializable {

    // Model

    private OperacionController operacionController = new OperacionController();
    private ObjectProperty<Operacion> operacion = new SimpleObjectProperty<>(new Operacion());
    private PacienteController pacienteController = new PacienteController();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sectorComboBox.getItems().addAll(Sectores.values());


        // Bindings
        asuntoTextField.textProperty().bindBidirectional(operacion.get().operacionProperty());
        fechaDatePicker.valueProperty().bindBidirectional(operacion.get().fechaProperty());
        horaTextField.textProperty().bindBidirectional(operacion.get().horaProperty());
        pacienteTextField.textProperty().bindBidirectional(operacion.get().nombrePacienteProperty());
        sectorComboBox.getSelectionModel().selectedItemProperty().addListener((o, ov, nv) -> {
            if (nv != null) {
                operacion.get().setNombreSector(nv.toString().replace("_", " "));
            }
        });

        operacion.get().nombrePacienteProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                pacienteTextField.setText(newVal);
            }
        });
        operacion.get().idPacienteProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                pacienteTextField.setText(newVal.toString());
            }
        });

        setTitle("Crear");
        setHeaderText("Introduzca los datos de la operación a crear:");
        getDialogPane().setContent(root);
        getDialogPane().getButtonTypes().setAll(
                new ButtonType("Crear", ButtonBar.ButtonData.OK_DONE),
                ButtonType.CANCEL
        );
        setResultConverter(this::onResult);
    }

    public CreateOperacionDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/operaciones/createOperacionView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private ComboBox<Sectores> sectorComboBox;

    @FXML
    private TextField asuntoTextField;

    @FXML
    private DatePicker fechaDatePicker;

    @FXML
    private TextField horaTextField;

    @FXML
    private TextField pacienteTextField;

    @FXML
    private GridPane root;

    @FXML
    void onSelectPacienteAction(ActionEvent event) {
        Paciente paciente = pacienteController.seleccionarPaciente();
        if (paciente != null) {
            pacienteTextField.setText(paciente.getNombrePaciente());
            operacion.get().setIdPaciente(paciente.getIdPaciente());

            try (Connection connection = DriverManager.getConnection(CreateDataBase.url)){
                int idSector = getIdSector(connection, operacion.get().getNombreSector());
                operacion.get().setIdSector(idSector);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public GridPane getRoot() {
        return root;
    }

    // Pendiente de este método

    private Operacion onResult(ButtonType buttonType) {
        if (buttonType.getButtonData() == ButtonBar.ButtonData.OK_DONE){
            return operacion.get();
        }
        return null;
    }
}
