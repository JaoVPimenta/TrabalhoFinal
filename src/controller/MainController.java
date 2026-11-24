package controller;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;

public class MainController {

    private static final String PASTA_VIEWS = "/view/";

    @FXML
    private BorderPane mainPane;

    @FXML
    private void abrirFuncionarios() {
        carregarTela("FuncionarioView.fxml");
    }

    @FXML
    private void abrirUniformes() {
        carregarTela("UniformeView.fxml");
    }

    @FXML
    private void abrirProducao() {
        carregarTela("ProducaoView.fxml");
    }

    @FXML
    private void abrirProdutividade() {
        mostrarEmDesenvolvimento("Relatório de Produtividade");
    }

    @FXML
    private void abrirCatalogo() {
        mostrarEmDesenvolvimento("Catálogo de Uniformes");
    }

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

    private void mostrarEmDesenvolvimento(String nomeRelatorio) {
        Alert alerta = new Alert(AlertType.INFORMATION);
        alerta.setTitle("Em desenvolvimento");
        alerta.setHeaderText(null);
        alerta.setContentText(nomeRelatorio + " ainda não foi implementado.");
        alerta.showAndWait();
    }
}