/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Alerta;
import model.Conexao;

/**
 *
 * @author Znoque
 */
public class Login extends Application {

    private static Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
        stage.initStyle(StageStyle.UNDECORATED);
        Scene scene = new Scene(root);
        Image icon = new Image(getClass().getResourceAsStream("/resource/icone-url.png"));
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.show();
        setStage(stage);
        stage.setOnCloseRequest(e -> {
            boolean r = Alerta.getAlertaSair();
            if (r) {
                
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