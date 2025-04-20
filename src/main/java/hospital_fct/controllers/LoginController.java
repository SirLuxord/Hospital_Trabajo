package hospital_fct.controllers;

import hospital_fct.CreateDataBase;
import hospital_fct.HospitalFCTApp;
import hospital_fct.Utils;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import java.sql.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static hospital_fct.CreateDataBase.url2;

public class LoginController implements Initializable {

    // Model



    // View

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public LoginController() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/loginView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private PasswordField passwordField;

    @FXML
    private BorderPane root;

    @FXML
    private TextField usuarioField;

    @FXML
    void onInsertarDatosAction(ActionEvent event) {
        CreateDataBase.insertarDatos();
    }

    @FXML
    void onLoginAction(ActionEvent event) {
        String usuario = usuarioField.getText();
        String contrasena = passwordField.getText();

        if (verificarLogin(usuario, contrasena)) {
            System.out.println("Login correcto");
            HospitalFCTApp.mostrarHospital();
            HospitalFCTApp.ocultarLogin();
        } else {
            System.out.println("Usuario o contraseña incorrectos");
            // Aquí puedes mostrar una alerta o notificación
        }
    }

    public BorderPane getRoot() {
        return root;
    }


    // Métodos

    public static boolean verificarLogin(String usuario, String contrasena) {
        String sql = "SELECT * FROM usuarios WHERE nombre = ? AND contrasena = ?";

        try (Connection conn = DriverManager.getConnection(url2);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, usuario);
            pstmt.setString(2, Utils.hashPassword(contrasena));

            ResultSet rs = pstmt.executeQuery();

            return rs.next(); // Devuelve true si encuentra el usuario

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
