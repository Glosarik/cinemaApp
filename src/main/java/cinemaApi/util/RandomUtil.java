package cinemaApi.util;

import cinemaApi.service.MovieService;
import cinemaApi.service.TicketService;

public class RandomUtil {

    public static int generateUniqueNumber(MovieService movieService, TicketService ticketService, Object... values) {
        StringBuilder inputBuilder = new StringBuilder();
        for (Object value : values) {
            inputBuilder.append(value);
        }
        String input = inputBuilder.toString();

        int randomNum;
        do {
            int hash = Math.abs(input.hashCode());
            randomNum = hash % Constants.NUMBER;
        } while (movieService.getMovieById(randomNum) != null || ticketService.getTicketById(randomNum) != null);

        return randomNum;
    }
}
