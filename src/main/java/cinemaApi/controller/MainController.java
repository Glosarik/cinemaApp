package cinemaApi.controller;

import cinemaApi.model.Movie;
import cinemaApi.model.Ticket;
import cinemaApi.model.User;
import cinemaApi.model.UserRole;
import cinemaApi.repository.film.MovieRepositoryImpl;
import cinemaApi.repository.ticket.TicketRepositoryImpl;
import cinemaApi.repository.user.UserRepositoryImpl;
import cinemaApi.service.*;
import cinemaApi.util.ConnectionManager;
import cinemaApi.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static cinemaApi.util.Constants.*;

public class MainController {
    private static final Connection connection = ConnectionManager.open();
    private final UserService userService = new UserServiceImpl(new UserRepositoryImpl(connection));
    private final MovieService movieService = new MovieServiceImpl(new MovieRepositoryImpl(connection));
    private final TicketService ticketService = new TicketServiceImpl(new TicketRepositoryImpl(connection),
            userService, movieService);
    private final List<Movie> movies = movieService.getAllMovies();
    private final Logger logger;

    public void startMenu() {
        userService.createAdminIfNotExists();
        userService.createManagerIfNotExists();

        while (true) {
            boolean running = userService.getCurrentUser() == null ? handleUnauthenticatedUser() :
                    handleAuthenticatedUser();
            if (!running) break;
        }
    }

    private boolean handleUnauthenticatedUser() {
        Utils.printMenu(UNAUTHENTICATED_MENU.strip().split(NEWLINE_REGEX));
        int choice = Integer.parseInt(Utils.getInput(ENTER_CHOICE, EMPTY_FIELD, Utils::isNumeric, INPUT_ERROR_MESSAGE));

        switch (choice) {
            case ONE:
                runLogin();
                return true;
            case TWO:
                runRegister();
                return true;
            case THREE:
                return false;
            default:
                Utils.showError();
                return true;
        }
    }

    private void runLogin() {
        Utils.attemptOperation(THREE, this::attemptLogin, Utils.getGreenString(LOGIN_SUCCESS));
    }

    private boolean attemptLogin() {
        String username = Utils.getInput(ENTER_LOGIN, EMPTY_FIELD);
        String password = Utils.getInput(ENTER_PASSWORD, EMPTY_FIELD);

        return userService.loginUser(username, password);
    }

    private void runRegister() {
        Utils.attemptOperation(THREE, this::attemptRegistration, Utils.getGreenString(REGISTRATION_SUCCESS));
    }

    private boolean attemptRegistration() {
        String login = Utils.getInput(ENTER_LOGIN, EMPTY_FIELD, userService::isLoginAvailable, LOGIN_ALREADY_TAKEN);

        boolean isEmailAvailable;
        String email;
        do {
            email = Utils.getInput(ENTER_EMAIL, EMPTY_FIELD, Utils::isValidEmail, INVALID_EMAIL_ERROR);
            isEmailAvailable = userService.getUserByEmail(email) == null;
            if (!isEmailAvailable) {
                System.out.println(Utils.getRedString(EMAIL_ALREADY_REGISTERED_ERROR));
            }
        } while (!isEmailAvailable);
        String password = Utils.getInput(ENTER_PASSWORD, EMPTY_FIELD);

        return userService.registerUser(login, email, password);
    }

    private boolean handleAuthenticatedUser() {
        logger.info(MAIN_MENU_OPENED);
        menuOptions(userService.getCurrentUser().getUserRole());

        int choice = getUserChoice();
        if (choice == ZERO) {
            logger.info(LOGGED_OUT);
            userService.setCurrentUser(null);
            return true;
        }
        performAction(choice, userService.getCurrentUser().getUserRole());

        return true;
    }

    private int getUserChoice() {
        int choice;
        try {
            choice = Utils.SCANNER.nextInt();
        } catch (InputMismatchException e) {
            choice = -ONE;
        }
        Utils.SCANNER.nextLine();

        if (choice == -ONE) {
            System.out.println(INPUT_ERROR_MESSAGE);
        }

        return choice;
    }

    private void menuOptions(UserRole userRole) {
        List<String> optionsList = Arrays.stream(USER_MENU_OPTIONS.strip().split(NEWLINE_REGEX)).collect(
                Collectors.toList());

        if (userRole == UserRole.ADMIN) {
            optionsList.addAll(Arrays.stream((ADD_FILM + VIEW_USER_LIST).strip().split(NEWLINE_REGEX))
                    .toList());
        }
        optionsList.add(EXIT_SYSTEM);

        Utils.printMenu(optionsList.toArray(new String[ZERO]));
        System.out.print(ENTER_CHOICE);
    }

