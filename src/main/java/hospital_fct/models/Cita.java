package hospital_fct.models;

import javafx.beans.property.*;

import java.time.LocalDate;

public class Cita {

    private IntegerProperty idCita = new SimpleIntegerProperty();
    private IntegerProperty idDoctor = new SimpleIntegerProperty();
    private IntegerProperty idPaciente = new SimpleIntegerProperty();
    private StringProperty asunto = new SimpleStringProperty();
    private StringProperty nombreDoctor= new SimpleStringProperty();
    private StringProperty nombrePaciente= new SimpleStringProperty();
    private StringProperty hora= new SimpleStringProperty();
    private ObjectProperty<LocalDate> fechaCita = new SimpleObjectProperty<>();
    private StringProperty estado = new SimpleStringProperty();

    public Cita() {
    }

    public String getAsunto() {
        return asunto.get();
    }

    public StringProperty asuntoProperty() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto.set(asunto);
    }

    public String getNombreDoctor() {
        return nombreDoctor.get();
    }

    public StringProperty nombreDoctorProperty() {
        return nombreDoctor;
    }

    public void setNombreDoctor(String nombreDoctor) {
        this.nombreDoctor.set(nombreDoctor);
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

    public String getHora() {
        return hora.get();
    }

    public StringProperty horaProperty() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora.set(hora);
    }

    public LocalDate getFechaCita() {
        return fechaCita.get();
    }

    public ObjectProperty<LocalDate> fechaCitaProperty() {
        return fechaCita;
    }

    public void setFechaCita(LocalDate fechaCita) {
        this.fechaCita.set(fechaCita);
    }

    public String getEstado() {
        return estado.get();
    }

    public StringProperty estadoProperty() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado.set(estado);
    }

    public int getIdCita() {
        return idCita.get();
    }

    public IntegerProperty idCitaProperty() {
        return idCita;
    }

    public void setIdCita(int idCita) {
        this.idCita.set(idCita);
    }

    public int getIdDoctor() {
        return idDoctor.get();
    }

    public IntegerProperty idDoctorProperty() {
        return idDoctor;
    }

    public void setIdDoctor(int idDoctor) {
        this.idDoctor.set(idDoctor);
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
}
