package dao;

import dao.Conexao;
import model.Uniforme;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UniformeDAO {

    // Inserir Uniforme (Create)
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