    private void performAction(int choice, UserRole userRole) {
        switch (choice) {
            case ONE:
                movieService.viewMovies();
                if (movieService.getAllMovies().size() != ZERO) {
                    buyTicket();
                }
                logger.info(MOVIE_SELECTION_MENU_OPENED);
                break;
            case TWO:
                viewPurchasedTickets();
                break;
            case THREE, FOUR:
                if (userRole == UserRole.ADMIN) adminAction(choice);
                else Utils.showError();

                break;
            default:
                Utils.showError();
        }
    }

    private void viewPurchasedTickets() {
        ticketService.viewPurchasedTickets();
        logger.info(VIEW_TICKETS_MENU_OPENED);
        if (ticketService.getTicketsByLogin(userService.getCurrentUser().getLogin()).size() != ZERO) {
            while (true) {
                System.out.print(VIEW_TICKET_OPTIONS);
                int choice = Integer.parseInt(Utils.getInput(ENTER_CHOICE, EMPTY_FIELD, Utils::isNumeric,
                        INPUT_ERROR_MESSAGE));

                if (choice == ZERO) break;
                if (choice == ONE) {
                    returnTicket();
                } else {
                    Utils.showError();
                }
            }
        } else {
            handleAuthenticatedUser();
        }
    }

    private void adminAction(int choice) {
        switch (choice) {
            case THREE -> addMovie();
            case FOUR -> {
                userService.viewUsers();
                editUser();
            }
        }
    }

    private void addMovie() {
        addMovieWithRetries(MAX_RETRIES);
    }

    private void addMovieWithRetries(int retriesLeft) {
        if (retriesLeft <= ZERO) {
            System.out.println(MAX_ATTEMPTS_EXCEEDED);
            return;
        }
        System.out.println(ENTER_FILM_DETAILS);
        String name = Utils.getInput(TITLE_PROMPT, EMPTY_FIELD);

        int yearOfRelease = Integer.parseInt(Utils.getInput(YEAR_OF_RELEASE_PROMPT, EMPTY_FIELD, Utils::isNumeric,
                INPUT_ERROR_MESSAGE));

        String country = Utils.getInput(COUNTRY_PROMPT, EMPTY_FIELD);
        String genre = Utils.getInput(GENRE_PROMPT, EMPTY_FIELD);

        int duration = Integer.parseInt(Utils.getInput(DURATION_PROMPT, EMPTY_FIELD, Utils::isNumeric,
                INPUT_ERROR_MESSAGE));

        String description = Utils.getInput(DESCRIPTION_PROMPT, EMPTY_FIELD);
        String dateString = Utils.getInput(DATE_TIME_PROMPT, EMPTY_FIELD, Utils::isDateValid, INVALID_DATE_FORMAT);

        int price = Integer.parseInt(Utils.getInput(TICKET_PRICE_PROMPT, EMPTY_FIELD, Utils::isNumeric,
                INPUT_ERROR_MESSAGE));

        try {
            movieService.addMovieWithUserInput(movieService, ticketService, name, yearOfRelease, country, genre,
                    duration, description, dateString, price);
            System.out.println(Utils.getGreenString(DATA_SAVED_SUCCESSFULLY));
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println(Utils.getRedString(FILM_ALREADY_EXISTS));
            addMovieWithRetries(retriesLeft - ONE);
        } catch (Exception e) {
            System.out.println(Utils.getRedString(UNEXPECTED_ERROR + e.getMessage()));
        }
    }

    private void editUser() {
        while (true) {
            System.out.println(MENU_TEXT);
            int choice = Integer.parseInt(Utils.getInput(ENTER_CHOICE, EMPTY_FIELD, Utils::isNumeric, INPUT_ERROR_MESSAGE));

            if (choice == ZERO) return;

            if (choice == ONE) {
                int userId = Integer.parseInt(Utils.getInput(SELECT_USER_BY_ID, EMPTY_FIELD, Utils::isNumeric,
                        INPUT_ERROR_MESSAGE));

                while (true) {
                    System.out.println(EDIT_USER_OPTIONS);
                    int subChoice = Integer.parseInt(Utils.getInput(ENTER_CHOICE, EMPTY_FIELD, Utils::isNumeric,
                            INPUT_ERROR_MESSAGE));

                    if (subChoice == ZERO) break;

                    try {
                        userService.updateUserById(userId, subChoice);
                    } catch (IllegalArgumentException | UnsupportedOperationException e) {
                        System.out.println(e.getMessage());
                    }
                }
            } else {
                Utils.showError();
            }
        }
    }

