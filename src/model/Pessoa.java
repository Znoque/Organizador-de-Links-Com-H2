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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;

/**
 *
 * @author Znoque
 */
public class Pessoa {

    private IntegerProperty ID = new SimpleIntegerProperty();
    private StringProperty nome = new SimpleStringProperty();
    private StringProperty senha = new SimpleStringProperty();
    private ObservableList<Link> links = FXCollections.observableArrayList();
    //private ObservableSet<Link> links = FXCollections.observableSet();

    public Pessoa(int id, String nome, String senha) {
        this.ID.set(id);
        this.nome.set(nome);
        this.senha.set(senha);
    }

    @Override
    public String toString() {
        return "Id: "+getID().get()+"\nNome: "+getNome().get()+"\nSenha: "+getSenha().get();
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
     * @return the nome
     */
    public StringProperty getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(StringProperty nome) {
        this.nome = nome;
    }

    /**
     * @return the links
     */
    public ObservableList<Link> getLinks() {
        return links;
    }

    /**
     * @return the senha
     */
    public StringProperty getSenha() {
        return senha;
    }

    /**
     * @param senha the senha to set
     */
    public void setSenha(StringProperty senha) {
        this.senha = senha;
    }

}