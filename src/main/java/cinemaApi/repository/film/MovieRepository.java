package cinemaApi.repository.film;

import cinemaApi.model.Movie;

import java.sql.SQLException;
import java.util.List;

public interface MovieRepository {
    void addMovie(Movie movie) throws SQLException;

    Movie getMovieById(int movieId);

    List<Movie> getAllMovies();

    void updateMovie(Movie movie);
}
