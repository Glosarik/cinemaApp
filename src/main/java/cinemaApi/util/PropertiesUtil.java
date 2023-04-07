package cinemaApi.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {
    private static final Properties PROPERTIES = new Properties();

    static {
        loadProperties();
    }

    public static String get(String key) {
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException(Constants.ERROR_KEY_IS_NULL_OR_EMPTY);
        }
        return PROPERTIES.getProperty(key);
    }

    private static void loadProperties() {
        try (InputStream stream = PropertiesUtil.class.getClassLoader().getResourceAsStream(
                Constants.APPLICATION_PROPERTIES)) {
            if (stream == null) {
                throw new FileNotFoundException(Constants.ERROR_APPLICATION_PROPERTIES_NOT_FOUND);
            }
            PROPERTIES.load(stream);
        } catch (FileNotFoundException e) {
            System.err.println(Constants.ERROR_FILE_LOAD_FAILED + e.getMessage());
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.err.println(Constants.ERROR_READING_FILE + e.getMessage());
            throw new RuntimeException(e);
        } catch (Exception e) {
            System.err.println(Constants.UNEXPECTED_ERROR + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
