package hospital_fct;

import hospital_fct.QR_Email.EmailSender;
import hospital_fct.QR_Email.QRGenerator;
import javafx.application.Application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.Normalizer;

public class Main {
    public static void main(String[] args) {
        CreateDataBase.crear_bd();
        CreateDataBase.insertarDatosLogin();
        Application.launch(HospitalFCTApp.class, args);

//        try {
//            // Contenido que irá dentro del QR
//            String contenidoQR = "Operación: Apendicectomía\nFecha: 25/04/2025\nHora: 10:30\nSector: Quirófano 2";
//
//            // Normalización de caracteres
//            String contenidoNormalizado = Normalizer.normalize(contenidoQR, Normalizer.Form.NFD);
//            contenidoNormalizado = contenidoNormalizado.replaceAll("[^\\p{ASCII}]", "");
//
//            // Generar QR
//            byte[] qrBytes = QRGenerator.generarQR(contenidoNormalizado, 250, 250);
//
//            // Enviar correo con QR adjunto
//            EmailSender.enviarCorreoConQR(
//                    "haendel.tee.2013@gmail.com",                  // Reemplaza con un correo real
//                    "Confirmación de operación normalizada",
//                    "Adjunto encontrarás tu QR con los detalles de la operación.",
//                    qrBytes
//            );
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
