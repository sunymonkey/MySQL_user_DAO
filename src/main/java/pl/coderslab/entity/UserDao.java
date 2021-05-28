package pl.coderslab.entity;

import org.mindrot.jbcrypt.BCrypt;
import pl.coderslab.DbUtil;

import java.sql.*;
import java.util.Arrays;

public class UserDao {

    private static final String DB_NAME = "workshop2";

    private static final String CREATE_USER_QUERY = "INSERT INTO users(username, email, password) VALUES(?, ?, ?)";
    private static final String FIND_USER_QUERY = "SELECT * FROM users WHERE id = ?";
    private static final String FIND_NAME_USER_QUERY = "SELECT * FROM users WHERE username = ?";
    private static final String FIND_EMAIL_USER_QUERY = "SELECT * FROM users WHERE email = ?";
    private static final String FIND_ALL_USER_QUERY = "SELECT * FROM users";
    private static final String UPDATE_USER_QUERY = "UPDATE users SET email = ?, username = ?, password = ? WHERE id = ?";
    private static final String DELETE_USER_QUERY = "DELETE FROM users WHERE id = ?";

    public void create(User user) {
        try (Connection connection = DbUtil.connect(DB_NAME);
             PreparedStatement prepStmt = connection.prepareStatement(CREATE_USER_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            prepStmt.setString(1, user.getUserName());
            prepStmt.setString(2, user.getEmail());
            String password = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            prepStmt.setString(3, password);
            prepStmt.executeUpdate();

            ResultSet resultSet = prepStmt.getGeneratedKeys();
            if (resultSet.next()){
                user.setId(resultSet.getInt(1));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public User read(int id) {
        try (Connection connection = DbUtil.connect(DB_NAME)){
            PreparedStatement prepStmt = connection.prepareStatement(FIND_USER_QUERY);
            prepStmt.setInt(1, id);
            ResultSet resultSet = prepStmt.executeQuery();
            while (resultSet.next()) {
                int identify = resultSet.getInt("id");
                String email = resultSet.getString("email");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                return new User(identify, email, username, password);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
        return null;
    }

    public boolean update(User user) {
        try (Connection connection = DbUtil.connect(DB_NAME)) {
            PreparedStatement prepStmt = connection.prepareStatement(UPDATE_USER_QUERY);
            prepStmt.setString(1, user.getEmail());
            prepStmt.setString(2, user.getUserName());
            String password = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            prepStmt.setString(3, password);
            prepStmt.setInt(4, user.getId());
            prepStmt.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }


    public User[] findAll() {
        User[] users = new User[0];
        try (Connection connection = DbUtil.connect(DB_NAME)) {
            PreparedStatement prepStmt = connection.prepareStatement(FIND_ALL_USER_QUERY);
            return getUsers(users, prepStmt);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public boolean delete(int id) {
        try (Connection connection = DbUtil.connect(DB_NAME)) {
            PreparedStatement prepStmt = connection.prepareStatement(DELETE_USER_QUERY);
            prepStmt.setInt(1, id);
            prepStmt.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public User[] findName(String name) {
        return getUsers(name, FIND_NAME_USER_QUERY);
    }

    public User[] findEmail(String email) {
        return getUsers(email, FIND_EMAIL_USER_QUERY);
    }

    private User[] getUsers(String name, String findNameUserQuery) {
        User[] users = new User[0];
        try (Connection connection = DbUtil.connect(DB_NAME)) {
            PreparedStatement prepStmt = connection.prepareStatement(findNameUserQuery);
            prepStmt.setString(1, name);
            return getUsers(users, prepStmt);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    private User[] getUsers(User[] users, PreparedStatement prepStmt) throws SQLException {
        ResultSet resultSet = prepStmt.executeQuery();
        while (resultSet.next()) {
            users = Arrays.copyOf(users, users.length + 1);
            int identify = resultSet.getInt("id");
            String email = resultSet.getString("email");
            String username = resultSet.getString("username");
            String password = resultSet.getString("password");
            users[users.length - 1] = new User(identify, email, username, password);
        }
        return users;
    }

}
