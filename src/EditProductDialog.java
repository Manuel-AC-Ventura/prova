import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditProductDialog extends JDialog {

    private JTextField productNameField;
    private JTextField productQuantityField;
    private JTextField productPriceField;
    private boolean productEdited;

    public EditProductDialog(JFrame owner, String currentName, int currentQuantity, double currentPrice) {
        super(owner, "Editar Produto", ModalityType.APPLICATION_MODAL);

        setSize(300, 200);
        setLocationRelativeTo(owner);

        // Inicializar campos e variáveis
        productNameField = new JTextField(currentName);
        productQuantityField = new JTextField(String.valueOf(currentQuantity));
        productPriceField = new JTextField(String.valueOf(currentPrice));
        productEdited = false;

        // Criar layout
        setLayout(new GridLayout(4, 2));

        // Adicionar componentes ao diálogo
        add(new JLabel("Nome:"));
        add(productNameField);
        add(new JLabel("Quantidade:"));
        add(productQuantityField);
        add(new JLabel("Preço:"));
        add(productPriceField);

        JButton editButton = new JButton("Editar");
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Lógica para editar produto
                try {
                    String name = getProductName();
                    int quantity = getProductQuantity();
                    double price = Double.parseDouble(getProductPrice());

                    // Encontrar o OptionsFrame a partir do owner usando SwingUtilities
                    OptionsFrame optionsFrame = findOptionsFrame(owner);

                    if (optionsFrame != null) {
                        optionsFrame.editProductInDatabase(name, quantity, price);
                        productEdited = true;
                        dispose();  // Fechar o diálogo
                    } else {
                        // Handle the case where OptionsFrame is not found
                        JOptionPane.showMessageDialog(EditProductDialog.this, "Não foi possível encontrar OptionsFrame.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(EditProductDialog.this, "Por favor, insira valores válidos.");
                }
            }
        });

        add(editButton);

        // Configurar o botão de cancelar
        JButton cancelButton = new JButton("Cancelar");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Fechar o diálogo sem editar produto
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

    // Método para verificar se o produto foi editado
    public boolean isProductEdited() {
        return productEdited;
    }

    // Método para encontrar o OptionsFrame a partir do owner usando SwingUtilities
    private OptionsFrame findOptionsFrame(Component owner) {
        if (owner instanceof OptionsFrame) {
            return (OptionsFrame) owner;
        } else if (owner instanceof JFrame) {
            return findOptionsFrame(((JFrame) owner).getContentPane());
        } else if (owner instanceof JDialog) {
            return findOptionsFrame(((JDialog) owner).getContentPane());
        } else if (owner instanceof JInternalFrame) {
            return findOptionsFrame(((JInternalFrame) owner).getContentPane());
        } else if (owner instanceof JRootPane) {
            return findOptionsFrame(((JRootPane) owner).getContentPane());
        } else if (owner instanceof Container) {
            Component[] components = ((Container) owner).getComponents();
            for (Component component : components) {
                OptionsFrame optionsFrame = findOptionsFrame(component);
                if (optionsFrame != null) {
                    return optionsFrame;
                }
            }
        }
        return null;
    }
}
