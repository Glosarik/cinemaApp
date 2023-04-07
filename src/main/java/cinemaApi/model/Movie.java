package cinemaApi.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class Movie {
    private Integer movieId;
    private String movieName;
    private Integer yearOfRelease;
    private String country;
    private String genre;
    private Integer duration;
    private String description;
    private LocalDateTime date;
    private Integer seatNumber;
    private boolean isActive;
}
