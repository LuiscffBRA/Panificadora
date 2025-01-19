package panificadora.utilities;

import panificadora.model.Produto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ProdutoDAO {

    private static final Logger logger = Logger.getLogger(ProdutoDAO.class.getName());

    // Método utilitário para log de erros
    private void logErro(String mensagem, SQLException e) {
        logger.severe(mensagem + ": " + e.getMessage());
    }

    // Inserir um novo produto
    public boolean inserirProduto(Produto produto) {
        String sql = "INSERT INTO Produto (nome, descricao, preco, quantidade_estoque) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.setDouble(3, produto.getPreco());
            stmt.setInt(4, produto.getQuantidadeEstoque());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        produto.setIdProduto(generatedKeys.getInt(1)); // Define o ID gerado no objeto
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            logErro("Erro ao inserir produto", e);
        }
        return false;
    }

    // Listar todos os produtos
    public List<Produto> listarProdutos() {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM Produto";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                produtos.add(new Produto(
                        rs.getInt("id_produto"),
                        rs.getString("nome"),
                        rs.getString("descricao"),
                        rs.getDouble("preco"),
                        rs.getInt("quantidade_estoque")
                ));
            }
        } catch (SQLException e) {
            logErro("Erro ao listar produtos", e);
        }
        return produtos;
    }

    // Atualizar um produto existente
    public boolean atualizarProduto(Produto produto) {
        String sql = "UPDATE Produto SET nome = ?, descricao = ?, preco = ?, quantidade_estoque = ? WHERE id_produto = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.setDouble(3, produto.getPreco());
            stmt.setInt(4, produto.getQuantidadeEstoque());
            stmt.setInt(5, produto.getIdProduto());

            return stmt.executeUpdate() > 0; // Retorna true se o produto for atualizado com sucesso
        } catch (SQLException e) {
            logErro("Erro ao atualizar produto", e);
            return false;
        }
    }

    // Excluir um produto
    public boolean excluirProduto(int idProduto) {
        String sql = "DELETE FROM Produto WHERE id_produto = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idProduto);
            return stmt.executeUpdate() > 0; // Retorna true se o produto for excluído com sucesso
        } catch (SQLException e) {
            logErro("Erro ao excluir produto", e);
            return false;
        }
    }
}
