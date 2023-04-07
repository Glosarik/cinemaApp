package cinemaApi.service;

import cinemaApi.model.UserDTO;
import cinemaApi.util.UserRole;
import cinemaApi.repository.user.UserRepository;
import cinemaApi.util.Utils;
import cinemaApi.model.User;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static cinemaApi.util.Constants.*;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private User currentUser = null;
    private Logger logger;

    @Override
    public boolean loginUser(String username, String password) {
        User user = userRepository.getUserByLogin(username);
        if (user != null && user.isPasswordCorrect(password)) {
            currentUser = user;
            logger.info("Пользователь " + username + " авторизовался");
            return true;
        } else {
            System.out.println(INPUT_ERROR_MESSAGE);
            logger.error(INVALID_LOGIN_OR_PASSWORD);
            return false;
        }
    }

    @Override
    public boolean registerUser(String login, String email, String password) {
        User newUser = User.builder()
                .login(login)
                .email(email)
                .uuid(UUID.randomUUID())
                .userRole(UserRole.USER)
                .build();
        newUser.setPassword(password);
        addUser(new UserDTO(newUser));
        currentUser = newUser;
        logger.info("Пользователь" + login + " зарегестрировался");
        return true;
    }

    @Override
    public boolean isLoginAvailable(String login) {
        return getUserByLogin(login) == null;
    }

    @Override
    public void updateUserById(int userId, int choice) {
        Optional<User> optionalUser = Optional.ofNullable(getUserById(userId));

        if (optionalUser.isEmpty()) {
            System.out.println(USER_ID_NOT_FOUND);
            return;
        }
        User user = optionalUser.get();

        boolean isUpdated = false;
        String value;
        switch (choice) {
            case ONE -> {
                value = Utils.getInput(ENTER_LOGIN, EMPTY_FIELD, this::isLoginAvailable, LOGIN_ALREADY_TAKEN);
                user.setLogin(value);
            }
            case TWO -> {
                value = Utils.getInput(ENTER_PASSWORD, EMPTY_FIELD);
                user.setPassword(value);
            }
            case THREE -> {
                do {
                    value = Utils.getInput(ENTER_EMAIL, EMPTY_FIELD, Utils::isValidEmail, INVALID_EMAIL_ERROR);
                    if (getUserByEmail(value) == null) {
                        user.setEmail(value);
                        isUpdated = true;
                    } else {
                        System.out.println(Utils.getRedString(EMAIL_ALREADY_REGISTERED_ERROR));
                    }
                } while (!isUpdated);
            }
            case FOUR -> {
                do {
                    int newRole = Integer.parseInt(Utils.getInput(ROLE_PROMPT, EMPTY_FIELD, Utils::isNumeric,
                            INPUT_ERROR_MESSAGE));
                    if (newRole >= ZERO && newRole <= TWO) {
                        user.setUserRole(switch (newRole) {
                            case ONE -> UserRole.MANAGER;
                            case TWO -> UserRole.ADMIN;
                            default -> UserRole.USER;
                        });
                        isUpdated = true;
                    } else {
                        System.out.println(Utils.getRedString(INVALID_ROLE_VALUE));
                    }
                } while (!isUpdated);
            }
            case FIVE -> {
                deleteUser(userId);
            }
            default -> throw new UnsupportedOperationException(UNSUPPORTED_UPDATE_ACTION);
        }
        updateUser(new UserDTO(user));
        System.out.println(Utils.getGreenString(USER_UPDATED_SUCCESS));
        viewUsers();
    }

    @Override
    public void addUser(UserDTO user) {
        userRepository.addUser(user);
    }

    @Override
    public User getUserById(int userId) {
        return userRepository.getUserById(userId);
    }

    @Override
    public User getUserByLogin(String login) {
        return userRepository.getUserByLogin(login);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    @Override
    public void deleteUser(int userId) {
        userRepository.deleteUser(userId);
    }

    @Override
    public void createAdminIfNotExists() {
        String adminLogin = ADMIN;
        User admin = getUserByLogin(adminLogin);

        if (admin == null) {

            User newUser = User.builder()
                    .login(adminLogin)
                    .email(ADMIN_EMAIL)
                    .uuid(UUID.randomUUID())
                    .userRole(UserRole.ADMIN)
                    .build();
            newUser.setPassword(ADMIN);
            addUser(new UserDTO(newUser));
        }
    }

    @Override
    public void createManagerIfNotExists() {
        String managerLogin = MANAGER;
        User manager = getUserByLogin(managerLogin);

        if (manager == null) {

            User newUser = User.builder()
                    .login(managerLogin)
                    .email(MANAGER_EMAIL)
                    .uuid(UUID.randomUUID())
                    .userRole(UserRole.MANAGER)
                    .build();
            newUser.setPassword(MANAGER);
            addUser(new UserDTO(newUser));
        }
    }

    @Override
    public void viewUsers() {
        List<User> users = getAllUsers();
        Utils.viewData(Arrays.asList(ID_HEADER, LOGIN_HEADER, EMAIL_HEADER, ROLE_HEADER, REGISTRATION_DATE_HEADER),
                users,
                Arrays.asList(
                        user -> String.valueOf(user.getUserId()).length(),
                        user -> user.getLogin().length(),
                        user -> user.getEmail().length(),
                        user -> user.getUserRole().name().length(),
                        user -> Utils.formatDateTime(user.getRegistrationDate()).length()
                ),
                this::printUserData);
    }

    @Override
    public void updateUser(UserDTO user) {
        userRepository.updateUser(user);
    }

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.logger = LoggerFactory.getLogger(getClass());
    }

    @Override
    public User getCurrentUser() {
        return currentUser;
    }

    @Override
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    private void printUserData(String format, User user) {
        System.out.printf(format, user.getUserId(), user.getLogin(), user.getEmail(),
                user.getUserRole().name(), Utils.formatDateTime(user.getRegistrationDate()));
    }
}
