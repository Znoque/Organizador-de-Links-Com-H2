/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import static controller.CadastroController.tabelaPrincipal;
import static controller.CadastroController.estaSuspensa;
import static controller.CadastroController.foiTerminada;
import detectarcopiaby4java.DetectarCopiaBy4Java;
import java.awt.Desktop;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.stream.Collectors.toList;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.Login;
import main.Principal;
import model.Alerta;
import model.Conexao;
import model.Link;
import model.Notificacao;
import model.Pessoa;
import model.TituloComparator;
import model.TituloDoLink;

/**
 * FXML Controller class
 *
 * @author Znoque
 */
public class PrincipalController implements Initializable {

    @FXML
    private Button btnTrocar;
    @FXML
    private Label lbUsuario;
    @FXML
    private TableView<Link> tvLinks;
    public static TableView<Link> tvLinksCompartilhado;
    @FXML
    private TableColumn<Link, String> tcIcon;
    @FXML
    private TableColumn<Link, String> tcTitulo;
    @FXML
    private TableColumn<Link, String> tcLink;
    @FXML
    private TableColumn<Link, String> tcCategoria;
    @FXML
    private TableColumn<Link, String> tcTag;
    @FXML
    private TextField tfPesquisa;
    @FXML
    private Button btnAdicionar;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnRemover;
    @FXML
    private ToggleButton tBtn;
    @FXML
    private CheckBox ckbAutomatico;

    public static Button adicionarPrincipal;
    public static Button removerPrincipal;
    public static Button editarPrincipal;
    public static TextField tfPesquisaPrincipal;
    public static CheckBox ckbPrincipal;
    private static ObservableList<Link> resultado = FXCollections.observableArrayList();
    public static Link linkEditar = new Link(0, "", "", "", "");
    public static Link linkTitulo = new Link(0, "", "", "", "");
    private static Stage janela = new Stage();
    DetectarCopiaBy4Java obj = new DetectarCopiaBy4Java();
    public static String copiado = "";
    public static String titulo = "";
    public static String titulo2 = "";
    public static boolean fecharT1 = false;
    public static boolean selecionado = false;
    TituloDoLink tituloLink = new TituloDoLink();
    String titulotemp = "";

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Conexao.criarTabelas();
        Conexao.pegarLinks();
        Conexao.pegarLinksTemporarios();
        
        //INICIALIZANDO OBEJETOS E COMPONENTES
        tvLinksCompartilhado = tvLinks;
        adicionarPrincipal = btnAdicionar;
        editarPrincipal = btnEditar;
        removerPrincipal = btnRemover;
        tfPesquisaPrincipal = tfPesquisa;
        ckbPrincipal = ckbAutomatico;
                
        tfPesquisa.setDisable(true);
        ckbAutomatico.setDisable(true);
        
        tBtn.setOnAction(e -> botaoAlternar());
        
        carregarTabela();
        //getResultado().addAll(Conexao.getLinks());
        //lbUsuario.setText(lbUsuario.getText() + " " + Conexao.getUsuarioAtual().getNome().get());
        SetBtns();

        //AÇÃO DOS BOTÕES
        btnTrocar.setOnAction(value -> trocar());
        btnAdicionar.setOnMouseClicked(e -> cadastro("Adicionar"));
        btnEditar.setOnMouseClicked(e -> cadastro("Editar"));
        btnRemover.setOnAction(e -> deletar(tvLinks.getSelectionModel().getSelectedItem()));

