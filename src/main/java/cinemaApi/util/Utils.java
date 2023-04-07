package cinemaApi.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static cinemaApi.util.Constants.*;

public class Utils {

    public static final Scanner SCANNER = new Scanner(System.in);
    public static final LocalDateTime CURRENT_DATE = LocalDateTime.now();

    public static final int[][] SEATS = new int[][]{
            {ONE, INTERVAL_1_END},
            {INTERVAL_2_START, INTERVAL_2_END},
            {INTERVAL_3_START, INTERVAL_3_END},
            {INTERVAL_4_START, INTERVAL_4_END},
            {INTERVAL_5_START, INTERVAL_5_END},
            {INTERVAL_6_START, INTERVAL_6_END},
            {INTERVAL_7_START, INTERVAL_7_END},
            {INTERVAL_8_START, INTERVAL_8_END},
            {INTERVAL_9_START, INTERVAL_9_END},
            {INTERVAL_10_START, SEAT_LIMIT},
    };

    public static boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static String buildFormatString(int[] lengths) {
        return Arrays.stream(lengths).mapToObj(length -> TABLE_FORMAT_LEFT_ALIGN + length + SPACE_STRING)
                .collect(Collectors.joining()) + TABLE_ROW_SEPARATOR;
    }

    public static String buildLineString(int[] lengths) {
        return Arrays.stream(lengths).mapToObj(length -> TABLE_BORDER_SYMBOL + TABLE_HORIZONTAL_LINE.repeat(
                        length + TWO))
                .collect(Collectors.joining()) + TABLE_BORDER_NEW_LINE;
    }

    public static String formatTime(int duration) {
        int hours = duration / SIXTY, minutes = duration % SIXTY;
        return duration + MINUTES_ABBREVIATION + String.format(TIME_FORMAT, hours, minutes);
    }

    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
    }

    public static void showError() {
        System.out.println(getYellowString(INVALID_CHOICE));
    }

    public static String getInput(String prompt, String emptyError) {
        return getInput(prompt, emptyError, s -> true,
                "");
    }

    public static String getInput(String prompt, String emptyError, Predicate<String> validator, String validationError) {
        Scanner scanner = new Scanner(System.in);
        String input;
        do {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println(getYellowString(emptyError));
            } else if (!validator.test(input)) {
                System.out.println(getRedString(validationError));
            } else {
                break;
            }
        } while (true);
        return input;
    }

    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static String getSeparator(int length) {
        return String.join("", Collections.nCopies(length, EM_DASH));
    }

    public static String getColoredString(String color, String str) {
        return color + str + RESET;
    }

    public static String getRedString(String str) {
        String separator = getSeparator(CROSS_MARK.length() + str.length());

        return getColoredString(RED, separator + NEW_LINE + CROSS_MARK + str + NEW_LINE + separator);
    }

    public static String getGreenString(String str) {
        String separator = getSeparator(CHECKMARK.length() + str.length());

        return getColoredString(GREEN, separator + NEW_LINE + CHECKMARK + str + NEW_LINE + separator);
    }

    public static String getYellowString(String str) {
        String separator = getSeparator(QUESTION_MARK.length() + str.length());

        return getColoredString(YELLOW, separator + NEW_LINE + QUESTION_MARK + str + NEW_LINE + separator);
    }

    public static boolean isDateValid(String dateString) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
            LocalDateTime.parse(dateString, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static void printMenu(String[] items) {
        int maxLength = findMaxLength(items);
        String line = createLine(maxLength + FOUR);

        System.out.println(line);
        for (String item : items) {
            System.out.printf("| %-" + (maxLength + ONE) + "s|%n", item);
        }
        System.out.println(line);
    }

    public static <T> void viewData(List<String> headers, List<T> items, List<ToIntFunction<T>> columnLengthExtractors,
                                    BiConsumer<String, T> printDataMethod) {
        int[] lengths = updateHeaderLengths(headers, items, columnLengthExtractors);
        printHeadersAndData(headers, lengths, items, printDataMethod);
    }

    public static void processPurchaseResult(boolean isPurchased) {
        if (isPurchased) {
            System.out.println(GREEN + Utils.getGreenString(TICKET_PURCHASE_SUCCESS) + RESET);
        } else {
            System.out.println(YELLOW + Utils.getYellowString(TICKET_PURCHASE_FAILURE) + RESET);
        }
    }

    public static int[] getSeatsByRow(int rowNumber) {
        if (rowNumber < ONE || rowNumber > SEATS.length) {
            System.out.println(INVALID_PLACE_ROW_NUMBER);
            return new int[ZERO];
        }

        int rowStart = SEATS[rowNumber - ONE][ZERO];
        int rowEnd = SEATS[rowNumber - ONE][ONE];

        int[] rowSeats = new int[rowEnd - rowStart + ONE];
        for (int i = ZERO; i < rowSeats.length; i++) {
            rowSeats[i] = rowStart + i;
        }

        return rowSeats;
    }

    public static void attemptOperation(int attempts, Supplier<Boolean> operation, String successMessage) {
        while (attempts-- > ZERO) {
            if (operation.get()) {
                System.out.println(successMessage);
                break;
            }
            System.out.println(getYellowString(REMAINING_ATTEMPTS + attempts));
        }
    }

    private static <T> int[] updateHeaderLengths(List<String> headers, List<T> items, List<ToIntFunction<T>>
            columnLengthExtractors) {
        int[] lengths = headers.stream().mapToInt(String::length).toArray();

        for (T item : items) {
            for (int i = ZERO; i < lengths.length; i++) {
                try {
                    lengths[i] = Math.max(lengths[i], columnLengthExtractors.get(i).applyAsInt(item));
                } catch (NullPointerException | IndexOutOfBoundsException e) {
                    System.out.println(ERROR_MESSAGE_PREFIX + e.getMessage());
                }
            }
        }
        return lengths;
    }

    private static <T> void printHeadersAndData(List<String> headers, int[] lengths, List<T> items,
                                                BiConsumer<String, T> printDataMethod) {
        String format = Utils.buildFormatString(lengths);
        String line = Utils.buildLineString(lengths);
        printHeaders(format, line, headers.toArray(new String[ZERO]));

        for (T item : items) {
            printDataMethod.accept(format, item);
            System.out.printf(line);
        }
    }

    private static void printHeaders(String format, String line, String... items) {
        System.out.printf(line + format, (Object[]) items);
        System.out.printf(line);
    }

    private static int findMaxLength(String[] items) {
        int maxLength = ZERO;
        for (String item : items) {
            maxLength = Math.max(maxLength, item.length());
        }
        return maxLength;
    }

    private static String createLine(int length) {
        return PLUS_SIGN + DASH.repeat(length - TWO) + PLUS_SIGN;
    }
}
