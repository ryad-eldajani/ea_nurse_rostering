package model.schedule;

import helper.DateTimeHelper;

import java.util.*;

/**
 * Represents a roster for a day.
 */
public class DayRoster {
    /**
     * Roster information containing an employee for each shift type.
     */
    private Map<ShiftType, Employee> roster = new HashMap<ShiftType, Employee>();

    /**
     * The date of this roster.
     */
    private Date date;

    /**
     * Returns a deep copy of this instance.
     * @return DayRoster deep copy instance
     */
    public static DayRoster copy(DayRoster dayRoster) {
        DayRoster copyInstance = new DayRoster();
        copyInstance.setDate(DateTimeHelper.getInstance().getDateCopy(dayRoster.date));

        // deep copy roster information
        Map<ShiftType, Employee> copyRoster = new HashMap<ShiftType, Employee>();
        for (Map.Entry<ShiftType, Employee> entry: dayRoster.roster.entrySet()) {
            // We can use the given references for shift types and employees
            // here, thus we don't need to deep copy them.
            ShiftType shiftType = entry.getKey();
            Employee employee = entry.getValue();
            copyRoster.put(shiftType, employee);
        }
        copyInstance.roster = copyRoster;

        return copyInstance;
    }

    /**
     * Returns the date.
     * @return Date instance
     */
    public Date getDate() {
        return date;
    }

    /**
     * Sets the date.
     * @param date Date instance
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Returns true, if an employee is already planned this day.
     * @param employee Employee instance to check
     * @return True, if employee is already planned
     */
    public boolean isEmployeePlanned(Employee employee) {
        for (Employee employee1: roster.values()) {
            if (employee1.equals(employee)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns the roster information for this day.
     * @return Roster information
     */
    public Map<ShiftType, Employee> getDayRoster() {
        return roster;
    }

    /**
     * Returns true, if demanded shift is assigned to a nurse and the nurse
     * has the required skills.
     * @return True, if shift is assigned to appropriate nurse
     */
    public boolean isDemandedShiftAssignedToNurse() {
        for (Map.Entry<ShiftType, Employee> entry: roster.entrySet()) {
            ShiftType shiftType = entry.getKey();
            Employee employee = entry.getValue();

            // If the nurse doesn't have the required skill, the demand is unsatisfied.
            if (!employee.hasRequiredSkillsForShiftType(shiftType)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        List<String> out = new ArrayList<String>();
        out.add(DateTimeHelper.getInstance().getDateString(date));

        for (Map.Entry<ShiftType, Employee> entry: roster.entrySet()) {
            ShiftType shiftType = entry.getKey();
            Employee employee = entry.getValue();

            out.add("(Shift type: " + shiftType.getId() + ", Employee: " + employee.getName() + ")");
        }

        return out.toString();
    }

    /**
     * Adds a roster information.
     * @param shiftType ShiftType instance
     * @param employee Employee instance
     */
    public void addToDayRoster(ShiftType shiftType, Employee employee) {
        roster.put(shiftType, employee);
    }
}
