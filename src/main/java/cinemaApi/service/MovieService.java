package cinemaApi.service;

import cinemaApi.model.Movie;

import java.sql.SQLException;
import java.util.List;

public interface MovieService {
    void addMovie(Movie movie) throws SQLException;

    Movie getMovieById(int movieId);

    List<Movie> getAllMovies();

    void updateMovie(Movie movie);

    void addMovieWithUserInput(MovieService movieService, TicketService ticketService, String name, int
            yearOfRelease, String country, String genre, int duration, String description, String dateString,
                               float price) throws Exception;

    void viewMovies();
}
