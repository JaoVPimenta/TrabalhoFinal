package controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import dao.FuncionarioDAO;
import dao.RegistroProducaoDAO;
import dao.UniformeDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Funcionario;
import model.RegistroProducao;
import model.Uniforme;


public class ProducaoController {



    // Formulário
    @FXML private ComboBox<Funcionario> cbFuncionario;
    @FXML private ComboBox<Uniforme> cbUniforme;
    @FXML private TextField txtQuantidade;
    @FXML private DatePicker dpDataProducao;

    // Botões
    @FXML private Button btnNovo;
    @FXML private Button btnSalvar;
    @FXML private Button btnExcluir;

    // Tabela e colunas
    @FXML private TableView<RegistroProducao> tblRegistros;
    @FXML private TableColumn<RegistroProducao, Integer> colId;
    @FXML private TableColumn<RegistroProducao, String> colFuncionario;
    @FXML private TableColumn<RegistroProducao, String> colUniforme;
    @FXML private TableColumn<RegistroProducao, Integer> colQuantidade;
    @FXML private TableColumn<RegistroProducao, LocalDate> colData;



    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
    private final UniformeDAO uniformeDAO = new UniformeDAO();
    private final RegistroProducaoDAO registroDAO = new RegistroProducaoDAO();



    @FXML
    private void initialize() {
        carregarComboBoxes();
        configurarTabela();
        configurarListeners();
        carregarTabela();
        dpDataProducao.setValue(LocalDate.now());
    }

    private void carregarComboBoxes() {
        cbFuncionario.setItems(FXCollections.observableArrayList(funcionarioDAO.listar()));
        cbUniforme.setItems(FXCollections.observableArrayList(uniformeDAO.listar()));
    }

    private void configurarTabela() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colData.setCellValueFactory(new PropertyValueFactory<>("dataProducao"));

        colFuncionario.setCellValueFactory(celula ->
                new SimpleStringProperty(celula.getValue().getFuncionario().getNome()));

        colUniforme.setCellValueFactory(celula -> {
            Uniforme u = celula.getValue().getUniforme();
            return new SimpleStringProperty(u.getPeca() + " " + u.getTamanho() + " - " + u.getCor());
        });

        colData.setCellFactory(coluna -> new TableCell<RegistroProducao, LocalDate>() {
            @Override
            protected void updateItem(LocalDate data, boolean vazio) {
                super.updateItem(data, vazio);
                setText(vazio || data == null ? null : data.format(FORMATO_DATA));
            }
        });
    }

    private void configurarListeners() {
        btnExcluir.disableProperty().bind(
                tblRegistros.getSelectionModel().selectedItemProperty().isNull());
    }


    @FXML
    private void acaoNovo() {
        limparFormulario();
    }

    @FXML
    private void acaoSalvar() {
        Funcionario funcionario = cbFuncionario.getValue();
        Uniforme uniforme = cbUniforme.getValue();
        LocalDate dataProducao = dpDataProducao.getValue();

        if (funcionario == null || uniforme == null || dataProducao == null) {
            mostrarAlerta(AlertType.WARNING, "Campos obrigatórios",
                    "Selecione o funcionário, o uniforme e a data de produção.");
            return;
        }

        int quantidade;
        try {
            quantidade = Integer.parseInt(txtQuantidade.getText().trim());
        } catch (NumberFormatException e) {
            mostrarAlerta(AlertType.WARNING, "Quantidade inválida",
                    "Informe a quantidade como um número inteiro.");
            txtQuantidade.requestFocus();
            return;
        }

        if (quantidade <= 0) {
            mostrarAlerta(AlertType.WARNING, "Quantidade inválida",
                    "A quantidade deve ser maior que zero.");
            txtQuantidade.requestFocus();
            return;
        }


        registroDAO.inserir(new RegistroProducao(0, funcionario, uniforme, quantidade, dataProducao));

        carregarTabela();
        limparFormulario();
        mostrarAlerta(AlertType.INFORMATION, "Sucesso", "Produção registrada com sucesso!");
    }


    @FXML
    private void acaoExcluir() {
        RegistroProducao selecionado = tblRegistros.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            mostrarAlerta(AlertType.WARNING, "Nenhum item selecionado",
                    "Selecione um registro na tabela para excluir.");
            return;
        }

        Alert confirmacao = new Alert(AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar exclusão");
        confirmacao.setHeaderText(null);
        confirmacao.setContentText("Deseja realmente excluir o registro de "
                + selecionado.getFuncionario().getNome() + " ("
                + selecionado.getQuantidade() + " un.)?");

        Optional<ButtonType> resposta = confirmacao.showAndWait();
        if (resposta.isEmpty() || resposta.get() != ButtonType.OK) {
            return;
        }

        registroDAO.excluir(selecionado.getId());
        carregarTabela();
        limparFormulario();
        mostrarAlerta(AlertType.INFORMATION, "Sucesso", "Registro excluído com sucesso!");
    }



    private void carregarTabela() {
        List<RegistroProducao> lista = registroDAO.listar();
        tblRegistros.setItems(FXCollections.observableArrayList(lista));
    }


    private void limparFormulario() {
        tblRegistros.getSelectionModel().clearSelection();
        cbFuncionario.setValue(null);
        cbUniforme.setValue(null);
        txtQuantidade.clear();
        dpDataProducao.setValue(LocalDate.now());
        cbFuncionario.requestFocus();
    }

 
    private void mostrarAlerta(AlertType tipo, String titulo, String mensagem) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
}