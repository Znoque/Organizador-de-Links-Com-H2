/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javafx.beans.property.Property;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 *
 * @author Znoque
 */
public class Notificacao {
    
    public static void getNotificacaoAdd(String tituloLink){
        TrayNotification notificacao = new TrayNotification();
        notificacao.setTitle("Gerenciador de Links");
        notificacao.setMessage(tituloLink+" Adicionado Com Sucesso!!");
        notificacao.setNotificationType(NotificationType.SUCCESS);
        notificacao.setAnimationType(AnimationType.POPUP);
        notificacao.showAndDismiss(Duration.millis(3000));
    }
    
    public static void getNotificacaoEdit(String Mensagem){
        TrayNotification notificacao = new TrayNotification();
        notificacao.setTitle("Gerenciador de Links");
        notificacao.setMessage(Mensagem);
        notificacao.setNotificationType(NotificationType.INFORMATION);
        notificacao.setAnimationType(AnimationType.POPUP);
        notificacao.showAndDismiss(Duration.millis(5000));
    }
    
    public static void getNotificacaoRemove(String tituloLink){
        TrayNotification notificacao = new TrayNotification();
        notificacao.setTitle("Gerenciador de Links");
        notificacao.setMessage(tituloLink+" Removido Com Sucesso!!");
        notificacao.setNotificationType(NotificationType.ERROR);
        notificacao.setAnimationType(AnimationType.POPUP);
        notificacao.showAndDismiss(Duration.millis(3000));
    }
}
