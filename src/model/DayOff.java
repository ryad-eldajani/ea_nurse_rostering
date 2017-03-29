package model;

import java.util.Date;

/**
 * Day off request model.
 */
public class DayOff {
    /**
     * Penalty weight if constraint is unsatisfied.
     */
    private int weight;

    /**
     * Requested day off date.
     */
    private Date date;

    /**
     * Returns penalty weight.
     * @return Penalty weight.
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Sets penalty weight.
     * @param weight Penalty weight.
     */
    public void setWeight(int weight) {
        this.weight = weight;
    }

    /**
     * Returns date.
     * @return Date.
     */
    public Date getDate() {
        return date;
    }

    /**
     * Sets date.
     * @param date Date.
     */
    public void setDate(Date date) {
        this.date = date;
    }
}
