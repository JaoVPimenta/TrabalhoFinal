package dao;

import dao.Conexao;
import model.Material;
import model.Peca;
import model.Tamanho;
import model.Uniforme;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) responsável pelo acesso à tabela {@code uniforme}.
 * <p>
 * Implementa as operações de CRUD para {@link Uniforme}. Peça, tamanho e
 * material são gravados no banco pelo nome da constante do enum
 * ({@code name()}) e convertidos de volta com {@code valueOf()} na leitura.
 * </p>
 * <p>
 * Os métodos tratam {@link SQLException} internamente, exibindo a mensagem de
 * erro no console, e não repassam a exceção para quem os chama.
 * </p>
 */
public class UniformeDAO {

    /**
     * Insere um novo uniforme no banco de dados.
     * <p>
     * O id é gerado pelo banco (AUTO_INCREMENT), portanto o id do objeto é ignorado.
     * </p>
     *
     * @param u uniforme a ser cadastrado (peça, tamanho e material não podem ser {@code null})
     */
    public void inserir(Uniforme u) {
        String sql = "INSERT INTO uniforme (peca, tamanho, cor, material, preco_base) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Os enums são gravados pelo nome (ex.: "ALGODAO")
            stmt.setString(1, u.getPeca().name());
            stmt.setString(2, u.getTamanho().name());
            stmt.setString(3, u.getCor());
            stmt.setString(4, u.getMaterial().name());
            stmt.setDouble(5, u.getPrecoBase());

            stmt.executeUpdate();
            System.out.println("Uniforme cadastrado com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao inserir uniforme: " + e.getMessage());
        }
    }

    /**
     * Lista todos os uniformes cadastrados.
     * <p>
     * Os textos de peça, tamanho e material gravados no banco são convertidos
     * para os enums correspondentes. Se existir um valor que não pertença ao enum,
     * {@code valueOf()} lança {@link IllegalArgumentException}.
     * </p>
     *
     * @return lista de uniformes; vazia se não houver registros ou se ocorrer erro de banco
     */
    public List<Uniforme> listar() {
        String sql = "SELECT * FROM uniforme";
        List<Uniforme> lista = new ArrayList<>();

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Uniforme u = new Uniforme();
                u.setId(rs.getInt("id"));
                // valueOf converte o texto do banco de volta para o enum
                u.setPeca(Peca.valueOf(rs.getString("peca")));
                u.setTamanho(Tamanho.valueOf(rs.getString("tamanho")));
                u.setCor(rs.getString("cor"));
                u.setMaterial(Material.valueOf(rs.getString("material")));
                u.setPrecoBase(rs.getDouble("preco_base"));

                lista.add(u);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar uniformes: " + e.getMessage());
        }

        return lista;
    }

    /**
     * Atualiza no banco os dados de um uniforme existente.
     * <p>
     * O registro alterado é identificado pelo id do objeto recebido.
     * </p>
     *
     * @param u uniforme com os novos dados e o id do registro a ser atualizado
     */
    public void atualizar(Uniforme u) {
        String sql = "UPDATE uniforme SET peca = ?, tamanho = ?, cor = ?, material = ?, preco_base = ? WHERE id = ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, u.getPeca().name());
            stmt.setString(2, u.getTamanho().name());
            stmt.setString(3, u.getCor());
            stmt.setString(4, u.getMaterial().name());
            stmt.setDouble(5, u.getPrecoBase());
            stmt.setInt(6, u.getId());

            stmt.executeUpdate();
            System.out.println("Uniforme atualizado com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar uniforme: " + e.getMessage());
        }
    }

    /**
     * Exclui um uniforme do banco de dados.
     * <p>
     * Se o uniforme estiver vinculado a algum registro de produção, o banco
     * recusa a exclusão (chave estrangeira) e o erro é exibido no console.
     * </p>
     *
     * @param id identificador do uniforme a ser excluído
     */
    public void excluir(int id) {
        String sql = "DELETE FROM uniforme WHERE id = ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Uniforme excluído com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao excluir uniforme (verifique se há registros de produção vinculados): " + e.getMessage());
        }
    }
}