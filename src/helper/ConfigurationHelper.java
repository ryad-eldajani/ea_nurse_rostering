package helper;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * Helper methods for application configuration.
 */
public class ConfigurationHelper {
    /**
     * Singleton instance.
     */
    private final static ConfigurationHelper instance = new ConfigurationHelper();

    /**
     * Returns the singleton instance.
     *
     * @return Singleton instance
     */
    public static ConfigurationHelper getInstance() {
        return ConfigurationHelper.instance;
    }

    /**
     * Private constructor to avoid bypassing singleton.
     */
    private ConfigurationHelper() {
        try {
            loadConfiguration();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Properties instance.
     */
    private Properties configuration = null;

    /**
     * Loads config.properties an instantiate the Properties object.
     * @throws Exception If instance already set.
     */
    private void loadConfiguration() throws Exception {
        if (configuration == null) {
            configuration = new Properties();
            FileInputStream in = new FileInputStream("config.properties");
            configuration.load(in);
            in.close();
        } else {
            throw new Exception("Properties instance already instantiated.");
        }
    }

    /**
     * Returns a configuration property for a specific key.
     * @param key Configuration key.
     * @return Configuration property for key with fallback.
     */
    public String getProperty(String key) {
        return configuration.getProperty(key);
    }

    /**
     * Returns a configuration property for a specific key.
     * @param key Configuration key.
     * @param fallBack Fall back value.
     * @return Configuration property for key with fallback.
     */
    public String getProperty(String key, String fallBack) {
        return configuration.getProperty(key, fallBack);
    }

    /**
     * Returns an array of strings.
     * @param key Configuration key
     * @param separator Separator
     * @return Array of strings
     */
    public String[] getPropertyArray(String key, String separator) {
        String values = getProperty(key);
        return values.split(separator);
    }

    /**
     * Returns an array of strings.
     * @param key Configuration key
     * @return Array of strings
     */
    public String[] getPropertyArray(String key) {
        return getPropertyArray(key, "\\,");
    }
}
