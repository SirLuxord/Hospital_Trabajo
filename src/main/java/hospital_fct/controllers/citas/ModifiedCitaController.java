package hospital_fct.controllers.citas;

import hospital_fct.CreateDataBase;
import hospital_fct.controllers.doctores.DoctorController;
import hospital_fct.controllers.pacientes.PacienteController;
import hospital_fct.models.Cita;
import hospital_fct.models.Doctor;
import hospital_fct.models.Estado;
import hospital_fct.models.Paciente;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.ResourceBundle;

public class ModifiedCitaController implements Initializable {


    // Model

    private CitaController citaController;
    private ObjectProperty<Cita> citaModify = new SimpleObjectProperty<>(new Cita());
    private DoctorController doctorController = new DoctorController();
    private PacienteController pacienteController = new PacienteController();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Rellenamos la comboBox
        estadoComboBox.getItems().addAll(Estado.values());

        // Bindings
        asuntoTextField.textProperty().bindBidirectional(citaModify.get().asuntoProperty());
        doctorTextField.textProperty().bindBidirectional(citaModify.get().nombreDoctorProperty());
        estadoComboBox.getSelectionModel().selectedItemProperty().addListener((o, ov, nv) -> {
            if (nv != null) {
                citaModify.get().setEstado(nv.toString());
            }
        });
        fechaDatePicker.valueProperty().bindBidirectional(citaModify.get().fechaCitaProperty());
        horaTextField.textProperty().bindBidirectional(citaModify.get().horaProperty());
        pacienteTextField.textProperty().bindBidirectional(citaModify.get().nombrePacienteProperty());
    }

    public ModifiedCitaController(CitaController citaController) {
        try{
            this.citaController = citaController;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/citas/modifiedCitaView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private TextField asuntoTextField;

    @FXML
    private TextField doctorTextField;

    @FXML
    private ComboBox<Estado> estadoComboBox;

    @FXML
    private DatePicker fechaDatePicker;

    @FXML
    private TextField horaTextField;

    @FXML
    private TextField pacienteTextField;

    @FXML
    private BorderPane root;

    @FXML
    void onCancelAction(ActionEvent event) {
        citaController.getSplitModificar().getItems().remove(this.getRoot());
        citaController.getCreateButton().setDisable(false);
        citaController.getModifyButton().setDisable(false);
        citaController.getDeleteButton().setDisable(false);
        citaController.setModificar(false);
    }

    @FXML
    void onConfirmAction(ActionEvent event) {
        int id = citaModify.get().getIdCita();
        int idDoctor = citaModify.get().getIdDoctor();
        int idPaciente = citaModify.get().getIdPaciente();
        String asunto = citaModify.get().getAsunto();
        String doctor = citaModify.get().getNombreDoctor();
        String paciente = citaModify.get().getNombrePaciente();
        String estado = citaModify.get().getEstado();
        LocalDate fecha = citaModify.get().getFechaCita();
        String hora = citaModify.get().getHora();


        // Dividir nombre completo en nombre + apellido
        String[] partesDoctor = doctor.trim().split(" ");
        String nombreDoctor = partesDoctor[0];
        String apellidoDoctor = String.join(" ", Arrays.copyOfRange(partesDoctor, 1, partesDoctor.length));

        String[] partesPaciente = paciente.trim().split(" ");
        String nombrePaciente = partesPaciente[0];
        String apellidoPaciente = String.join(" ", Arrays.copyOfRange(partesPaciente, 1, partesPaciente.length));

        
        
        String updateQuery = "UPDATE Citas SET Asunto = ?, IdDoctor = ?, IdPaciente = ?, Estado = ?, Fecha = ?, Hora = ? WHERE IdCita = ?";

        try (Connection connection = DriverManager.getConnection(CreateDataBase.url)) {

            // Hacer el update
            try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
                updateStmt.setString(1, asunto);
                updateStmt.setInt(2, idDoctor);
                updateStmt.setInt(3, idPaciente);
                updateStmt.setString(4, estado);
                updateStmt.setString(5, fecha.toString()); // o Date.valueOf(fecha)
                updateStmt.setString(6, hora);
                updateStmt.setInt(7, id);

                int filasActualizadas = updateStmt.executeUpdate();
                System.out.println(filasActualizadas + " cita actualizada correctamente.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    private static int getidPaciente(Connection connection, int id) throws SQLException {
//        String getIdPacienteQuery = "SELECT IdPaciente FROM Citas WHERE IdCita = ?";
//        try (PreparedStatement stmt = connection.prepareStatement(getIdPacienteQuery)) {
//            stmt.setInt(1, id);
//            ResultSet rs = stmt.executeQuery();
//            if (rs.next()) {
//                return rs.getInt("IdPaciente");
//            } else {
//                System.err.println("No se encontró la cita con IdCita = " + id);
//                return -1;
//            }
//        }
//    }
//
//    private static int getidDoctor(Connection connection, int id) throws SQLException {
//        String getIdDoctorQuery = "SELECT IdDoctor FROM Citas WHERE IdCita = ?";
//
//        try (PreparedStatement stmt = connection.prepareStatement(getIdDoctorQuery)) {
//            stmt.setInt(1, id);
//            ResultSet rs = stmt.executeQuery();
//            if (rs.next()) {
//                return rs.getInt("IdDoctor");
//            } else {
//                System.err.println("No se encontró la cita con IdCita = " + id);
//                return -1;
//            }
//        }
//    }

    @FXML
    void onSelectDoctorAction(ActionEvent event) {
        Doctor doctor = doctorController.seleccionarDoctor();
        if (doctor != null) {
            doctorTextField.setText(doctor.getNombreDoctor());
            citaModify.get().setIdDoctor(doctor.getIdDoctor());
        }
        citaController.buscarCita("","","");
    }

    @FXML
    void onSelectPacienteAction(ActionEvent event) {
        Paciente paciente = pacienteController.seleccionarPaciente();
        if (paciente != null) {
            pacienteTextField.setText(paciente.getNombrePaciente());
            citaModify.get().setIdPaciente(paciente.getIdPaciente());
        }
        citaController.buscarCita("","","");
    }

    public ComboBox<Estado> getEstadoComboBox() {
        return estadoComboBox;
    }

    public void setEstadoComboBox(ComboBox<Estado> estadoComboBox) {
        this.estadoComboBox = estadoComboBox;
    }

    public Cita getCitaModify() {
        return citaModify.get();
    }

    public ObjectProperty<Cita> citaModifyProperty() {
        return citaModify;
    }

    public void setCitaModify(Cita citaModify) {
        this.citaModify.set(citaModify);
    }

    public BorderPane getRoot() {
        return root;
    }
}
