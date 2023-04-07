package cinemaApi.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionManager {

    private ConnectionManager() {
    }

    public static Connection open() {
        Connection connection = null;
        try {
            String url = PropertiesUtil.get(Constants.URL_KEY);
            String username = PropertiesUtil.get(Constants.USERNAME_KEY);
            String password = PropertiesUtil.get(Constants.PASSWORD_KEY);

            if (url == null || username == null || password == null) {
                throw new IllegalArgumentException(Constants.DB_CONFIG_INCOMPLETE);
            }

            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.err.println(Constants.DB_CONNECTION_ERROR + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println(Constants.INVALID_CONFIGURATION + e.getMessage());
        } catch (Exception e) {
            System.err.println(Constants.UNEXPECTED_ERROR + e.getMessage());
        }

        if (connection == null) {
            throw new RuntimeException(Constants.DB_CONNECTION_FAILURE);
        }

        return connection;
    }
}
