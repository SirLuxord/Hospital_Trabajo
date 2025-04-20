package hospital_fct.controllers;

import hospital_fct.HospitalFCTApp;
import hospital_fct.models.Hospitales;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HospitalController implements Initializable {

    // Model

    public static String hospital;

    // View

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        hospitalComboBox.getItems().addAll(Hospitales.values());
        hospitalComboBox.getSelectionModel().select(0);
        hospitalComboBox.getSelectionModel().selectedItemProperty().addListener((o, ov, nv) -> {
            if (nv != null) {
                hospital = " and Hospital.Nombre = '" + nv.toString() + "' ";
            }
        });
        hospital = " and Hospital.Nombre = '" + hospitalComboBox.getSelectionModel().getSelectedItem().toString() + "' ";

    }

    public HospitalController() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/hospitalView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private Button entrarButton;

    @FXML
    private ComboBox<Hospitales> hospitalComboBox;

    @FXML
    private BorderPane root;

    @FXML
    void onLogOutAction(ActionEvent event) {
        HospitalFCTApp.mostarLogin();
        HospitalFCTApp.ocultarHospital();
    }

    @FXML
    void onSelectHospitalAction(ActionEvent event) {
        HospitalFCTApp.mostrarRoot();
        HospitalFCTApp.ocultarHospital();
    }

    public BorderPane getRoot() {
        return root;
    }
}
