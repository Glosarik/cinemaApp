package cinemaApi.repository.film;

import cinemaApi.model.Movie;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static cinemaApi.util.Constants.*;

public class MovieRepositoryImpl implements MovieRepository {

    private final Connection connection;

    public MovieRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addMovie(Movie movie) throws SQLException {
        String query = "INSERT INTO movies (movieId, movieName, yearOfRelease, country, genre, duration, description," +
                "seatNumber, date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            setMovieParams(statement, movie);
            statement.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            throw e;
        } catch (SQLException e) {
            System.out.println(DB_CONNECTION_FAILURE + e.getMessage());
        }
    }

    private void setMovieParams(PreparedStatement statement, Movie movie) throws SQLException {
        statement.setInt(ONE, movie.getMovieId());
        statement.setString(TWO, movie.getMovieName());
        statement.setInt(THREE, movie.getYearOfRelease());
        statement.setString(FOUR, movie.getCountry());
        statement.setString(FIVE, movie.getGenre());
        statement.setFloat(SIX, movie.getDuration());
        statement.setString(SEVEN, movie.getDescription());
        statement.setInt(EIGHT, movie.getSeatNumber());
        statement.setTimestamp(NINE, Timestamp.valueOf(movie.getDate()));
    }

    @Override
    public Movie getMovieById(int movieId) {
        String query = "SELECT * FROM movies WHERE movieId = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(ONE, movieId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? convertResultToMovie(resultSet) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Movie convertResultToMovie(ResultSet resultSet) throws SQLException {
        return Movie.builder()
                .movieId(resultSet.getInt(SQL_MOVIE_ID))
                .movieName(resultSet.getString(SQL_MOVIE_NAME_FIELD))
                .yearOfRelease(resultSet.getInt(SQL_YEAR_OF_RELEASE))
                .country(resultSet.getString(SQL_COUNTRY))
                .genre(resultSet.getString(SQL_GENRE))
                .duration(resultSet.getInt(SQL_DURATION))
                .description(resultSet.getString(SQL_DESCRIPTION))
                .date(resultSet.getTimestamp(SQL_DATE).toLocalDateTime())
                .isActive(resultSet.getBoolean(SQL_IS_ACTIVE))
                .build();
    }

    @Override
    public List<Movie> getAllMovies() {
        List<Movie> movies = new ArrayList<>();
        String query = "SELECT * FROM movies WHERE isActive = true";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                movies.add(convertResultToMovie(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return movies;
    }

    @Override
    public void updateMovie(Movie movie) {
        StringBuilder queryBuilder = new StringBuilder(SQL_UPDATE_MOVIES_SET);
        List<String> queryParts = new ArrayList<>();
        List<Object> values = new ArrayList<>();

        queryParts.add(SQL_IS_ACTIVE + EQUALS_QUESTION_MARK);
        values.add(movie.isActive());

        if (movie.getMovieName() != null) {
            queryParts.add(SQL_MOVIE_NAME_FIELD + EQUALS_QUESTION_MARK);
            values.add(movie.getMovieName());
        }
        if (movie.getYearOfRelease() != null) {
            queryParts.add(SQL_YEAR_OF_RELEASE + EQUALS_QUESTION_MARK);
            values.add(movie.getYearOfRelease());
        }
        if (movie.getCountry() != null) {
            queryParts.add(SQL_COUNTRY + EQUALS_QUESTION_MARK);
            values.add(movie.getCountry());
        }
        if (movie.getGenre() != null) {
            queryParts.add(SQL_GENRE + EQUALS_QUESTION_MARK);
            values.add(movie.getGenre());
        }
        if (movie.getDuration() != null) {
            queryParts.add(SQL_DURATION + EQUALS_QUESTION_MARK);
            values.add(movie.getDuration());
        }
        if (movie.getDescription() != null) {
            queryParts.add(SQL_DESCRIPTION + EQUALS_QUESTION_MARK);
            values.add(movie.getDescription());
        }
        if (movie.getSeatNumber() != null) {
            queryParts.add(SQL_SEAT_NUMBER + EQUALS_QUESTION_MARK);
            values.add(movie.getSeatNumber());
        }
        if (movie.getDate() != null) {
            queryParts.add(SQL_DATE + EQUALS_QUESTION_MARK);
            values.add(movie.getDate());
        }

        queryBuilder.append(String.join(", ", queryParts));
        queryBuilder.append(SQL_WHERE_MOVIE_ID + EQUALS_QUESTION_MARK);

        try (PreparedStatement statement = connection.prepareStatement(queryBuilder.toString())) {
            int index = ONE;
            for (Object value : values) {
                statement.setObject(index++, value);
            }
            statement.setInt(index, movie.getMovieId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
