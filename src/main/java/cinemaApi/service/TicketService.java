package cinemaApi.service;

import cinemaApi.model.Movie;
import cinemaApi.model.Ticket;

import java.util.List;

public interface TicketService {
    void addTicket(Ticket ticket);

    boolean updateTicket(Ticket ticket);

    void returnTicket(int ticketId);

    List<Ticket> getTicketsByMovieId(int movieId);

    Ticket getTicketById(int ticketId);

    List<Ticket> getTicketsByLogin(String login);

    void addTicketsForMovie(int movieId, int seatCount, float price);

    boolean purchaseTicket(Movie movie, int seatNum, String login);

    void viewAllTicket();

    void viewPurchasedTickets();

    boolean isTicketBought(Movie movie, int seatNumber);

    void printSeatingPlan(Movie movie);

    List<Ticket> getAllTickets();
}
