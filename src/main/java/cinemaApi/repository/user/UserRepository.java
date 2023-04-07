package cinemaApi.repository.user;

import cinemaApi.model.UserDTO;
import cinemaApi.model.User;

import java.util.List;

public interface UserRepository {
    void addUser(UserDTO user);

    User getUserById(int userId);

    User getUserByLogin(String login);

    List<User> getAllUsers();

    void updateUser(UserDTO user);

    void deleteUser(int userId);

    User getUserByEmail(String email);
}
