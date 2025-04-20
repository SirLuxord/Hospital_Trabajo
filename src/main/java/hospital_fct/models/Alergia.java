package hospital_fct.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Alergia {

    private IntegerProperty idAlergia = new SimpleIntegerProperty();
    private StringProperty nombreAlergia = new SimpleStringProperty();

    public Alergia() {
    }

    public int getIdAlergia() {
        return idAlergia.get();
    }

    public IntegerProperty idAlergiaProperty() {
        return idAlergia;
    }

    public void setIdAlergia(int idAlergia) {
        this.idAlergia.set(idAlergia);
    }

    public String getNombreAlergia() {
        return nombreAlergia.get();
    }

    public StringProperty nombreAlergiaProperty() {
        return nombreAlergia;
    }

    public void setNombreAlergia(String nombreAlergia) {
        this.nombreAlergia.set(nombreAlergia);
    }

}
