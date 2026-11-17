package controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import dao.FuncionarioDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Funcionario;


public class FuncionarioController {



    // Formulário
    @FXML private TextField txtNome;
    @FXML private TextField txtCpf;
    @FXML private TextField txtCargo;
    @FXML private DatePicker dpDataAdmissao;

    // Botões
    @FXML private Button btnSalvar;
    @FXML private Button btnConsultar;
    @FXML private Button btnLimpar;
    @FXML private Button btnExcluir;

    // Tabela e colunas
    @FXML private TableView<Funcionario> tblFuncionarios;
    @FXML private TableColumn<Funcionario, Integer> colId;
    @FXML private TableColumn<Funcionario, String> colNome;
    @FXML private TableColumn<Funcionario, String> colCpf;
    @FXML private TableColumn<Funcionario, String> colCargo;
    @FXML private TableColumn<Funcionario, LocalDate> colDataAdmissao;

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final FuncionarioDAO funcionarioDAO = new FuncionarioDAO();

 

    @FXML
    private void initialize() {
        configurarTabela();
        configurarListeners();
        carregarTabela();
    }

    private void configurarTabela() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        colCargo.setCellValueFactory(new PropertyValueFactory<>("cargo"));
        colDataAdmissao.setCellValueFactory(new PropertyValueFactory<>("dataAdmissao"));

        colCpf.setCellFactory(coluna -> new TableCell<Funcionario, String>() {
            @Override
            protected void updateItem(String cpf, boolean vazio) {
                super.updateItem(cpf, vazio);
                setText(vazio || cpf == null ? null : formatarCpf(cpf));
            }
        });

        colDataAdmissao.setCellFactory(coluna -> new TableCell<Funcionario, LocalDate>() {
            @Override
            protected void updateItem(LocalDate data, boolean vazio) {
                super.updateItem(data, vazio);
                setText(vazio || data == null ? null : data.format(FORMATO_DATA));
            }
        });
    }

    private void configurarListeners() {
        tblFuncionarios.getSelectionModel().selectedItemProperty().addListener((obs, antigo, selecionado) -> {
            if (selecionado != null) {
                preencherFormulario(selecionado);
            }
        });

        btnExcluir.disableProperty().bind(
                tblFuncionarios.getSelectionModel().selectedItemProperty().isNull());
    }

    @FXML
    private void acaoSalvar() {
        String nome = txtNome.getText().trim();
        String cpf = txtCpf.getText().replaceAll("\\D", ""); 
        String cargo = txtCargo.getText().trim();
        LocalDate dataAdmissao = dpDataAdmissao.getValue();

        if (nome.isEmpty() || cargo.isEmpty() || dataAdmissao == null) {
            mostrarAlerta(AlertType.WARNING, "Campos obrigatórios",
                    "Preencha nome, cargo e data de admissão.");
            return;
        }

        if (cpf.length() != 11) {
            mostrarAlerta(AlertType.WARNING, "CPF inválido",
                    "O CPF deve ter 11 dígitos.");
            txtCpf.requestFocus();
            return;
        }

        Funcionario selecionado = tblFuncionarios.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            funcionarioDAO.inserir(new Funcionario(0, nome, cpf, cargo, dataAdmissao));
        } else {
            funcionarioDAO.atualizar(new Funcionario(selecionado.getId(), nome, cpf, cargo, dataAdmissao));
        }

        carregarTabela();
        limparFormulario();
        mostrarAlerta(AlertType.INFORMATION, "Sucesso", "Funcionário salvo com sucesso!");
    }

    @FXML
    private void acaoConsultar() {
        String filtro = txtNome.getText().trim().toLowerCase();

        List<Funcionario> resultado = funcionarioDAO.listar().stream()
                .filter(f -> f.getNome().toLowerCase().contains(filtro))
                .toList();

        tblFuncionarios.setItems(FXCollections.observableArrayList(resultado));

        if (resultado.isEmpty() && !filtro.isEmpty()) {
            mostrarAlerta(AlertType.INFORMATION, "Consulta",
                    "Nenhum funcionário encontrado com esse nome.");
        }
    }

    @FXML
    private void acaoLimpar() {
        limparFormulario();
        carregarTabela();
    }

    @FXML
    private void acaoExcluir() {
        Funcionario selecionado = tblFuncionarios.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            mostrarAlerta(AlertType.WARNING, "Nenhum item selecionado",
                    "Selecione um funcionário na tabela para excluir.");
            return;
        }

        Alert confirmacao = new Alert(AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar exclusão");
        confirmacao.setHeaderText(null);
        confirmacao.setContentText("Deseja realmente excluir o funcionário " + selecionado.getNome() + "?");

        Optional<ButtonType> resposta = confirmacao.showAndWait();
        if (resposta.isEmpty() || resposta.get() != ButtonType.OK) {
            return;
        }

        int id = selecionado.getId();
        funcionarioDAO.excluir(id);
        carregarTabela();


        boolean aindaExiste = tblFuncionarios.getItems().stream().anyMatch(f -> f.getId() == id);

        if (aindaExiste) {
            mostrarAlerta(AlertType.WARNING, "Não foi possível excluir",
                    "Este funcionário possui registros de produção vinculados. "
                    + "Exclua esses registros primeiro.");
        } else {
            limparFormulario();
            mostrarAlerta(AlertType.INFORMATION, "Sucesso", "Funcionário excluído com sucesso!");
        }
    }



    private void carregarTabela() {
        List<Funcionario> lista = funcionarioDAO.listar();
        tblFuncionarios.setItems(FXCollections.observableArrayList(lista));
    }

    private void preencherFormulario(Funcionario f) {
        txtNome.setText(f.getNome());
        txtCpf.setText(formatarCpf(f.getCpf()));
        txtCargo.setText(f.getCargo());
        dpDataAdmissao.setValue(f.getDataAdmissao());
    }

    private void limparFormulario() {
        tblFuncionarios.getSelectionModel().clearSelection();
        txtNome.clear();
        txtCpf.clear();
        txtCargo.clear();
        dpDataAdmissao.setValue(null);
        txtNome.requestFocus();
    }

    private String formatarCpf(String cpf) {
        if (cpf == null || cpf.length() != 11) {
            return cpf;
        }
        return cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "."
                + cpf.substring(6, 9) + "-" + cpf.substring(9);
    }

    private void mostrarAlerta(AlertType tipo, String titulo, String mensagem) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
}