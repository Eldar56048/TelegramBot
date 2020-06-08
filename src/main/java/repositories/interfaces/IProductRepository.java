package repositories.interfaces;

import domain.models.Product;

import java.util.ArrayList;

public interface IProductRepository  extends  IEntityRepository<Product>{
    Product getProductByID(long id);

    ArrayList<Product> getProductByName(String name);
}
