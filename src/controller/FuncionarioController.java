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

/**
 * Controlador da tela de cadastro e consulta de funcionários
 * ({@code FuncionarioView.fxml}).
 * <p>
 * Permite incluir, alterar, consultar (por nome) e excluir funcionários,
 * exibindo-os em uma tabela. Ao selecionar uma linha da tabela, o formulário é
 * preenchido para edição.
 * </p>
 */
public class FuncionarioController {



    // Formulário

    /** Campo de texto para o nome do funcionário (também usado como filtro na consulta). */
    @FXML private TextField txtNome;

    /** Campo de texto para o CPF (aceita máscara; somente os dígitos são salvos). */
    @FXML private TextField txtCpf;

    /** Campo de texto para o cargo. */
    @FXML private TextField txtCargo;

    /** Seletor da data de admissão. */
    @FXML private DatePicker dpDataAdmissao;

    // Botões

    /** Botão que salva (insere ou atualiza) o funcionário. */
    @FXML private Button btnSalvar;

    /** Botão que filtra a tabela pelo nome informado. */
    @FXML private Button btnConsultar;

    /** Botão que limpa o formulário e recarrega a tabela. */
    @FXML private Button btnLimpar;

    /** Botão que exclui o funcionário selecionado (desabilitado sem seleção). */
    @FXML private Button btnExcluir;

    // Tabela e colunas

    /** Tabela que lista os funcionários cadastrados. */
    @FXML private TableView<Funcionario> tblFuncionarios;

    /** Coluna do identificador. */
    @FXML private TableColumn<Funcionario, Integer> colId;

    /** Coluna do nome. */
    @FXML private TableColumn<Funcionario, String> colNome;

    /** Coluna do CPF (exibido formatado como 000.000.000-00). */
    @FXML private TableColumn<Funcionario, String> colCpf;

    /** Coluna do cargo. */
    @FXML private TableColumn<Funcionario, String> colCargo;

    /** Coluna da data de admissão (exibida como dd/MM/yyyy). */
    @FXML private TableColumn<Funcionario, LocalDate> colDataAdmissao;

    /** Formato de exibição de datas na tabela ({@code dd/MM/yyyy}). */
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /** DAO utilizado para acessar os dados de funcionários. */
    private final FuncionarioDAO funcionarioDAO = new FuncionarioDAO();

 
    /**
     * Inicialização da tela, chamada automaticamente pelo JavaFX após o
     * carregamento do FXML. Configura a tabela, os listeners e carrega os dados.
     */
    @FXML
    private void initialize() {
        configurarTabela();
        configurarListeners();
        carregarTabela();
    }

    /**
     * Vincula as colunas às propriedades de {@link Funcionario} e define a
     * formatação das células de CPF (000.000.000-00) e de data (dd/MM/yyyy).
     */
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

    /**
     * Registra os listeners da tela: preenche o formulário ao selecionar uma
     * linha da tabela e desabilita o botão Excluir enquanto não houver seleção.
     */
    private void configurarListeners() {
        tblFuncionarios.getSelectionModel().selectedItemProperty().addListener((obs, antigo, selecionado) -> {
            if (selecionado != null) {
                preencherFormulario(selecionado);
            }
        });

        btnExcluir.disableProperty().bind(
                tblFuncionarios.getSelectionModel().selectedItemProperty().isNull());
    }

    /**
     * Ação do botão Salvar.
     * <p>
     * Valida os campos (nome, cargo e data de admissão obrigatórios; CPF com
     * 11 dígitos, ignorando a máscara). Se não houver funcionário selecionado na
     * tabela, insere um novo; caso contrário, atualiza o selecionado. Ao final,
     * recarrega a tabela, limpa o formulário e exibe mensagem de sucesso.
     * </p>
     */
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

    /**
     * Ação do botão Consultar.
     * <p>
     * Filtra os funcionários cujo nome contém o texto digitado (busca parcial,
     * sem diferenciar maiúsculas de minúsculas) e exibe o resultado na tabela.
     * Informa o usuário caso nenhum funcionário seja encontrado.
     * </p>
     */
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

    /** Ação do botão Limpar: limpa o formulário e recarrega a tabela completa. */
    @FXML
    private void acaoLimpar() {
        limparFormulario();
        carregarTabela();
    }

    /**
     * Ação do botão Excluir.
     * <p>
     * Pede confirmação e exclui o funcionário selecionado. Como o DAO não
     * propaga erros, a tabela é recarregada e verifica-se se o funcionário ainda
     * existe: se sim, a exclusão foi bloqueada por registros de produção
     * vinculados e o usuário é avisado; se não, a exclusão é confirmada.
     * </p>
     */
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



    /** Busca todos os funcionários no banco e atualiza a tabela. */
    private void carregarTabela() {
        List<Funcionario> lista = funcionarioDAO.listar();
        tblFuncionarios.setItems(FXCollections.observableArrayList(lista));
    }

    /**
     * Preenche os campos do formulário com os dados de um funcionário
     * (o CPF é exibido formatado).
     *
     * @param f funcionário cujos dados serão exibidos
     */
    private void preencherFormulario(Funcionario f) {
        txtNome.setText(f.getNome());
        txtCpf.setText(formatarCpf(f.getCpf()));
        txtCargo.setText(f.getCargo());
        dpDataAdmissao.setValue(f.getDataAdmissao());
    }

    /** Limpa a seleção da tabela e os campos do formulário, devolvendo o foco ao campo nome. */
    private void limparFormulario() {
        tblFuncionarios.getSelectionModel().clearSelection();
        txtNome.clear();
        txtCpf.clear();
        txtCargo.clear();
        dpDataAdmissao.setValue(null);
        txtNome.requestFocus();
    }

    /**
     * Formata um CPF no padrão {@code 000.000.000-00}.
     *
     * @param cpf CPF com 11 dígitos
     * @return CPF formatado; o valor original caso seja {@code null} ou não tenha 11 caracteres
     */
    private String formatarCpf(String cpf) {
        if (cpf == null || cpf.length() != 11) {
            return cpf;
        }
        return cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "."
                + cpf.substring(6, 9) + "-" + cpf.substring(9);
    }

    /**
     * Exibe um alerta e aguarda o usuário fechá-lo.
     *
     * @param tipo     tipo do alerta (informação, aviso, erro, etc.)
     * @param titulo   título da janela
     * @param mensagem texto exibido ao usuário
     */
    private void mostrarAlerta(AlertType tipo, String titulo, String mensagem) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
}