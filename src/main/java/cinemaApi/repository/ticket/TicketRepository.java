package cinemaApi.repository.ticket;

import cinemaApi.model.Ticket;

import java.util.List;

public interface TicketRepository {
    void addTicket(Ticket ticket);

    boolean updateTicket(Ticket ticket);

    List<Ticket> getTicketsByMovieId(int movieId);

    Ticket getTicketById(int ticketId);

    void returnTicket(int ticketId);

    List<Ticket> getTicketsByLogin(String login);

    List<Ticket> getAllTickets();
}
