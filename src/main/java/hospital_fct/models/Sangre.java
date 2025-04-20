package hospital_fct.models;

public enum Sangre {
    A_POS("A+"),
    A_NEG("A-"),
    B_POS("B+"),
    B_NEG("B-"),
    AB_POS("AB+"),
    AB_NEG("AB-"),
    O_POS("O+"),
    O_NEG("O-");

    private final String etiqueta;

    Sangre(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public static Sangre fromEtiqueta(String etiqueta) {
        for (Sangre sangre : Sangre.values()) {
            if (sangre.getEtiqueta().equals(etiqueta)) {
                return sangre;
            }
        }
        throw new IllegalArgumentException("Tipo de sangre no válido: " + etiqueta);
    }

    @Override
    public String toString() {
        return etiqueta;
    }
}
