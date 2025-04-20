package hospital_fct;

import java.sql.*;

public class CreateDataBase {

    public static String url = "jdbc:sqlite:src/main/resources/base_de_datos/hospitaldb.db";
    public static String url2 = "jdbc:sqlite:src/main/resources/base_de_datos/usuarios.db";

    public static void crear_bd(){

        String[] sqlStatements = {
                "CREATE TABLE IF NOT EXISTS Hospital (" +
                        "IdHospital INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "Nombre TEXT NOT NULL" +
                        ");",

                "CREATE TABLE IF NOT EXISTS Sectores_Hospital (" +
                        "IdSector INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "IdHospital INTEGER NOT NULL," +
                        "NombreSeccion TEXT NOT NULL," +
                        "FOREIGN KEY (IdHospital) REFERENCES Hospital(IdHospital)" +
                        ");",

                "CREATE TABLE IF NOT EXISTS Doctores (" +
                        "IdDoctor INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "IdSector INTEGER NOT NULL," +
                        "Nombre TEXT NOT NULL," +
                        "Apellido TEXT NOT NULL," +
                        "Especialidad TEXT NOT NULL," +
                        "Email TEXT," +
                        "FOREIGN KEY (IdSector) REFERENCES Sectores_Hospital(IdSector)" +
                        ");",

                "CREATE TABLE IF NOT EXISTS Pacientes (" +
                        "IdPaciente INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "Nombre TEXT NOT NULL," +
                        "Apellido TEXT NOT NULL," +
                        "TipoSangre TEXT," +
                        "Email TEXT," +
                        "Telefono TEXT" +
                        ");",

                "CREATE TABLE IF NOT EXISTS Citas (" +
                        "IdCita INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "IdPaciente INTEGER NOT NULL," +
                        "IdDoctor INTEGER NOT NULL," +
                        "Asunto TEXT NOT NULL," +
                        "Fecha DATE NOT NULL," +
                        "Hora TEXT NOT NULL," +
                        "Estado TEXT," +
                        "FOREIGN KEY (IdPaciente) REFERENCES Pacientes(IdPaciente)," +
                        "FOREIGN KEY (IdDoctor) REFERENCES Doctores(IdDoctor)" +
                        ");",

                "CREATE TABLE IF NOT EXISTS Alergias (" +
                        "IdAlergia INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "Nombre TEXT NOT NULL" +
                        ");",

                "CREATE TABLE IF NOT EXISTS Alergias_Pacientes (" +
                        "IdAlergia INTEGER NOT NULL," +
                        "IdPaciente INTEGER NOT NULL," +
                        "PRIMARY KEY (IdAlergia, IdPaciente)," +
                        "FOREIGN KEY (IdAlergia) REFERENCES Alergias(IdAlergia)," +
                        "FOREIGN KEY (IdPaciente) REFERENCES Pacientes(IdPaciente)" +
                        ");",

                "CREATE TABLE IF NOT EXISTS Operaciones (" +
                        "IdOperacion INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "IdPaciente INTEGER NOT NULL," +
                        "IdSector INTEGER NOT NULL," +
                        "Operacion TEXT NOT NULL," +
                        "Fecha DATE NOT NULL," +
                        "Hora TEXT NOT NULL," +
                        "FOREIGN KEY (IdPaciente) REFERENCES Pacientes(IdPaciente)," +
                        "FOREIGN KEY (IdSector) REFERENCES Sectores_Hospital(IdSector)" +
                        ");",

                "CREATE TABLE IF NOT EXISTS Operaciones_Doctores (" +
                        "IdOperacion INTEGER NOT NULL," +
                        "IdDoctor INTEGER NOT NULL," +
                        "PRIMARY KEY (IdOperacion, IdDoctor)," +
                        "FOREIGN KEY (IdOperacion) REFERENCES Operaciones(IdOperacion)," +
                        "FOREIGN KEY (IdDoctor) REFERENCES Doctores(IdDoctor)" +
                        ");"
        };

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            // Activar las restricciones de foreign keys
            stmt.execute("PRAGMA foreign_keys = ON;");

            // Ejecutar las sentencias para crear las tablas
            for (String sql : sqlStatements) {
                stmt.execute(sql);
            }

            System.out.println("Base de datos creada correctamente ✅");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void insertarDatos() {

        String[] sqlStatements = {
                "PRAGMA foreign_keys = ON;",

                "INSERT INTO Hospital (Nombre) VALUES\n" +
                        "('Hospital Central'),\n" +
                        "('Clínica del Sol'),\n" +
                        "('Sanatorio Norte'),\n" +
                        "('Hospital del Oeste');",

                "INSERT INTO Sectores_Hospital (IdHospital, NombreSeccion) VALUES\n" +
                        "(1, 'Emergencias'), (1, 'Consultorio'), (1, 'Internación'), (1, 'Quirófanos'),\n" +
                        "(2, 'Emergencias'), (2, 'Consultorio'), (2, 'Maternidad'), (2, 'Pediatría'),\n" +
                        "(3, 'Consultorio'), (3, 'Internación'), (3, 'Quirófanos'), (3, 'Pediatría'),\n" +
                        "(4, 'Emergencias'), (4, 'Consultorio'), (4, 'Internación'), (4, 'Maternidad');",

                "INSERT INTO Doctores (IdSector, Nombre, Apellido, Especialidad, Email) VALUES\n" +
                        "(1, 'Ana', 'Gómez', 'Cardiólogo', 'ana.gomez@hospital.com'),\n" +
                        "(2, 'Luis', 'Martínez', 'Médico general', 'luis.martinez@hospital.com'),\n" +
                        "(3, 'Paula', 'Lopez', 'Ginecólogo', 'paula.lopez@hospital.com'),\n" +
                        "(4, 'Carlos', 'Perez', 'Neurologista', 'carlos.perez@hospital.com'),\n" +
                        "(2, 'Julia', 'Sosa', 'Dermatólogo', 'julia.sosa@hospital.com'),\n" +
                        "(5, 'María', 'Juarez', 'Pediatra', 'maria.juarez@clinica.com'),\n" +
                        "(6, 'Federico', 'Ramirez', 'Cardiólogo', 'fede.ramirez@clinica.com'),\n" +
                        "(7, 'Sandra', 'Bustos', 'Urologista', 'sandra.bustos@clinica.com'),\n" +
                        "(8, 'Jorge', 'Alvarez', 'Oftalmólogo', 'jorge.alvarez@clinica.com'),\n" +
                        "(5, 'Laura', 'Fernandez', 'Médico general', 'laura.fernandez@clinica.com'),\n" +
                        "(9, 'Mateo', 'Ibarra', 'Neurologista', 'mateo.ibarra@sanatorio.com'),\n" +
                        "(10, 'Claudia', 'Mendez', 'Cardiólogo', 'claudia.mendez@sanatorio.com'),\n" +
                        "(11, 'Nicolas', 'Ortega', 'Dermatólogo', 'nicolas.ortega@sanatorio.com'),\n" +
                        "(12, 'Bruno', 'Suarez', 'Pediatra', 'bruno.suarez@sanatorio.com'),\n" +
                        "(10, 'Lorena', 'Reyes', 'Ginecólogo', 'lorena.reyes@sanatorio.com'),\n" +
                        "(13, 'Agustin', 'Diaz', 'Urologista', 'agustin.diaz@oeste.com'),\n" +
                        "(14, 'Camila', 'Paz', 'Oftalmólogo', 'camila.paz@oeste.com'),\n" +
                        "(15, 'Emanuel', 'Romero', 'Cardiólogo', 'emanuel.romero@oeste.com'),\n" +
                        "(16, 'Silvina', 'Ayala', 'Médico general', 'silvina.ayala@oeste.com'),\n" +
                        "(14, 'Andrés', 'López', 'Pediatra', 'andres.lopez@oeste.com');",

                "INSERT INTO Pacientes (Nombre, Apellido, TipoSangre, Email, Telefono) VALUES\n" +
                        "('Valentina', 'García', 'A+', 'valen.garcia@gmail.com', '123456789'),\n" +
                        "('Martín', 'Rodríguez', 'O-', 'martin.rodriguez@gmail.com', '987654321'),\n" +
                        "('Lucía', 'Fernández', 'B+', 'lucia.fernandez@gmail.com', '456789123'),\n" +
                        "('Santiago', 'Gómez', 'AB+', 'santi.gomez@gmail.com', '321654987'),\n" +
                        "('Camila', 'Morales', 'O+', 'camila.morales@gmail.com', '789123456');",

                "INSERT INTO Alergias (Nombre) VALUES\n" +
                        "('Penicilina'),\n" +
                        "('Penicilamina'),\n" +
                        "('Clorexidina'),\n" +
                        "('Polvo'),\n" +
                        "('Lactosa'),\n" +
                        "('Anti_Inflamatorios');",

                "INSERT INTO Alergias_Pacientes (IdAlergia, IdPaciente) VALUES\n" +
                        "(1, 1), (4, 1),\n" +
                        "(2, 2), (5, 2),\n" +
                        "(3, 3), (6, 3),\n" +
                        "(1, 4), (5, 4),\n" +
                        "(2, 5), (3, 5);",

                "INSERT INTO Citas (IdPaciente, IdDoctor, Asunto, Fecha, Hora, Estado) VALUES\n" +
                        "(1, 1, 'Chequeo general', '2025-04-20', '09:00', 'Pendiente'),\n" +
                        "(1, 2, 'Consulta de rutina', '2025-04-25', '14:00', 'Pendiente'),\n" +
                        "(2, 6, 'Pediatría control', '2025-04-21', '10:30', 'Pendiente'),\n" +
                        "(2, 7, 'Consulta de presión', '2025-04-27', '16:00', 'Pendiente'),\n" +
                        "(3, 11, 'Consulta neurológica', '2025-04-22', '08:45', 'Pendiente'),\n" +
                        "(3, 12, 'Chequeo cardíaco', '2025-04-29', '13:30', 'Pendiente'),\n" +
                        "(4, 16, 'Urología control', '2025-04-23', '11:00', 'Pendiente'),\n" +
                        "(4, 17, 'Examen ocular', '2025-04-30', '15:15', 'Pendiente'),\n" +
                        "(5, 19, 'Control general', '2025-04-24', '09:30', 'Pendiente'),\n" +
                        "(5, 20, 'Consulta pediátrica', '2025-04-28', '17:00', 'Pendiente');",

                "INSERT INTO Operaciones (IdPaciente, IdSector, Operacion, Fecha, Hora) VALUES\n" +
                        "(1, 4, 'Apendicectomía', '2025-05-01', '08:00'),\n" +
                        "(1, 4, 'Laparoscopía', '2025-06-01', '07:30'),\n" +
                        "(2, 8, 'Cesárea programada', '2025-05-03', '09:00'),\n" +
                        "(2, 8, 'Ligadura de trompas', '2025-06-03', '10:00'),\n" +
                        "(3, 11, 'Biopsia cerebral', '2025-05-05', '08:30'),\n" +
                        "(3, 11, 'Implante neurológico', '2025-06-05', '09:30'),\n" +
                        "(4, 13, 'Prostatectomía', '2025-05-07', '10:00'),\n" +
                        "(4, 13, 'Litotricia', '2025-06-07', '11:00'),\n" +
                        "(5, 16, 'Cesárea de urgencia', '2025-05-09', '12:00'),\n" +
                        "(5, 16, 'Revisión post-parto', '2025-06-09', '13:00');",

                "INSERT INTO Operaciones_Doctores (IdOperacion, IdDoctor) VALUES\n" +
                        "(1, 1), (1, 3),\n" +
                        "(2, 2), (2, 4),\n" +
                        "(3, 6), (3, 8),\n" +
                        "(4, 7), (4, 9),\n" +
                        "(5, 11), (5, 13),\n" +
                        "(6, 12), (6, 14),\n" +
                        "(7, 16), (7, 18),\n" +
                        "(8, 17), (8, 19),\n" +
                        "(9, 19), (9, 20),\n" +
                        "(10, 18), (10, 20);"
        };

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            for (String sql : sqlStatements) {
                stmt.execute(sql);
            }

            System.out.println("Datos insertados correctamente ✅");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void insertarDatosLogin() {



        String createTableSQL = """
                CREATE TABLE IF NOT EXISTS usuarios (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nombre TEXT NOT NULL UNIQUE,
                    contrasena TEXT NOT NULL
                );
                """;

        String insertAdminSQL = "INSERT OR IGNORE INTO usuarios (nombre, contrasena) VALUES (?, ?);";

        try (Connection conn = DriverManager.getConnection(url2);
             Statement stmt = conn.createStatement()) {

            // Crear tabla
            stmt.execute(createTableSQL);

            // Insertar usuario admin
            try (PreparedStatement pstmt = conn.prepareStatement(insertAdminSQL)) {
                pstmt.setString(1, "admin");
                pstmt.setString(2, Utils.hashPassword("admin"));
                pstmt.executeUpdate();
                System.out.println("Usuario admin insertado correctamente.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}



//Statement stmt = conn.createStatement();
//                stmt.execute("CREATE TABLE IF NOT EXISTS usuarios (id INTEGER PRIMARY KEY, nombre TEXT)");
//                stmt.execute("INSERT INTO usuarios (nombre) VALUES ('Juan')");
//
//ResultSet rs = stmt.executeQuery("SELECT * FROM usuarios");
//                while (rs.next()) {
//        System.out.println("Usuario: " + rs.getString("nombre"));
//        }