    private void buyTicket() {
        boolean isAdmin = false;
        User currentUser = userService.getCurrentUser();
        if (currentUser != null && currentUser.getUserRole() != UserRole.USER) {
            isAdmin = true;
        }
        logger.info(TICKET_PURCHASE_MENU_OPENED);

        while (true) {
            System.out.print(VIEW_FILM_OPTIONS);
            if (isAdmin) {
                System.out.println(EDIT_MOVIE_OPTIONS);
            } else {
                System.out.println(GO_BACK);
            }
            int choice = Integer.parseInt(Utils.getInput(ENTER_CHOICE, EMPTY_FIELD, Utils::isNumeric, INPUT_ERROR_MESSAGE));

            switch (choice) {
                case ZERO:
                    return;
                case ONE, FOUR:
                    handleTicketPurchase(choice);
                    break;
                case TWO:
                    if (isAdmin) {
                        editMovie();
                    }
                    break;
                case THREE:
                    ticketService.viewAllTicket();
                    if (isAdmin) {
                        returnTicket();
                    }
                    break;
                default:
                    Utils.showError();
            }
        }
    }

    private void handleTicketPurchase(int choice) {
        try {
            Movie movie = selectMovie();
            int seatNum = inputSeatNumber(movie);
            String login = userService.getCurrentUser().getLogin();

            if (seatNum == -ONE) {
                System.out.println(Utils.getRedString(INVALID_PLACE_NUMBER));
                return;
            }
            if (userService.getCurrentUser().getUserRole() != UserRole.USER && choice == FOUR) {
                login = Utils.getInput(ENTER_LOGIN, EMPTY_FIELD);
                System.out.println(login);
            }
            Utils.processPurchaseResult(ticketService.purchaseTicket(movie, seatNum, login));

            logger.info(USER_PURCHASED_MOVIE_TICKET + movie.getMovieName());

        } catch (InputMismatchException e) {
            System.out.println(INVALID_INPUT_FORMAT);
            Utils.SCANNER.nextLine();
        } catch (Exception e) {
            System.out.println(ERROR_MESSAGE_PREFIX + e.getMessage());
        }
    }

    private int inputSeatNumber(Movie movie) {
        ticketService.printSeatingPlan(movie);
        while (true) {
            int rowNumber = Integer.parseInt(Utils.getInput(ROW_PROMPT, EMPTY_FIELD, Utils::isNumeric,
                    INPUT_ERROR_MESSAGE));
            if (rowNumber <= ZERO || rowNumber > Utils.SEATS.length) {
                System.out.println(Utils.getYellowString(INVALID_PLACE_ROW_NUMBER));
                continue;
            }
            int seatNumber = Integer.parseInt(Utils.getInput(PROMPT, EMPTY_FIELD, Utils::isNumeric, INPUT_ERROR_MESSAGE));
            if (seatNumber <= ZERO || seatNumber > SEAT_LIMIT) {
                System.out.println(Utils.getYellowString(INVALID_PLACE_NUMBER));
                continue;
            }
            int[] rowSeats = Utils.getSeatsByRow(rowNumber);
            int seatIndex = seatNumber - ONE;

            if (rowSeats.length <= seatIndex || ticketService.isTicketBought(movie, rowSeats[seatIndex])) {
                System.out.println(Utils.getRedString(INVALID_PLACE_NUMBER));
            } else {
                return rowSeats[seatIndex];
            }
        }
    }

    private void returnTicket() {
        List<Ticket> tickets = ticketService.getTicketsByLogin(userService.getCurrentUser().getLogin());
        if (tickets.isEmpty() && userService.getCurrentUser().getUserRole() != UserRole.USER) {
            System.out.println(Utils.getYellowString(NO_TICKETS_PURCHASED));
            handleAuthenticatedUser();
            return;
        }
        try {
            boolean isValid = false;
            do {
                int ticketId = Integer.parseInt(Utils.getInput(ENTER_TICKET_NUMBER, EMPTY_FIELD, Utils::isNumeric,
                        INPUT_ERROR_MESSAGE));

                Ticket ticket = ticketService.getTicketById(ticketId);

                if (ticket == null) {
                    System.out.println(Utils.getYellowString(TICKET_NOT_FOUND));
                } else if (!movieService.getMovieById(ticket.getMovieId()).isActive()) {
                    System.out.println(Utils.getRedString(TICKET_EXPIRED));
                } else if (!Objects.equals(ticket.getLogin(), userService.getCurrentUser().getLogin()) &&
                        userService.getCurrentUser().getUserRole() == UserRole.USER) {
                    System.out.println(Utils.getYellowString(RETURN_OWN_TICKETS_ONLY));
                } else {
                    ticketService.returnTicket(ticketId);
                    System.out.println(Utils.getGreenString(TICKET_RETURN_SUCCESS));

                    logger.info(USER_RETURNED_TICKET_FOR + movieService.getMovieById(ticket.getMovieId()).getMovieName());
                    isValid = true;
                }
            } while (!isValid);
        } catch (InputMismatchException e) {
            System.out.println(INVALID_INPUT_FORMAT);
            Utils.SCANNER.nextLine();
        } catch (Exception e) {
            System.out.println(ERROR_MESSAGE_PREFIX + e.getMessage());
        }
    }

