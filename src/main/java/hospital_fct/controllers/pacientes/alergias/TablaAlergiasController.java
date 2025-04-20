package hospital_fct.controllers.pacientes.alergias;

import hospital_fct.CreateDataBase;
import hospital_fct.controllers.doctores.CreateDoctorDialog;
import hospital_fct.controllers.operaciones.OperacionController;
import hospital_fct.controllers.pacientes.PacienteController;
import hospital_fct.models.Alergia;
import hospital_fct.models.Doctor;
import hospital_fct.models.Paciente;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class TablaAlergiasController implements Initializable {

    private PacienteController pacienteController;
    private ObjectProperty<Paciente> pacienteTabla = new SimpleObjectProperty<>(new Paciente());
    private ListProperty<Alergia> listaAlergias = new SimpleListProperty<>(FXCollections.observableArrayList());

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Bindings

        alergiasColumn.setCellValueFactory(v -> v.getValue().nombreAlergiaProperty());
        IdAlergia.setCellValueFactory(v -> v.getValue().idAlergiaProperty());
        alergiasTable.itemsProperty().bind(listaAlergias);
        //alergiasTable.setSelectionModel(null);

        IdAlergia.setVisible(false);

    }

    public TablaAlergiasController(PacienteController pacienteController) {
        try{
            this.pacienteController = pacienteController;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/alergiasClientes/tablaAlergiasView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private TableColumn<Alergia, Number> IdAlergia;

    @FXML
    private TableColumn<Alergia, String> alergiasColumn;

    @FXML
    private TableView<Alergia> alergiasTable;

    @FXML
    private BorderPane root;

    @FXML
    void onCloseAction(ActionEvent event) {
        pacienteController.getSplitModificar().getItems().remove(this.getRoot());
        pacienteController.setTablaAlergias(false);
    }

    @FXML
    void onAddAction(ActionEvent event) {
        CreateAlergiaDialog createDialog = new CreateAlergiaDialog();
        Optional<Alergia> result = createDialog.showAndWait();
        result.ifPresent(this::insertarAlergia);
        buscarAlergias();
    }

    @FXML
    void onDeleteAction(ActionEvent event) {
        Alergia alergia = alergiasTable.getSelectionModel().getSelectedItem();
        if (alergia != null) {
            String query = "DELETE FROM Alergias_Pacientes WHERE IdPaciente = ? AND IdAlergia = ?";
            try (Connection connection = DriverManager.getConnection(CreateDataBase.url);
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                if (pacienteTabla.get() != null) {
                    preparedStatement.setInt(1, pacienteTabla.get().getIdPaciente());
                    preparedStatement.setInt(2, alergia.getIdAlergia());
                    preparedStatement.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            buscarAlergias();
        }
    }

    public BorderPane getRoot() {
        return root;
    }

    public void setRoot(BorderPane root) {
        this.root = root;
    }

    public Paciente getPacienteTabla() {
        return pacienteTabla.get();
    }

    public ObjectProperty<Paciente> pacienteTablaProperty() {
        return pacienteTabla;
    }

    public void setPacienteTabla(Paciente pacienteTabla) {
        this.pacienteTabla.set(pacienteTabla);
    }

    public void buscarAlergias() {
        listaAlergias.clear();

        String query = "SELECT Alergias.Nombre AS NombreAlergia, Alergias.IdAlergia AS IdAlergias " +
                "FROM Pacientes " +
                "JOIN Alergias_Pacientes ON Pacientes.IdPaciente = Alergias_Pacientes.IdPaciente " +
                "JOIN Alergias ON Alergias_Pacientes.IdAlergia = Alergias.IdAlergia " +
                "WHERE Pacientes.IdPaciente = ?";

        try (Connection connection = DriverManager.getConnection(CreateDataBase.url);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            if (pacienteTabla.get() != null) {
                preparedStatement.setInt(1, pacienteTabla.get().getIdPaciente());
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Alergia alergia = new Alergia();
                alergia.setNombreAlergia(resultSet.getString("NombreAlergia"));
                alergia.setIdAlergia(resultSet.getInt("IdAlergias"));
                listaAlergias.add(alergia);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertarAlergia(Alergia alergia) {
        String query = "INSERT INTO Alergias_Pacientes (IdPaciente, IdAlergia) VALUES (?, ?)";

        try (Connection connection = DriverManager.getConnection(CreateDataBase.url);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            if (pacienteTabla.get() != null) {
                preparedStatement.setInt(1, pacienteTabla.get().getIdPaciente());
                preparedStatement.setInt(2, alergia.getIdAlergia());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
