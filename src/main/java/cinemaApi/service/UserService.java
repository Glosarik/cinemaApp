package cinemaApi.service;

import cinemaApi.model.UserDTO;
import cinemaApi.model.User;

import java.util.List;

public interface UserService {
    boolean loginUser(String username, String password);

    boolean registerUser(String login, String email, String password);

    boolean isLoginAvailable(String login);

    void updateUserById(int userId, int choice);

    void createAdminIfNotExists();

    void createManagerIfNotExists();

    void addUser(UserDTO user);

    User getUserById(int userId);

    User getUserByLogin(String login);

    List<User> getAllUsers();

    void viewUsers();

    void setCurrentUser(User currentUser);

    void updateUser(UserDTO user);

    User getCurrentUser();

    void deleteUser(int userId);

    User getUserByEmail(String email);
}
