package panificadora.utilities;

import panificadora.model.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    // Método para inserir cliente
    public boolean inserirCliente(Cliente cliente) {
        String sql = "INSERT INTO Cliente (nome, email, telefone, cpf, data_cadastro) VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getEmail());
            stmt.setString(3, cliente.getTelefone());
            stmt.setString(4, cliente.getCpf());

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                // Recuperando o ID gerado automaticamente
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        cliente.setIdCliente(rs.getInt(1));  // Atribuindo o ID gerado ao cliente
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao inserir cliente: " + e.getMessage());
        }
        return false;
    }

    // Método para listar clientes
    public List<Cliente> listarClientes() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT id_cliente, nome, email, telefone, cpf, data_cadastro FROM Cliente"; // Ajustado para refletir a estrutura correta
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getInt("id_cliente"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("telefone"),
                        rs.getString("cpf"),
                        rs.getTimestamp("data_cadastro")
                );
                clientes.add(cliente);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar clientes: " + e.getMessage());
        }
        return clientes;
    }

    // Método para buscar cliente por ID
    public Cliente buscarClientePorId(int idCliente) {
        String sql = "SELECT id_cliente, nome, email, telefone, cpf, data_cadastro FROM Cliente WHERE id_cliente = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCliente);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Cliente(
                        rs.getInt("id_cliente"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("telefone"),
                        rs.getString("cpf"),
                        rs.getTimestamp("data_cadastro")
                );
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar cliente por ID: " + e.getMessage());
        }
        return null;
    }

    // Método para atualizar cliente
    public boolean atualizarCliente(Cliente cliente) {
        String sql = "UPDATE Cliente SET nome = ?, email = ?, telefone = ?, cpf = ? WHERE id_cliente = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getEmail());
            stmt.setString(3, cliente.getTelefone());
            stmt.setString(4, cliente.getCpf());
            stmt.setInt(5, cliente.getIdCliente());

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar cliente: " + e.getMessage());
            return false;
        }
    }

    // Método para excluir cliente
    public boolean excluirCliente(int idCliente) {
        String sql = "DELETE FROM Cliente WHERE id_cliente = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCliente);
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao excluir cliente: " + e.getMessage());
            return false;
        }
    }

    // Método para buscar cliente por CPF
    public Cliente buscarClientePorCpf(String cpf) {
        String sql = "SELECT id_cliente, nome, email, telefone, cpf, data_cadastro FROM Cliente WHERE cpf = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Cliente(
                        rs.getInt("id_cliente"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("telefone"),
                        rs.getString("cpf"),
                        rs.getTimestamp("data_cadastro")
                );
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar cliente por CPF: " + e.getMessage());
        }
        return null;
    }

    // Método para buscar cliente por nome
    public List<Cliente> buscarClientePorNome(String nome) {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT id_cliente, nome, email, telefone, cpf, data_cadastro FROM Cliente WHERE nome LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + nome + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getInt("id_cliente"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("telefone"),
                        rs.getString("cpf"),
                        rs.getTimestamp("data_cadastro")
                );
                clientes.add(cliente);
            }
            rs.close();
        } catch (SQLException e) {
            System.err.println("Erro ao buscar cliente por nome: " + e.getMessage());
        }
        return clientes;
    }

    // Método para buscar cliente por telefone
    public List<Cliente> buscarClientePorTelefone(String telefone) {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT id_cliente, nome, email, telefone, cpf, data_cadastro FROM Cliente WHERE telefone LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + telefone + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getInt("id_cliente"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("telefone"),
                        rs.getString("cpf"),
                        rs.getTimestamp("data_cadastro")
                );
                clientes.add(cliente);
            }
            rs.close();
        } catch (SQLException e) {
            System.err.println("Erro ao buscar cliente por telefone: " + e.getMessage());
        }
        return clientes;
    }

    // Método para buscar cliente por email
    public Cliente buscarClientePorEmail(String email) {
        String sql = "SELECT id_cliente, nome, email, telefone, cpf, data_cadastro FROM Cliente WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Cliente(
                        rs.getInt("id_cliente"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("telefone"),
                        rs.getString("cpf"),
                        rs.getTimestamp("data_cadastro")
                );
            }
            rs.close();
        } catch (SQLException e) {
            System.err.println("Erro ao buscar cliente por email: " + e.getMessage());
        }
        return null;
    }
}
