package helper;

import java.io.File;

/**
 * Helper for file system methods.
 */
public class FilesystemHelper {
    /**
     * Singleton instance.
     */
    private final static FilesystemHelper instance = new FilesystemHelper();

    /**
     * Returns the singleton instance.
     *
     * @return Singleton instance
     */
    public static FilesystemHelper getInstance() {
        return FilesystemHelper.instance;
    }

    /**
     * Private constructor to avoid bypassing singleton.
     */
    private FilesystemHelper() {}

    /**
     * Replaces place holders with concrete values.
     * @param filename File name
     * @return Replaced file path
     */
    private String parsePlaceholders(String filename) {
        String date = DateTimeHelper.getInstance().getDateString("_");
        String time = DateTimeHelper.getInstance().getTimeString("_");
        String homeDirectory = System.getProperty("user.home");

        return filename.replace("/", System.getProperty("file.separator"))
                .replace("%d", date)
                .replace("%t", time)
                .replace("%h", homeDirectory);
    }

    /**
     * Returns a full path to a file name.
     * @param filename Filename
     * @return Full path to filename
     */
    public String getFullPath(String filename) {
        return (new File(parsePlaceholders(filename))).getAbsolutePath();
    }
}
