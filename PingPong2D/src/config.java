import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class config {
    /** Variables */
    public static Properties prop;
    private static final String path = "config";

    /**
     * load and processes the data from the config file
     */
    public static void initializePropertyFile() {
        prop = new Properties();
        try {
            InputStream ip = new FileInputStream(path);
            prop.load(ip);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
