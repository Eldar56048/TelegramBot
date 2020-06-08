package services.interfaces;

import domain.models.Product;

import java.util.ArrayList;

public interface IProductService{
    Product getProductByID(long id);

    ArrayList<Product> getProductByName(String name);

    void addProduct(Product product);

    void updateProduct(Product product);
}
