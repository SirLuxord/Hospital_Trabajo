package hospital_fct.controllers.doctores;

import hospital_fct.CreateDataBase;
import hospital_fct.models.Doctor;
import hospital_fct.models.Especialidad;
import hospital_fct.models.Sectores;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static hospital_fct.controllers.doctores.DoctorController.getIdSector;

public class CreateDoctorDialog extends Dialog<Doctor> implements Initializable {


    // Model

    private DoctorController doctorController = new DoctorController();
    private ObjectProperty<Doctor> doctor = new SimpleObjectProperty<>(new Doctor());
    private int idSector;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        especialidadComboBox.getItems().addAll(Especialidad.values());
        sectorComboBox.getItems().addAll(Sectores.values());

        // Bindings

        nombreTextField.textProperty().bindBidirectional(doctor.get().nombreDoctorProperty());
        apellidoTextField.textProperty().bindBidirectional(doctor.get().apellidoDoctorProperty());
        emailTextField.textProperty().bindBidirectional(doctor.get().emailDoctorProperty());
        sectorComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                doctor.get().setSector(newVal.toString().replace("_", " "));
            }
        });
        especialidadComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                doctor.get().setEspecialidad(newVal.toString().replace("_", " "));
            }
        });

        setTitle("Crear");
        setHeaderText("Introduzca los datos del doctor a crear:");
        getDialogPane().setContent(root);
        getDialogPane().getButtonTypes().setAll(
                new ButtonType("Crear", ButtonBar.ButtonData.OK_DONE),
                ButtonType.CANCEL
        );
        setResultConverter(this::onResult);

    }

    public CreateDoctorDialog() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/doctor/createDoctorView.fxml"));
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
    private GridPane root;

    @FXML
    private ComboBox<Sectores> sectorComboBox;

    public GridPane getRoot() {
        return root;
    }

    // Pendiente de este método

    private Doctor onResult(ButtonType buttonType) {

        try (Connection connection = DriverManager.getConnection(CreateDataBase.url)){

            idSector = getIdSector(connection, doctor.get().getSector());
            doctor.get().setIdSector(idSector);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (buttonType.getButtonData() == ButtonBar.ButtonData.OK_DONE){
            return doctor.get();
        }
        return null;
    }

}
