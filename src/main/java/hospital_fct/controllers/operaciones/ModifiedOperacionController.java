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
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static hospital_fct.controllers.doctores.DoctorController.getIdSector;

public class ModifiedOperacionController implements Initializable {

    // Model

    private OperacionController operacionController;
    private ObjectProperty<Operacion> operacionModify = new SimpleObjectProperty<>(new Operacion());
    private PacienteController pacienteController = new PacienteController();
    private DoctorController doctorController = new DoctorController();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        sectorComboBox.getItems().addAll(Sectores.values());

        // Bindings

        operacionTextField.textProperty().bindBidirectional(operacionModify.get().operacionProperty());
        fechaDatePicker.valueProperty().bindBidirectional(operacionModify.get().fechaProperty());
        horaTextField.textProperty().bindBidirectional(operacionModify.get().horaProperty());
        pacienteTextField.textProperty().bindBidirectional(operacionModify.get().nombrePacienteProperty());
        System.out.println("Sector: " + operacionModify.get().getNombreSector());
        sectorComboBox.getSelectionModel().selectedItemProperty().addListener((o, ov, nv) -> {
            if (nv != null) {
                operacionModify.get().setNombreSector(nv.toString().replace("_", " "));
            }
        });
    }

    public ModifiedOperacionController(OperacionController operacionController) {
        try{
            this.operacionController = operacionController;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/operaciones/modifiedOperacionView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private TextField operacionTextField;

    @FXML
    private TextArea doctoresTextField;

    @FXML
    private DatePicker fechaDatePicker;

    @FXML
    private TextField horaTextField;

    @FXML
    private TextField pacienteTextField;

    @FXML
    private ComboBox<Sectores> sectorComboBox;

    @FXML
    private BorderPane root;

    @FXML
    void onCancelAction(ActionEvent event) {
        operacionController.getSplitModificar().getItems().remove(this.getRoot());
        operacionController.getCreateButton().setDisable(false);
        operacionController.getModifyButton().setDisable(false);
        operacionController.getDeleteButton().setDisable(false);
        operacionController.setModificar(false);
    }

    @FXML
    void onConfirmAction(ActionEvent event) {

        int idoperacion = operacionModify.get().getIdOperacion();
        int idpaciente = operacionModify.get().getIdPaciente();
        String operacion = operacionTextField.getText();
        String fecha = fechaDatePicker.getValue().toString();
        String hora = horaTextField.getText();

        String updateQuery = "UPDATE Operaciones SET Operacion = ?, Fecha = ?, Hora = ?, IdPaciente = ?, IdSector = ? WHERE idoperacion = ?";

        try (Connection connection = DriverManager.getConnection(CreateDataBase.url)){

            int idSector = getIdSector(connection, sectorComboBox.getValue().toString());

            try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
                updateStmt.setString(1, operacion);
                updateStmt.setString(2, fecha);
                updateStmt.setString(3, hora);
                updateStmt.setInt(4, idpaciente);
                updateStmt.setInt(5, idSector);
                updateStmt.setInt(6, idoperacion);

                int filasActualizadas = updateStmt.executeUpdate();
                System.out.println(filasActualizadas + " operacion actualizada correctamente.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void onSelectDoctoresAction(ActionEvent event) {
        doctorController.seleccionarDoctor();

    }

    @FXML
    void onSelectPacienteAction(ActionEvent event) {
        Paciente paciente = pacienteController.seleccionarPaciente();
        if (paciente != null) {
            pacienteTextField.setText(paciente.getNombrePaciente());
            operacionModify.get().setIdPaciente(paciente.getIdPaciente());

            try (Connection connection = DriverManager.getConnection(CreateDataBase.url)){
                int idSector = getIdSector(connection, operacionModify.get().getNombreSector());
                operacionModify.get().setIdSector(idSector);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public TextArea getDoctoresTextField() {
        return doctoresTextField;
    }

    public void setDoctoresTextField(TextArea doctoresTextField) {
        this.doctoresTextField = doctoresTextField;
    }

    public Operacion getOperacionModify() {
        return operacionModify.get();
    }

    public ObjectProperty<Operacion> operacionModifyProperty() {
        return operacionModify;
    }

    public void setOperacionModify(Operacion operacionModify) {
        this.operacionModify.set(operacionModify);
    }

    public ComboBox<Sectores> getSectorComboBox() {
        return sectorComboBox;
    }

    public void setSectorComboBox(ComboBox<Sectores> sectorComboBox) {
        this.sectorComboBox = sectorComboBox;
    }

    public BorderPane getRoot() {
        return root;
    }
}
