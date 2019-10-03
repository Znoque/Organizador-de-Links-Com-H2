/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author znoque
 */
public class ManipulandoData {
    
    public String getDataAtual(){
        Date data = new Date();
        SimpleDateFormat formatar = new SimpleDateFormat("dd/MM/yyyy");
        String dataFormatada = formatar.format(data);
        return dataFormatada;
    }
    
    public String getDataFormatada(Date data){
        SimpleDateFormat formatar = new SimpleDateFormat("dd/MM/yyyy");
        String dataFormatada = formatar.format(data);
        return dataFormatada;
    }
    
    public Date getStringForData(String data){
        Date dataFormatada = new Date();
        try {
            SimpleDateFormat formatar = new SimpleDateFormat("dd/MM/yyyy"); 
            dataFormatada = formatar.parse(data);
        } catch (ParseException ex) {
            Logger.getLogger(ManipulandoData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dataFormatada;
    }
}
