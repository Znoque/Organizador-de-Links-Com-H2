/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import static controller.PrincipalController.copiado;
import static controller.PrincipalController.getResultado;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.Alerta;
import model.Conexao;
import detectarcopiaby4java.DetectarCopiaBy4Java;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Scanner;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import model.Link;
import model.ManipulandoData;
import model.Notificacao;
import model.TituloComparator;
import model.TituloDoLink;

/**
 * FXML Controller class
 *
 * @author Znoque
 */
public class CadastroController implements Initializable {

    @FXML
    private Button btnLimpar;
    @FXML
    private Button btnSalvar;
    @FXML
    private Label lbUser;
    @FXML
    private TextField tfTitulo;
    @FXML
    private TextField tfLink;
    @FXML
    private TextField tfCategoria;
    @FXML
    private TextField tfTag;
    @FXML
    private CheckBox cbTemp;

    public static boolean aberto = false;
    public static TableView<Link> tabelaPrincipal;
    DetectarCopiaBy4Java obj = new DetectarCopiaBy4Java();
    public static boolean foiTerminada;
    public static boolean estaSuspensa;
    public static boolean temporario = false;
    TituloDoLink tituloLink = new TituloDoLink();
    public static boolean fecharT1 = false;
    public static boolean selecionado = false;
    public ManipulandoData mData = new ManipulandoData();
    public String dataAtual = mData.getDataAtual();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // CARREGANDO ELEMENTOS VISUAIS
        //tfLink2 = tfLink;
        carregarElementos();

        //SE HOUVER ALTERA????O NOS TEXTFILDS
        monitor();

        //A????O DOS BOT??ES
        btnLimpar.setOnAction(e -> limpar()); //A????O DO BOT??O LIMPAR
        btnSalvar.setOnAction(e -> {
            if (btnSalvar.getText().equals("Salvar Edi????o")) {
                editar();
            } else {
                suspend();
                adicionar();
                resume();
            }
        });

