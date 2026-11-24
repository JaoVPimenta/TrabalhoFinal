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
import model.Material;
import model.Peca;
import model.Tamanho;
import model.Uniforme;

/**
 * Controller da tela de uniformes ({@code UniformeView.fxml}).
 * <p>
 * Implementa o CRUD de {@link Uniforme} usando o {@link UniformeDAO}.
 * Responsabilidades:
 * </p>
 * <ul>
 *   <li>popular os ComboBoxes de peça, tamanho, cor e material;</li>
 *   <li>sugerir o preço automaticamente com a regra de negócio de
 *       {@link Uniforme#calcularPrecoSugerido(Material, Tamanho)};</li>
 *   <li>tratar os botões Novo, Salvar e Excluir;</li>
 *   <li>manter a tabela sincronizada com o banco de dados.</li>
 * </ul>
 * <p>
 * Peça, tamanho e material usam os enums {@link Peca}, {@link Tamanho} e
 * {@link Material}; a cor é um texto escolhido em uma lista fixa.
 * </p>
 */
public class UniformeController {

    // Formulário (peça, tamanho e material usam enums; a cor continua String)
    @FXML private ComboBox<Peca> cbPeca;
    @FXML private ComboBox<Tamanho> cbTamanho;
    @FXML private ComboBox<String> cbCor;
    @FXML private ComboBox<Material> cbMaterial;
    @FXML private TextField txtPreco;

    // Botões
    @FXML private Button btnNovo;
    @FXML private Button btnSalvar;
    @FXML private Button btnExcluir;

    // Tabela e colunas
    @FXML private TableView<Uniforme> tblUniformes;
    @FXML private TableColumn<Uniforme, Integer> colId;
    @FXML private TableColumn<Uniforme, Peca> colPeca;
    @FXML private TableColumn<Uniforme, Tamanho> colTamanho;
    @FXML private TableColumn<Uniforme, String> colCor;
    @FXML private TableColumn<Uniforme, Material> colMaterial;
    @FXML private TableColumn<Uniforme, Double> colPreco;

    /** Localidade usada para formatar valores monetários (vírgula como separador decimal). */
    private static final Locale PT_BR = Locale.forLanguageTag("pt-BR");

    /** DAO usado para ler, gravar e excluir uniformes no banco de dados. */
    private final UniformeDAO uniformeDAO = new UniformeDAO();

    /**
     * Indica que o formulário está sendo preenchido a partir de uma linha da
     * tabela. Evita que o listener de preço sobrescreva o valor vindo do banco.
     */
    private boolean preenchendoFormulario = false;

    /**
     * Inicializa a tela. Chamado automaticamente pelo JavaFX após o carregamento do FXML.
     * <p>
     * Popula os ComboBoxes, configura a tabela e os listeners e carrega os
     * uniformes do banco.
     * </p>
     */
    @FXML
    private void initialize() {
        configurarComboBoxes();
        configurarTabela();
        configurarListeners();
        carregarTabela();
    }

    /**
     * Popula os ComboBoxes. As opções de peça, tamanho e material vêm dos
     * enums, por meio do método {@code values()}; a cor usa uma lista fixa.
     */
    private void configurarComboBoxes() {
        cbPeca.setItems(FXCollections.observableArrayList(Peca.values()));
        cbTamanho.setItems(FXCollections.observableArrayList(Tamanho.values()));
        cbMaterial.setItems(FXCollections.observableArrayList(Material.values()));

        // A cor não é um enum, então mantém a lista de opções fixa
        cbCor.setItems(FXCollections.observableArrayList(
                "Branco", "Preto", "Azul", "Vermelho", "Verde", "Amarelo"));
    }

    /**
     * Liga cada coluna a um atributo da classe {@link Uniforme} e formata o
     * preço como moeda (ex.: {@code R$ 35,00}).
     */
    private void configurarTabela() {
        // O texto entre aspas é o nome do atributo: "id" -> getId(), "precoBase" -> getPrecoBase()
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPeca.setCellValueFactory(new PropertyValueFactory<>("peca"));
        colTamanho.setCellValueFactory(new PropertyValueFactory<>("tamanho"));
        colCor.setCellValueFactory(new PropertyValueFactory<>("cor"));
        colMaterial.setCellValueFactory(new PropertyValueFactory<>("material"));
        colPreco.setCellValueFactory(new PropertyValueFactory<>("precoBase"));

        // Exibe o preço formatado como moeda (ex.: R$ 35,00)
        colPreco.setCellFactory(coluna -> new TableCell<Uniforme, Double>() {
            @Override
            protected void updateItem(Double valor, boolean vazio) {
                super.updateItem(valor, vazio);
                setText(vazio || valor == null ? null : String.format(PT_BR, "R$ %.2f", valor));
            }
        });
    }

