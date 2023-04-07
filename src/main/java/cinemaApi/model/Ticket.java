package cinemaApi.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class Ticket {
    private int ticketId;
    private String login;
    private int movieId;
    private int seatNumber;
    private float price;
    private boolean isPurchased;
    private LocalDateTime purchaseTime;
}
