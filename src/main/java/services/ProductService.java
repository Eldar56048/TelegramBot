package services;

import domain.models.Product;
import repositories.ProductRepository;
import repositories.interfaces.IProductRepository;
import services.interfaces.IProductService;

import java.util.ArrayList;

public class ProductService implements IProductService {
    private IProductRepository productRepo = new ProductRepository();
    @Override
    public Product getProductByID(long id) {
        return productRepo.getProductByID(id);
    }

    @Override
    public ArrayList<Product> getProductByName(String name) {
        return productRepo.getProductByName(name);
    }

    @Override
    public void addProduct(Product product) {
        productRepo.add(product);
    }

    @Override
    public void updateProduct(Product product) {
        productRepo.update(product);
    }
}
