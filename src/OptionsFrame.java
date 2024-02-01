import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.*;
import java.util.List;

public class OptionsFrame extends JInternalFrame implements ProductOperations {

    private JTable table;
    private DefaultTableModel tableModel;

    // Informações do banco de dados (substitua com suas configurações)
    private static final String DB_URL = "jdbc:mysql://localhost:3306/Stocks";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "123";

    public OptionsFrame() {
        super("Opções", true, true, true, true);
        setSize(600, 400);

        setResizable(true);
        setMaximizable(true);
        setIconifiable(false);

        // Criar modelo da tabela
        String[] columnNames = {"ID", "Nome", "Quantidade", "Preço", "Editar", "Apagar"};
        tableModel = new DefaultTableModel(columnNames, 0);

        // Criar tabela com modelo personalizado
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Adicionar botões de ação à tabela
        ButtonColumn editButtonColumn = new ButtonColumn(table, editProductAction, deleteProductAction, 4, 5);

        // Adicionar tabela ao painel
        getContentPane().add(scrollPane);

        // Adicionar botão para adicionar produto
        JButton addProductButton = new JButton("Adicionar Produto");
        addProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Lógica para abrir o diálogo de adição de produto
                AddProductDialog dialog = new AddProductDialog(OptionsFrame.this);
                dialog.setVisible(true);

                // Obter dados do diálogo e adicionar à tabela (substituir com lógica real)
                if (dialog.isProductAdded()) {
                    // Adicionar produto ao banco de dados
                    addProductToDatabase(dialog.getProductName(), dialog.getProductQuantity(), Double.parseDouble(dialog.getProductPrice()));

                    // Recarregar dados da tabela após adição
                    loadProductsFromDatabase();
                }
            }
        });

        // Adicionar atalho de teclado global para o botão "Adicionar Produto"
        addProductButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK), "Adicionar Produto");
        addProductButton.getActionMap().put("Adicionar Produto", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Chame a lógica de adicionar produto aqui
                JOptionPane.showMessageDialog(OptionsFrame.this, "Atalho de teclado: Adicionar Produto");
            }
        });

        // Adicionar botão ao painel
        getContentPane().add(addProductButton, BorderLayout.NORTH);

        // Carregar dados iniciais do banco de dados
        loadProductsFromDatabase();
    }

    // Adiciona um produto ao banco de dados (substituir com sua lógica real)
    public void addProductToDatabase(String name, int quantity, double price) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "INSERT INTO produtos (nome, quantidade, preco) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, name);
                preparedStatement.setInt(2, quantity);
                preparedStatement.setDouble(3, price);
                preparedStatement.executeUpdate();

                // Obter o ID gerado para o novo produto
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int productId = generatedKeys.getInt(1);
                        System.out.println("Novo produto adicionado com ID: " + productId);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao adicionar produto ao banco de dados.");
        }
    }

    // Edita um produto no banco de dados (substituir com sua lógica real)
    @Override
    public void editProductInDatabase(String name, int quantity, double price) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "UPDATE produtos SET quantidade=?, preco=? WHERE nome=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, quantity);
                preparedStatement.setDouble(2, price);
                preparedStatement.setString(3, name);

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao editar produto no banco de dados.");
        }
    }

    // Carrega os produtos da tabela do banco de dados
    @Override
    public List<Product> loadProductsFromDatabase() {
        tableModel.setRowCount(0); // Limpar dados existentes

        // Implementar lógica de consulta no banco de dados aqui
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT * FROM produtos";
            try (Statement statement = connection.createStatement()) {
                try (ResultSet resultSet = statement.executeQuery(query)) {
                    while (resultSet.next()) {
                        int id = resultSet.getInt("id");
                        String name = resultSet.getString("nome");
                        int quantity = resultSet.getInt("quantidade");
                        double price = resultSet.getDouble("preco");

                        // Adicionar produto à tabela
                        Object[] rowData = {id, name, quantity, price};
                        tableModel.addRow(rowData);
                    }
                }
            }

            // Notificar a tabela sobre as mudanças após adicionar os dados
            tableModel.fireTableDataChanged();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar produtos do banco de dados.");
        }
        return null;
    }

    // Ação para o botão de editar
    private final Action editProductAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = Integer.parseInt(e.getActionCommand());
            JOptionPane.showMessageDialog(OptionsFrame.this, "Editar produto na linha " + selectedRow);
        }
    };

    // Ação para o botão de apagar
    private final Action deleteProductAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = Integer.parseInt(e.getActionCommand());
            int option = JOptionPane.showConfirmDialog(OptionsFrame.this, "Deseja realmente apagar o produto?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                // Lógica para apagar o produto do banco de dados
                int productId = (int) tableModel.getValueAt(selectedRow, 0);
                deleteProductFromDatabase(productId);
                // Atualizar a tabela após a exclusão
                loadProductsFromDatabase();
            }
        }
    };

    // Exclui um produto do banco de dados
    @Override
    public void deleteProductFromDatabase(int productId) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "DELETE FROM produtos WHERE id=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, productId);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao excluir produto do banco de dados.");
        }
    }
}
