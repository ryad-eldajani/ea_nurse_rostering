package model.schedule;

import java.util.ArrayList;
import java.util.List;

/**
 * Model for an employee.
 */
public class Employee {
    /**
     * Identifier.
     */
    private int id;

    /**
     * Name.
     */
    private String name;

    /**
     * Assigned contract.
     */
    private Contract contract;

    /**
     * List of required skills for this shift.
     */
    private List<Skill> skills = new ArrayList<Skill>();

    /**
     * List of requested days off.
     */
    private List<DayOff> dayOffRequests = new ArrayList<DayOff>();

    /**
     * List of requested shifts off.
     */
    private List<ShiftOff> shiftOffRequests = new ArrayList<ShiftOff>();

    /**
     * Adds a day off request.
     * @param dayOff DayOff instance.
     */
    public void addDayOffRequest(DayOff dayOff) {
        dayOffRequests.add(dayOff);
    }

    /**
     * Adds a shift off request.
     * @param shiftOff ShiftOff instance.
     */
    public void addShiftOffRequest(ShiftOff shiftOff) {
        shiftOffRequests.add(shiftOff);
    }

    /***
     * Following getters and setters are trivial and self explanatory, therefore not documented further.
     ***/

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return String.format("%2s", name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    /**
     * Returns true, if employee has required skills for a specific shift type.
     * @param shiftType ShiftType instance
     * @return True, if employee has required skills for a specific shift type
     */
    public boolean hasRequiredSkillsForShiftType(ShiftType shiftType) {
        // Check, if employee has required skill.
        for (Skill skill: skills) {
            if (shiftType.getRequiredSkills().contains(skill)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        return "ID: " + String.valueOf(id) + ", "
                + "name: " + name + ", "
                + "contract: " + contract.getDescription() + ", "
                + "skills: " + skills.toString();
    }
}
