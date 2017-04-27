package model.ea.constraints;

import helper.DateTimeHelper;
import model.ea.Individual;
import model.schedule.*;

import java.util.ArrayList;
import java.util.List;

/**
 * This class calculates counters for Employee instances.
 */
public class Counter {
    /**
     * Employee assigned to this counter.
     */
    private Employee employee;

    /**
     * Time unit as integer assigned to this counter.
     */
    private Integer[] timeUnit;

    /**
     * Booleans assigned to this counter, if employee has a shift.
     */
    private Boolean[] event;

    /**
     * Last integers assigned to this counter.
     */
    private Integer[] last;

    /**
     * Total integers assigned to this counter.
     */
    private Integer[] total;

    /**
     * Consecutive integers assigned to this counter.
     */
    private Integer[] consecutive;

    /**
     * Per time unit integers assigned to this counter.
     */
    private Integer[][] perts;

    /**
     * Calculates a list of Counter instances from a scheduling period and an individual.
     * @param period SchedulingPeriod instance
     * @param individual Individual instance
     * @return List of Counter instances
     */
    public static List<Counter> calculate(SchedulingPeriod period, Individual individual) {
        List<Counter> counters = new ArrayList<Counter>();

        // calculate counter for each employee
        for (Employee employee: period.getEmployees()) {
            counters.add(calculatePerEmployee(employee, period, individual));
        }

        return counters;
    }

    /**
     * Calculates the counter for an employee.
     * @param employee Employee instance
     * @param period SchedulingPeriod instance
     * @param individual Individual instance
     * @return Counter instance
     */
    private static Counter calculatePerEmployee(Employee employee, SchedulingPeriod period, Individual individual) {
        int numberDays = DateTimeHelper.getInstance().getNumberOfDays(period);
        int numberShiftTypes = period.getShiftTypes().size();
        int numberTimeUnits = numberDays * numberShiftTypes;
        List<DayRoster> rosters = individual.getDayRosters();
        List<ShiftType> shiftTypes = period.getShiftTypes();

        // setup counter
        Counter counter = new Counter();
        counter.timeUnit = new Integer[numberTimeUnits];
        counter.employee = employee;
        counter.event = new Boolean[numberTimeUnits];
        counter.last = new Integer[numberTimeUnits];
        counter.total = new Integer[numberTimeUnits];
        counter.consecutive = new Integer[numberTimeUnits];
        counter.perts = new Integer[numberDays][numberTimeUnits];

        int timeUnit = 0;
        int last = 0;
        int total = 0;
        int consecutive = 0;

        // Calculate for each rosters in this individual and each shift type in
        // this schedule period.
        for (int i = 0; i < rosters.size(); i++) {
            for (int j = 0; j < shiftTypes.size(); j++) {
                counter.timeUnit[timeUnit] = timeUnit;

                // if employee has this shift, set appropriate variables
                /*
                if (rosters.get(i).getDayRoster().get(shiftTypes.get(j)).equals(employee)) {
                    counter.event[timeUnit] = true;
                    last = timeUnit;
                    total++;
                    consecutive++;
                } else {
                    counter.event[timeUnit] = false;
                    consecutive = 0;
                }
                */

                counter.last[timeUnit] = last;
                counter.total[timeUnit] = total;
                counter.consecutive[timeUnit] = consecutive;

                timeUnit++;
            }
        }

        return counter;
    }
}
