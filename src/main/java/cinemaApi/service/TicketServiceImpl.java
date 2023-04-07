package cinemaApi.service;

import cinemaApi.util.Utils;
import cinemaApi.model.Movie;
import cinemaApi.model.Ticket;
import cinemaApi.repository.ticket.TicketRepository;

import java.time.LocalDateTime;
import java.util.*;

import static cinemaApi.util.Constants.*;

public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;
    private final UserService userService;
    private final MovieService movieService;

    @Override
    public void addTicketsForMovie(int movieId, int seatCount, float price) {
        for (int i = ONE; i <= seatCount; i++) {
            Ticket ticket = Ticket.builder()
                    .ticketId(movieId + i)
                    .movieId(movieId)
                    .seatNumber(i)
                    .price(price)
                    .build();
            addTicket(ticket);
        }
    }

    @Override
    public void addTicket(Ticket ticket) {
        ticketRepository.addTicket(ticket);
    }

    @Override
    public boolean updateTicket(Ticket ticket) {
        return ticketRepository.updateTicket(ticket);
    }

    @Override
    public void returnTicket(int ticketId) {
        ticketRepository.returnTicket(ticketId);
    }

    @Override
    public List<Ticket> getTicketsByMovieId(int movieId) {
        return ticketRepository.getTicketsByMovieId(movieId);
    }

    @Override
    public Ticket getTicketById(int ticketId) {
        return ticketRepository.getTicketById(ticketId);
    }

    @Override
    public List<Ticket> getTicketsByLogin(String login) {
        return ticketRepository.getTicketsByLogin(login);
    }

    @Override
    public List<Ticket> getAllTickets() {
        return ticketRepository.getAllTickets();
    }

    @Override
    public boolean purchaseTicket(Movie movie, int seatNum, String login) {
        Ticket ticket = Ticket.builder()
                .login(login)
                .movieId(movie.getMovieId())
                .seatNumber(seatNum)
                .build();

        List<Ticket> tickets = getTicketsByMovieId(movie.getMovieId());
        Ticket targetTicket = (tickets != null) ? tickets.get(seatNum - ONE) : null;
        return (targetTicket != null && !targetTicket.isPurchased()) && updateTicket(ticket);
    }

    @Override
    public boolean isTicketBought(Movie movie, int seatNumber) {
        return getTicketsByMovieId(movie.getMovieId()).get(seatNumber - ONE).isPurchased();
    }

    public TicketServiceImpl(TicketRepository ticketRepository, UserService userService, MovieService movieService) {
        this.ticketRepository = ticketRepository;
        this.userService = userService;
        this.movieService = movieService;
    }

    @Override
    public void printSeatingPlan(Movie movie) {
        for (int i = ZERO; i < Utils.SEATS.length; i++) {
            int row = i + ONE;
            String rowStr = String.format(ROW_NUMBER_FORMAT, row);
            int rowLength = Utils.SEATS[i][ONE] - Utils.SEATS[i][ZERO] + ONE;
            int spacesCount = (MAX_ROW_LENGTH - rowLength) * TWO;
            String spacesStr = " ".repeat(spacesCount / TWO);
            System.out.printf(STRING_CONCAT_FORMAT, rowStr, spacesStr);
            for (int seat = Utils.SEATS[i][ZERO]; seat <= Utils.SEATS[i][ONE]; seat++) {
                System.out.print(isTicketBought(movie, seat) ? RED + NO_FREE_PLACE_FOR_FILM + RESET : GREEN +
                        FREE_PLACE_FOR_FILM + RESET);
            }
            System.out.println();
        }
    }

    @Override
    public void viewPurchasedTickets() {
        List<Ticket> tickets = getTicketsByLogin(userService.getCurrentUser().getLogin());
        if (!tickets.isEmpty()) {
            Utils.viewData(Arrays.asList(TICKET_NUM_HEADER, TICKET_TITLE_HEADER, TICKET_DATE_HEADER,
                            TICKET_PLACE_NUMBER_HEADER, PURCHASE_DATE),
                    tickets,
                    Arrays.asList(item -> Integer.toString((item).getTicketId()).length(),
                            item -> movieService.getMovieById((item).getMovieId()).getMovieName().length(),
                            item -> getFormattedDate(item).length(),
                            item -> {
                                int seatNumber = (item).getSeatNumber();
                                return Objects.requireNonNull(getSeatInfoLength(seatNumber)).length();
                            },
                            item -> getFormattedPurchaseDate(item).length()),
                    this::printTicketData);
        } else {
            System.out.println(Utils.getYellowString(EMPTY_LIST));
        }
    }

    @Override
    public void viewAllTicket() {
        List<Ticket> tickets = getAllTickets();
        if (!tickets.isEmpty()) {
            Utils.viewData(Arrays.asList(BUYER_LOGIN, TICKET_NUM_HEADER, TICKET_TITLE_HEADER, TICKET_DATE_HEADER,
                            TICKET_PLACE_NUMBER_HEADER, PURCHASE_DATE),
                    tickets,
                    Arrays.asList(item -> item.getLogin().length(),
                            item -> Integer.toString((item).getTicketId()).length(),
                            item -> movieService.getMovieById((item).getMovieId()).getMovieName().length(),
                            item -> getFormattedDate(item).length(),
                            item -> {
                                int seatNumber = (item).getSeatNumber();
                                return Objects.requireNonNull(getSeatInfoLength(seatNumber)).length();
                            },
                            item -> getFormattedPurchaseDate(item).length()),
                    this::printAllTicketData);
        } else {
            System.out.println(Utils.getYellowString(EMPTY_LIST));
        }
    }

    private void printTicketData(String formatLine, Ticket ticket) {
        Movie movie = movieService.getMovieById(ticket.getMovieId());
        int seatNumber = ticket.getSeatNumber();
        String seatInfo = String.valueOf(getSeatInfoLength(seatNumber));
        getSeatInfoLength(seatNumber);
        Object[] data = {ticket.getTicketId(), movie.getMovieName(), getFormattedDate(ticket), seatInfo,
                getFormattedPurchaseDate(ticket)};
        System.out.printf(formatLine, data);
    }

    private void printAllTicketData(String formatLine, Ticket ticket) {
        Movie movie = movieService.getMovieById(ticket.getMovieId());
        int seatNumber = ticket.getSeatNumber();
        String seatInfo = String.valueOf(getSeatInfoLength(seatNumber));
        getSeatInfoLength(seatNumber);
        Object[] data = {ticket.getLogin(), ticket.getTicketId(), movie.getMovieName(), getFormattedDate(ticket),
                seatInfo, getFormattedPurchaseDate(ticket)};
        System.out.printf(formatLine, data);
    }

    private static final int MAX_ROW_LENGTH = Arrays.stream(Utils.SEATS)
            .mapToInt(seatRange -> seatRange[ONE] - seatRange[ZERO] + ONE)
            .max()
            .orElse(ZERO);


    private StringBuilder getSeatInfoLength(int seatNumber) {
        for (int i = ZERO; i < Utils.SEATS.length; i++) {
            if (seatNumber >= Utils.SEATS[i][ZERO] && seatNumber <= Utils.SEATS[i][ONE]) {
                int row = i + ONE;
                int seatInRow = seatNumber - Utils.SEATS[i][ZERO] + ONE;
                StringBuilder sb = new StringBuilder();
                sb.append(ROW_TITLE).append(row).append(PLACE).append(seatInRow);
                return sb;
            }
        }
        return null;
    }

    private static final Map<LocalDateTime, String> DATE_CACHE = new HashMap<>();

    private String getFormattedDate(LocalDateTime date, Movie movie) {
        String cached = DATE_CACHE.get(date);
        if (cached != null) {
            return movie.isActive() ? cached : cached + TICKET_OVERDUE;
        }

        String formatted = Utils.formatDateTime(date);
        DATE_CACHE.put(date, formatted);
        return movie.isActive() ? formatted : formatted + TICKET_OVERDUE;
    }

    private String getFormattedDate(Ticket ticket) {
        Movie movie = movieService.getMovieById(ticket.getMovieId());
        return getFormattedDate(movie.getDate(), movie);
    }

    private String getFormattedPurchaseDate(Ticket ticket) {
        Movie movie = movieService.getMovieById(ticket.getMovieId());
        return getFormattedDate(ticket.getPurchaseTime(), movie);
    }
}
