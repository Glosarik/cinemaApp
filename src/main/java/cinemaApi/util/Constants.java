package cinemaApi.util;

public final class Constants {
    public static final String EM_DASH = "\u2014";
    public static final String CHECKMARK = "\u2713 ";
    public static final String CROSS_MARK = "\u2715 ";
    public static final String QUESTION_MARK = "\u2753 ";
    public static final String FREE_PLACE_FOR_FILM = "☐ ";
    public static final String NO_FREE_PLACE_FOR_FILM = "☒ ";
    public static final char PLUS_SIGN = '+';
    public static final String DASH = "-";
    public static final String NEW_LINE = System.lineSeparator();
    public static final int ZERO = 0;
    public static final int ONE = 1;
    public static final int TWO = 2;
    public static final int THREE = 3;
    public static final int FOUR = 4;
    public static final int FIVE = 5;
    public static final int SIX = 6;
    public static final int SEVEN = 7;
    public static final int EIGHT = 8;
    public static final int NINE = 9;
    public static final int SIXTY = 60;
    public static final int INTERVAL_1_END = 13;
    public static final int INTERVAL_2_START = 14;
    public static final int INTERVAL_2_END = 29;
    public static final int INTERVAL_3_START = 30;
    public static final int INTERVAL_3_END = 45;
    public static final int INTERVAL_4_START = 46;
    public static final int INTERVAL_4_END = 61;
    public static final int INTERVAL_5_START = 62;
    public static final int INTERVAL_5_END = 76;
    public static final int INTERVAL_6_START = 77;
    public static final int INTERVAL_6_END = 91;
    public static final int INTERVAL_7_START = 92;
    public static final int INTERVAL_7_END = 106;
    public static final int INTERVAL_8_START = 107;
    public static final int INTERVAL_8_END = 127;
    public static final int INTERVAL_9_START = 128;
    public static final int INTERVAL_9_END = 148;
    public static final int INTERVAL_10_START = 149;
    public final static int SEAT_LIMIT = 169;
    public static final int NUMBER = 1000000000;
    public static final int MAX_RETRIES = THREE;
    public static final String RESET = "\u001B[0m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String RED = "\u001B[31m";
    public static final String EQUALS_QUESTION_MARK = " = ?";
    public static final String NEWLINE_REGEX = "\\s*\\n\\s*";
    public static final String REGEX_SPLIT_100_CHARACTERS = "(?<=\\G.{100})";
    public static final String EMAIL_REGEX = "^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,})+$";
    public static final String ROW_NUMBER_FORMAT = "Ряд %2d: ";
    public static final String STRING_CONCAT_FORMAT = "%s%s";
    public static final String SQL_MOVIE_NAME_FIELD = "movieName";
    public static final String SQL_YEAR_OF_RELEASE = "yearOfRelease";
    public static final String SQL_COUNTRY = "country";
    public static final String SQL_GENRE = "genre";
    public static final String SQL_DURATION = "duration";
    public static final String SQL_DESCRIPTION = "description";
    public static final String SQL_DATE = "date";
    public static final String SQL_IS_ACTIVE = "isActive";
    public static final String SQL_SEAT_NUMBER = "seatNumber";
    public static final String SQL_UPDATE_MOVIES_SET = "UPDATE movies SET ";
    public static final String SQL_WHERE_MOVIE_ID = " WHERE movieId";
    public static final String SQL_INSERT_TICKET = "INSERT INTO tickets (ticketId, movieId, seatNumber, price) VALUES " +
            "(?, ?, ?, ?)";
    public static final String SQL_UPDATE_TICKET = "UPDATE tickets SET login = ?, isPurchased = 1, purchaseTime = " +
            "CURRENT_TIMESTAMP WHERE movieId = ? AND seatNumber = ?";
    public static final String SQL_SELECT_TICKETS_BY_MOVIE_ID = "SELECT * FROM tickets WHERE movieId = ?";
    public static final String SQL_CANCEL_TICKET = "UPDATE tickets SET login = NULL, isPurchased = 0, purchaseTime = " +
            "NULL WHERE ticketId = ?";
    public static final String SQL_SELECT_TICKET_BY_TICKET_ID = "SELECT * FROM tickets WHERE ticketId = ?";
    public static final String SQL_SELECT_TICKETS_BY_LOGIN = "SELECT * FROM tickets WHERE login = ?";
    public static final String SQL_TICKET_ID = "ticketId";
    public static final String SQL_LOGIN = "login";
    public static final String SQL_MOVIE_ID = "movieId";
    public static final String SQL_PRICE = "price";
    public static final String SQL_IS_PURCHASED = "isPurchased";
    public static final String SQL_PURCHASE_TIME = "purchaseTime";
    public static final String SQL_INSERT_USER = "INSERT INTO users (login, password, email, uuid, userRole, " +
            "registrationDate) VALUES (?, ?, ?, ?, ?, ?)";
    public static final String SQL_SELECT_USER_BY_ID = "SELECT * FROM users WHERE userId = ?";
    public static final String SQL_SELECT_USER_BY_LOGIN = "SELECT * FROM users WHERE login = ?";
    public static final String SQL_SELECT_USER_BY_EMAIL = "SELECT * FROM users WHERE email = ?";
    public static final String SQL_UPDATE_USER = "UPDATE users SET login = ?, password = ?, email = ?, userRole = ?," +
            " registrationDate = ? WHERE userId = ?";
    public static final String SQL_USER_ID = "userId";
    public static final String SQL_PASSWORD = "password";
    public static final String SQL_EMAIL = "email";
    public static final String SQL_USER_ROLE = "userRole";
    public static final String SQL_REGISTRATION_DATE = "registrationDate";
    public static final String UNAUTHENTICATED_MENU = """
            1. Войти в систему
            2. Регистрация
            3. Завершить работу
            """;
    public static final String INVALID_CHOICE = "Неверный выбор.";
    public static final String USER_MENU_OPTIONS = """
            Главное меню:
            1. Посмотреть сеансы
            2. Посмотреть приобретенные билеты""";
    public static final String ADD_FILM = "3. Добавить фильм" + NEW_LINE;
    public static final String VIEW_USER_LIST = "4. Просмотреть список пользователей" + NEW_LINE;
    public static final String EXIT_SYSTEM = "0. Выход из системы";
    public static final String ENTER_CHOICE = "Введите число: ";
    public static final String INPUT_ERROR_MESSAGE = "Ошибка ввода. Пожалуйста, введите число.";
    public static final String EMPTY_LIST = "Список пуст.";
    public static final String ID_HEADER = "ID";
    public static final String LOGIN_HEADER = "Логин";
    public static final String EMAIL_HEADER = "Email";
    public static final String ROLE_HEADER = "Роль";
    public static final String REGISTRATION_DATE_HEADER = "Дата регистрации";
    public static final String LOGIN_SUCCESS = " Вход в систему выполнен успешно.";
    public static final String REGISTRATION_SUCCESS = "Вы успешно зарегистрировались!";
    public static final String ENTER_LOGIN = "Введите логин: ";
    public static final String EMPTY_FIELD = "Поле не может быть пустым";
    public static final String ENTER_PASSWORD = "Введите пароль: ";
    public static final String LOGIN_ALREADY_TAKEN = "Данный логин уже занят, попробуйте еще раз.";
    public static final String ENTER_EMAIL = "Введите email в формате (example@gmail.com): ";
    public static final String INVALID_EMAIL_ERROR = "Email-адрес некорректный.";
    public static final String REMAINING_ATTEMPTS = "Осталось попыток: ";
    public static final String FILM_NUM_HEADER = "№";
    public static final String FILM_TITLE_HEADER = "Название";
    public static final String FILM_YEAR_HEADER = "Год выпуска";
    public static final String FILM_COUNTRY_HEADER = "Страна";
    public static final String FILM_GENRE_HEADER = "Жанр";
    public static final String FILM_DURATION_HEADER = "Продолжительность";
    public static final String FILM_DESCRIPTION_HEADER = "Описание";
    public static final String FILM_PREMIERE_HEADER = "Премьера";
    public static final String ENTER_FILM_NUMBER = "Введите номер фильма: ";
    public static final String FILM_NOT_FOUND = "Фильм не найден.";
    public static final String INVALID_PLACE_ROW_NUMBER = "Некорректный номер ряда.";
    public static final String INVALID_PLACE_NUMBER = "Некорректный номер места.";
    public static final String TICKET_PURCHASE_SUCCESS = "Билет успешно куплен.";
    public static final String TICKET_PURCHASE_FAILURE = "Не удалось купить билет.";
    public static final String INVALID_INPUT_FORMAT = "Неверный формат ввода.";
    public static final String ERROR_MESSAGE_PREFIX = "Ошибка: ";
    public static final String NO_TICKETS_PURCHASED = "Вы еще не купили билеты.";
    public static final String ENTER_TICKET_NUMBER = "Введите номер билета: ";
    public static final String TICKET_NOT_FOUND = "Билет не найден.";
    public static final String TICKET_EXPIRED = "Билет просрочен";
    public static final String RETURN_OWN_TICKETS_ONLY = "Вы можете вернуть только свои собственные билеты.";
    public static final String TICKET_RETURN_SUCCESS = "Билет успешно возвращен.";
    public static final String TICKET_OVERDUE = " - Прострочен";
    public static final String TICKET_NUM_HEADER = "№ билета";
    public static final String TICKET_TITLE_HEADER = "Название";
    public static final String TICKET_DATE_HEADER = "Дата сеанса";
    public static final String TICKET_PLACE_NUMBER_HEADER = "Номер места";
    public static final String TABLE_FORMAT_LEFT_ALIGN = "| %-";
    public static final String SPACE_STRING = "s ";
    public static final String TABLE_ROW_SEPARATOR = "|%n";
    public static final String TABLE_BORDER_SYMBOL = "+";
    public static final String TABLE_HORIZONTAL_LINE = "-";
    public static final String TABLE_BORDER_NEW_LINE = "+%n";
    public static final String MINUTES_ABBREVIATION = "мин./";
    public static final String TIME_FORMAT = "%02d:%02d";
    public static final String DATE_TIME_FORMAT = "dd.MM.yyyy HH:mm";
    public static final String EDIT_FILM_OPTIONS = """
            1 - Редактировать название
            2 - Редактировать год
            3 - Редактировать страну
            4 - Редактировать жанр
            5 - Редактировать продолжительность
            6 - Редактировать описание
            7 - Редактировать дату релиза
            """;
    public static final String REMOVE_MOVIE = "8 - Удалить фильм";
    public static final String VIEW_FILM_OPTIONS = "1 - Приобрести билет" + NEW_LINE;
    public static final String GO_BACK = "0 - Вернуться назад" + NEW_LINE;
    public static final String EDIT_MOVIE_OPTIONS = """
            2 - Редактировать фильм
            3 - Удалить билет у пользователя
            4 - Добавить билет пользователю
            """ + GO_BACK;

    public static final String VIEW_TICKET_OPTIONS = "1 - Вернуть билет" + NEW_LINE + GO_BACK + NEW_LINE;
    public static final String ENTER_NEW_TITLE = "Введите новое название: ";
    public static final String ENTER_NEW_YEAR = "Введите новый год: ";
    public static final String ENTER_NEW_COUNTRY = "Введите новую страну: ";
    public static final String ENTER_NEW_GENRE = "Введите новый жанр: ";
    public static final String ENTER_NEW_DURATION = "Введите новую продолжительность в минутах: ";
    public static final String ENTER_NEW_DESCRIPTION = "Введите новое описание: ";
    public static final String ENTER_NEW_RELEASE_DATE = "Введите новую дату релиза (формат dd.MM.yyyy HH:mm): ";
    public static final String FILM_UPDATED_SUCCESS = "Фильм успешно обновлен.";
    public static final String MAX_ATTEMPTS_EXCEEDED = "Превышено максимальное количество попыток. Попробуйте еще раз " +
            "позже.";
    public static final String ENTER_FILM_DETAILS = "Введите данные фильма:";
    public static final String YEAR_OF_RELEASE_PROMPT = "Год выпуска: ";
    public static final String TITLE_PROMPT = "Название: ";
    public static final String COUNTRY_PROMPT = "Страна: ";
    public static final String GENRE_PROMPT = "Жанр: ";
    public static final String DURATION_PROMPT = "Длительность в минутах: ";
    public static final String DESCRIPTION_PROMPT = "Описание: ";
    public static final String DATE_TIME_PROMPT = "Дата и время (формат dd.MM.yyyy HH:mm): ";
    public static final String TICKET_PRICE_PROMPT = "Цена билетов: ";
    public static final String DATA_SAVED_SUCCESSFULLY = "Данные успешно сохранены.";
    public static final String FILM_ALREADY_EXISTS = "Данный фильм уже существует в таблице, попробуйте еще раз.";
    public static final String DB_CONNECTION_ERROR = "Ошибка при подключении к базе: ";
    public static final String UNEXPECTED_ERROR = "Произошла непредвиденная ошибка: ";

    public static final String MENU_TEXT = "1 - Редактировать пользователя" + NEW_LINE + GO_BACK;
    public static final String EDIT_USER_OPTIONS = """
            1 - Изменить логин
            2 - Изменить пароль
            3 - Изменить email
            4 - Изменить роль
            5 - Удалить пользователя
            """ + GO_BACK;
    public static final String SELECT_USER_BY_ID = "Выберите пользователя по id: ";

    public static final String ROLE_PROMPT = "Введите новое значение (0 - User, 1 - Manager, 2 - Admin): ";
    public static final String USER_UPDATED_SUCCESS = "Пользователь успешно обновлен.";
    public static final String PROMPT = "Введите номер места: ";
    public static final String ROW_PROMPT = "Введите номер ряда: ";
    public static final String INVALID_DATE_FORMAT = "Неправильный формат даты.";
    public static final String DB_CONFIG_INCOMPLETE = "Конфигурация базы данных отсутствует или является неполной";
    public static final String INVALID_CONFIGURATION = "Неправильная конфигурация: ";
    public static final String DB_CONNECTION_FAILURE = "Не удалось установить соединение с базой данных";
    public static final String FETCH_USERS_ERROR = "Ошибка при извлечении всех пользователей";
    public static final String UPDATE_ERROR = "Ошибка при выполнении обновления";
    public static final String FETCH_USER_ERROR = "Ошибка получения пользователя";
    public static final String FETCH_TICKETS_ERROR = "Ошибка при получении билетов";
    public static final String INVALID_LOGIN_OR_PASSWORD = "Неверный логин или пароль";
    public static final String USER_ID_NOT_FOUND = "Пользователь с указанным ID не найден.";
    public static final String INVALID_ROLE_VALUE = "Указано неверное значение роли (должно быть между 0 и 2).";
    public static final String UNSUPPORTED_UPDATE_ACTION = "Неподдерживаемое действие обновления";
    public static final String EMAIL_ALREADY_REGISTERED_ERROR = "Данный email уже занят";
    public static final String PURCHASE_DATE = "Дата покупки";
    public static final String BUYER_LOGIN = "Логин покупателя";
    public static final String ROW_TITLE = "Ряд:";
    public static final String PLACE = ", Место:";
    public static final String ERROR_KEY_IS_NULL_OR_EMPTY = "Ключ свойства не может быть нулевым или пустым";
    public static final String APPLICATION_PROPERTIES = "application.properties";
    public static final String ERROR_APPLICATION_PROPERTIES_NOT_FOUND = "Ресурс 'application.properties' не найден";
    public static final String ERROR_FILE_LOAD_FAILED = "Не удалось загрузить файл свойств: ";
    public static final String ERROR_READING_FILE = "Ошибка при чтении файла свойств: ";

    public static final String URL_KEY = "db.url";
    public static final String USERNAME_KEY = "db.username";
    public static final String PASSWORD_KEY = "db.password";
    public static final String ADMIN = "admin";
    public static final String ADMIN_EMAIL = "admin@example.com";
    public static final String MANAGER = "manager";
    public static final String MANAGER_EMAIL = "manager@example.com";
    public static final String USER_PURCHASED_MOVIE_TICKET = "Пользователь приобрел билет на фильм - ";
    public static final String MAIN_MENU_OPENED = "Открыто главное меню.";
    public static final String LOGGED_OUT = "Вышел из аккаунта";
    public static final String TICKET_PURCHASE_MENU_OPENED = "Открыто меню покупки билета";
    public static final String MOVIE_SELECTION_MENU_OPENED = "Открыто меню выбора фильма";
    public static final String VIEW_TICKETS_MENU_OPENED = "Пользователь открыл меню просмотра билетов";
    public static final String USER_RETURNED_TICKET_FOR = "Пользователь вернул билет на ";
    public static final String USER_PREFIX = "Пользователь ";
    public static final String USER_AUTHORIZED = " авторизовался";
    public static final String USER_REGISTERED = " зарегистрировался";


    private Constants() {
    }
}
