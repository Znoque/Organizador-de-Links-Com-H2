/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 *
 * @author Znoque
 */
public class Alerta {
    public static void getAlertaErro(String titulo, String cabecalho, String conteudo){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecalho);
        alert.setContentText(conteudo);
        alert.showAndWait();
    }
    
    public static void getAlertaAviso(String titulo, String cabecalho){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecalho);
        alert.setContentText("");
        alert.showAndWait();
    }
    
    public static boolean getAlertaConfirma(String titulo, String cabecalho, String conteudo){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecalho);
        alert.setContentText(conteudo);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }
    }
    
    public static void getAlertaInfo(String titulo, String cabecalho, String conteudo){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecalho);
        alert.setContentText(conteudo);
        alert.showAndWait();
    }
    
    public static boolean getAletaTrocar(){
        boolean r;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        ButtonType btnSim = new ButtonType("Sim");
        ButtonType btnNao = new ButtonType("Não");
        alert.setTitle("Confirmação Para Trocar Conta");
        alert.setHeaderText("Deseja Realmente Trocar de Conta");
        alert.setContentText("Escolha Uma Opção");
        alert.getButtonTypes().setAll(btnSim, btnNao);
        Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == btnSim) {
                r = true;
            } else {
                r = false;
            }
        return r;
    }
    
    public static boolean getAlertaSair(){
        boolean r;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        ButtonType btnSim = new ButtonType("Sim");
        ButtonType btnNao = new ButtonType("Não");
        alert.setTitle("Confirmação Para Sair");
        alert.setHeaderText("Deseja Realmente Sair");
        alert.setContentText("Escolha Uma Opção");
        alert.getButtonTypes().setAll(btnSim, btnNao);
        Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == btnSim) {
                r = true;
            } else {
                r = false;
            }
        return r;
    }
}
