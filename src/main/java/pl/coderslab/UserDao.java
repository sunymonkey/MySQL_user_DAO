package pl.coderslab;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDao {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/%s";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "coderslab";
    private static final String DB_NAME = "workshop2";

    public void create(User user) {
        String insertNewUser = "INSERT INTO users(username, email, password) VALUES(?, ?, ?)";
        try (Connection connection = connect(DB_NAME)) {
            PreparedStatement prepStmt = connection.prepareStatement(insertNewUser);
            prepStmt.setString(1, user.getUserName());
            prepStmt.setString(2, user.getEmail());
            String password = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            prepStmt.setString(3, password);
            prepStmt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    private static Connection connect(String database) throws SQLException {
        String url = String.format(DB_URL, database);
        Connection connection = DriverManager.getConnection(url, DB_USER, DB_PASS);
        return connection;
    }



}
