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


/**
 * Controlador da tela de cadastro de uniformes ({@code UniformeView.fxml}).
 * <p>
 * Permite incluir, alterar e excluir uniformes, exibindo-os em uma tabela. Ao
 * escolher material e tamanho, o preço é sugerido automaticamente (veja
 * {@link Uniforme#calcularPrecoSugerido(String, String)}), podendo ser editado
 * pelo usuário.
 * </p>
 */
public class UniformeController {



    // Formulário

    /** Seleção da peça (Camisa, Calção, Calça, Meião, Jaqueta, Agasalho). */
    @FXML private ComboBox<String> cbPeca;

    /** Seleção do tamanho (P, M, G, GG, XG). */
    @FXML private ComboBox<String> cbTamanho;

    /** Seleção da cor (Branco, Preto, Azul, Vermelho, Verde, Amarelo). */
    @FXML private ComboBox<String> cbCor;

    /** Seleção do material (Algodão, Poliéster). */
    @FXML private ComboBox<String> cbMaterial;

    /** Campo de texto para o preço (aceita vírgula ou ponto como separador decimal). */
    @FXML private TextField txtPreco;

    // Botões

    /** Botão que limpa o formulário para um novo cadastro. */
    @FXML private Button btnNovo;

    /** Botão que salva (insere ou atualiza) o uniforme. */
    @FXML private Button btnSalvar;

    /** Botão que exclui o uniforme selecionado (desabilitado sem seleção). */
    @FXML private Button btnExcluir;

    // Tabela e colunas

    /** Tabela que lista os uniformes cadastrados. */
    @FXML private TableView<Uniforme> tblUniformes;

    /** Coluna do identificador. */
    @FXML private TableColumn<Uniforme, Integer> colId;

    /** Coluna da peça. */
    @FXML private TableColumn<Uniforme, String> colPeca;

    /** Coluna do tamanho. */
    @FXML private TableColumn<Uniforme, String> colTamanho;

    /** Coluna da cor. */
    @FXML private TableColumn<Uniforme, String> colCor;

    /** Coluna do material. */
    @FXML private TableColumn<Uniforme, String> colMaterial;

    /** Coluna do preço (exibido como R$ 0,00). */
    @FXML private TableColumn<Uniforme, Double> colPreco;



    /** Localidade (pt-BR) usada na formatação de valores monetários. */
    private static final Locale PT_BR = Locale.forLanguageTag("pt-BR");

    /** DAO utilizado para acessar os dados de uniformes. */
    private final UniformeDAO uniformeDAO = new UniformeDAO();


    /**
     * Indica que o formulário está sendo preenchido a partir de uma linha da
     * tabela. Evita que o preço sugerido sobrescreva o preço já cadastrado.
     */
    private boolean preenchendoFormulario = false;


    /**
     * Inicialização da tela, chamada automaticamente pelo JavaFX após o
     * carregamento do FXML. Configura combos, tabela e listeners e carrega os dados.
     */
    @FXML
    private void initialize() {
        configurarComboBoxes();
        configurarTabela();
        configurarListeners();
        carregarTabela();
    }

    /** Define as opções disponíveis em cada ComboBox do formulário. */
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

    /**
     * Vincula as colunas às propriedades de {@link Uniforme} e define a
     * formatação da coluna de preço (R$ 0,00).
     */
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

    /**
     * Registra os listeners da tela: recalcula o preço sugerido ao mudar material
     * ou tamanho, preenche o formulário ao selecionar uma linha da tabela e
     * desabilita o botão Excluir enquanto não houver seleção.
     */
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


    /** Ação do botão Novo: limpa o formulário para um novo cadastro. */
    @FXML
    private void acaoNovo() {
        limparFormulario();
    }


    /**
     * Ação do botão Salvar.
     * <p>
     * Valida a seleção de peça, tamanho, cor e material, e o preço (numérico,
     * aceita vírgula e deve ser maior que zero). Se não houver uniforme
     * selecionado na tabela, insere um novo; caso contrário, atualiza o
     * selecionado. Ao final, recarrega a tabela, limpa o formulário e exibe
     * mensagem de sucesso.
     * </p>
     */
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

    /**
     * Ação do botão Excluir.
     * <p>
     * Pede confirmação e exclui o uniforme selecionado. Como o DAO não propaga
     * erros, a tabela é recarregada e verifica-se se o uniforme ainda existe: se
     * sim, a exclusão foi bloqueada (provavelmente por registros de produção
     * vinculados) e o usuário é avisado; se não, a exclusão é confirmada.
     * </p>
     */
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


    /** Busca todos os uniformes no banco e atualiza a tabela. */
    private void carregarTabela() {
        List<Uniforme> lista = uniformeDAO.listar();
        tblUniformes.setItems(FXCollections.observableArrayList(lista));
    }

    /**
     * Preenche o campo de preço com o valor sugerido para o material e tamanho
     * selecionados. Não faz nada se algum dos dois não estiver selecionado ou se
     * o formulário estiver sendo preenchido a partir da tabela.
     */
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

    /**
     * Preenche os campos do formulário com os dados de um uniforme.
     * <p>
     * Ativa temporariamente {@link #preenchendoFormulario} para que o preço
     * cadastrado não seja substituído pelo preço sugerido.
     * </p>
     *
     * @param u uniforme cujos dados serão exibidos
     */
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

    /** Limpa a seleção da tabela e os campos do formulário, devolvendo o foco ao combo de peça. */
    private void limparFormulario() {
        tblUniformes.getSelectionModel().clearSelection();
        cbPeca.setValue(null);
        cbTamanho.setValue(null);
        cbCor.setValue(null);
        cbMaterial.setValue(null);
        txtPreco.clear();
        cbPeca.requestFocus();
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