    private void editMovie() {
        Movie movie = selectMovie();
        editSelectedMovie(movie);
    }

    private Movie selectMovie() {
        while (true) {
            int movieId = Integer.parseInt(Utils.getInput(ENTER_FILM_NUMBER, EMPTY_FIELD, Utils::isNumeric,
                    INPUT_ERROR_MESSAGE));
            if (movieId < ZERO || movieId >= movies.size()) {
                System.out.println(Utils.getRedString(FILM_NOT_FOUND));
            } else {
                Movie movie = movies.get(movieId - ONE);
                if (movie == null) {
                    System.out.println(Utils.getRedString(FILM_NOT_FOUND));
                } else {
                    return movie;
                }
            }
        }
    }

    private void editSelectedMovie(Movie movie) {
        try {
            while (true) {
                if (userService.getCurrentUser().getUserRole() == UserRole.MANAGER) {
                    System.out.print(EDIT_FILM_OPTIONS + GO_BACK);
                } else if (userService.getCurrentUser().getUserRole() == UserRole.ADMIN) {
                    System.out.print(EDIT_FILM_OPTIONS + REMOVE_MOVIE + NEW_LINE + GO_BACK);
                }
                int choice = Integer.parseInt(Utils.getInput(ENTER_CHOICE, EMPTY_FIELD, Utils::isNumeric,
                        INPUT_ERROR_MESSAGE));

                if (choice == ZERO) break;

                String input;
                boolean isUpdated = false;
                switch (choice) {
                    case ONE -> {
                        input = Utils.getInput(ENTER_NEW_TITLE, EMPTY_FIELD);
                        if (!input.isEmpty()) movie.setMovieName(input);
                        isUpdated = true;
                    }
                    case TWO -> {
                        input = Utils.getInput(ENTER_NEW_YEAR, EMPTY_FIELD, Utils::isNumeric, INPUT_ERROR_MESSAGE);
                        if (!input.isEmpty()) movie.setYearOfRelease(Integer.parseInt(input));
                        isUpdated = true;
                    }
                    case THREE -> {
                        input = Utils.getInput(ENTER_NEW_COUNTRY, EMPTY_FIELD);
                        if (!input.isEmpty()) movie.setCountry(input);
                        isUpdated = true;
                    }
                    case FOUR -> {
                        input = Utils.getInput(ENTER_NEW_GENRE, EMPTY_FIELD);
                        if (!input.isEmpty()) movie.setGenre(input);
                        isUpdated = true;
                    }
                    case FIVE -> {
                        input = Utils.getInput(ENTER_NEW_DURATION, EMPTY_FIELD, Utils::isNumeric, INPUT_ERROR_MESSAGE);
                        if (!input.isEmpty()) movie.setDuration(Integer.parseInt(input));
                        isUpdated = true;
                    }
                    case SIX -> {
                        input = Utils.getInput(ENTER_NEW_DESCRIPTION, EMPTY_FIELD);
                        if (!input.isEmpty()) movie.setDescription(input);
                        isUpdated = true;
                    }
                    case SEVEN -> {
                        input = Utils.getInput(ENTER_NEW_RELEASE_DATE, EMPTY_FIELD, Utils::isDateValid, INVALID_DATE_FORMAT);
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
                        LocalDateTime date = LocalDateTime.parse(input, formatter);
                        movie.setDate(date);
                        isUpdated = true;
                    }
                    case EIGHT -> {
                        if (userService.getCurrentUser().getUserRole() == UserRole.ADMIN) {
                            movie.setActive(false);
                            isUpdated = true;
                        }
                    }
                    default -> Utils.showError();
                }
                if (isUpdated) {
                    movieService.updateMovie(movie);
                    System.out.println(Utils.getGreenString(FILM_UPDATED_SUCCESS));
                    movieService.viewMovies();
                    if (choice == EIGHT)
                        break;
                }
            }
        } catch (InputMismatchException e) {
            Utils.showError();
        }
    }

    public MainController() {
        this.logger = LoggerFactory.getLogger(getClass());
    }
}