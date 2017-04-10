package model.schedule;

import helper.DateTimeHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Model for a shift type.
 */
public class ShiftType {
    /**
     * Identifier.
     */
    private String id;

    /**
     * Start time.
     */
    private Date startTime;

    /**
     * End time.
     */
    private Date endTime;

    /**
     * Description.
     */
    private String description;

    /**
     * List of required skills for this shift.
     */
    private List<Skill> requiredSkills = new ArrayList<Skill>();

    @Override
    public String toString() {
        return "ID: " + id + ", " +
                "start time: " + DateTimeHelper.getInstance().getTimeString(startTime) + ", " +
                "end time: " + DateTimeHelper.getInstance().getTimeString(endTime) + ", " +
                "description: " + description + ", " +
                "required skills: " + requiredSkills;
    }

    /**
     * Returns true, if this is an early shift type.
     * @return True, if early shift type
     */
    public boolean isEarly() {
        return id.equals("E");
    }

    /**
     * Returns true, if this is a day shift type.
     * @return True, if day shift type
     */
    public boolean isDay() {
        return id.equals("D");
    }

    /**
     * Returns true, if this is a late shift type.
     * @return True, if late shift type
     */
    public boolean isLate() {
        return id.equals("L");
    }

    /**
     * Returns true, if this is a night shift type.
     * @return True, if night shift type
     */
    public boolean isNight() {
        return id.equals("N");
    }

    /**
     * Returns true, if this is a day head nurse shift type.
     * @return True, if day head nurse shift type
     */
    public boolean isDayHeadNurse() {
        return id.equals("DH");
    }

    /***
     * Following getters and setters are trivial and self explanatory, therefore not documented further.
     ***/

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Skill> getRequiredSkills() {
        return requiredSkills;
    }

    public void setRequiredSkills(List<Skill> requiredSkills) {
        this.requiredSkills = requiredSkills;
    }
}
