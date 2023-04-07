package cinemaApi.repository.ticket;

import cinemaApi.model.Ticket;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static cinemaApi.util.Constants.*;

public class TicketRepositoryImpl implements TicketRepository {
    private final Connection connection;

    public TicketRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addTicket(Ticket ticket) {
        executeUpdate(SQL_INSERT_TICKET,
                ticket.getTicketId(), ticket.getMovieId(), ticket.getSeatNumber(), ticket.getPrice());
    }

    @Override
    public boolean updateTicket(Ticket ticket) {
        return executeUpdate(SQL_UPDATE_TICKET,
                ticket.getLogin(), ticket.getMovieId(), ticket.getSeatNumber());
    }

    @Override
    public List<Ticket> getTicketsByMovieId(int movieId) {
        return getTickets(SQL_SELECT_TICKETS_BY_MOVIE_ID, movieId);
    }

    @Override
    public void returnTicket(int ticketId) {
        executeUpdate(SQL_CANCEL_TICKET, ticketId);
    }

    @Override
    public Ticket getTicketById(int ticketId) {
        List<Ticket> tickets = getTickets(SQL_SELECT_TICKET_BY_TICKET_ID, ticketId);
        return tickets.isEmpty() ? null : tickets.get(ZERO);
    }

    @Override
    public List<Ticket> getAllTickets() {
        List<Ticket> tickets = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM tickets WHERE isPurchased = "
                + ONE)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tickets.add(mapResultSetToTicket(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(FETCH_TICKETS_ERROR, e);
        }
        return tickets;
    }

    @Override
    public List<Ticket> getTicketsByLogin(String login) {
        return getTickets(SQL_SELECT_TICKETS_BY_LOGIN, login);
    }

    private boolean executeUpdate(String query, Object... parameters) {
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            setParameters(stmt, parameters);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(UPDATE_ERROR, e);
        }
    }

    private List<Ticket> getTickets(String query, Object... parameters) {
        List<Ticket> tickets = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            setParameters(stmt, parameters);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tickets.add(mapResultSetToTicket(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(FETCH_TICKETS_ERROR, e);
        }
        return tickets;
    }

    private Ticket mapResultSetToTicket(ResultSet rs) throws SQLException {
        return Ticket.builder()
                .ticketId(rs.getInt(SQL_TICKET_ID))
                .login(rs.getString(SQL_LOGIN))
                .movieId(rs.getInt(SQL_MOVIE_ID))
                .seatNumber(rs.getInt(SQL_SEAT_NUMBER))
                .price(rs.getFloat(SQL_PRICE))
                .isPurchased(rs.getBoolean(SQL_IS_PURCHASED))
                .purchaseTime(rs.getObject(SQL_PURCHASE_TIME, LocalDateTime.class))
                .build();
    }

    private void setParameters(PreparedStatement stmt, Object... parameters) throws SQLException {
        for (int i = ZERO; i < parameters.length; i++) {
            if (parameters[i] instanceof Integer) stmt.setInt(i + ONE, (int) parameters[i]);
            else if (parameters[i] instanceof String) stmt.setString(i + ONE, (String) parameters[i]);
            else if (parameters[i] instanceof Float) stmt.setFloat(i + ONE, (float) parameters[i]);
        }
    }
}
