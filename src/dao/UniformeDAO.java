package dao;

import dao.Conexao;
import model.Uniforme;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) responsável pelas operações de persistência da
 * entidade {@link Uniforme}, na tabela {@code uniforme}.
 * <p>
 * Utiliza consultas parametrizadas ({@link PreparedStatement}) e
 * try-with-resources. Erros de SQL são tratados internamente e exibidos em
 * {@code System.err}, sem serem propagados ao chamador.
 * </p>
 */
public class UniformeDAO {

    // Inserir Uniforme (Create)

    /**
     * Insere um novo uniforme no banco de dados.
     * <p>
     * O {@code id} é gerado pelo banco e, portanto, não é utilizado.
     * </p>
     *
     * @param u uniforme a ser cadastrado (peça, tamanho, cor, material e preço base)
     */
    public void inserir(Uniforme u) {
        String sql = "INSERT INTO uniforme (peca, tamanho, cor, material, preco_base) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, u.getPeca());
            stmt.setString(2, u.getTamanho());
            stmt.setString(3, u.getCor());
            stmt.setString(4, u.getMaterial());
            stmt.setDouble(5, u.getPrecoBase());
            
            stmt.executeUpdate();
            System.out.println("Uniforme cadastrado com sucesso!");
            
        } catch (SQLException e) {
            System.err.println("Erro ao inserir uniforme: " + e.getMessage());
        }
    }

    // Listar Uniformes (Read)

    /**
     * Lista todos os uniformes cadastrados.
     *
     * @return lista com todos os uniformes; lista vazia caso não haja
     *         registros ou ocorra algum erro de acesso ao banco
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
                u.setPeca(rs.getString("peca"));
                u.setTamanho(rs.getString("tamanho"));
                u.setCor(rs.getString("cor"));
                u.setMaterial(rs.getString("material"));
                u.setPrecoBase(rs.getDouble("preco_base"));
                
                lista.add(u);
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao listar uniformes: " + e.getMessage());
        }
        
        return lista;
    }

    // Atualizar Uniforme (Update)

    /**
     * Atualiza peça, tamanho, cor, material e preço base de um uniforme existente.
     *
     * @param u uniforme com os dados atualizados; o {@code id} identifica
     *          qual registro será alterado
     */
    public void atualizar(Uniforme u) {
        String sql = "UPDATE uniforme SET peca = ?, tamanho = ?, cor = ?, material = ?, preco_base = ? WHERE id = ?";
        
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, u.getPeca());
            stmt.setString(2, u.getTamanho());
            stmt.setString(3, u.getCor());
            stmt.setString(4, u.getMaterial());
            stmt.setDouble(5, u.getPrecoBase());
            stmt.setInt(6, u.getId());
            
            stmt.executeUpdate();
            System.out.println("Uniforme atualizado com sucesso!");
            
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar uniforme: " + e.getMessage());
        }
    }

    // Excluir Uniforme (Delete)

    /**
     * Exclui um uniforme pelo seu identificador.
     * <p>
     * A exclusão falha (apenas registrada em {@code System.err}) caso existam
     * registros de produção vinculados ao uniforme, devido à chave estrangeira.
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