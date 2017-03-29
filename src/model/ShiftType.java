package model;

import helper.DayHelper;

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
                "start time: " + DayHelper.getInstance().getTimeString(startTime) + ", " +
                "end time: " + DayHelper.getInstance().getTimeString(endTime) + ", " +
                "description: " + description;
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
