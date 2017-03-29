package helper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Simple date and time helper.
 */
public class DateTimeHelper {
    /**
     * Singleton instance.
     */
    private final static DateTimeHelper instance = new DateTimeHelper();

    /**
     * Returns the singleton instance.
     *
     * @return Singleton instance
     */
    public static DateTimeHelper getInstance() {
        return DateTimeHelper.instance;
    }

    /**
     * Private constructor to avoid bypassing singleton.
     */
    private DateTimeHelper() {}

    /**
     * Parses a YYYY-MM-DD date to a Date instance.
     *
     * @param date Date as a string.
     * @return Date instance
     * @throws ParseException Parse exception.
     */
    public Date parseDate(String date) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        return dateFormat.parse(date);
    }

    /**
     * Parses a HH:MM:SS time to a Date instance.
     *
     * @param time Time as a string.
     * @return Date instance
     * @throws ParseException Parse exception.
     */
    public Date parseTime(String time) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("kk:mm:ss", Locale.ENGLISH);
        return dateFormat.parse(time);
    }
}
