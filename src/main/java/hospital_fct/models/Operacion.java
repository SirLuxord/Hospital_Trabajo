package hospital_fct.models;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;

public class Operacion {

    private IntegerProperty idOperacion = new SimpleIntegerProperty();
    private IntegerProperty idPaciente = new SimpleIntegerProperty();
    private IntegerProperty idSector = new SimpleIntegerProperty();
    private StringProperty nombreSector = new SimpleStringProperty();
    private StringProperty operacion = new SimpleStringProperty();
    private StringProperty nombrePaciente = new SimpleStringProperty();
//    private ObservableList<Doctor> doctores = FXCollections.observableArrayList();
    private StringProperty Doctores = new SimpleStringProperty();
    private StringProperty hora = new SimpleStringProperty();
    private ObjectProperty<LocalDate> fecha = new SimpleObjectProperty<>();

    public Operacion() {
    }

    public String getOperacion() {
        return operacion.get();
    }

    public StringProperty operacionProperty() {
        return operacion;
    }

    public void setOperacion(String operacion) {
        this.operacion.set(operacion);
    }

    public String getNombrePaciente() {
        return nombrePaciente.get();
    }

    public StringProperty nombrePacienteProperty() {
        return nombrePaciente;
    }

    public void setNombrePaciente(String nombrePaciente) {
        this.nombrePaciente.set(nombrePaciente);
    }

//    public ObservableList<Doctor> getDoctores() {
//        return doctores;
//    }
//
//    public void setDoctores(ObservableList<Doctor> doctores) {
//        this.doctores = doctores;
//    }


    public String getDoctores() {
        return Doctores.get();
    }

    public StringProperty doctoresProperty() {
        return Doctores;
    }

    public void setDoctores(String doctores) {
        this.Doctores.set(doctores);
    }

    public String getHora() {
        return hora.get();
    }

    public StringProperty horaProperty() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora.set(hora);
    }

    public LocalDate getFecha() {
        return fecha.get();
    }

    public ObjectProperty<LocalDate> fechaProperty() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha.set(fecha);
    }

    public int getIdOperacion() {
        return idOperacion.get();
    }

    public IntegerProperty idOperacionProperty() {
        return idOperacion;
    }

    public void setIdOperacion(int idOperacion) {
        this.idOperacion.set(idOperacion);
    }

    public int getIdPaciente() {
        return idPaciente.get();
    }

    public IntegerProperty idPacienteProperty() {
        return idPaciente;
    }

    public void setIdPaciente(int idPaciente) {
        this.idPaciente.set(idPaciente);
    }

    public int getIdSector() {
        return idSector.get();
    }

    public IntegerProperty idSectorProperty() {
        return idSector;
    }

    public void setIdSector(int idSector) {
        this.idSector.set(idSector);
    }

    public String getNombreSector() {
        return nombreSector.get();
    }

    public StringProperty nombreSectorProperty() {
        return nombreSector;
    }

    public void setNombreSector(String nombreSector) {
        this.nombreSector.set(nombreSector);
    }
}
