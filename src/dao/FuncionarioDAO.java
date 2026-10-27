package dao;

import dao.Conexao;
import model.Funcionario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioDAO {

    // Método para Inserir (Create)
    public void inserir(Funcionario f) {
        String sql = "INSERT INTO funcionario (nome, cpf, cargo, data_admissao) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, f.getNome());
            stmt.setString(2, f.getCpf());
            stmt.setString(3, f.getCargo());
            stmt.setDate(4, Date.valueOf(f.getDataAdmissao()));
            
            stmt.executeUpdate();
            System.out.println("Funcionário cadastrado com sucesso no banco de dados!");
            
        } catch (SQLException e) {
            System.err.println("Erro ao inserir funcionário: " + e.getMessage());
        }
    }

    // Método para Listar todos (Read)
    public List<Funcionario> listar() {
        String sql = "SELECT * FROM funcionario";
        List<Funcionario> lista = new ArrayList<>();
        
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Funcionario f = new Funcionario();
                f.setId(rs.getInt("id"));
                f.setNome(rs.getString("nome"));
                f.setCpf(rs.getString("cpf"));
                f.setCargo(rs.getString("cargo"));
                f.setDataAdmissao(rs.getDate("data_admissao").toLocalDate());
                
                lista.add(f);
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao listar funcionários: " + e.getMessage());
        }
        
        return lista;
    }

    // Método para Atualizar (Update)
    public void atualizar(Funcionario f) {
        String sql = "UPDATE funcionario SET nome = ?, cpf = ?, cargo = ?, data_admissao = ? WHERE id = ?";
        
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, f.getNome());
            stmt.setString(2, f.getCpf());
            stmt.setString(3, f.getCargo());
            stmt.setDate(4, Date.valueOf(f.getDataAdmissao()));
            stmt.setInt(5, f.getId());
            
            stmt.executeUpdate();
            System.out.println("Funcionário atualizado com sucesso!");
            
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar funcionário: " + e.getMessage());
        }
    }

    // Método para Excluir (Delete)
    public void excluir(int id) {
        String sql = "DELETE FROM funcionario WHERE id = ?";
        
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Funcionário excluído com sucesso!");
            
        } catch (SQLException e) {
            System.err.println("Erro ao excluir funcionário (verifique se há registros de produção vinculados): " + e.getMessage());
        }
    }
}