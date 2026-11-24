package dao;

import dao.Conexao;
import model.Funcionario;
import model.Uniforme;
import model.RegistroProducao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) responsável pelas operações de persistência da
 * entidade {@link RegistroProducao}, na tabela {@code registro_producao}.
 * <p>
 * Não há operação de atualização: um lançamento incorreto deve ser excluído
 * e registrado novamente. Erros de SQL são tratados internamente e exibidos em
 * {@code System.err}, sem serem propagados ao chamador.
 * </p>
 */
public class RegistroProducaoDAO {

    // Inserir Registro de Produção (Create)

    /**
     * Insere um novo registro de produção no banco de dados.
     * <p>
     * São gravados os identificadores do funcionário e do uniforme associados,
     * além da quantidade e da data de produção. O {@code id} do registro é
     * gerado pelo banco.
     * </p>
     *
     * @param rp registro de produção a ser inserido; o funcionário e o uniforme
     *           devem estar previamente cadastrados (possuir {@code id} válido)
     */
    public void inserir(RegistroProducao rp) {
        String sql = "INSERT INTO registro_producao (id_funcionario, id_uniforme, quantidade, data_producao) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, rp.getFuncionario().getId());
            stmt.setInt(2, rp.getUniforme().getId());
            stmt.setInt(3, rp.getQuantidade());
            stmt.setDate(4, Date.valueOf(rp.getDataProducao()));
            
            stmt.executeUpdate();
            System.out.println("Registro de produção lançado com sucesso!");
            
        } catch (SQLException e) {
            System.err.println("Erro ao inserir registro de produção: " + e.getMessage());
        }
    }

    // Listar Registros com JOIN para resgatar dados do Funcionário e Uniforme (Read)

    /**
     * Lista todos os registros de produção, já com os dados completos do
     * funcionário e do uniforme associados.
     * <p>
     * A consulta utiliza {@code JOIN} com as tabelas {@code funcionario} e
     * {@code uniforme}, com aliases (ex.: {@code f_nome}, {@code u_peca}) para
     * evitar conflito entre colunas de mesmo nome.
     * </p>
     *
     * @return lista de registros de produção; lista vazia caso não haja
     *         registros ou ocorra algum erro de acesso ao banco
     */
    public List<RegistroProducao> listar() {
        String sql = "SELECT rp.id AS rp_id, rp.quantidade, rp.data_producao, " +
                     "f.id AS f_id, f.nome AS f_nome, f.cpf AS f_cpf, f.cargo AS f_cargo, f.data_admissao AS f_admissao, " +
                     "u.id AS u_id, u.peca AS u_peca, u.tamanho AS u_tamanho, u.cor AS u_cor, u.material AS u_material, u.preco_base AS u_preco " +
                     "FROM registro_producao rp " +
                     "JOIN funcionario f ON rp.id_funcionario = f.id " +
                     "JOIN uniforme u ON rp.id_uniforme = u.id";
        
        List<RegistroProducao> lista = new ArrayList<>();
        
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                // Instancia e popula o Funcionário
                Funcionario f = new Funcionario();
                f.setId(rs.getInt("f_id"));
                f.setNome(rs.getString("f_nome"));
                f.setCpf(rs.getString("f_cpf"));
                f.setCargo(rs.getString("f_cargo"));
                f.setDataAdmissao(rs.getDate("f_admissao").toLocalDate());
                
                // Instancia e popula o Uniforme
                Uniforme u = new Uniforme();
                u.setId(rs.getInt("u_id"));
                u.setPeca(rs.getString("u_peca"));
                u.setTamanho(rs.getString("u_tamanho"));
                u.setCor(rs.getString("u_cor"));
                u.setMaterial(rs.getString("u_material"));
                u.setPrecoBase(rs.getDouble("u_preco"));
                
                // Instancia e popula o Registro de Produção
                RegistroProducao rp = new RegistroProducao();
                rp.setId(rs.getInt("rp_id"));
                rp.setFuncionario(f);
                rp.setUniforme(u);
                rp.setQuantidade(rs.getInt("quantidade"));
                rp.setDataProducao(rs.getDate("data_producao").toLocalDate());
                
                lista.add(rp);
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao listar registros de produção: " + e.getMessage());
        }
        
        return lista;
    }

    // Excluir Registro de Produção (Delete)

    /**
     * Exclui um registro de produção pelo seu identificador.
     *
     * @param id identificador do registro de produção a ser excluído
     */
    public void excluir(int id) {
        String sql = "DELETE FROM registro_producao WHERE id = ?";
        
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Registro de produção excluído com sucesso!");
            
        } catch (SQLException e) {
            System.err.println("Erro ao excluir registro de produção: " + e.getMessage());
        }
    }
}