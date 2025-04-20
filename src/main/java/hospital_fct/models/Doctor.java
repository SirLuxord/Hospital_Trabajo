package hospital_fct.models;

import javafx.beans.property.*;

import java.time.LocalDate;

public class Doctor {

    private IntegerProperty idDoctor = new SimpleIntegerProperty();
    private IntegerProperty idSector = new SimpleIntegerProperty();
    private StringProperty nombreDoctor = new SimpleStringProperty();
    private StringProperty apellidoDoctor = new SimpleStringProperty();
    private StringProperty especialidad = new SimpleStringProperty();
    private StringProperty sector = new SimpleStringProperty();
    private StringProperty emailDoctor = new SimpleStringProperty();

    public Doctor() {
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

    public String getApellidoDoctor() {
        return apellidoDoctor.get();
    }

    public StringProperty apellidoDoctorProperty() {
        return apellidoDoctor;
    }

    public void setApellidoDoctor(String apellidoDoctor) {
        this.apellidoDoctor.set(apellidoDoctor);
    }

    public String getEspecialidad() {
        return especialidad.get();
    }

    public StringProperty especialidadProperty() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad.set(especialidad);
    }

    public String getSector() {
        return sector.get();
    }

    public StringProperty sectorProperty() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector.set(sector);
    }

    public String getEmailDoctor() {
        return emailDoctor.get();
    }

    public StringProperty emailDoctorProperty() {
        return emailDoctor;
    }

    public void setEmailDoctor(String emailDoctor) {
        this.emailDoctor.set(emailDoctor);
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

    public int getIdSector() {
        return idSector.get();
    }

    public IntegerProperty idSectorProperty() {
        return idSector;
    }

    public void setIdSector(int idSector) {
        this.idSector.set(idSector);
    }
}
