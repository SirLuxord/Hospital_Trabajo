package hospital_fct.controllers.pacientes.alergias;

import hospital_fct.CreateDataBase;
import hospital_fct.models.Alergia;
import hospital_fct.models.Alergias;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static hospital_fct.controllers.doctores.DoctorController.getIdSector;
import static hospital_fct.controllers.pacientes.PacienteController.getAlergia;

public class CreateAlergiaDialog extends Dialog<Alergia> implements Initializable {

    // Model

    private AlergiaController alergiaController = new AlergiaController();
    private int idAlergia;

    //Pendiente de esta declaración
    private ObjectProperty<Alergia> alergia = new SimpleObjectProperty<>();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        alergiaComboBox.getItems().addAll(Alergias.values());

        setTitle("Añadir");
        setHeaderText("Introduzca el tipo de alergia a añadir:");
        getDialogPane().setContent(root);
        getDialogPane().getButtonTypes().setAll(
                new ButtonType("Añadir", ButtonBar.ButtonData.OK_DONE),
                ButtonType.CANCEL
        );
        setResultConverter(this::onResult);
    }

    public CreateAlergiaDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/alergiasClientes/createAlergiaView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    private ComboBox<Alergias> alergiaComboBox;

    @FXML
    private GridPane root;

    // Pendiente de este método

    private Alergia onResult(ButtonType buttonType) {

        if (buttonType.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
            Alergia nuevaAlergia = new Alergia();
            try (Connection connection = DriverManager.getConnection(CreateDataBase.url)) {

                String nombre = alergiaComboBox.getValue().toString();
                int id = getAlergia(connection, nombre);

                nuevaAlergia.setNombreAlergia(nombre);
                nuevaAlergia.setIdAlergia(id);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return nuevaAlergia;
        }

        return null;
    }
}
