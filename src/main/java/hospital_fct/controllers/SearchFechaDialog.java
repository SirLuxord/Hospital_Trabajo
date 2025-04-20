package hospital_fct.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class SearchFechaDialog extends Dialog<List<LocalDate>> implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // init dialog
        setTitle("Buscar");
        setHeaderText("Elija el rango de fechas a buscar:");
        getDialogPane().setContent(root);
        getDialogPane().getButtonTypes().setAll(
                new ButtonType("Buscar", ButtonBar.ButtonData.OK_DONE),
                ButtonType.CANCEL
        );

        setResultConverter(this::onResult);
    }

    public SearchFechaDialog() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/searchFechaView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private DatePicker primeraFechaDatePicker;

    @FXML
    private DatePicker segundaFechaDatePicker;

    @FXML
    private GridPane root;


    private List<LocalDate> onResult(ButtonType buttonType) {
        if (buttonType.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
            LocalDate fecha1 = primeraFechaDatePicker.getValue();
            LocalDate fecha2 = segundaFechaDatePicker.getValue();

            if (fecha1 != null && fecha2 != null && !fecha2.isBefore(fecha1)) {
                return List.of(fecha1, fecha2);
            }
        }
        return null;
    }
}
