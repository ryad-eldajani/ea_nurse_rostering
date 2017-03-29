package helper;

import model.Day;

import java.text.ParseException;
import java.util.*;

/**
 * Helper methods for days.
 */
public class DayHelper {
    /**
     * Singleton instance.
     */
    private final static DayHelper instance = new DayHelper();

    /**
     * Returns the singleton instance.
     *
     * @return Singleton instance
     */
    public static DayHelper getInstance() {
        return DayHelper.instance;
    }

    /**
     * Private constructor to avoid bypassing singleton.
     */
    private DayHelper() {}

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
     * @return String in HH:MM:SS format.
     */
    public String getTimeString(Date date) {
        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
        calendar.setTime(date);   // assigns calendar to given date
        return String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY))
                + ":" + String.format("%02d", calendar.get(Calendar.MINUTE))
                + ":" + String.format("%02d", calendar.get(Calendar.SECOND));
    }

    /**
     * Returns a date for a Date instance in DD.MM.YYYY format.
     * @param date Date instance
     * @return String in DD.MM.YYYY format.
     */
    public String getDateString(Date date) {
        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
        calendar.setTime(date);   // assigns calendar to given date
        return String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH))
                + "." + String.format("%02d", calendar.get(Calendar.MONTH))
                + "." + String.format("%02d", calendar.get(Calendar.YEAR));
    }
}
