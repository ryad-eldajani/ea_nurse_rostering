package helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

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
     * %d is replaced by date in YYYY_MM_DD format
     * %t is replaced by time in HH_MM format
     * %h is replaced by users home directory (Unix-like: ~, Windows: %USERPROFILE%)
     * @param filename File name
     * @return Replaced file path
     */
    private String parsePlaceholders(String filename) {
        String date = DateTimeHelper.getInstance().getDateStringReversed("_");
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

    /**
     * Copies a file. See: http://stackoverflow.com/a/115086
     * @param sourceFile Source file
     * @param destFile Destination file
     * @throws IOException Exception on IO error.
     */
    public void copyFile(String sourceFile, String destFile) throws IOException {
        if (!(new File(destFile)).exists()) {
            (new File(destFile)).createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }
}
