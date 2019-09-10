/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Locale;

/**
 *
 * @author Znoque
 */
public class TituloDoLink {
    public String pegarTitulo(String link){
        String link2 = "";
        String linkPonto = new String(link);
        String linkPonto2 = "";
        String titulo = "";
        StringBuilder sb = new StringBuilder(linkPonto);
        int x = 0;
        int contador = 0;
        System.out.println(link);
        for(int c=0; c<linkPonto.length(); c++){
            if(linkPonto.contains(".")){
                sb.deleteCharAt(linkPonto.indexOf("."));
                linkPonto2 = new String(sb.toString());
                contador++;
                if(linkPonto2.contains(".")){
                    contador++;
                    break;
                }
                break;
            }
        }
        if (link.contains("www.")) { //SE O LINK CONTER "www" ELE PEGA O TITULO DESSA FORMA
            for (int i = 0; i < link.length(); i++) {
                if (link.contains(".")) {
                    x = link.indexOf(".") + 1;
                    link2 = new String(link.substring(x));
                    if (link2.contains(".")) {
                        x = link2.indexOf(".");
                        titulo = new String(link2.substring(0, x));
                    }
                    break;
                }
            }
        } else if(contador==2){ //SE O LINK NÃO CONTER "www" E CONTER "."x2 ELE PEGA O TITULO DESSA FORMA
            for (int i = 0; i < linkPonto2.length(); i++) {
                  if (linkPonto2.contains("//")) {
                        x = linkPonto2.indexOf("//") + 2;
                        link2 = new String(linkPonto2.substring(x));
                        if (link2.contains(".")) {
                            x = link2.indexOf(".");
                            titulo = new String(link2.substring(0, x));
                        }
                        break;
                  }
            }
        } else { //SE O LINK NÃO CONTER "www" ELE PEGA O TITULO DESSA FORMA
            for (int i = 0; i < link.length(); i++) {
                if (link.contains("//")) {
                    x = link.indexOf("//") + 2;
                    link2 = new String(link.substring(x));
                    if (link2.contains(".")) {
                        x = link2.indexOf(".");
                        titulo = new String(link2.substring(0, x));
                    }
                    break;
                }
            }
        }
        return titulo;
        //System.out.println(titulo);
    }
}
