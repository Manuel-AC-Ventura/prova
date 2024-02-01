import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddProductDialog extends JDialog {

    private JTextField productNameField;
    private JTextField productQuantityField;
    private JTextField productPriceField;
    private boolean productAdded;

    public AddProductDialog(OptionsFrame owner) {
        super();

        setSize(300, 200);
        setLocationRelativeTo(owner);

        // Inicializar campos e variáveis
        productNameField = new JTextField();
        productQuantityField = new JTextField();
        productPriceField = new JTextField();
        productAdded = false;

        // Criar layout
        setLayout(new GridLayout(4, 2));

        // Adicionar componentes ao diálogo
        add(new JLabel("Nome:"));
        add(productNameField);
        add(new JLabel("Quantidade:"));
        add(productQuantityField);
        add(new JLabel("Preço:"));
        add(productPriceField);

        JButton addButton = new JButton("Adicionar");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Lógica para adicionar produto
                try {
                    String name = getProductName();
                    int quantity = getProductQuantity();
                    double price = Double.parseDouble(getProductPrice());

                    // Chamar o método de adicionar produto no OptionsFrame
                    owner.addProductToDatabase(name, quantity, price);
                    productAdded = true;
                    dispose();  // Fechar o diálogo
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(AddProductDialog.this, "Por favor, insira um valor válido para o preço.");
                }
            }
        });

        add(addButton);

        // Configurar o botão de cancelar
        JButton cancelButton = new JButton("Cancelar");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Fechar o diálogo sem adicionar produto
                dispose();
            }
        });
        add(cancelButton);
    }

    // Métodos para obter dados do produto
    public String getProductName() {
        return productNameField.getText();
    }

    public int getProductQuantity() {
        // Adicione a lógica para converter o texto em um número inteiro
        return Integer.parseInt(productQuantityField.getText());
    }

    public String getProductPrice() {
        return productPriceField.getText();
    }

    // Método para verificar se o produto foi adicionado
    public boolean isProductAdded() {
        return productAdded;
    }
}
