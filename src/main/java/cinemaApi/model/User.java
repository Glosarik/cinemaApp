package cinemaApi.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public class User {
    @Getter
    private int userId;

    @Getter
    @Setter
    private String login;

    private String password;

    @Getter
    private UUID uuid;

    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private UserRole userRole;

    @Getter
    private LocalDateTime registrationDate;

    String getPassword() {
        return password;
    }

    public void setPassword(String rawPassword) {
        this.password = new BCryptPasswordEncoder().encode(rawPassword);
    }

    public boolean isPasswordCorrect(String providedPassword) {
        return new BCryptPasswordEncoder().matches(providedPassword, getPassword());
    }
}
