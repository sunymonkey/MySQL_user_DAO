package pl.coderslab.entity;

import org.mindrot.jbcrypt.BCrypt;
import pl.coderslab.DbUtil;

import java.sql.*;

public class UserDao {

    private static final String DB_NAME = "workshop2";

    private static final String CREATE_USER_QUERY = "INSERT INTO users(username, email, password) VALUES(?, ?, ?)";
    private static final String FIND_USER_QUERY = "SELECT * FROM users WHERE id = ?";
    private static final String UPDATE_USER_QUERY = "UPDATE users SET email = ?, username = ?, password = ? WHERE id = ?";

    public User create(User user) {
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
            return user;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
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

    public void update(User user) {
        try (Connection connection = DbUtil.connect(DB_NAME)) {
            PreparedStatement prepStmt = connection.prepareStatement(UPDATE_USER_QUERY);
            prepStmt.setString(1, user.getEmail());
            prepStmt.setString(2, user.getUserName());
            String password = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            prepStmt.setString(3, password);
            prepStmt.setInt(4, user.getId());
            prepStmt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
