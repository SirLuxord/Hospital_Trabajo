package hospital_fct;

import hospital_fct.controllers.HospitalController;
import hospital_fct.controllers.LoginController;
import hospital_fct.controllers.RootController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HospitalFCTApp extends Application {

    // Controllers

    private static HospitalController hospitalController = new HospitalController();
    private static LoginController loginController = new LoginController();
    public static RootController rootController = new RootController();

    // Login

    private static Stage loginStage;
    private static Stage hospitalStage = new Stage();
    private static Stage rootStage = new Stage();

    @Override
    public void start(Stage primaryStage) throws Exception {

        Scene scene = new Scene(loginController.getRoot());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Proyecto Hospital");
        primaryStage.show();
        loginStage = primaryStage;
        hospitalStage.setScene(new Scene (hospitalController.getRoot()));
        rootStage.setScene(new Scene(rootController.getRoot()));
    }

    public static void mostarLogin(){
        loginStage.show();
    }

    public static void mostrarHospital(){
        hospitalStage.show();
    }

    public static void mostrarRoot(){
        rootStage.show();
    }

    public static HospitalController getRootController() {
        return hospitalController;
    }

    public static LoginController getLoginController() {
        return loginController;
    }

    public static HospitalController getHospitalController() {
        return hospitalController;
    }

    public static void ocultarLogin(){
        loginStage.hide();
    }

    public static void ocultarHospital(){
        hospitalStage.hide();
    }

    public static void ocultarRoot(){
        rootStage.hide();
    }

}
