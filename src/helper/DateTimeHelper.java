package helper;

import model.schedule.Day;
import model.schedule.SchedulingPeriod;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

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

    /**
     * Returns the number of days between start and end date of a scheduling period.
     * @param period SchedulingPeriod instance
     * @return Number of days between start and end date
     */
    public int getNumberOfDays(SchedulingPeriod period) {
        long daysDifference = period.getEndDate().getTime() - period.getStartDate().getTime();
        return (int) TimeUnit.DAYS.convert(daysDifference, TimeUnit.MILLISECONDS);
    }

    /**
     * Returns a Date instance by start date plus number of days.
     * @param period SchedulingPeriod instance
     * @param number Number of date from starting date
     * @return Date of start date plus number of days
     */
    public Date getDateByNumber(SchedulingPeriod period, int number) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(period.getStartDate());
        calendar.add(Calendar.DAY_OF_MONTH, number);

        return calendar.getTime();
    }

    /**
     * Returns a Day instance by a specific date.
     * @param date Date instance
     * @return Day instance
     */
    public Day getDayByDate(Date date) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        switch (dayOfWeek) {
            case 1: return Day.SUNDAY;
            case 2: return Day.MONDAY;
            case 3: return Day.TUESDAY;
            case 4: return Day.WEDNESDAY;
            case 5: return Day.THURSDAY;
            case 6: return Day.FRIDAY;
        }

        return Day.SATURDAY;
    }

    /**
     * Returns a Day value by its string equivalent.
     * @param dayName Day name to convert.
     * @return Day value.
     * @throws ParseException If not a valid day name.
     */
    public Day getDayByName(String dayName) throws ParseException {
        if (dayName.trim().toLowerCase().equals("monday")) {
            return Day.MONDAY;
        } else if (dayName.trim().toLowerCase().equals("tuesday")) {
            return Day.TUESDAY;
        } else if (dayName.trim().toLowerCase().equals("wednesday")) {
            return Day.WEDNESDAY;
        } else if (dayName.trim().toLowerCase().equals("thursday")) {
            return Day.THURSDAY;
        } else if (dayName.trim().toLowerCase().equals("friday")) {
            return Day.FRIDAY;
        } else if (dayName.trim().toLowerCase().equals("saturday")) {
            return Day.SATURDAY;
        } else if (dayName.trim().toLowerCase().equals("sunday")) {
            return Day.SUNDAY;
        }

        throw new ParseException("Not a valid day name.", 0);
    }

    /**
     * Returns a List of Day instances by a string containing day names.
     * @param dayNames Day names (e.g.: MondayTuesdaySaturday)
     * @return List of Day instances.
     */
    public List<Day> getDayListFromString(String dayNames) {
        List<Day> dayList = new ArrayList<Day>();

        if (dayNames.contains("Monday")) {
            dayList.add(Day.MONDAY);
        }

        if (dayNames.contains("Tuesday")) {
            dayList.add(Day.TUESDAY);
        }

        if (dayNames.contains("Wednesday")) {
            dayList.add(Day.WEDNESDAY);
        }

        if (dayNames.contains("Thursday")) {
            dayList.add(Day.THURSDAY);
        }

        if (dayNames.contains("Friday")) {
            dayList.add(Day.FRIDAY);
        }

        if (dayNames.contains("Saturday")) {
            dayList.add(Day.SATURDAY);
        }

        if (dayNames.contains("Sunday")) {
            dayList.add(Day.SUNDAY);
        }

        return dayList;
    }

    /**
     * Returns a time for a Date instance in HH:MM:SS format.
     * @param date Date instance
     * @param separator Separator
     * @return String in HH[separator]MM[separator]SS format.
     */
    public String getTimeString(Date date, String separator) {
        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
        calendar.setTime(date);   // assigns calendar to given date
        return String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY))
                + separator + String.format("%02d", calendar.get(Calendar.MINUTE))
                + separator + String.format("%02d", calendar.get(Calendar.SECOND));
    }

    /**
     * Returns a time for a Date instance in HH:MM:SS format.
     * @param date Date instance
     * @return String in HH:MM:SS format.
     */
    public String getTimeString(Date date) {
        return getTimeString(date, ":");
    }

    /**
     * Returns the current time in HH[separator]MM[separator]SS format.
     * @param separator Separator
     * @return String in HH[separator]MM[separator]SS format.
     */
    public String getTimeString(String separator) {
        return getTimeString(new Date(), separator);
    }

    /**
     * Returns the current time in HH:MM:SS format.
     * @return String in HH:MM:SS format.
     */
    public String getTimeString() {
        return getTimeString(new Date());
    }

    /**
     * Returns a copy of a date instance.
     * @param date Date instance
     * @return Date instance of copied date
     */
    public Date getDateCopy(Date date) {
        return new Date(date.getTime());
    }

    /**
     * Returns a date for a Date instance in DD.MM.YYYY format.
     * @param date Date instance
     * @param separator Separator
     * @return String in DD[separator]MM[separator]YYYY format.
     */
    public String getDateString(Date date, String separator) {
        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
        calendar.setTime(date);   // assigns calendar to given date
        return String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH))
                + separator + String.format("%02d", calendar.get(Calendar.MONTH) + 1) // +1, as zero indexed
                + separator + String.format("%02d", calendar.get(Calendar.YEAR));
    }

    /**
     * Returns a date for a Date instance in DD.MM.YYYY format.
     * @param date Date instance
     * @return String in DD.MM.YYYY format.
     */
    public String getDateString(Date date) {
        return getDateString(date, ".");
    }

    /**
     * Returns the current date in DD[separator]MM[separator]YYYY format.
     * @param separator Separator
     * @return String in DD[separator]MM[separator]YYYY format.
     */
    public String getDateString(String separator) {
        return getDateString(new Date(), separator);
    }

    /**
     * Returns the current date in DD.MM.YYYY format.
     * @return String in DD.MM.YYYY format.
     */
    public String getDateString() {
        return getDateString(new Date());
    }
}
