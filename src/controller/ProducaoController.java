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

/**
 * Controlador da tela de registro de produção ({@code ProducaoView.fxml}).
 * <p>
 * Permite lançar a produção de um uniforme por um funcionário (com quantidade e
 * data) e excluir lançamentos existentes. Não há edição: um lançamento incorreto
 * deve ser excluído e registrado novamente.
 * </p>
 */
public class ProducaoController {



    // Formulário

    /** Seleção do funcionário responsável pela produção. */
    @FXML private ComboBox<Funcionario> cbFuncionario;

    /** Seleção do uniforme produzido. */
    @FXML private ComboBox<Uniforme> cbUniforme;

    /** Campo de texto para a quantidade produzida (inteiro maior que zero). */
    @FXML private TextField txtQuantidade;

    /** Seletor da data de produção (padrão: data atual). */
    @FXML private DatePicker dpDataProducao;

    // Botões

    /** Botão que limpa o formulário para um novo lançamento. */
    @FXML private Button btnNovo;

    /** Botão que salva o registro de produção. */
    @FXML private Button btnSalvar;

    /** Botão que exclui o registro selecionado (desabilitado sem seleção). */
    @FXML private Button btnExcluir;

    // Tabela e colunas

    /** Tabela que lista os registros de produção. */
    @FXML private TableView<RegistroProducao> tblRegistros;

    /** Coluna do identificador do registro. */
    @FXML private TableColumn<RegistroProducao, Integer> colId;

    /** Coluna com o nome do funcionário. */
    @FXML private TableColumn<RegistroProducao, String> colFuncionario;

    /** Coluna com a descrição do uniforme (peça, tamanho e cor). */
    @FXML private TableColumn<RegistroProducao, String> colUniforme;

    /** Coluna da quantidade produzida. */
    @FXML private TableColumn<RegistroProducao, Integer> colQuantidade;

    /** Coluna da data de produção (exibida como dd/MM/yyyy). */
    @FXML private TableColumn<RegistroProducao, LocalDate> colData;



    /** Formato de exibição de datas na tabela ({@code dd/MM/yyyy}). */
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /** DAO utilizado para listar os funcionários no ComboBox. */
    private final FuncionarioDAO funcionarioDAO = new FuncionarioDAO();

    /** DAO utilizado para listar os uniformes no ComboBox. */
    private final UniformeDAO uniformeDAO = new UniformeDAO();

    /** DAO utilizado para acessar os registros de produção. */
    private final RegistroProducaoDAO registroDAO = new RegistroProducaoDAO();



    /**
     * Inicialização da tela, chamada automaticamente pelo JavaFX após o
     * carregamento do FXML. Carrega os combos, configura a tabela e os listeners,
     * carrega os registros e define a data atual no formulário.
     */
    @FXML
    private void initialize() {
        carregarComboBoxes();
        configurarTabela();
        configurarListeners();
        carregarTabela();
        dpDataProducao.setValue(LocalDate.now());
    }

    /** Popula os ComboBoxes com os funcionários e uniformes cadastrados no banco. */
    private void carregarComboBoxes() {
        cbFuncionario.setItems(FXCollections.observableArrayList(funcionarioDAO.listar()));
        cbUniforme.setItems(FXCollections.observableArrayList(uniformeDAO.listar()));
    }

    /**
     * Vincula as colunas às propriedades de {@link RegistroProducao}.
     * <p>
     * A coluna Funcionário exibe o nome; a coluna Uniforme exibe o texto
     * {@code "peça tamanho - cor"}; a coluna Data é formatada como dd/MM/yyyy.
     * </p>
     */
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

    /** Desabilita o botão Excluir enquanto nenhum registro estiver selecionado. */
    private void configurarListeners() {
        btnExcluir.disableProperty().bind(
                tblRegistros.getSelectionModel().selectedItemProperty().isNull());
    }


    /** Ação do botão Novo: limpa o formulário para um novo lançamento. */
    @FXML
    private void acaoNovo() {
        limparFormulario();
    }

    /**
     * Ação do botão Salvar.
     * <p>
     * Valida a seleção de funcionário, uniforme e data, e a quantidade (número
     * inteiro maior que zero). Em seguida insere o registro, recarrega a tabela,
     * limpa o formulário e exibe mensagem de sucesso.
     * </p>
     */
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


    /**
     * Ação do botão Excluir.
     * <p>
     * Pede confirmação (exibindo o nome do funcionário e a quantidade) e exclui
     * o registro selecionado. Ao final, recarrega a tabela e limpa o formulário.
     * </p>
     */
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



    /** Busca todos os registros de produção no banco e atualiza a tabela. */
    private void carregarTabela() {
        List<RegistroProducao> lista = registroDAO.listar();
        tblRegistros.setItems(FXCollections.observableArrayList(lista));
    }


    /**
     * Limpa a seleção da tabela, os combos e a quantidade, restaura a data atual
     * e devolve o foco ao combo de funcionário.
     */
    private void limparFormulario() {
        tblRegistros.getSelectionModel().clearSelection();
        cbFuncionario.setValue(null);
        cbUniforme.setValue(null);
        txtQuantidade.clear();
        dpDataProducao.setValue(LocalDate.now());
        cbFuncionario.requestFocus();
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