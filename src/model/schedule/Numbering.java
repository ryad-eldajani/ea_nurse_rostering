package model.schedule;


import helper.DateTimeHelper;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Numbering model based on Burke et al. (2001)
 * "Fitness Evaluation for Nurse Scheduling Problems".
 */
public class Numbering {
    /**
     * Last global identifier.
     */
    private static int lastId = 0;

    /**
     * Numbering for constraints related to days (N1).
     */
    private static Numbering days = new Numbering();

    /**
     * Numbering for constraints related to night shifts (N2).
     */
    private static Numbering nights = new Numbering();

    /**
     * Numbering for constraints related to weekends (N3).
     */
    private static Numbering weekends = new Numbering();

    /**
     * Numbering for constraints related number of assignments.
     */
    private static Numbering numberOfAssignments = new Numbering();

    /**
     * Numbering mapping. Instance of LinkedHashMap to preserve the sorting by
     * time unit date. If Integer is null, the value correspondents to "undefined".
     */
    private Map<TimeUnit, Integer> mapping = new LinkedHashMap<TimeUnit, Integer>();

    /**
     * Identifier for this instance.
     */
    private int id = lastId++;

    /**
     * Returns the identifier of this instance.
     * @return Identifier
     */
    public long getId() {
        return id;
    }

    /**
     * Returns the numbering mapping.
     * @return Map with TimeUnit to Integer instances
     */
    public Map<TimeUnit, Integer> getMapping() {
        return mapping;
    }

    /**
     * Sets up the correct numberings for a scheduling period.
     * @param period SchedulingPeriod instance
     */
    public static void setupNumberings(SchedulingPeriod period) {
        int dayValue = 0;
        int nightValue = 0;
        int weekendValue = 0;
        int numberOfAssignmentsValue = 0;
        for (Date date: DateTimeHelper.getInstance().getListOfDates(period)) {
            for (ShiftType shiftType: period.getShiftTypes()) {
                TimeUnit timeUnit = TimeUnit.getTimeUnit(date, shiftType);

                numberOfAssignments.mapping.put(timeUnit, numberOfAssignmentsValue++);

                days.mapping.put(timeUnit, dayValue);

                // If this is a night shift, add value to night numbering,
                // otherwise add null ("undefined").
                nights.mapping.put(timeUnit, shiftType.isNight() ? nightValue++ : null);

                // if this is a week end day, add value to weekends
                weekends.mapping.put(timeUnit, DateTimeHelper.getInstance().isWeekend(date) ? weekendValue : null);
            }

            // increase values, if necessary
            dayValue++;
            if (DateTimeHelper.getInstance().isWeekend(date)) {
                weekendValue++;
            }
        }
    }

    /**
     * Returns the numbering for constraints related to days (N1).
     * @return Numbering instance
     */
    public static Numbering getDays() {
        return days;
    }

    /**
     * Returns the numbering for constraints related to night shifts (N2).
     * @return Numbering instance
     */
    public static Numbering getNights() {
        return nights;
    }

    /**
     * Returns the numbering for constraints related to weekends (N3).
     * @return Numbering instance
     */
    public static Numbering getWeekends() {
        return weekends;
    }
}