        //AÇÃO DE CLIQUE SOBRE A TABELA
        tvLinks.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                abrirLink(tvLinks.getSelectionModel().getSelectedItem().getLink().get());
                limpar();
            } else {
                try {
                    btnEditar.setDisable(false);
                    btnRemover.setDisable(false);
                    btnAdicionar.setDisable(true);

                    linkEditar.setID(tvLinks.getSelectionModel().getSelectedItem().getID());
                    linkEditar.setTituloString(tvLinks.getSelectionModel().getSelectedItem().getTitulo().get());
                    linkEditar.setLinkString(tvLinks.getSelectionModel().getSelectedItem().getLink().get());
                    linkEditar.setCategoriaString(tvLinks.getSelectionModel().getSelectedItem().getCategoria().get());
                    linkEditar.setTagString(tvLinks.getSelectionModel().getSelectedItem().getTag().get());
                } catch (NullPointerException ex) {
                    btnEditar.setDisable(true);
                    btnRemover.setDisable(true);
                    btnAdicionar.setDisable(false);
                }
                e.consume();
            }
        });
        
        //FILTRO DE PESQUISA
        tfPesquisa.setOnKeyReleased(obj -> {
            tvLinks.getItems().clear();
            tvLinks.getItems().addAll(getResultado().stream().filter((Link item) -> {
                if (item.getTitulo().get().toLowerCase().contains(tfPesquisa.getText().toLowerCase())) {
                    return true;
                }
                if (item.getLink().get().toLowerCase().contains(tfPesquisa.getText().toLowerCase())) {
                    return true;
                }
                if (item.getCategoria().get().toLowerCase().contains(tfPesquisa.getText().toLowerCase())) {
                    return true;
                }
                if (item.getTag().get().toLowerCase().contains(tfPesquisa.getText().toLowerCase())) {
                    return true;
                }
                return false;
            }).collect(toList()));
        });

        //THREAD RESPONSAVEL POR CARREGAR OS ICONES DOS LINKS NA INICIALIZAÇÃO
        new Thread(() -> {
            int cont = 0;
            int cont2 = 0;
            for (Link link : Conexao.getLinks()) {
                ImageView imagem = pegarIcone(link.getLink().get());
                Conexao.getLinks().get(cont).setIcone(imagem);
                tvLinks.refresh();
                cont++;
            }
            for (Link link : Conexao.getLinksTemp()) {
                ImageView imagem = pegarIcone(link.getLink().get());
                Conexao.getLinksTemp().get(cont2).setIcone(imagem);
                tvLinks.refresh();
                cont2++;
            }
            tfPesquisa.setDisable(false);
            ckbAutomatico.setDisable(false);
            getResultado().addAll(Conexao.getLinks());
        }).start();
        //FIM DA THREAD
        
        //THREAD RESPONSAVEL POR MUDAR A COR DO CHECKBOX QUANDO SELECIONADO
        Thread t1 = new Thread(() -> {
            while (!fecharT1) {
                try {
                    if (ckbAutomatico.isSelected()) {
                        selecionado = true;
                        ckbAutomatico.setStyle("-fx-text-fill: #00ff00");
                    } else {
                        selecionado = false;
                        ckbAutomatico.setStyle("-fx-text-fill: #ffffff");
                    }
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t1.start();
        //FIM DA THREAD
        
        //THREAD RESPONSAVEL POR ADICIONAR OS LINKS AUTOMÁTICAMENTE
        Thread auto = new Thread(() -> {
            while (!fecharT1) {
                try {
                    if(selecionado){
                        //System.out.println("selecionado");
                        //ADICIONAR LINKS AUTOMÁTICAMENTE
                        if (obj.detectar().contains("www") || obj.detectar().contains("https://") || obj.detectar().contains("http://")) {
                            if (copiado.equals(obj.detectar())) {
                                System.out.println("Já adicionado");
                            } else {
                                copiado = obj.detectar();
                                titulotemp = tituloLink.pegarTitulo(copiado);
                                ImageView icone = pegarIcone(copiado);
                                Platform.runLater(() -> {
                                    Conexao.insertLinkTemporarios(new Link(0, titulotemp, copiado, "", ""));
                                    Notificacao.getNotificacaoAdd(titulotemp);
                                });
                            }
                        }
                    } else{
                        //System.out.println("Não Selecionado");
                    }
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        auto.start();
        //FIM DA THREAD T1
        
        //THREAD RESPONSAVEL POR EDITAR O TITULO DOS LINKS TEMPORARIOS
        Thread autoTitulo = new Thread(() -> {
            String t = "";
            String novoTitulo = "";
            while (!fecharT1) {
                try {
                    for(Link l: Conexao.getLinksTemp()){
                        if(l.getTag().get().equals("")){
                            t = l.getTitulo().get();
                            try{
                                novoTitulo = editarTitulo(l.getLink().get());
                                linkTitulo = new Link(l.getID().get(), novoTitulo, l.getLink().get(), l.getCategoria().get(), l.getTitulo().get());
                                Conexao.atualizarLinkTemporario(linkTitulo);
                                tvLinks.refresh();
                            } catch(NullPointerException ex){
                                linkTitulo = new Link(l.getID().get(), l.getTitulo().get(), l.getLink().get(), l.getCategoria().get(), l.getTitulo().get());
                                Conexao.atualizarLinkTemporario(linkTitulo);
                                tvLinks.refresh();
                            }
                            System.out.println(novoTitulo);
                        }
                    }
                    Thread.sleep(30000);
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (foiTerminada) {
                return;
            }
        });
        autoTitulo.start();
        //FIM DA THREAD T1
        
        //THREAD RESPONSAVEL POR HABILITAR E DESABILITAR O BOTÃO QUE ALTERNA ENTRE OS LINKS SALVOS E TEMPORARIOS
        Thread t2 = new Thread(() -> {
            while (!fecharT1) {
                try {
                    if (!tfPesquisa.getText().equals("")) {
                        tBtn.setDisable(true);
                    } else {
                        tBtn.setDisable(false);
                    }
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t2.start();
        //FIM DA THREAD T2
    }

    public void carregarTabela() {
        tcTitulo.setCellValueFactory(value -> value.getValue().getTitulo());
        tcLink.setCellValueFactory(value -> value.getValue().getLink());
        tcCategoria.setCellValueFactory(value -> value.getValue().getCategoria());
        tcTag.setCellValueFactory(value -> value.getValue().getTag());
        tcIcon.setCellValueFactory(new PropertyValueFactory<>("icone"));
        Collections.sort(Conexao.getLinks(), new TituloComparator());
        Collections.sort(Conexao.getLinksTemp(), new TituloComparator());
        tvLinks.setItems(Conexao.getLinks());
    }

    public void SetBtns() {

        //COLOCANDO CURSOR DE MÃOS NOS BOTÕES
        btnAdicionar.setCursor(Cursor.HAND);
        btnEditar.setCursor(Cursor.HAND);
        btnTrocar.setCursor(Cursor.HAND);
        btnRemover.setCursor(Cursor.HAND);

        //DESABILITANDO BOTÕES DE EDITAR E EXCLUIR
        btnEditar.setDisable(true);
        btnRemover.setDisable(true);
    }

    public void abrirLink(String url) {
        Desktop d = Desktop.getDesktop();
        try {
            d.browse(new URI(url));
        } catch (Exception e) {
            Alerta.getAlertaErro("Erro Ao Abrir Link", "Erro Ao Abrir Link", "Não Foi Possivel Abrir o Link, Verifique o Link Ou a Sua Conexão Com a Internet");
        }
    }

    public void trocar() {
        Conexao.getLinksTemp().remove(0, Conexao.getLinksTemp().size());
        //Conexao.ZeraContId();
        Login m = new Login();
        try {
            m.start(new Stage());
            getJanela().close();
            CadastroController.aberto = false;
            Principal.getStage().close();

        } catch (Exception e) {
            Alerta.getAletaTrocar();
        }
    }

    public void limpar() {
        tfPesquisa.setText("");
        tvLinks.getSelectionModel().select(null);
        btnEditar.setDisable(true);
        btnRemover.setDisable(true);
        btnAdicionar.setDisable(false);
        Collections.sort(Conexao.getLinks(), new TituloComparator());
        Collections.sort(resultado, new TituloComparator());
    }

    public void cadastro(String s) {
        if (CadastroController.aberto) {
            Alerta.getAlertaAviso("Janela Aberta", "A Janela já está aberta, feche antes de abrir outra");
            return;
        }

        try {
            if (s.equals("Adicionar")) {
                linkEditar = new Link(0, "", "", "", "");
                titulo = "Tela de Cadastro";
            } else {
                titulo = "Tela de Edição";
                tabelaPrincipal = tvLinks;
                if(!tBtn.getText().equals("Links Salvos")){
                    CadastroController.temporario = true;
                }else{
                     CadastroController.temporario = false;
                }
                
            }
            tfPesquisa.setDisable(true);
            ckbAutomatico.setDisable(true);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/Cadastro.fxml"));
            AnchorPane layout = loader.load();
            Scene cena = new Scene(layout);
            Image icon = new Image(getClass().getResourceAsStream("/resource/icone-url.png"));
            getJanela().getIcons().add(icon);
            getJanela().setTitle(titulo);
            getJanela().setScene(cena);
            getJanela().show();
            CadastroController.aberto = true;

            getJanela().setOnCloseRequest(item2 -> {
                CadastroController.foiTerminada = true;
                CadastroController.aberto = false;
                ckbAutomatico.setDisable(false);
                tfPesquisa.setDisable(false);
                limpar();
            });
        } catch (IOException ex) {
            Logger.getLogger(PrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deletar(Link l) {
        if(tBtn.getText().equals("Links Salvos")){
            Conexao.deletarLink(l);
            getResultado().remove(0, PrincipalController.getResultado().size());
            getResultado().addAll(Conexao.getLinks());
            //Notificacao.getNotificacaoRemove(l.getTitulo().get());
            limpar();
        } else {
            Conexao.deletarLinkTemporario(l);
            getResultado().remove(0, PrincipalController.getResultado().size());
            getResultado().addAll(Conexao.getLinksTemp());
            //Notificacao.getNotificacaoRemove(l.getTitulo().get());
            limpar();
        }
        Notificacao.getNotificacaoRemove("Link");
        Collections.sort(Conexao.getLinks(), new TituloComparator());
        Collections.sort(Conexao.getLinksTemp(), new TituloComparator());
    }
    
    public void botaoAlternar(){
        if(tBtn.getText().equals("Links Salvos")){
            tBtn.setText("Links Temporários");
            tBtn.setStyle("-fx-background-color: #FF4500;");
            resultado.remove(0, resultado.size());
            resultado.addAll(Conexao.getLinksTemp());
            tvLinks.setItems(Conexao.getLinksTemp());
        }else{
            tBtn.setText("Links Salvos");
            tBtn.setStyle("-fx-background-color: #FF8000;");
            resultado.remove(0, resultado.size());
            resultado.addAll(Conexao.getLinks());
            tvLinks.setItems(Conexao.getLinks());
        }
        limpar();
    }

    public static ImageView pegarIcone(String link) {
        link += " ";
        String url = "https://www.google.com/s2/favicons?domain=";
        if (link.contains("www.") || link.contains("https:") || link.contains("http:") || link.contains(".com")) {
            int a = link.indexOf(" ");
            StringBuilder sb = new StringBuilder(link.trim());
            sb.delete(a, link.length());
            url += sb.toString();
        }
        ImageView novaImagem = new ImageView(url);
        return novaImagem;
    }

    public void cadastroCopiado() {
        System.out.println("Cadastro novo");
    }

    /**
     * @return the janela
     */
    public static Stage getJanela() {
        return janela;
    }

    /**
     * @return the resultado
     */
    public static ObservableList<Link> getResultado() {
        return resultado;
    }
    
    public String editarTitulo(String url) {
        InputStream response = null;
        Charset charset = Charset.forName("UTF8");
        try {

            URLConnection connection = new URL(url).openConnection();
            connection.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
            response = connection.getInputStream();

            //connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.155 Safari/537.36");
            Scanner scanner = new Scanner(response);
            String responseBody = scanner.useDelimiter("\\A").next();
            String titulo = responseBody.substring(responseBody.indexOf("<title") + responseBody.substring(responseBody.indexOf("<title")).indexOf(">"), responseBody.indexOf("</title>"));
            titulo2 = titulo.substring(1, titulo.length());
        } catch (IOException ex) {
            //ex.printStackTrace();
            titulo2 = tituloLink.pegarTitulo(obj.detectar());
        } catch (Exception ex) {
            titulo2 = tituloLink.pegarTitulo(obj.detectar());
            //tfTitulo.setText("Erro ao pegar titulo automático");
            System.out.println("Não foi capaz de pegar o titulo");
        } finally {
            try {
                response.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return titulo2;
    }
}
