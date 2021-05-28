package pl.coderslab;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtil {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/%s";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "coderslab";

    public static Connection connect(String database) throws SQLException {
        String url = String.format(DB_URL, database);
        Connection connection = DriverManager.getConnection(url, DB_USER, DB_PASS);
        return connection;
    }
}
