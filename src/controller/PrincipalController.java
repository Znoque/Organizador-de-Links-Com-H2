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
import java.net.URI;
import java.net.URL;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.stream.Collectors.toList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
import model.Pessoa;
import model.TituloComparator;

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

    public static Button adicionarPrincipal;
    public static Button removerPrincipal;
    public static Button editarPrincipal;
    private static ObservableList<Link> resultado = FXCollections.observableArrayList();
    public static Link linkEditar = new Link(0, "", "", "", "");
    private static Stage janela = new Stage();
    DetectarCopiaBy4Java obj = new DetectarCopiaBy4Java();
    public static String copiado = "";
    public static String titulo = "";

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //INICIALIZANDO OBEJETOS E COMPONENTES
        tvLinksCompartilhado = tvLinks;
        adicionarPrincipal = btnAdicionar;
        editarPrincipal = btnEditar;
        removerPrincipal = btnRemover;
        
        tBtn.setOnAction(e -> {
            if(tBtn.getText().equals("Links Salvos")){
                tBtn.setText("Links Temporários");
                tBtn.setStyle("-fx-background-color: #FF4500;");
                tvLinks.setItems(Conexao.getLinksTemp());
            }else{
                tBtn.setText("Links Salvos");
                tBtn.setStyle("-fx-background-color: #FF8000;");
                tvLinks.setItems(Conexao.getUsuarioAtual().getLinks());
            }
        });
        
        carregarTabela(Conexao.getUsuarioAtual());
        getResultado().addAll(Conexao.getUsuarioAtual().getLinks());
        lbUsuario.setText(lbUsuario.getText() + " " + Conexao.getUsuarioAtual().getNome().get());
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

        //CARREGAMENTO DOS ICONES DOS LINKS NA TABELA
        new Thread(() -> {
            int cont = 0;
            for (Link link : Conexao.getUsuarioAtual().getLinks()) {
                ImageView imagem = pegarIcone(link.getLink().get());
                Conexao.getUsuarioAtual().getLinks().get(cont).setIcone(imagem);
                tvLinks.refresh();
                cont++;
            }
        }).start();

    }

    public void carregarTabela(Pessoa p) {
        tcTitulo.setCellValueFactory(value -> value.getValue().getTitulo());
        tcLink.setCellValueFactory(value -> value.getValue().getLink());
        tcCategoria.setCellValueFactory(value -> value.getValue().getCategoria());
        tcTag.setCellValueFactory(value -> value.getValue().getTag());
        tcIcon.setCellValueFactory(new PropertyValueFactory<>("icone"));
        Collections.sort(p.getLinks(), new TituloComparator());
        tvLinks.setItems(p.getLinks());
    }

    public void SetBtns() {

        //COLOCANDO CURSOR DE MÃOS NOS BOTÕES
        btnAdicionar.setCursor(Cursor.HAND);
        btnEditar.setCursor(Cursor.HAND);
        //btnPesquisa.setCursor(Cursor.HAND);
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
        tvLinks.getSelectionModel().select(null);
        btnEditar.setDisable(true);
        btnRemover.setDisable(true);
        btnAdicionar.setDisable(false);
        Collections.sort(Conexao.getUsuarioAtual().getLinks(), new TituloComparator());
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
                //estaSuspensa = true;
                CadastroController.foiTerminada = true;
                CadastroController.aberto = false;
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
            getResultado().addAll(Conexao.getUsuarioAtual().getLinks());
            limpar();
        } else {
            int c=0;
            for (Link link : Conexao.getLinksTemp()) {
                if (link.getID().get() == l.getID().get()) {
                    Conexao.getLinksTemp().remove(c);
                    break;
                }
                c++;
            }
            limpar();
            tvLinks.refresh();
            
        }
        Collections.sort(Conexao.getUsuarioAtual().getLinks(), new TituloComparator());
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
}
