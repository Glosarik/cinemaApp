package cinemaApi.repository.user;

import cinemaApi.util.Utils;
import cinemaApi.model.User;
import cinemaApi.model.UserDTO;
import cinemaApi.util.UserRole;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static cinemaApi.util.Constants.*;

public class UserRepositoryImpl implements UserRepository {
    private final Connection connection;

    public UserRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    public void addUser(UserDTO user) {
        executeUpdate(SQL_INSERT_USER,
                user.getLogin(), user.getPassword(), user.getEmail(), user.getUuid().toString(),
                user.getUserRole().name(), user.getRegistrationDate() != null ? Timestamp.valueOf(user.
                        getRegistrationDate()) : Timestamp.valueOf(Utils.CURRENT_DATE));
    }

    @Override
    public User getUserById(int userId) {
        return getUser(SQL_SELECT_USER_BY_ID, userId);
    }

    @Override
    public User getUserByLogin(String login) {
        return getUser(SQL_SELECT_USER_BY_LOGIN, login);
    }

    @Override
    public User getUserByEmail(String email) {
        return getUser(SQL_SELECT_USER_BY_EMAIL, email);
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users";
        try (PreparedStatement statement = connection.prepareStatement(query); ResultSet resultSet = statement.
                executeQuery()) {
            while (resultSet.next()) users.add(getUserFromResultSet(resultSet));
        } catch (SQLException e) {
            throw new RuntimeException(FETCH_USERS_ERROR, e);
        }
        return users;
    }

    @Override
    public void updateUser(UserDTO user) {
        executeUpdate(SQL_UPDATE_USER,
                user.getLogin(), user.getPassword(), user.getEmail(), user.getUserRole().name(),
                user.getRegistrationDate() != null ? Timestamp.valueOf(user.getRegistrationDate()) : Timestamp.
                        valueOf(Utils.CURRENT_DATE), user.getUserId());
    }

    @Override
    public void deleteUser(int userId) {
        String query = "DELETE FROM users WHERE userId = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private User getUserFromResultSet(ResultSet resultSet) throws SQLException {
        return User.builder()
                .userId(resultSet.getInt(SQL_USER_ID))
                .login(resultSet.getString(SQL_LOGIN))
                .password(resultSet.getString(SQL_PASSWORD))
                .email(resultSet.getString(SQL_EMAIL))
                .userRole(UserRole.valueOf(resultSet.getString(SQL_USER_ROLE)))
                .registrationDate(resultSet.getTimestamp(SQL_REGISTRATION_DATE).toLocalDateTime())
                .build();
    }

    private void executeUpdate(String query, Object... parameters) {
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            for (int i = ZERO; i < parameters.length; i++) {
                statement.setObject(i + ONE, parameters[i]);
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(UPDATE_ERROR, e);
        }
    }

    private User getUser(String query, Object parameter) {
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(ONE, parameter);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) return getUserFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(FETCH_USER_ERROR, e);
        }
        return null;
    }
}