        //THREAD RESPONS??VEL POR PEGAR OS LINKS COPIADOS AUTOMATICAMENTE
        Thread t = new Thread(() -> {
            while (!foiTerminada) {
                try {
                    synchronized (this) {
                        while (estaSuspensa) {
                            wait();
                        }
                    }
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                try {
                    if (obj.detectar().contains("www") || obj.detectar().contains("https://") || obj.detectar().contains("http://")) {
                        if (copiado.equals(obj.detectar())) {

                        } else {
                            tfLink.setText(obj.detectar());
                            copiado = obj.detectar();
                            new Thread(() -> {
                                try{
                                     pegarTitulo(obj.detectar());
                                } catch (Exception ex) {
                                    //ex.printStackTrace();
                                }
                               
                            }).start();
                        }
                    }
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (foiTerminada) {
                return;
            }
        });
        t.start();
        
    }

    void suspend() {
        this.estaSuspensa = true;
    }

    synchronized void stop() {
        this.foiTerminada = true;
        notifyAll();
    }

    synchronized void resume() {
        this.estaSuspensa = false;
        notifyAll();
    }
    
    public void adicionar() {
        Link temp = new Link(0, "", "", "", "");
        boolean r = Alerta.getAlertaConfirma("Confirma????o Para Adicionar Link", "Deseja Realmente Adicionar o Novo Link?", "Escolha Uma Op????o");
        if (r) {
            if (cbTemp.isSelected()) {
                ImageView imagem = null;
                if (tfLink.getText().contains("https://") || tfLink.getText().contains("http://")) {
                    temp = new Link(1, tfTitulo.getText().trim(), tfLink.getText().trim(), tfCategoria.getText().trim(), tfTag.getText().trim(), dataAtual, dataAtual, 0);
                    Conexao.insertLinkTemporarios(temp);
                } else {
                    String link = "www." + tfLink.getText().trim();
                    temp = new Link(1, tfTitulo.getText().trim(), link, tfCategoria.getText().trim(), tfTag.getText().trim(), dataAtual, dataAtual, 0);
                    Conexao.insertLinkTemporarios(temp);
                }
                
            } else {
                if (tfLink.getText().contains("https://") || tfLink.getText().contains("http://")) {
                    temp = new Link(1, tfTitulo.getText().trim(), tfLink.getText().trim(), tfCategoria.getText().trim(), tfTag.getText().trim(), dataAtual, dataAtual, 0);
                    Conexao.insertLink(temp);
                } else {
                    String link = "www." + tfLink.getText().trim();
                    temp = new Link(1, tfTitulo.getText().trim(), link, tfCategoria.getText().trim(), tfTag.getText().trim(), dataAtual, dataAtual, 0);
                    Conexao.insertLink(temp);
                }
                //Notificacao.getNotificacaoAdd(temp.getTitulo().get());
            }
            Notificacao.getNotificacaoAdd("Link");
        }
        PrincipalController.getResultado().remove(0, PrincipalController.getResultado().size());
        PrincipalController.getResultado().addAll(Conexao.getLinks());
        Collections.sort(Conexao.getLinks(), new TituloComparator());
        PrincipalController.tvLinksCompartilhado.refresh();
        limpar();
    }

    public void editar() {
        boolean r = Alerta.getAlertaConfirma("Confirma????o Para Editar Link", "Deseja Realmente Editar o Link Selecionado?", "Escolha Uma Op????o");
        boolean temp = false; //SE FOR FALSO O LINK PERTENCE A LISTA DE LINKS SALVOS

        //COMPARA OS LINKS TEMPORARIOS COM O LINK SELECIONADO PRA SABER SE PERTENCE A LISTA DOS TEMPORARIOS
        int c = 0;
        for (Link l : Conexao.getLinksTemp()) {
            if (l.getID().get() == PrincipalController.linkEditar.getID().get()) {
                temp = true; //O LINK PERTENCE A LISTA DE LINKS TEMPORARIOS
                break;
            }
            c++;
        }

        //VERIFICA SE ?? PRA EDITAR OU CANCELAR
        if (r) {
            //SE O LINK FOR DA LISTA DE TEMPORARIOS E FOR CONTINUAR COMO TEMPORARIO S?? EDITA NA LISTA DE TEMPORARIOS
            if (cbTemp.isSelected() && temp) {
                PrincipalController.linkEditar.setTituloString(tfTitulo.getText().trim());
                PrincipalController.linkEditar.setCategoriaString(tfCategoria.getText().trim());
                PrincipalController.linkEditar.setTagString(tfTag.getText().trim());
                PrincipalController.linkEditar.setDataAdd(dataAtual);
                PrincipalController.linkEditar.setDataUltima(dataAtual);
                PrincipalController.linkEditar.setContador(0);
                if (tfLink.getText().contains("www") || tfLink.getText().contains("https://") || tfLink.getText().contains("http://")) {
                    PrincipalController.linkEditar.setLinkString(tfLink.getText().trim());
                    Conexao.atualizarLinkTemporario(PrincipalController.linkEditar);
                } else {
                    String link = "www." + tfLink.getText().trim();
                    PrincipalController.linkEditar.setLinkString(link);
                    Conexao.atualizarLinkTemporario(PrincipalController.linkEditar);
                }
                PrincipalController.getResultado().remove(0, PrincipalController.getResultado().size());
                PrincipalController.getResultado().addAll(Conexao.getLinksTemp());
                PrincipalController.tvLinksCompartilhado.refresh();
                //Notificacao.getNotificacaoEdit("O Link (Tempor??rio): "+tfTitulo.getText().trim()+" Foi Editado Com Sucesso!!");
            } else if (!cbTemp.isSelected() && !temp) { //SE O LINK FOR DA LISTA DE SALVOS E FOR CONTINUAR NA LISTA DE SALVOS EDITA NO BANCO E NA LISTA DO USUARIO 
                PrincipalController.linkEditar.setTituloString(tfTitulo.getText().trim());
                PrincipalController.linkEditar.setCategoriaString(tfCategoria.getText().trim());
                PrincipalController.linkEditar.setTagString(tfTag.getText().trim());
                if (tfLink.getText().contains("www") || tfLink.getText().contains("https://") || tfLink.getText().contains("http://")) {
                    PrincipalController.linkEditar.setLinkString(tfLink.getText().trim());
                    Conexao.atualizarLink(PrincipalController.linkEditar);
                } else {
                    String link = "www." + tfLink.getText().trim();
                    PrincipalController.linkEditar.setLinkString(link);
                    Conexao.atualizarLink(PrincipalController.linkEditar);
                }
                PrincipalController.getResultado().remove(0, PrincipalController.getResultado().size());
                PrincipalController.getResultado().addAll(Conexao.getLinks());
                PrincipalController.tvLinksCompartilhado.refresh();
                //Notificacao.getNotificacaoEdit("O Link (Salvo): "+tfTitulo.getText().trim()+" Foi Editado Com Sucesso!!");
            } else if (!cbTemp.isSelected() && temp) { //SE O LINK FOR DA LISTA DE TEMPORARIOS E FOR MUDAR PRA LISTA DE SALVOS
                int c2 = 0;
                for (Link l : Conexao.getLinksTemp()) {
                    if (l.getID().get() == PrincipalController.linkEditar.getID().get()) {
                        Conexao.deletarLinkTemporario(l);
                        getResultado().remove(0, PrincipalController.getResultado().size());
                        getResultado().addAll(Conexao.getLinksTemp());
                        break;
                    }
                    c2++;
                }
                if (tfLink.getText().contains("www") || tfLink.getText().contains("https://") || tfLink.getText().contains("http://")) {
                    Conexao.insertLink(new Link(1, tfTitulo.getText().trim(), tfLink.getText().trim(), tfCategoria.getText().trim(), tfTag.getText().trim()));
                } else {
                    String link = "www." + tfLink.getText().trim();
                    Conexao.insertLink(new Link(1, tfTitulo.getText().trim(), link, tfCategoria.getText().trim(), tfTag.getText().trim()));
                }
                PrincipalController.getResultado().remove(0, PrincipalController.getResultado().size());
                PrincipalController.getResultado().addAll(Conexao.getLinks());
                PrincipalController.tvLinksCompartilhado.refresh();
                //Notificacao.getNotificacaoEdit("O Link: "+tfTitulo.getText().trim()+" Foi Editado Pra (Salvo) Com Sucesso!!");
            } else if (cbTemp.isSelected() && !temp) { //SE O LINK FOR DA LISTA DE SALVOS E FOR MUDAR PRA LISTA DE TEMPORARIOS
                int c1 = 0;
                for (Link l : Conexao.getLinks()) {
                    if (l.getID().get() == PrincipalController.linkEditar.getID().get()) {
                        Conexao.deletarLink(l);
                        getResultado().remove(0, PrincipalController.getResultado().size());
                        getResultado().addAll(Conexao.getLinks());
                        break;
                    }
                    c1++;
                }
                if (tfLink.getText().contains("www") || tfLink.getText().contains("https://") || tfLink.getText().contains("http://")) {
                    Conexao.insertLinkTemporarios(new Link(1, tfTitulo.getText().trim(), tfLink.getText().trim(), tfCategoria.getText().trim(), tfTag.getText().trim(), dataAtual, dataAtual, 0));
                } else {
                    String link = "www." + tfLink.getText().trim();
                    Conexao.insertLinkTemporarios(new Link(1, tfTitulo.getText().trim(), link, tfCategoria.getText().trim(), tfTag.getText().trim(), dataAtual, dataAtual, 0));
                }
                PrincipalController.getResultado().remove(0, PrincipalController.getResultado().size());
                PrincipalController.getResultado().addAll(Conexao.getLinks());
                PrincipalController.tvLinksCompartilhado.refresh();
                //Notificacao.getNotificacaoEdit("O Link: "+tfTitulo.getText().trim()+" Foi Editado Pra (Tempor??rio) Com Sucesso!!");
            }
            Notificacao.getNotificacaoEdit("O Link Foi Editado Com Sucesso!!");
            tabelaPrincipal.getSelectionModel().select(null);
            Collections.sort(Conexao.getLinks(), new TituloComparator());
            tabelaPrincipal.refresh();
            PrincipalController.adicionarPrincipal.setDisable(false);
            PrincipalController.editarPrincipal.setDisable(true);
            PrincipalController.removerPrincipal.setDisable(true);
            PrincipalController.tfPesquisaPrincipal.setDisable(false);
            PrincipalController.ckbPrincipal.setDisable(false);
            aberto = false;
            PrincipalController.getJanela().close();
        }
    }

    public void monitor() {
        tfTitulo.textProperty().addListener(e -> {
            btnSalvar.setDisable(true);
            btnLimpar.setDisable(true);
            if (tfTitulo.getText().equals("") || tfLink.getText().equals("") || tfCategoria.getText().equals("") || tfTag.getText().equals("")) {
                btnSalvar.setDisable(true);
                btnLimpar.setDisable(false);
                if (tfTitulo.getText().equals("")) {
                    btnLimpar.setDisable(true);
                }
            } else {
                btnSalvar.setDisable(false);
                btnLimpar.setDisable(false);
            }
        });
        tfLink.textProperty().addListener(e -> {
            btnSalvar.setDisable(true);
            btnLimpar.setDisable(true);
            if (tfTitulo.getText().equals("") || tfLink.getText().equals("") || tfCategoria.getText().equals("") || tfTag.getText().equals("")) {
                btnSalvar.setDisable(true);
                btnLimpar.setDisable(false);
                if (tfLink.getText().equals("")) {
                    btnLimpar.setDisable(true);
                }
            } else {
                btnSalvar.setDisable(false);
                btnLimpar.setDisable(false);
            }
        });
        tfCategoria.textProperty().addListener(e -> {
            btnSalvar.setDisable(true);
            btnLimpar.setDisable(true);
            if (tfTitulo.getText().equals("") || tfLink.getText().equals("") || tfCategoria.getText().equals("") || tfTag.getText().equals("")) {
                btnSalvar.setDisable(true);
                btnLimpar.setDisable(false);
                if (tfCategoria.getText().equals("")) {
                    btnLimpar.setDisable(true);
                }
            } else {
                btnSalvar.setDisable(false);
                btnLimpar.setDisable(false);
            }
        });
        tfTag.textProperty().addListener(e -> {
            btnSalvar.setDisable(true);
            btnLimpar.setDisable(true);
            if (tfTitulo.getText().equals("") || tfLink.getText().equals("") || tfCategoria.getText().equals("") || tfTag.getText().equals("")) {
                btnSalvar.setDisable(true);
                btnLimpar.setDisable(false);
                if (tfTag.getText().equals("")) {
                    btnLimpar.setDisable(true);
                }
            } else {
                btnSalvar.setDisable(false);
                btnLimpar.setDisable(false);
            }
        });
    }

    public void carregarElementos() {
        //lbUser.setText(lbUser.getText() + " " + Conexao.getUsuarioAtual().getNome().get());
        if (PrincipalController.linkEditar.getLink().get().equals("")) {
            btnLimpar.setDisable(true);
            btnSalvar.setDisable(true);
            foiTerminada = false;
            //resume();
        } else {
            tfTitulo.setText(PrincipalController.linkEditar.getTitulo().get());
            tfLink.setText(PrincipalController.linkEditar.getLink().get());
            tfCategoria.setText(PrincipalController.linkEditar.getCategoria().get());
            tfTag.setText(PrincipalController.linkEditar.getTag().get());
            btnSalvar.setStyle("-fx-background-color: #1E90FF;");
            btnSalvar.setText("Salvar Edi????o");
            cbTemp.setSelected(temporario);
            btnLimpar.setDisable(false);
            stop();

        }
    }

    public void limpar() {
        tfTitulo.setText("");
        tfLink.setText("");
        tfCategoria.setText("");
        tfTag.setText("");
        cbTemp.setSelected(false);
    }

    public void pegarTitulo(String url) {
        InputStream response = null;
        Charset charset = Charset.forName("UTF8");
        String titulo2;
        try {

            URLConnection connection = new URL(url).openConnection();
            connection.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
            response = connection.getInputStream();

            //connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.155 Safari/537.36");
            Scanner scanner = new Scanner(response);
            String responseBody = scanner.useDelimiter("\\A").next();
            String titulo = responseBody.substring(responseBody.indexOf("<title") + responseBody.substring(responseBody.indexOf("<title")).indexOf(">"), responseBody.indexOf("</title>"));
            /*if(titulo.contains(">")){
                titulo.indexOf(>)
            }*/
            titulo2 = titulo.substring(1, titulo.length());
            tfTitulo.setText(titulo2);
        } catch (IOException ex) {
            //ex.printStackTrace();
            tfTitulo.setText(tituloLink.pegarTitulo(obj.detectar()));
        } catch (Exception ex) {
            tfTitulo.setText(tituloLink.pegarTitulo(obj.detectar()));
            //tfTitulo.setText("Erro ao pegar titulo autom??tico");
            //System.out.println("N??o foi capaz de pegar o titulo");
        } finally {
            try {
                response.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
