package hospital_fct.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Sector {

    private IntegerProperty idSector = new SimpleIntegerProperty();
    private StringProperty nombreSector = new SimpleStringProperty();

    public Sector() {
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
