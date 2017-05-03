package model.schedule;

/**
 * Definition for a pattern entry.
 */
public class PatternEntry {
    /**
     * Identifier.
     */
    private int id;

    /**
     * If true, shiftType instance is irrelevant.
     */
    private boolean isShiftTypeAny = false;

    /**
     * If true, this day should not be assigned to an employee.
     */
    private boolean isShiftTypeNone = false;

    /**
     * ShiftType for this entry or null if none/any.
     */
    private ShiftType shiftType = null;

    /**
     * If true, day instance is irrelevant.
     */
    private boolean isDayAny = false;

    /**
     * Day enumeration element or null if any.
     */
    private Day day = null;

    /**
     * Getter for identifier.
     * @return Identifier
     */
    public int getId() {
        return id;
    }

    /**
     * Setter for identifier.
     * @param id Identifier
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter for isShiftTypeAny.
     * @return True, if shift type is irrelevant
     */
    public boolean isShiftTypeAny() {
        return isShiftTypeAny;
    }

    /**
     * Setter for isShiftTypeAny
     * @param shiftTypeAny Boolean
     */
    public void setShiftTypeAny(boolean shiftTypeAny) {
        isShiftTypeAny = shiftTypeAny;
    }

    /**
     * Getter for isShiftTypeNone
     * @return True, if no shift should be assigned
     */
    public boolean isShiftTypeNone() {
        return isShiftTypeNone;
    }

    /**
     * Setter for isShiftTypeNone.
     * @param shiftTypeNone Boolean
     */
    public void setShiftTypeNone(boolean shiftTypeNone) {
        isShiftTypeNone = shiftTypeNone;
    }

    /**
     * Getter for shiftType
     * @return ShiftType instance or null
     */
    public ShiftType getShiftType() {
        return shiftType;
    }

    /**
     * Setter for shiftType.
     * @param shiftType ShiftType instance
     */
    public void setShiftType(ShiftType shiftType) {
        this.shiftType = shiftType;
    }

    /**
     * Returns true, if day is irrelevant.
     * @return True, if day is irrelevant
     */
    public boolean isDayAny() {
        return isDayAny;
    }

    /**
     * Setter for dayAny.
     * @param dayAny Boolean
     */
    public void setDayAny(boolean dayAny) {
        isDayAny = dayAny;
    }

    /**
     * Getter for day.
     * @return Day instance or null
     */
    public Day getDay() {
        return day;
    }

    /**
     * Setter for day.
     * @param day Day instance
     */
    public void setDay(Day day) {
        this.day = day;
    }
}
