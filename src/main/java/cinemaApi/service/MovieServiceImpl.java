package cinemaApi.service;

import cinemaApi.util.Utils;
import cinemaApi.model.Movie;
import cinemaApi.repository.film.MovieRepository;
import cinemaApi.util.RandomUtil;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static cinemaApi.util.Constants.*;

public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public void addMovieWithUserInput(MovieService movieService, TicketService ticketService, String name, int
            yearOfRelease, String country, String genre, int duration, String description, String dateString,
                                      float price) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        LocalDateTime date = LocalDateTime.parse(dateString, formatter);
        Movie movie = Movie.builder()
                .movieId(RandomUtil.generateUniqueNumber(movieService, ticketService, name, yearOfRelease, genre,
                        duration, description, dateString))
                .movieName(name)
                .yearOfRelease(yearOfRelease)
                .country(country)
                .genre(genre)
                .duration(duration)
                .description(description)
                .date(date)
                .seatNumber(SEAT_LIMIT)
                .isActive(true)
                .build();
        addMovie(movie);

        ticketService.addTicketsForMovie(movie.getMovieId(), SEAT_LIMIT, price);
    }

    @Override
    public void viewMovies() {
        if (getAllMovies().size() != ZERO) {
            Utils.viewData(Arrays.asList(FILM_NUM_HEADER, FILM_TITLE_HEADER, FILM_YEAR_HEADER, FILM_COUNTRY_HEADER,
                            FILM_GENRE_HEADER, FILM_DURATION_HEADER, FILM_DESCRIPTION_HEADER, FILM_PREMIERE_HEADER),
                    getAllMovies(),
                    Arrays.asList(
                            item -> Integer.toString(getAllMovies().indexOf(item)).length(),
                            item -> item.getMovieName().length(),
                            item -> String.valueOf(item.getYearOfRelease()).length(),
                            item -> item.getCountry().length(),
                            item -> item.getGenre().length(),
                            item -> Utils.formatTime(item.getDuration()).length(),
                            item -> Math.min(100, item.getDescription().length()),
                            item -> Utils.formatDateTime(item.getDate()).length()
                    ),
                    this::printMovieData);
        } else {
            System.out.println(Utils.getYellowString(EMPTY_LIST));
        }
    }

    @Override
    public void addMovie(Movie movie) throws SQLException {
        movieRepository.addMovie(movie);
    }

    @Override
    public Movie getMovieById(int movieId) {
        return movieRepository.getMovieById(movieId);
    }

    @Override
    public List<Movie> getAllMovies() {
        return movieRepository.getAllMovies();
    }

    @Override
    public void updateMovie(Movie movie) {
        movieRepository.updateMovie(movie);
    }

    private void printMovieData(String formatLine, Movie movie) {
        int id = getAllMovies().indexOf(movie) + ONE;
        String[] paragraphs = movie.getDescription().split(REGEX_SPLIT_100_CHARACTERS);
        for (int i = ZERO; i < paragraphs.length; i++) {
            Object[] data = i == ZERO ? new Object[]{id, movie.getMovieName(), movie.getYearOfRelease(),
                    movie.getCountry(), movie.getGenre(), Utils.formatTime(movie.getDuration()),
                    paragraphs[i], Utils.formatDateTime(movie.getDate())} : new Object[]{"", "", "", "", "", "",
                    paragraphs[i], ""};
            System.out.printf(formatLine, data);
        }
    }
}
