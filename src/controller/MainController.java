package controller;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;

/**
 * Controlador da janela principal ({@code MainView.fxml}).
 * <p>
 * Gerencia a navegação do sistema: cada opção do menu carrega uma tela
 * (arquivo FXML) na região central do {@link BorderPane} principal.
 * </p>
 */
public class MainController {

    /** Pasta (no classpath) onde ficam os arquivos FXML das telas. */
    private static final String PASTA_VIEWS = "/view/";

    /** Painel principal; sua região central exibe a tela ativa. */
    @FXML
    private BorderPane mainPane;

    /** Abre a tela de cadastro de funcionários. */
    @FXML
    private void abrirFuncionarios() {
        carregarTela("FuncionarioView.fxml");
    }

    /** Abre a tela de cadastro de uniformes. */
    @FXML
    private void abrirUniformes() {
        carregarTela("UniformeView.fxml");
    }

    /** Abre a tela de registro de produção. */
    @FXML
    private void abrirProducao() {
        carregarTela("ProducaoView.fxml");
    }

    /** Exibe aviso de que o Relatório de Produtividade ainda não foi implementado. */
    @FXML
    private void abrirProdutividade() {
        mostrarEmDesenvolvimento("Relatório de Produtividade");
    }

    /** Exibe aviso de que o Catálogo de Uniformes ainda não foi implementado. */
    @FXML
    private void abrirCatalogo() {
        mostrarEmDesenvolvimento("Catálogo de Uniformes");
    }

    /**
     * Carrega um arquivo FXML da pasta de views e o exibe no centro do painel principal.
     * <p>
     * Caso o arquivo não seja encontrado ou não possa ser carregado
     * ({@link IOException} ou {@link NullPointerException}), exibe um alerta de erro.
     * </p>
     *
     * @param arquivoFxml nome do arquivo FXML (ex.: {@code "FuncionarioView.fxml"})
     */
    private void carregarTela(String arquivoFxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(PASTA_VIEWS + arquivoFxml));
            Parent raiz = loader.load();

            mainPane.setCenter(raiz);

        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            Alert alerta = new Alert(AlertType.ERROR);
            alerta.setTitle("Erro");
            alerta.setHeaderText(null);
            alerta.setContentText("Não foi possível abrir a tela " + arquivoFxml
                    + ".\nVerifique o caminho da pasta de views no MainController.\n\n" + e.getMessage());
            alerta.showAndWait();
        }
    }

    /**
     * Exibe um alerta informando que um recurso ainda não foi implementado.
     *
     * @param nomeRelatorio nome do recurso a ser citado na mensagem
     */
    private void mostrarEmDesenvolvimento(String nomeRelatorio) {
        Alert alerta = new Alert(AlertType.INFORMATION);
        alerta.setTitle("Em desenvolvimento");
        alerta.setHeaderText(null);
        alerta.setContentText(nomeRelatorio + " ainda não foi implementado.");
        alerta.showAndWait();
    }
}