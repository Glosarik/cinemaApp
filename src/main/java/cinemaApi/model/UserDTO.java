package cinemaApi.model;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class UserDTO {
    private final int userId;
    private final String login;
    private final String password;
    private final String email;
    private final UUID uuid;
    private final UserRole userRole;
    private final LocalDateTime registrationDate;

    public UserDTO(User user) {
        this.userId = user.getUserId();
        this.login = user.getLogin();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.uuid = user.getUuid();
        this.userRole = user.getUserRole();
        this.registrationDate = user.getRegistrationDate();
    }
}