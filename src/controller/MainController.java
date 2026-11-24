package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import javafx.scene.control.ButtonBar;
import service.RelatorioService;

public class MainController {

    private static final String PASTA_VIEWS = "/view/";

    @FXML
    private BorderPane mainPane;

    private final RelatorioService relatorioService = new RelatorioService();

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
        Optional<Pair<Integer, Integer>> periodo = solicitarMesAno();
        if (periodo.isEmpty()) {
            return;
        }

        int mes = periodo.get().getKey();
        int ano = periodo.get().getValue();

        try {
            relatorioService.gerarProdutividadeMensal(mes, ano);
        } catch (Exception e) {
            e.printStackTrace();
            mostrarErro("Erro ao abrir o Relatório de Produtividade", e.getMessage());
        }
    }

    @FXML
    private void abrirCatalogo() {
        try {
            relatorioService.gerarCatalogoUniformes();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarErro("Erro ao abrir o Catálogo de Uniformes", e.getMessage());
        }
    }

    private Optional<Pair<Integer, Integer>> solicitarMesAno() {
        Dialog<Pair<Integer, Integer>> dialog = new Dialog<>();
        dialog.setTitle("Produtividade Mensal");
        dialog.setHeaderText("Informe o mês e o ano de referência");

        ButtonType btnGerar = new ButtonType("Gerar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnGerar, ButtonType.CANCEL);

        ComboBox<Integer> cbMes = new ComboBox<>(FXCollections.observableArrayList(
                1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12));
        cbMes.setValue(LocalDate.now().getMonthValue());

        TextField txtAno = new TextField(String.valueOf(LocalDate.now().getYear()));

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Mês (1-12):"), 0, 0);
        grid.add(cbMes, 1, 0);
        grid.add(new Label("Ano:"), 0, 1);
        grid.add(txtAno, 1, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(botao -> {
            if (botao == btnGerar) {
                Integer mes = cbMes.getValue();
                try {
                    int ano = Integer.parseInt(txtAno.getText().trim());
                    if (mes != null && ano >= 1900 && ano <= 2100) {
                        return new Pair<>(mes, ano);
                    }
                } catch (NumberFormatException ignored) {
                }
                mostrarErro("Valor inválido", "Informe um mês válido e um ano numérico válido.");
            }
            return null;
        });

        return dialog.showAndWait();
    }

    private void carregarTela(String arquivoFxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(PASTA_VIEWS + arquivoFxml));
            Parent raiz = loader.load();

            mainPane.setCenter(raiz);

        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            mostrarErro("Erro", "Não foi possível abrir a tela " + arquivoFxml
                    + ".\nVerifique o caminho da pasta de views no MainController.\n\n" + e.getMessage());
        }
    }

    private void mostrarErro(String titulo, String mensagem) {
        Alert alerta = new Alert(AlertType.ERROR);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
}