package webapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final Config INSTANCE = new Config();
    protected static final File PROPS = new File(".\\config\\resume.properties");
    private Properties properties = new Properties();

    public static Config get() {
        return INSTANCE;
    }

    private Config() {
        try (InputStream inputStream = new FileInputStream(PROPS)) {
            properties.load(inputStream);
            properties.getProperty("storage.dir");
        } catch (IOException e) {
            throw new IllegalStateException("Invalid config file " + PROPS.getAbsolutePath());
        }
    }
}
