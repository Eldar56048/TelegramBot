package repositories;

import domain.models.Orders;
import repositories.interfaces.IDBRepository;
import repositories.interfaces.IOrderRepository;

import javax.ws.rs.BadRequestException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

public class OrderRepository implements IOrderRepository {
    private IDBRepository dbrepo = new PostgresRepository();

    @Override
    public void add(String korzina, String getType,String adress, String phone, String status, String oplata) {
        try {
            String sql = "INSERT INTO orders(korzina,getType,adress,phone,status,oplata) " +
                    "VALUES(?, ?, ?,?, ?,?)";
            PreparedStatement stmt = dbrepo.getConnection().prepareStatement(sql);
            stmt.setString(1,korzina);
            stmt.setString(2,getType);
            stmt.setString(3, adress);
            stmt.setString(4,phone);
            stmt.setString(5, status);
            stmt.setString(6, oplata);
            stmt.execute();
        } catch (SQLException ex) {
            throw new BadRequestException("Cannot run SQL statement: " + ex.getMessage());
        }
    }

    @Override
    public void add(Orders entity) {

    }

    @Override
    public void update(Orders entity) {
        String sql = "UPDATE order " +
                "SET ";
        int c = 0;
        if (entity.getKorzina()!=null) {
            sql += "korzina=?, "; c++;
        }
        if (entity.getGetType()!=null) {
            sql += "getType=?, "; c++;
        }
        if (entity.getAdress()!=null) {
            sql += "adress=?, "; c++;
        }  if (entity.getPhone()!=null) {
            sql += "phone=?, "; c++;
        }
        if (entity.getStatus()!=null) {
            sql += "status=?, "; c++;
        }
        if (entity.getOplata()!=null) {
            sql += "oplata=?, "; c++;
        }


        sql = sql.substring(0, sql.length() - 2);

        sql += " WHERE OrderID = ?";

        try {
            int i = 1;
            PreparedStatement stmt = dbrepo.getConnection().prepareStatement(sql);
            if (entity.getKorzina()!=null) {
                stmt.setString(i++, entity.getKorzina());
            }
            if (entity.getGetType()!=null) {
                stmt.setString(i++, entity.getGetType());
            }
            if (entity.getAdress()!=null) {
                stmt.setString(i++, entity.getAdress());
            }  if (entity.getPhone()!=null) {
                stmt.setString(i++, entity.getPhone());
            }
            if (entity.getStatus()!=null) {

                stmt.setString(i++, entity.getStatus());
            }
            if (entity.getOplata()!=null) {
                stmt.setString(i++, entity.getOplata());
            }


            stmt.setLong(i++, entity.getOrder_id());

            stmt.execute();
        } catch (SQLException ex) {
            throw new BadRequestException("Cannot run SQL statement: " + ex.getMessage());
        }
    }

    @Override
    public void remove(Orders entity) {
        try {
            String sql = "Delete from order WHERE OrderID = ?";
            PreparedStatement stmt = dbrepo.getConnection().prepareStatement(sql);
            stmt.execute();
        } catch (SQLException ex) {
            throw new BadRequestException("Cannot run SQL statement: " + ex.getMessage());
        }
    }

    @Override
    public List<Orders> query(String sql) {
        try {
            Statement stmt = dbrepo.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            LinkedList<Orders> orders = new LinkedList<>();
            while (rs.next()) {
                Orders order = new Orders(
                       rs.getInt("OrderID"),
                        rs.getString("korzina"),
                        rs.getString("getType"),
                        rs.getString("adress"),
                        rs.getString("phone"),
                        rs.getString("status"),
                        rs.getString("oplata")
                          );
                orders.add(order);
            }
            return orders;
        } catch (SQLException ex) {
            throw new BadRequestException("Cannot run SQL statement: " + ex.getSQLState());
        }
    }

    @Override
    public Orders queryOne(String sql) {
        try {
            Statement stmt = dbrepo.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            LinkedList<Orders> orders = new LinkedList<>();
            while (rs.next()) {
                Orders order = new Orders(
                        rs.getInt("OrderID"),
                        rs.getString("korzina"),
                        rs.getString("getType"),
                        rs.getString("adress"),
                        rs.getString("phone"),
                        rs.getString("status"),
                        rs.getString("oplata")
                );
                orders.add(order);
            }
            System.out.println("Order created successfully");
        } catch (SQLException ex) {
            throw new BadRequestException("Cannot run SQL statement: " + ex.getSQLState());
        }
        return null;
    }
}
