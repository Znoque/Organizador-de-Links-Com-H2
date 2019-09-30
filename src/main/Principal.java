/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import controller.CadastroController;
import controller.PrincipalController;
import javafx.scene.image.Image;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Alerta;

/**
 *
 * @author Znoque
 */
public class Principal extends Application {

    private static Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/Principal.fxml"));
        Scene scene = new Scene(root);
        Image icon = new Image(getClass().getResourceAsStream("/resource/icone-url.png"));
        stage.getIcons().add(icon);
        stage.setTitle("Tela Principal");
        stage.setScene(scene);
        stage.show();
        setStage(stage);
        stage.setOnCloseRequest(e -> {
            boolean r = Alerta.getAlertaSair();
            if (r) {
                System.exit(0);
                //PrincipalController.fecharT1 = true;
                //CadastroController.estaSuspensa = false;
                //CadastroController.foiTerminada = true;
                //PrincipalController.getJanela().close();
            } else {
                e.consume();
            }
        });
    }

    public static void main(String[] args) {
        // TODO code application logic here
        launch(args);
    }
    
    /**
     * @return the stage
     */
    public static Stage getStage() {
        return stage;
    }

    /**
     * @param aStage the stage to set
     */
    public static void setStage(Stage aStage) {
        stage = aStage;
    }
}