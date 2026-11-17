package controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class MainController {


    private static final String PASTA_VIEWS = "/view/";



    @FXML
    private void abrirFuncionarios() {
        abrirTela("FuncionarioView.fxml", "Cadastro de Funcionários");
    }

    @FXML
    private void abrirUniformes() {
        abrirTela("UniformeView.fxml", "Cadastro de Uniformes");
    }



    @FXML
    private void abrirProducao() {
        abrirTela("ProducaoView.fxml", "Registro de Produção");
    }



    @FXML
    private void abrirProdutividade() {
        mostrarEmDesenvolvimento("Relatório de Produtividade");
    }

    @FXML
    private void abrirCatalogo() {
        mostrarEmDesenvolvimento("Catálogo de Uniformes");
    }


    private void abrirTela(String arquivoFxml, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(PASTA_VIEWS + arquivoFxml));
            Parent raiz = loader.load();

            Stage janela = new Stage();
            janela.setTitle(titulo);
            janela.setScene(new Scene(raiz));
            janela.setResizable(false);
            janela.initModality(Modality.APPLICATION_MODAL);
            janela.showAndWait();

        } catch (IOException e) {
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