/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import main.Login;
import main.Principal;
import model.Alerta;
import model.Conexao;

/**
 * FXML Controller class
 *
 * @author Znoque
 */
public class LoginController implements Initializable {

    @FXML
    private TextField tfUsuario;
    @FXML
    private PasswordField tfSenha;
    @FXML
    private Button btnLogin;
    @FXML
    private Button btnSair;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cursorMao();
        Conexao.criarTabelas();
        
        btnSair.setOnAction(value -> System.exit(0));
        
        btnSair.setOnKeyPressed(e -> {//SE PRECIONAR ENTER QUANDO O BOTÃO TIVER SELECIONADO
            if (e.getCode().equals(KeyCode.ENTER)) {
                System.exit(0);
            }
        });
        
        btnLogin.setOnAction(value -> logar());
        
        btnLogin.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ENTER)) {
                logar();
            }
        });
    }    
    
    public void cursorMao() {
        btnLogin.setCursor(Cursor.HAND);
        btnSair.setCursor(Cursor.HAND);
    }
    
    public void logar() {
        //Conexao.login(tfUsuario.getText().trim(), tfSenha.getText().trim());
        try {
            Principal p = new Principal();
            p.start(new Stage());
            Login.getStage().close();
        } catch (Exception ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            Alerta.getAlertaErro("Erro ao Abrir a Tela Principal", "Falha ao Abrir a Tela Principal", "Não Foi Possivel Abir a Tela Principal");
        }
    }
}
