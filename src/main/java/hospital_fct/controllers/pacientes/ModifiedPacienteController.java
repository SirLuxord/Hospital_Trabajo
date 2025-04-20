package hospital_fct.controllers.pacientes;

import hospital_fct.CreateDataBase;
import hospital_fct.controllers.operaciones.OperacionController;
import hospital_fct.models.Operacion;
import hospital_fct.models.Paciente;
import hospital_fct.models.Sangre;
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

public class ModifiedPacienteController implements Initializable {

    // Model

    private PacienteController pacienteController;
    private ObjectProperty<Paciente> pacienteModify = new SimpleObjectProperty<>(new Paciente());

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sangreComboBox.getItems().addAll(Sangre.values());

        // Bindings

        nombreTextField.textProperty().bindBidirectional(pacienteModify.get().nombrePacienteProperty());
        apellidoTextField.textProperty().bindBidirectional(pacienteModify.get().apellidoPacienteProperty());
        emailTextField.textProperty().bindBidirectional(pacienteModify.get().emailPacienteProperty());
        telefonoTextField.textProperty().bindBidirectional(pacienteModify.get().telefonoProperty());
        sangreComboBox.getSelectionModel().selectedItemProperty().addListener((o, ov, nv) -> {
            if (nv != null) {
                pacienteModify.get().setTipoSangre(nv.toString());
            }
        });
    }

    public ModifiedPacienteController(PacienteController pacienteController) {
        try{
            this.pacienteController = pacienteController;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/pacientes/modifiedPacienteView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private TextArea alergiasTextField;

    @FXML
    private TextField apellidoTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField nombreTextField;

    @FXML
    private BorderPane root;

    @FXML
    private ComboBox<Sangre> sangreComboBox;

    @FXML
    private TextField telefonoTextField;

    @FXML
    void onCancelAction(ActionEvent event) {
        pacienteController.getSplitModificar().getItems().remove(this.getRoot());
        pacienteController.getCreateButton().setDisable(false);
        pacienteController.getModifyButton().setDisable(false);
        pacienteController.getDeleteButton().setDisable(false);
        pacienteController.setModificar(false);
    }

    @FXML
    void onConfirmAction(ActionEvent event) {
        int idPaciente = pacienteModify.get().getIdPaciente();
        String nombre = pacienteModify.get().getNombrePaciente();
        String apellido = pacienteModify.get().getApellidoPaciente();
        String email = pacienteModify.get().getEmailPaciente();
        String telefono = pacienteModify.get().getTelefono();
        String sangre = pacienteModify.get().getTipoSangre();

        String updateQuery = "UPDATE Pacientes SET Nombre = ?, Apellido = ?, Email = ?, Telefono = ?, TipoSangre = ? WHERE idPaciente = ?";

        try (Connection connection = DriverManager.getConnection(CreateDataBase.url)){

            try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
                updateStmt.setString(1, nombre);
                updateStmt.setString(2, apellido);
                updateStmt.setString(3, email);
                updateStmt.setString(4, telefono);
                updateStmt.setString(5, sangre);
                updateStmt.setInt(6, idPaciente);

                int filasActualizadas = updateStmt.executeUpdate();
                System.out.println(filasActualizadas + " cita actualizada correctamente.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onSelectAlergiasAction(ActionEvent event) {

    }

    public Paciente getPacienteModify() {
        return pacienteModify.get();
    }

    public ObjectProperty<Paciente> pacienteModifyProperty() {
        return pacienteModify;
    }

    public void setPacienteModify(Paciente pacienteModify) {
        this.pacienteModify.set(pacienteModify);
    }

    public ComboBox<Sangre> getSangreComboBox() {
        return sangreComboBox;
    }

    public void setSangreComboBox(ComboBox<Sangre> sangreComboBox) {
        this.sangreComboBox = sangreComboBox;
    }

    public BorderPane getRoot() {
        return root;
    }
}
