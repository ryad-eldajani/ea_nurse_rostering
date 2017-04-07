package model.schedule;

import java.util.Date;

/**
 * ShiftType off request model.
 */
public class ShiftOff {
    /**
     * Penalty weight if constraint is unsatisfied.
     */
    private int weight;

    /**
     * Shift type for requested shift off.
     */
    private ShiftType shiftType;

    /**
     * Requested day off date.
     */
    private Date date;

    /**
     * Returns the shift type.
     * @return ShiftType instance.
     */
    public ShiftType getShiftType() {
        return shiftType;
    }

    /**
     * Sets the shift type.
     * @param shiftType ShiftType instance.
     */
    public void setShiftType(ShiftType shiftType) {
        this.shiftType = shiftType;
    }

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
