package hospital_fct.models;

public enum Hospitales {
    HOSPITAL_CENTRAL("Hospital Central"),
    CLINICA_DEL_SOL("Clínica del Sol"),
    SANATORIO_NORTE("Sanatorio Norte"),
    HOSPITAL_DEL_OESTE("Hospital del Oeste");

    private final String nombre;

    Hospitales(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public static Hospitales fromNombre(String nombre) {
        for (Hospitales hospital : Hospitales.values()) {
            if (hospital.getNombre().equalsIgnoreCase(nombre)) {
                return hospital;
            }
        }
        throw new IllegalArgumentException("Hospital no válido: " + nombre);
    }

    @Override
    public String toString() {
        return nombre;
    }
}
