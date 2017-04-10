package model.schedule;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Time unit model based on Burke et al. (2001)
 * "Fitness Evaluation for Nurse Scheduling Problems".
 */
public class TimeUnit {
    /**
     * Date of this time unit.
     */
    private Date date;

    /**
     * Shift type of this time unit.
     */
    private ShiftType shiftType;

    /**
     * Employees assigned to this time unit.
     */
    private List<Employee> employees = new ArrayList<Employee>();

    /**
     * List of all time units.
     */
    private static List<TimeUnit> timeUnits = new ArrayList<TimeUnit>();

    /**
     * Constructor.
     * @param date Date instance
     * @param shiftType ShiftType instance
     */
    public TimeUnit(Date date, ShiftType shiftType) {
        this.date = date;
        this.shiftType = shiftType;
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
     * Returns the shift type.
     * @return ShiftType instance
     */
    public ShiftType getShiftType() {
        return shiftType;
    }

    /**
     * Sets the shift type.
     * @param shiftType ShiftType instance
     */
    public void setShiftType(ShiftType shiftType) {
        this.shiftType = shiftType;
    }

    /**
     * Returns the list of Employee instances assigned to this time unit.
     * @return List of Employee instances
     */
    public List<Employee> getEmployees() {
        return employees;
    }

    /**
     * Adds an employee to the list of employees.
     * @param employee Employee instance
     */
    public void addEmployee(Employee employee) {
        if (!employees.contains(employee)) {
            employees.add(employee);
        }
    }

    /**
     * Returns a time unit for a date and shift type.
     * @param date Date instance
     * @param shiftType ShiftType instance
     * @return TimeUnit instance
     */
    public static TimeUnit getTimeUnit(Date date, ShiftType shiftType) {
        for (TimeUnit timeUnit: timeUnits) {
            if (timeUnit.getShiftType().equals(shiftType)
                    && timeUnit.getDate().equals(date)) {
                return timeUnit;
            }
        }

        // TimeUnit is not available already, add new instance and return it.
        TimeUnit timeUnit = new TimeUnit(date, shiftType);
        timeUnits.add(timeUnit);
        return timeUnit;
    }
}
