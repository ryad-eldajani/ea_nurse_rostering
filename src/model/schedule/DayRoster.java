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
    private List<Map<ShiftType, Employee>> roster = new ArrayList<Map<ShiftType, Employee>>();

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
        List<Map<ShiftType, Employee>> copyRoster = new ArrayList<Map<ShiftType, Employee>>();

        for (Map<ShiftType, Employee> map: dayRoster.roster) {
            for (Map.Entry<ShiftType, Employee> entry: map.entrySet()) {
                // We can use the given references for shift types and employees
                // here, thus we don't need to deep copy them.
                ShiftType shiftType = entry.getKey();
                Employee employee = entry.getValue();
                Map<ShiftType, Employee> copyMap = new HashMap<ShiftType, Employee>();
                copyMap.put(shiftType, employee);
                copyRoster.add(copyMap);
            }
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
     * Returns the day by date.
     * @return Day instance
     */
    public Day getDay() {
        return DateTimeHelper.getInstance().getDayByDate(date);
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
        for (Map<ShiftType, Employee> map: roster) {
            for (Employee employee1: map.values()) {
                if (employee1.equals(employee)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Returns the roster information for this day.
     * @return Roster information
     */
    public List<Map<ShiftType, Employee>> getDayRoster() {
        return roster;
    }

    /**
     * Returns true, if demanded shift is assigned to a nurse and the nurse
     * has the required skills.
     * @return True, if shift is assigned to appropriate nurse
     */
    public boolean isDemandedShiftAssignedToNurse() {
        for (Map<ShiftType, Employee> map: roster) {
            for (Map.Entry<ShiftType, Employee> entry: map.entrySet()) {
                ShiftType shiftType = entry.getKey();
                Employee employee = entry.getValue();

                // If the nurse doesn't have the required skill, the demand is unsatisfied.
                if (!employee.hasRequiredSkillsForShiftType(shiftType)) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Adds a roster information.
     * @param shiftType ShiftType instance
     * @param employee Employee instance
     */
    public void addToDayRoster(ShiftType shiftType, Employee employee) {
        Map<ShiftType, Employee> map = new HashMap<ShiftType, Employee>();
        map.put(shiftType, employee);
        roster.add(map);
    }

    /**
     * Returns the shift type of employee for this day if available, otherwise null.
     * @param employee Employee instance
     * @return ShiftType instance for employee or null
     */
    public ShiftType getShiftTypeForEmployee(Employee employee) {
        for (Map<ShiftType, Employee> map: roster) {
            for (Map.Entry<ShiftType, Employee> entry : map.entrySet()) {
                ShiftType shiftType = entry.getKey();
                Employee employee1 = entry.getValue();

                if (employee1.equals(employee)) {
                    return shiftType;
                }
            }
        }

        return null;
    }

    @Override
    public String toString() {
        List<String> out = new ArrayList<String>();
        out.add(DateTimeHelper.getInstance().getDateString(date) + ", "
            + String.format("%9s", DateTimeHelper.getInstance().getDayByDate(date)));

        for (Map<ShiftType, Employee> map: roster) {
            for (Map.Entry<ShiftType, Employee> entry: map.entrySet()) {
                ShiftType shiftType = entry.getKey();
                Employee employee = entry.getValue();

                out.add("(Shift type: " + shiftType.getId() + ", Employee: "
                        + employee.getName() + " " + employee.getSkills() + ")");
            }
        }

        return out.toString();
    }
}
