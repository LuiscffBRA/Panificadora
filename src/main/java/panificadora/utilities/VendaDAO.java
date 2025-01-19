package panificadora.utilities;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import panificadora.model.Venda;

public class VendaDAO {

    private Connection connection;

    public VendaDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean inserirVenda(Venda venda) {
        String sql = "INSERT INTO venda (id_cliente, data_venda, valor_total) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, venda.getIdCliente());
            stmt.setTimestamp(2, Timestamp.valueOf(venda.getDataVenda()));
            stmt.setDouble(3, venda.getValorTotal());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    venda.setIdVenda(generatedKeys.getInt(1)); // Definir o ID gerado automaticamente
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean atualizarVenda(Venda venda) {
        String sql = "UPDATE venda SET id_cliente = ?, data_venda = ?, valor_total = ? WHERE id_venda = ?";  // Corrigido para 'venda'
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, venda.getIdCliente());
            stmt.setTimestamp(2, Timestamp.valueOf(venda.getDataVenda()));
            stmt.setDouble(3, venda.getValorTotal());
            stmt.setInt(4, venda.getIdVenda());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean excluirVenda(int idVenda) {
        String sql = "DELETE FROM venda WHERE id_venda = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idVenda);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Venda obterVenda(int idVenda) {
        String sql = "SELECT v.id_venda, c.nome AS nome_cliente, v.data_venda, v.valor_total " +
                "FROM venda v " +
                "JOIN cliente c ON v.id_cliente = c.id_cliente " +
                "WHERE v.id_venda = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idVenda);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String nomeCliente = rs.getString("nome_cliente"); // Pegando o nome do cliente
                Timestamp dataVendaTimestamp = rs.getTimestamp("data_venda");
                LocalDateTime dataVenda = dataVendaTimestamp.toLocalDateTime(); // Convertendo Timestamp para LocalDateTime
                double valorTotal = rs.getDouble("valor_total");

                return new Venda(idVenda, rs.getInt("id_cliente"), nomeCliente, dataVenda, valorTotal); // Passando o nome do cliente
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<Venda> obterTodasVendas() {
        String sql = "SELECT v.id_venda, c.nome AS nome_cliente, v.data_venda, v.valor_total " +
                "FROM venda v " +
                "JOIN cliente c ON v.id_cliente = c.id_cliente";
        List<Venda> vendas = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int idVenda = rs.getInt("id_venda");
                String nomeCliente = rs.getString("nome_cliente"); // Pegando o nome do cliente
                Timestamp dataVendaTimestamp = rs.getTimestamp("data_venda");
                LocalDateTime dataVenda = dataVendaTimestamp.toLocalDateTime(); // Convertendo Timestamp para LocalDateTime
                double valorTotal = rs.getDouble("valor_total");

                vendas.add(new Venda(idVenda, rs.getInt("id_cliente"), nomeCliente, dataVenda, valorTotal)); // Passando o nome do cliente
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return vendas;
    }

}
