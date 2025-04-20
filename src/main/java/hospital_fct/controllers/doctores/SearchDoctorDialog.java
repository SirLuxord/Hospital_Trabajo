package hospital_fct.controllers.doctores;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SearchDoctorDialog extends Dialog<String> implements Initializable {

    // Model

    private final StringProperty campo = new SimpleStringProperty();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        fieldComboBox.getItems().addAll("Nombre", "Apellido", "Especialidad", "Sector", "Email");

        // bindings

        campo.bind(fieldComboBox.getSelectionModel().selectedItemProperty());

        // init dialog

        setTitle("Buscar");
        setHeaderText("Elija el campo del doctor a buscar:");
        getDialogPane().setContent(root);
        getDialogPane().getButtonTypes().setAll(
                new ButtonType("Buscar", ButtonBar.ButtonData.OK_DONE),
                ButtonType.CANCEL
        );

        setResultConverter(this::onResult);
    }

    public SearchDoctorDialog() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SearchView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private ComboBox<String> fieldComboBox;

    @FXML
    private BorderPane root;

    private String onResult(ButtonType buttonType) {
        if (buttonType.getButtonData() == ButtonBar.ButtonData.OK_DONE){
            return campo.get();
        }
        return "";
    }
}
