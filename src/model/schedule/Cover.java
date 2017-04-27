package model.schedule;

import helper.DateTimeHelper;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Covering model.
 */
public class Cover {
    /**
     * Day to cover
     */
    private Day day;

    /**
     * Covering information (shift type -> number of employees preferred).
     */
    private Map<ShiftType, Integer> covers = new HashMap<ShiftType, Integer>();

    /**
     * Returns the day.
     * @return Day
     */
    public Day getDay() {
        return day;
    }

    /**
     * Sets the day.
     * @param day Day
     */
    public void setDay(Day day) {
        this.day = day;
    }

    /**
     * Adds one cover definition to the covers map.
     * @param shiftType ShiftType instance.
     * @param preferred Number of persons preferred.
     */
    public void addCover(ShiftType shiftType, Integer preferred) {
        covers.put(shiftType, preferred);
    }

    /**
     * Returns covering information.
     * @return Cover information.
     */
    public Map<ShiftType, Integer> getCovers() {
        return covers;
    }

    @Override
    public String toString() {
        String nl = System.getProperty("line.separator");
        StringBuilder coverInfo = new StringBuilder();

        for (Map.Entry<ShiftType, Integer> cover: covers.entrySet()) {
            coverInfo.append(" -- ").append(cover.getKey().getDescription()).append(": ").append(cover.getValue()).append(nl);
        }

        return "Day: " + day.toString() + nl + " - Cover information: " + nl + coverInfo;

    }

    /**
     * Returns the preferred number of employees requested for a shift type.
     * @param shiftType ShiftType instance
     * @param date Date instance
     * @return Count of preferred employees per shift type
     */
    public Integer getPreferredEmployeeCount(ShiftType shiftType, Date date) {
        for (Map.Entry<ShiftType, Integer> cover: covers.entrySet()) {
            if (shiftType.equals(cover.getKey()) && DateTimeHelper.getInstance().getDayByDate(date).equals(day)) {
                return cover.getValue();
            }
        }

        return null;
    }
}
