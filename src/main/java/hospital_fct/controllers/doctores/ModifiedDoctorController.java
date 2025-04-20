package hospital_fct.controllers.doctores;

import hospital_fct.CreateDataBase;
import hospital_fct.models.Doctor;
import hospital_fct.models.Especialidad;
import hospital_fct.models.Sectores;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import static hospital_fct.controllers.doctores.DoctorController.getIdSector;

public class ModifiedDoctorController implements Initializable {

    // Model

    private DoctorController doctorController;
    private ObjectProperty<Doctor> doctorModify = new SimpleObjectProperty<>(new Doctor());

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        especialidadComboBox.getItems().addAll(Especialidad.values());
        sectorComboBox.getItems().addAll(Sectores.values());


        // Bindings

        nombreTextField.textProperty().bindBidirectional(doctorModify.get().nombreDoctorProperty());
        apellidoTextField.textProperty().bindBidirectional(doctorModify.get().apellidoDoctorProperty());
        emailTextField.textProperty().bindBidirectional(doctorModify.get().emailDoctorProperty());
        especialidadComboBox.getSelectionModel().selectedItemProperty().addListener((o, ov, nv) -> {
            if (nv != null) {
                doctorModify.get().setEspecialidad(nv.toString().replace("_", " "));
            }
        });
        sectorComboBox.getSelectionModel().selectedItemProperty().addListener((o, ov, nv) -> {
            if (nv != null) {
                doctorModify.get().setSector(nv.toString().replace("_", " "));
            }
        });
    }


    public ModifiedDoctorController(DoctorController doctorController) {
        try{
            this.doctorController = doctorController;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/doctor/modifiedDoctorView.fxml"));
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
    private ComboBox<Especialidad> especialidadComboBox;

    @FXML
    private TextField nombreTextField;

    @FXML
    private BorderPane root;

    @FXML
    private ComboBox<Sectores> sectorComboBox;

    @FXML
    void onCancelAction(ActionEvent event) {
        doctorController.getSplitModificar().getItems().remove(this.getRoot());
        doctorController.getCreateButton().setDisable(false);
        doctorController.getModifyButton().setDisable(false);
        doctorController.getDeleteButton().setDisable(false);
        doctorController.setModificar(false);
    }

    @FXML
    void onConfirmAction(ActionEvent event) {

        String nombre = doctorModify.get().getNombreDoctor();
        String apellido = doctorModify.get().getApellidoDoctor();
        String email = doctorModify.get().getEmailDoctor();
        String especialidad = doctorModify.get().getEspecialidad();
        String sector = doctorModify.get().getSector();
        int id = doctorModify.get().getIdDoctor();
        int idSector = doctorModify.get().getIdSector();
        System.out.println("ID: " + id);

        String updateQuery = "UPDATE Doctores SET Nombre = ?, Apellido = ?, Email = ?, Especialidad = ?, IdSector = ? WHERE idDoctor = ?";

        try (Connection connection = DriverManager.getConnection(CreateDataBase.url)){

            idSector = getIdSector(connection, sector);

            try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
                updateStmt.setString(1, nombre);
                updateStmt.setString(2, apellido);
                updateStmt.setString(3, email);
                updateStmt.setString(4, especialidad);
                updateStmt.setInt(5, idSector);
                updateStmt.setInt(6, id);

                int filasActualizadas = updateStmt.executeUpdate();
                System.out.println(filasActualizadas + " cita actualizada correctamente.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ComboBox<Sectores> getSectorComboBox() {
        return sectorComboBox;
    }

    public void setSectorComboBox(ComboBox<Sectores> sectorComboBox) {
        this.sectorComboBox = sectorComboBox;
    }

    public ComboBox<Especialidad> getEspecialidadComboBox() {
        return especialidadComboBox;
    }

    public void setEspecialidadComboBox(ComboBox<Especialidad> especialidadComboBox) {
        this.especialidadComboBox = especialidadComboBox;
    }

    public Doctor getDoctorModify() {
        return doctorModify.get();
    }

    public ObjectProperty<Doctor> doctorModifyProperty() {
        return doctorModify;
    }

    public void setDoctorModify(Doctor doctorModify) {
        this.doctorModify.set(doctorModify);
    }

    public BorderPane getRoot() {
        return root;
    }

    // Métodos


}
