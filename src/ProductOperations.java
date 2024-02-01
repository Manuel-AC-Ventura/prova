import java.util.List;

public interface ProductOperations {
    void addProductToDatabase(String name, int quantity, double price);
    void editProductInDatabase(String name, int quantity, double price);
    void deleteProductFromDatabase(int productId);
    <Product> List<Product> loadProductsFromDatabase();
}
