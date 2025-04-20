package hospital_fct.controllers.operaciones.doctoresOpe;

import hospital_fct.CreateDataBase;
import hospital_fct.controllers.doctores.DoctorController;
import hospital_fct.controllers.doctores.SelectDoctorController;
import hospital_fct.controllers.operaciones.OperacionController;
import hospital_fct.models.Doctor;
import hospital_fct.models.Operacion;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.print.Doc;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static hospital_fct.CreateDataBase.url;

public class TablaDoctoresController implements Initializable {

    // Model

    private OperacionController operacionController;
    private DoctorController doctorController = new DoctorController();
    private ListProperty<Doctor> listaDoctor = new SimpleListProperty<>(FXCollections.observableArrayList());
    private ObjectProperty<Operacion> operacionAsistente = new SimpleObjectProperty<>(new Operacion());
    private ObjectProperty<Doctor> selectedDoctor = new SimpleObjectProperty<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Bindings

        selectedDoctor.bind(DoctoresTable.getSelectionModel().selectedItemProperty());

        // Bindings
        doctorColumn.setCellValueFactory(v -> v.getValue().nombreDoctorProperty());
        idDoctorColumn.setCellValueFactory(v -> v.getValue().idDoctorProperty());
        DoctoresTable.itemsProperty().bind(listaDoctor);

        idDoctorColumn.setVisible(false);
    }

    public TablaDoctoresController(OperacionController operacionController) {
        try {
            this.operacionController = operacionController;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/operacionesDoctores/tablaDoctoresView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private TableView<Doctor> DoctoresTable;

    @FXML
    private TableColumn<Doctor, String> doctorColumn;

    @FXML
    private TableColumn<Doctor, Number> idDoctorColumn;

    @FXML
    void onCloseAction(ActionEvent event) {
        operacionController.getSplitModificar().getItems().remove(this.getRoot());
        operacionController.setTablaDoctores(false);
    }

    @FXML
    void onAddDoctorAction(ActionEvent event) {
        Doctor doctor = doctorController.seleccionarDoctor();
        if (doctor != null) {
            listaDoctor.add(doctor);
            String query = "INSERT INTO Operaciones_Doctores (IdOperacion, IdDoctor) VALUES (?, ?)";

            try (Connection conn = DriverManager.getConnection(url);
                 PreparedStatement pstmt = conn.prepareStatement(query)) {

                pstmt.setInt(1, operacionAsistente.get().getIdOperacion());
                pstmt.setInt(2, doctor.getIdDoctor());

                // Ejecutar la consulta
                pstmt.executeUpdate();

                System.out.println("Datos insertados correctamente ✅");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void onDeleteDoctorAction(ActionEvent event) {

        String query = "DELETE FROM Operaciones_Doctores WHERE IdDoctor = " + selectedDoctor.get().getIdDoctor();

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            // Ejecutar la consulta
            stmt.executeUpdate(query);

            System.out.println("Datos elminidados correctamente ✅");

        } catch (Exception e) {
            e.printStackTrace();
        }
        listaDoctor.remove(selectedDoctor.get());
    }

    @FXML
    private BorderPane root;

    public BorderPane getRoot() {
        return root;
    }

    public void setRoot(BorderPane root) {
        this.root = root;
    }

    public Operacion getOperacionAsistente() {
        return operacionAsistente.get();
    }

    public ObjectProperty<Operacion> operacionAsistenteProperty() {
        return operacionAsistente;
    }

    public void setOperacionAsistente(Operacion operacionAsistente) {
        this.operacionAsistente.set(operacionAsistente);
    }

    // Métodos

    public void buscarDoctor() {
        listaDoctor.clear();
        String query = "SELECT Doctores.Nombre || ' ' || Doctores.Apellido AS NombreDoctor, Doctores.IdDoctor AS IdDoctor " +
                "FROM Operaciones " +
                "JOIN Operaciones_Doctores ON Operaciones.IdOperacion = Operaciones_Doctores.IdOperacion " +
                "JOIN Doctores ON Operaciones_Doctores.IdDoctor = Doctores.IdDoctor " +
                "WHERE Operaciones.IdOperacion = ?";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            if (operacionAsistente.get() != null) {
                preparedStatement.setInt(1, operacionAsistente.get().getIdOperacion());
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Doctor doctor = new Doctor();
                doctor.setNombreDoctor(resultSet.getString("NombreDoctor"));
                doctor.setIdDoctor(resultSet.getInt("IdDoctor"));
                listaDoctor.add(doctor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
