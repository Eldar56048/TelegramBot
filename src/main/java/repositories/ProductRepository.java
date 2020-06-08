package repositories;

import domain.models.Product;
import repositories.interfaces.IDBRepository;
import repositories.interfaces.IProductRepository;

import javax.ws.rs.BadRequestException;
import java.sql.*;
import java.util.ArrayList;

public class ProductRepository implements IProductRepository {
    private IDBRepository dbrepo = new PostgresRepository();

    @Override
    public void add(Product entity) {

        try {
            String sql = "INSERT INTO product(name, price, structure, photoUrl) " +
                    "VALUES(?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = dbrepo.getConnection().prepareStatement(sql);
            stmt.setString(1, entity.getName());
            stmt.setInt(2, entity.getPrice());
            stmt.setString(3, entity.getStructure());
            stmt.setString(4, entity.getPhotoUrl());
            stmt.execute();
        } catch (SQLException ex) {
            throw new BadRequestException("Cannot run SQL statement: " + ex.getMessage());
        }
    }

    @Override
    public void update(Product entity) {
        String sql = "UPDATE product " +
                "SET ";
        int c = 0;
        if (entity.getName() != null) {
            sql += "name=?, "; c++;
        }
        if (entity.getPrice()>0) {
            sql += "price=?, "; c++;
        }
        if (entity.getStructure() != null) {
            sql += "structure=?, "; c++;
        }
        if (entity.getPhotoUrl() != null) {
            sql += "photoUrl=?, "; c++;
        }

        sql = sql.substring(0, sql.length() - 2);

        sql += " WHERE name = ?";

        try {
            int i = 1;
            PreparedStatement stmt = dbrepo.getConnection().prepareStatement(sql);

            if (entity.getPrice() >0) {
                stmt.setInt(i++, entity.getPrice());
            }
            if (entity.getStructure() != null) {
                stmt.setString(i++, entity.getStructure());
            }
            if (entity.getPhotoUrl() != null) {
                stmt.setString(i++, entity.getPhotoUrl());
            }
            stmt.setString(i++, entity.getName());

            stmt.execute();
        } catch (SQLException ex) {
            throw new BadRequestException("Cannot run SQL statement: " + ex.getMessage());
        }
    }

    @Override
    public void remove(Product entity) {
        try {
            String sql = "Delete from product WHERE name = ?";
            PreparedStatement stmt = dbrepo.getConnection().prepareStatement(sql);
            stmt.execute();
        } catch (SQLException ex) {
            throw new BadRequestException("Cannot run SQL statement: " + ex.getMessage());
        }
    }
    public Product getProductByID(long id) {
        String sql = "SELECT * FROM product WHERE id = " + id ;
        return queryOne(sql);
    }
    public ArrayList<Product> FillProduct() {
        String sql = "SELECT * FROM product  "  ;
        return query(sql);
    }
    public ArrayList<Product> getProductByName(String  name) {
        ArrayList<Product>products=new ArrayList<>();

            try (Connection conn = dbrepo.getConnection()) {
                Statement statement = conn.createStatement();
                ResultSet rs = statement.executeQuery("SELECT * FROM product WHERE  name="+"'"+name+"'");

                    while (rs.next()) {
                        Product product = new Product(

                                rs.getLong("id"),
                                rs.getString("name"),
                                rs.getInt("price"),
                                rs.getString("structure"),
                                rs.getString("photoUrl")
                        );
                        products.add(product);
                    }

            } catch (Exception ex) {
                System.out.println(ex);
            }


        return products;
    }
    @Override

    public ArrayList<Product> query(String sql) {

        try {
            Statement stmt = dbrepo.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ArrayList<Product> products = new ArrayList<>();
            while (rs.next()) {
                Product product = new Product(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getInt("price"),
                        rs.getString("structure"),
                        rs.getString("photoUrl")
                );
                products.add(product);
            }
            return products;
        } catch (SQLException ex) {
            throw new BadRequestException("Cannot run SQL statement: " + ex.getSQLState());
        }
    }
    @Override
    public Product queryOne(String sql) {
        try {
            Statement stmt = dbrepo.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return new Product(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getInt("price"),
                        rs.getString("structure"),
                        rs.getString("photoUrl")
                );
            }
        } catch (SQLException ex) {
            throw new BadRequestException("Cannot run SQL statement: " + ex.getMessage());
        }
        return null;
    }
}
