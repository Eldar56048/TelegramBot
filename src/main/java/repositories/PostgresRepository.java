package repositories;

import repositories.interfaces.IDBRepository;

import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresRepository implements IDBRepository {

    public Connection getConnection() {
        try {
            String connStr = "jdbc:mysql://localhost/myapp?serverTimezone=Europe/Moscow&useSSL=false";


            return DriverManager.getConnection(connStr, "root", "");


        } catch (SQLException ex) {
            throw new ServerErrorException("Cannot connect to DB: " + ex.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
}
