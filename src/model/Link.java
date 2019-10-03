/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.ImageView;

/**
 *
 * @author Znoque
 */
public class Link {

    private IntegerProperty ID = new SimpleIntegerProperty();
    private StringProperty titulo = new SimpleStringProperty();
    private StringProperty link = new SimpleStringProperty();
    private StringProperty categoria = new SimpleStringProperty();
    private StringProperty tag = new SimpleStringProperty();
    private ImageView icone;
    private String dataAdd;
    private String dataUltima;
    private int contador;
    

    public Link(int id, String titulo, String link, String categoria, String tag, ImageView icone) {
        this.ID.set(id);
        this.titulo.set(titulo);
        this.link.set(link);
        this.categoria.set(categoria);
        this.tag.set(tag);
        icone.setFitHeight(20);
        icone.setFitWidth(20);
        this.icone = icone;
    }
    
    public Link(int id, String titulo, String link, String categoria, String tag) {
        this.ID.set(id);
        this.titulo.set(titulo);
        this.link.set(link);
        this.categoria.set(categoria);
        this.tag.set(tag);
    }
    
    public Link(int id, String titulo, String link, String categoria, String tag, ImageView icone, String dataAdd, String dataUltima, int contador) {
        this.ID.set(id);
        this.titulo.set(titulo);
        this.link.set(link);
        this.categoria.set(categoria);
        this.tag.set(tag);
        icone.setFitHeight(20);
        icone.setFitWidth(20);
        this.icone = icone;
        this.dataAdd = dataAdd;
        this.dataUltima = dataUltima;
        this.contador = contador;
    }
    
    public Link(int id, String titulo, String link, String categoria, String tag, String dataAdd, String dataUltima, int contador) {
        this.ID.set(id);
        this.titulo.set(titulo);
        this.link.set(link);
        this.categoria.set(categoria);
        this.tag.set(tag);
        this.dataAdd = dataAdd;
        this.dataUltima = dataUltima;
        this.contador = contador;
    }
    
    @Override
    public String toString() {
        return "ID: " + getID().get() + "\nTitulo: " + getTitulo().get() + "\nLink: " + getLink().get();
    }

    /**
     * @return the ID
     */
    public IntegerProperty getID() {
        return ID;
    }

    /**
     * @param ID the ID to set
     */
    public void setID(IntegerProperty ID) {
        this.ID = ID;
    }

    /**
     * @return the titulo
     */
    public StringProperty getTitulo() {
        return titulo;
    }

    /**
     * @param titulo the titulo to set
     */
    public void setTitulo(StringProperty titulo) {
        this.titulo = titulo;
    }

    /**
     * @return the link
     */
    public StringProperty getLink() {
        return link;
    }

    /**
     * @param link the link to set
     */
    public void setLink(StringProperty link) {
        this.link = link;
    }

    /**
     * @return the categoria
     */
    public StringProperty getCategoria() {
        return categoria;
    }

    /**
     * @param categoria the categoria to set
     */
    public void setCategoria(StringProperty categoria) {
        this.categoria = categoria;
    }

    /**
     * @return the subcategoria
     */
    public StringProperty getTag() {
        return tag;
    }

    /**
     * @param subcategoria the subcategoria to set
     */
    public void setTag(StringProperty tag) {
        this.tag = tag;
    }

    public void setTituloString(String titulo) {
        this.titulo.set(titulo);
    }

    public void setLinkString(String link) {
        this.link.set(link);
    }

    public void setCategoriaString(String categoria) {
        this.categoria.set(categoria);
    }

    public void setTagString(String tag) {
        this.tag.set(tag);
    }

    /**
     * @return the icone
     */
    public ImageView getIcone() {
        return icone;
    }

    /**
     * @param icone the icone to set
     */
    public void setIcone(ImageView icone) {
        icone.setFitHeight(20);
        icone.setFitWidth(20);
        this.icone = icone;
    }

    /**
     * @return the dataAdd
     */
    public String getDataAdd() {
        return dataAdd;
    }
    
    /**
     * @param dataAdd the dataUltima to set
     */
    public void setDataAdd(String dataAdd) {
        this.dataAdd = dataAdd;
    }
    
    /**
     * @return the dataUltima
     */
    public String getDataUltima() {
        return dataUltima;
    }

    /**
     * @param dataUltima the dataUltima to set
     */
    public void setDataUltima(String dataUltima) {
        this.dataUltima = dataUltima;
    }

    /**
     * @return the contador
     */
    public int getContador() {
        return contador;
    }

    /**
     * @param contador the contador to set
     */
    public void setContador(int contador) {
        this.contador = contador;
    }
    
    public void incrementaContador() {
        this.contador +=1;
    }

}
