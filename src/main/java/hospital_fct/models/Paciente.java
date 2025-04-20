package hospital_fct.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Paciente {

    private IntegerProperty idPaciente = new SimpleIntegerProperty();
    private StringProperty nombrePaciente = new SimpleStringProperty();
    private StringProperty apellidoPaciente = new SimpleStringProperty();
    private StringProperty tipoSangre = new SimpleStringProperty();
    private StringProperty alergia = new SimpleStringProperty();
    private StringProperty emailPaciente = new SimpleStringProperty();
    private StringProperty telefono = new SimpleStringProperty();

    public Paciente() {
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

    public String getApellidoPaciente() {
        return apellidoPaciente.get();
    }

    public StringProperty apellidoPacienteProperty() {
        return apellidoPaciente;
    }

    public void setApellidoPaciente(String apellidoPaciente) {
        this.apellidoPaciente.set(apellidoPaciente);
    }

    public String getTipoSangre() {
        return tipoSangre.get();
    }

    public StringProperty tipoSangreProperty() {
        return tipoSangre;
    }

    public void setTipoSangre(String tipoSangre) {
        this.tipoSangre.set(tipoSangre);
    }

    public String getAlergia() {
        return alergia.get();
    }

    public StringProperty alergiaProperty() {
        return alergia;
    }

    public void setAlergia(String alergia) {
        this.alergia.set(alergia);
    }

    public String getEmailPaciente() {
        return emailPaciente.get();
    }

    public StringProperty emailPacienteProperty() {
        return emailPaciente;
    }

    public void setEmailPaciente(String emailPaciente) {
        this.emailPaciente.set(emailPaciente);
    }

    public String getTelefono() {
        return telefono.get();
    }

    public StringProperty telefonoProperty() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono.set(telefono);
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
