package hospital_fct.controllers;

import hospital_fct.HospitalFCTApp;
import hospital_fct.controllers.citas.CitaController;
import hospital_fct.controllers.doctores.DoctorController;
import hospital_fct.controllers.operaciones.OperacionController;
import hospital_fct.controllers.pacientes.PacienteController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RootController implements Initializable {

    // Controllers

    private CitaController citaController = new CitaController();
    private DoctorController doctorController = new DoctorController();
    private OperacionController operacionController = new OperacionController();
    private PacienteController pacienteController = new PacienteController();


    // View

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        citasTab.setContent(citaController.getRoot());
        doctoresTab.setContent(doctorController.getRoot());
        pacientesTab.setContent(pacienteController.getRoot());
        operacionesTab.setContent(operacionController.getRoot());
    }

    public RootController() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/rootView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private Tab citasTab;

    @FXML
    private Tab doctoresTab;

    @FXML
    private Tab operacionesTab;

    @FXML
    private Tab pacientesTab;

    @FXML
    private BorderPane root;

    @FXML
    void onExitButtonAction(ActionEvent event) {
        HospitalFCTApp.mostrarHospital();
        HospitalFCTApp.ocultarRoot();
    }

    public DoctorController getDoctorController() {
        return doctorController;
    }

    public void setDoctorController(DoctorController doctorController) {
        this.doctorController = doctorController;
    }

    public OperacionController getOperacionController() {
        return operacionController;
    }

    public void setOperacionController(OperacionController operacionController) {
        this.operacionController = operacionController;
    }

    public BorderPane getRoot() {
        return root;
    }
    public CitaController getCitaController() {
        return citaController;
    }
    public void setCitaController(CitaController citaController) {
        this.citaController = citaController;
    }
    public PacienteController getPacienteController() {
        return pacienteController;
    }
    public void setPacienteController(PacienteController pacienteController) {
        this.pacienteController = pacienteController;
    }
}
