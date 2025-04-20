package hospital_fct.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class OperacionDoctores {

    private IntegerProperty idOperacion = new SimpleIntegerProperty();
    private IntegerProperty idDoctor = new SimpleIntegerProperty();
    private StringProperty nombreDoctor = new SimpleStringProperty();

    public OperacionDoctores() {
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

    public int getIdDoctor() {
        return idDoctor.get();
    }

    public IntegerProperty idDoctorProperty() {
        return idDoctor;
    }

    public void setIdDoctor(int idDoctor) {
        this.idDoctor.set(idDoctor);
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
}
