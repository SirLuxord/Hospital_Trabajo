package hospital_fct.controllers.pacientes.alergias;

import hospital_fct.models.Alergias;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AlergiaController implements Initializable {

    private ListProperty<Alergias> listaCita = new SimpleListProperty<>(FXCollections.observableArrayList());

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public AlergiaController() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/alergiasClientes/alergiaView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    private TableColumn<Alergias, String> alergiaColumn;

    @FXML
    private TableColumn<Alergias, Number> idAlergiaColumn;

    @FXML
    private TableView<Alergias> alergiaTable;

    @FXML
    private Button createButton;

    @FXML
    private Button deleteButton;

    @FXML
    private BorderPane root;

    @FXML
    void onAddAlergiaAction(ActionEvent event) {

    }

    @FXML
    void onDeleteAlergiaAction(ActionEvent event) {

    }
}
