/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.ImageView;

/**
 *
 * @author Znoque
 */
public class Conexao {

    static final String driver = "org.h2.Driver"; //DRIVER DE CONEXÃO RESPONSÁVEL POR IDENTIFICAR O SERVIÇO DO BANCO DE DADOS
    static final String caminho = "jdbc:h2:~/DatabaseH2/Links"; //ENDEREÇO DO BD, RESPONSÁVEL POR SETAR O LOCAL DO BANCO DE DADOS
    static final String usuario = "root"; //usuario do banco
    static final String senha = ""; //senha do banco
    private static Pessoa usuarioAtual = null;
    private static ObservableList<Link> linksTemp = FXCollections.observableArrayList();
    private static int contId = 1;

    public static boolean login(String user, String pass) {
        if (user.equals(usuario) && pass.equals(senha)) {
            usuarioAtual = new Pessoa(1, usuario, senha);
            pegarLinks();
            return true;
        } else {
            System.exit(0);
        }
        return false;
    }

    public static void criarTabelas() {
        Connection conn = null;
        Statement stmt = null;

        try {
            // PASSO 1: Registrar o driver JDBC 
            Class.forName(driver);

            //PASSO 2: Abra uma conexão 
            System.out.println("Conectando ao banco de dados...");
            conn = DriverManager.getConnection(caminho, usuario, senha);
            System.out.println("Conectado com sucesso!!");

            //PASSO 3: Execute uma requisição (query) 
            System.out.println("Criando tabela no banco de dados...");
            stmt = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS   links "
                    + "(id INTEGER not NULL AUTO_INCREMENT, "
                    + " titulo VARCHAR(255), "
                    + " link VARCHAR(255), "
                    + " categoria VARCHAR(255), "
                    + " tag VARCHAR(255), "
                    + " PRIMARY KEY ( id ))";
            stmt.executeUpdate(sql);
            System.out.println("Tabela criada no banco de dados com sucesso!!");

            // PASSO 4: Ambiente de limpeza 
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            //Lidar com erros para JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Lidar com erros para Class.forName 
            e.printStackTrace();
        } finally {
            //bloco usado para fechar recursos
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
            } // nada que possamos fazer 
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            } //fim do finally 
        } //fim do try 
    }

    public static void insertLink(Link l) {
        String inserir = "INSERT INTO links VALUES (default, '" + l.getTitulo().get() + "', '" + l.getLink().get() + "', '" + l.getCategoria().get() + "', '" + l.getTag().get() + "')";
        Connection conn = null;
        Statement stmt = null;
        try {
            // PASSO 1: Registrar o driver JDBC 
            Class.forName(driver);

            //PASSO 2: Abra uma conexão
            System.out.println("Conectando ao banco de dados...");
            conn = DriverManager.getConnection(caminho, usuario, senha);
            System.out.println("Conectado com sucesso!!");

            //PASSO 3: Execute uma requisição (query) 
            stmt = conn.createStatement();
            stmt.executeUpdate(inserir);
            System.out.println("Novo link adicionado com sucesso!!");
            Notificacao.getNotificacaoAdd(l.getTitulo().get());

            // PASSO 4: Ambiente de limpeza
            pegarUltimo(l);
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            //Lidar com erros para JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Lidar com erros para Class.forName  
            e.printStackTrace();
        } finally {
            //bloco usado para fechar recursos
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
            } // nada que possamos fazer 
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            } //fim do finally  
        } //fim do try 
    }

    public static void pegarLinks() {
        int tamanho = usuarioAtual.getLinks().size();
        Connection conn = null;
        Statement stmt = null;
        try {
            // PASSO 1: Registrar o driver JDBC 
            Class.forName(driver);

            // PASSO 2: Abra uma conexão
            System.out.println("Conectando ao banco de dados...");
            conn = DriverManager.getConnection(caminho, usuario, senha);
            System.out.println("Conectado com sucesso!!");

            //PASSO 3: Execute uma requisição (query) 
            stmt = conn.createStatement();
            String sql = "SELECT * FROM links order by id";
            ResultSet rs = stmt.executeQuery(sql);
            usuarioAtual.getLinks().remove(0, tamanho);

            // PASSO 4: Extrair dados do conjunto de resultados 
            while (rs.next()) {
                usuarioAtual.getLinks().add(new Link(rs.getInt("id"), rs.getString("titulo"), rs.getString("link"), rs.getString("categoria"), rs.getString("tag")));
            }

            // PASSO 5: Ambiente de limpeza
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            //Lidar com erros para JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Lidar com erros para Class.forName  
            e.printStackTrace();
        } finally {
            //bloco usado para fechar recursos
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
            } // nada que possamos fazer 
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            } //fim do finally  
        } //fim do try 
    }

    public static void deletarLink(Link l) {
        Connection conn = null;
        Statement stmt = null;
        int c = 0;
        String sql = "DELETE FROM links WHERE id = " + l.getID().get() + "";
        try {
            // PASSO 1: Registrar o driver JDBC 
            Class.forName(driver);

            // PASSO 2: Abra uma conexão
            System.out.println("Conectando ao banco de dados...");
            conn = DriverManager.getConnection(caminho, usuario, senha);
            System.out.println("Conectado com sucesso!!");

            //PASSO 3: Execute uma requisição (query) 
            stmt = conn.createStatement();
            System.out.println("Tentando remover link do banco de dados...");
            stmt.executeUpdate(sql);
            System.out.println("Link removido com sucesso do banco de dados!!");
            for (Link link : usuarioAtual.getLinks()) {
                if (link.getID().get() == l.getID().get()) {
                    usuarioAtual.getLinks().remove(c);
                    break;
                }
                c++;
            }
            Notificacao.getNotificacaoRemove(l.getTitulo().get());
            System.out.println("Link removido com sucesso da tabela!!");

            // PASSO 4: Ambiente de limpeza
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            //Lidar com erros para JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Lidar com erros para Class.forName  
            e.printStackTrace();
        } finally {
            //bloco usado para fechar recursos
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
            } // nada que possamos fazer 
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            } //fim do finally  
        } //fim do try 
    }

    public static void atualizarLink(Link l) {
        ImageView image = null;
        Connection conn = null;
        Statement stmt = null;
        Link linkAntigo = null;
        int c = 0;
        String sql = "UPDATE links " + "SET titulo = '" + l.getTitulo().get() + "', link = '" + l.getLink().get() + "', categoria = '" + l.getCategoria().get() + "', tag = '" + l.getTag().get() + "' WHERE id in (" + l.getID().get() + ")";
        try {
            // PASSO 1: Registrar o driver JDBC 
            Class.forName(driver);

            // PASSO 2: Abra uma conexão
            System.out.println("Conectando ao banco de dados...");
            conn = DriverManager.getConnection(caminho, usuario, senha);
            System.out.println("Conectado com sucesso!!");

            //PASSO 3: Execute uma requisição (query)  
            stmt = conn.createStatement();
            System.out.println("Tentando atualizar link no banco de dados...");
            stmt.executeUpdate(sql);
            System.out.println("Link atualizado com sucesso no banco de dados...");
            for (Link link : usuarioAtual.getLinks()) {
                if (link.getID().get() == l.getID().get()) {
                    linkAntigo = link;
                    usuarioAtual.getLinks().get(c).setTituloString(l.getTitulo().get());
                    usuarioAtual.getLinks().get(c).setLinkString(l.getLink().get());
                    usuarioAtual.getLinks().get(c).setCategoriaString(l.getCategoria().get());
                    usuarioAtual.getLinks().get(c).setTagString(l.getTag().get());
                    image = pegarIcone(l.getLink().get());
                    usuarioAtual.getLinks().get(c).setIcone(image);
                    break;
                }
                c++;
            }
            System.out.println("Link atualizado com sucesso na tabela...");

            // PASSO 4: Ambiente de limpeza
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            //Lidar com erros para JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Lidar com erros para Class.forName  
            e.printStackTrace();
        } finally {
            //bloco usado para fechar recursos
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
            } // nada que possamos fazer 
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            } //fim do finally  
        } //fim do try 
    }

    //PEGA O ÚLTIMO LINK ADICIONADO
    public static void pegarUltimo(Link l) {
        ImageView image = null;
        int tamanho = usuarioAtual.getLinks().size();
        Connection conn = null;
        Statement stmt = null;
        try {
            // PASSO 1: Registrar o driver JDBC 
            Class.forName(driver);

            // PASSO 2: Abra uma conexão
            System.out.println("Conectando ao banco de dados...");
            conn = DriverManager.getConnection(caminho, usuario, senha);
            System.out.println("Conectado com sucesso!!");

            //PASSO 3: Execute uma requisição (query)  
            stmt = conn.createStatement();
            String sql = "SELECT * FROM links where link='" + l.getLink().get() + "'";
            ResultSet rs = stmt.executeQuery(sql);

            // PASSO 4: Extrair dados do conjunto de resultados  
            while (rs.next()) {
                image = pegarIcone(rs.getString("link"));
                usuarioAtual.getLinks().add(new Link(rs.getInt("id"), rs.getString("titulo"), rs.getString("link"), rs.getString("categoria"), rs.getString("tag"), image));
            }

            // PASSO 5: Ambiente de limpeza 
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            //Lidar com erros para JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Lidar com erros para Class.forName  
            e.printStackTrace();
        } finally {
            //bloco usado para fechar recursos
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
            } // nada que possamos fazer 
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            } //fim do finally  
        } //fim do try 
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

    /**
     * @return the usuarioAtual
     */
    public static Pessoa getUsuarioAtual() {
        return usuarioAtual;
    }

    /**
     * @return the linksTemp
     */
    public static ObservableList<Link> getLinksTemp() {
        return linksTemp;
    }

    /**
     * @return the contId
     */
    public static int getContId() {
        return contId;
    }

    /**
     * @param aContId the contId to set
     */
    public static void setContId(int aContId) {
        contId += aContId;
    }
    
    public static void ZeraContId(){
        contId = 0;
    }
}