    /**
     * Registra os listeners da tela:
     * <ol>
     *   <li>ao mudar o material ou o tamanho, recalcula o preço sugerido;</li>
     *   <li>ao selecionar uma linha da tabela, os dados vão para o formulário;</li>
     *   <li>o botão Excluir só fica habilitado com uma linha selecionada.</li>
     * </ol>
     */
    private void configurarListeners() {
        // 1) Quando o material OU o tamanho mudar, recalcula o preço sugerido
        cbMaterial.valueProperty().addListener((obs, antigo, novo) -> atualizarPrecoSugerido());
        cbTamanho.valueProperty().addListener((obs, antigo, novo) -> atualizarPrecoSugerido());

        // 2) Quando o usuário clicar em uma linha da tabela, joga os dados no formulário
        tblUniformes.getSelectionModel().selectedItemProperty().addListener((obs, antigo, selecionado) -> {
            if (selecionado != null) {
                preencherFormulario(selecionado);
            }
        });

        // 3) O botão Excluir só fica habilitado quando existe uma linha selecionada
        btnExcluir.disableProperty().bind(
                tblUniformes.getSelectionModel().selectedItemProperty().isNull());
    }

    /**
     * Ação do botão Novo.
     * Limpa o formulário para cadastrar um uniforme do zero.
     */
    @FXML
    private void acaoNovo() {
        limparFormulario();
    }

    /**
     * Ação do botão Salvar.
     * <p>
     * Valida os campos (peça, tamanho, cor, material e preço maior que zero; o
     * preço aceita vírgula ou ponto). Se não houver linha selecionada na tabela,
     * insere um novo uniforme; caso contrário, atualiza o uniforme selecionado.
     * Ao final, recarrega a tabela, limpa o formulário e exibe uma mensagem de sucesso.
     * </p>
     */
    @FXML
    private void acaoSalvar() {
        Peca peca = cbPeca.getValue();
        Tamanho tamanho = cbTamanho.getValue();
        String cor = cbCor.getValue();
        Material material = cbMaterial.getValue();

        // Validação dos campos obrigatórios
        if (peca == null || tamanho == null || cor == null || material == null) {
            mostrarAlerta(AlertType.WARNING, "Campos obrigatórios",
                    "Selecione peça, tamanho, cor e material.");
            return;
        }

        // Converte o preço digitado (aceita vírgula ou ponto)
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
            // Cadastro novo (id 0: o banco gera o id real com AUTO_INCREMENT)
            uniformeDAO.inserir(new Uniforme(0, peca, tamanho, cor, material, preco));
        } else {
            // Edição de um uniforme que já existe
            uniformeDAO.atualizar(new Uniforme(selecionado.getId(), peca, tamanho, cor, material, preco));
        }

        carregarTabela();
        limparFormulario();
        mostrarAlerta(AlertType.INFORMATION, "Sucesso", "Uniforme salvo com sucesso!");
    }

    /**
     * Ação do botão Excluir.
     * <p>
     * Pede confirmação e exclui o uniforme selecionado. Como o DAO apenas imprime
     * os erros no console, o método confere se o uniforme realmente saiu da
     * tabela; se não saiu (por exemplo, por estar vinculado a registros de
     * produção), exibe um aviso.
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

        // O DAO apenas imprime o erro no console (ex.: uniforme usado em um registro
        // de produção, que viola a chave estrangeira). Por isso conferimos se ele
        // realmente saiu da tabela.
        boolean aindaExiste = tblUniformes.getItems().stream().anyMatch(u -> u.getId() == id);

        if (aindaExiste) {
            mostrarAlerta(AlertType.WARNING, "Não foi possível excluir",
                    "Este uniforme provavelmente está vinculado a registros de produção.");
        } else {
            limparFormulario();
            mostrarAlerta(AlertType.INFORMATION, "Sucesso", "Uniforme excluído com sucesso!");
        }
    }

    /**
     * Busca os uniformes no banco e atualiza a tabela.
     */
    private void carregarTabela() {
        List<Uniforme> lista = uniformeDAO.listar();
        tblUniformes.setItems(FXCollections.observableArrayList(lista));
    }

    /**
     * Calcula o preço sugerido pela regra de negócio da classe {@link Uniforme}
     * e o exibe no campo de preço.
     * <p>
     * Não faz nada enquanto o formulário está sendo preenchido a partir da tabela
     * ou quando material e tamanho ainda não foram ambos escolhidos.
     * </p>
     */
    private void atualizarPrecoSugerido() {
        if (preenchendoFormulario) {
            return; // está carregando uma linha da tabela: mantém o preço que veio do banco
        }

        Material material = cbMaterial.getValue();
        Tamanho tamanho = cbTamanho.getValue();

        // Só sugere quando os dois já foram escolhidos
        if (material == null || tamanho == null) {
            return;
        }

        double sugerido = Uniforme.calcularPrecoSugerido(material, tamanho);
        txtPreco.setText(String.format(PT_BR, "%.2f", sugerido));
    }

    /**
     * Copia os dados de um uniforme para os campos do formulário, mantendo o
     * preço original (sem recalcular a sugestão).
     *
     * @param u uniforme selecionado na tabela
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

    /**
     * Limpa o formulário e a seleção da tabela, voltando ao modo "cadastro novo".
     */
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
     * Exibe uma caixa de diálogo e aguarda o usuário fechá-la.
     *
     * @param tipo     tipo do alerta (informação, aviso, erro...)
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