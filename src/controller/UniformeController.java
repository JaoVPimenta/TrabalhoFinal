package controller;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import dao.UniformeDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Uniforme;



public class UniformeController {



    // Formulário
    @FXML private ComboBox<String> cbPeca;
    @FXML private ComboBox<String> cbTamanho;
    @FXML private ComboBox<String> cbCor;
    @FXML private ComboBox<String> cbMaterial;
    @FXML private TextField txtPreco;

    // Botões
    @FXML private Button btnNovo;
    @FXML private Button btnSalvar;
    @FXML private Button btnExcluir;

    // Tabela e colunas
    @FXML private TableView<Uniforme> tblUniformes;
    @FXML private TableColumn<Uniforme, Integer> colId;
    @FXML private TableColumn<Uniforme, String> colPeca;
    @FXML private TableColumn<Uniforme, String> colTamanho;
    @FXML private TableColumn<Uniforme, String> colCor;
    @FXML private TableColumn<Uniforme, String> colMaterial;
    @FXML private TableColumn<Uniforme, Double> colPreco;



    private static final Locale PT_BR = Locale.forLanguageTag("pt-BR");

    private final UniformeDAO uniformeDAO = new UniformeDAO();


    private boolean preenchendoFormulario = false;


    @FXML
    private void initialize() {
        configurarComboBoxes();
        configurarTabela();
        configurarListeners();
        carregarTabela();
    }

    private void configurarComboBoxes() {
        cbPeca.setItems(FXCollections.observableArrayList(
                "Camisa", "Calção", "Calça", "Meião", "Jaqueta", "Agasalho"));

        cbTamanho.setItems(FXCollections.observableArrayList(
                "P", "M", "G", "GG", "XG"));

        cbCor.setItems(FXCollections.observableArrayList(
                "Branco", "Preto", "Azul", "Vermelho", "Verde", "Amarelo"));

        cbMaterial.setItems(FXCollections.observableArrayList(
                "Algodão", "Poliéster"));
    }

    private void configurarTabela() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPeca.setCellValueFactory(new PropertyValueFactory<>("peca"));
        colTamanho.setCellValueFactory(new PropertyValueFactory<>("tamanho"));
        colCor.setCellValueFactory(new PropertyValueFactory<>("cor"));
        colMaterial.setCellValueFactory(new PropertyValueFactory<>("material"));
        colPreco.setCellValueFactory(new PropertyValueFactory<>("precoBase"));

        colPreco.setCellFactory(coluna -> new TableCell<Uniforme, Double>() {
            @Override
            protected void updateItem(Double valor, boolean vazio) {
                super.updateItem(valor, vazio);
                setText(vazio || valor == null ? null : String.format(PT_BR, "R$ %.2f", valor));
            }
        });
    }

    private void configurarListeners() {
        cbMaterial.valueProperty().addListener((obs, antigo, novo) -> atualizarPrecoSugerido());
        cbTamanho.valueProperty().addListener((obs, antigo, novo) -> atualizarPrecoSugerido());

        tblUniformes.getSelectionModel().selectedItemProperty().addListener((obs, antigo, selecionado) -> {
            if (selecionado != null) {
                preencherFormulario(selecionado);
            }
        });

        btnExcluir.disableProperty().bind(
                tblUniformes.getSelectionModel().selectedItemProperty().isNull());
    }


    @FXML
    private void acaoNovo() {
        limparFormulario();
    }


    @FXML
    private void acaoSalvar() {
        String peca = cbPeca.getValue();
        String tamanho = cbTamanho.getValue();
        String cor = cbCor.getValue();
        String material = cbMaterial.getValue();

        if (peca == null || tamanho == null || cor == null || material == null) {
            mostrarAlerta(AlertType.WARNING, "Campos obrigatórios",
                    "Selecione peça, tamanho, cor e material.");
            return;
        }

        double preco;
        try {
            preco = Double.parseDouble(txtPreco.getText().trim().replace(",", "."));
        } catch (NumberFormatException e) {
            mostrarAlerta(AlertType.WARNING, "Preço inválido",
                    "Informe um preço numérico válido. Exemplo: 35,00");
            txtPreco.requestFocus();
            return;
        }

        if (preco <= 0) {
            mostrarAlerta(AlertType.WARNING, "Preço inválido",
                    "O preço deve ser maior que zero.");
            txtPreco.requestFocus();
            return;
        }

        Uniforme selecionado = tblUniformes.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            uniformeDAO.inserir(new Uniforme(0, peca, tamanho, cor, material, preco));
        } else {
            uniformeDAO.atualizar(new Uniforme(selecionado.getId(), peca, tamanho, cor, material, preco));
        }

        carregarTabela();
        limparFormulario();
        mostrarAlerta(AlertType.INFORMATION, "Sucesso", "Uniforme salvo com sucesso!");
    }

    @FXML
    private void acaoExcluir() {
        Uniforme selecionado = tblUniformes.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            mostrarAlerta(AlertType.WARNING, "Nenhum item selecionado",
                    "Selecione um uniforme na tabela para excluir.");
            return;
        }

        Alert confirmacao = new Alert(AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar exclusão");
        confirmacao.setHeaderText(null);
        confirmacao.setContentText("Deseja realmente excluir: " + selecionado + "?");

        Optional<ButtonType> resposta = confirmacao.showAndWait();
        if (resposta.isEmpty() || resposta.get() != ButtonType.OK) {
            return;
        }

        int id = selecionado.getId();
        uniformeDAO.excluir(id);
        carregarTabela();

        boolean aindaExiste = tblUniformes.getItems().stream().anyMatch(u -> u.getId() == id);

        if (aindaExiste) {
            mostrarAlerta(AlertType.WARNING, "Não foi possível excluir",
                    "Este uniforme provavelmente está vinculado a registros de produção.");
        } else {
            limparFormulario();
            mostrarAlerta(AlertType.INFORMATION, "Sucesso", "Uniforme excluído com sucesso!");
        }
    }


    private void carregarTabela() {
        List<Uniforme> lista = uniformeDAO.listar();
        tblUniformes.setItems(FXCollections.observableArrayList(lista));
    }

    private void atualizarPrecoSugerido() {
        if (preenchendoFormulario) {
            return; 
        }

        String material = cbMaterial.getValue();
        String tamanho = cbTamanho.getValue();

        if (material == null || tamanho == null) {
            return;
        }

        double sugerido = Uniforme.calcularPrecoSugerido(material, tamanho);
        txtPreco.setText(String.format(PT_BR, "%.2f", sugerido));
    }

    private void preencherFormulario(Uniforme u) {
        preenchendoFormulario = true;
        try {
            cbPeca.setValue(u.getPeca());
            cbTamanho.setValue(u.getTamanho());
            cbCor.setValue(u.getCor());
            cbMaterial.setValue(u.getMaterial());
            txtPreco.setText(String.format(PT_BR, "%.2f", u.getPrecoBase()));
        } finally {
            preenchendoFormulario = false;
        }
    }

    private void limparFormulario() {
        tblUniformes.getSelectionModel().clearSelection();
        cbPeca.setValue(null);
        cbTamanho.setValue(null);
        cbCor.setValue(null);
        cbMaterial.setValue(null);
        txtPreco.clear();
        cbPeca.requestFocus();
    }

    private void mostrarAlerta(AlertType tipo, String titulo, String mensagem) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
}