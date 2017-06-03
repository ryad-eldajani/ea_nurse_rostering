package model.ea;

import helper.ClassLoaderHelper;
import helper.DateTimeHelper;
import model.ea.constraints.IFitnessCalculator;
import model.schedule.*;

import java.util.*;

/**
 * This class represents an individual (a solution) for the evolutionary algorithm.
 */
public class Individual {
    /**
     * Last global identifier.
     */
    private static long lastId = 0;

    /**
     * Identifier for this instance.
     */
    private long id = lastId++;

    /**
     * Returns the identifier of this instance.
     * @return Identifier
     */
    public long getId() {
        return id;
    }

    /**
     * The fitness value of this individual.
     */
    private Float fitness = null;

    /**
     * List of all rosters for all days
     */
    private List<DayRoster> roster = new ArrayList<DayRoster>();

    /**
     * The scheduling period for this individual.
     */
    private SchedulingPeriod period = null;

    /**
     * The fitness calculator instance
     */
    private IFitnessCalculator fitnessCalculator = ClassLoaderHelper.getInstance().getFitnessCalculator();

    /**
     * Cache for assignments for each employee.
     */
    private Map<Employee, Map<DayRoster, Boolean>> assignments = new HashMap<Employee, Map<DayRoster, Boolean>>();

    /**
     * Cache for number of assignments per employee.
     */
    private Map<Employee, Integer> numAssignments = new HashMap<Employee, Integer>();

    /**
     * Cache for maximum number of consecutive assignments per employee.
     */
    private Map<Employee, Integer> numConsecutiveMaxWork = new HashMap<Employee, Integer>();

    /**
     * Cache for minimum number of consecutive assignments per employee.
     */
    private Map<Employee, Integer> numConsecutiveMinWork = new HashMap<Employee, Integer>();


    /**
     * Cache for maximum number of consecutive free days per employee.
     */
    private Map<Employee, Integer> numConsecutiveMaxFree = new HashMap<Employee, Integer>();

    /**
     * Cache for minimum number of consecutive free days per employee.
     */
    private Map<Employee, Integer> numConsecutiveMinFree = new HashMap<Employee, Integer>();

    /**
     * Cache for total number of working weekends per employee.
     */
    private Map<Employee, Integer> numWeekendsTotal = new HashMap<Employee, Integer>();

    /**
     * Cache for minimum number of consecutive working weekends per employee.
     */
    private Map<Employee, Integer> numConsecutiveWeekendsMin = new HashMap<Employee, Integer>();

    /**
     * Cache for maximum number of consecutive working weekends per employee.
     */
    private Map<Employee, Integer> numConsecutiveWeekendsMax = new HashMap<Employee, Integer>();

    /**
     * Cache for identical shift types during weekend per employee.
     */
    private Map<Employee, Boolean> identicalShiftTypesDuringWeekend = new HashMap<Employee, Boolean>();

    /**
     * Cache for complete working weekend per employee.
     */
    private Map<Employee, Boolean> completeWeekends = new HashMap<Employee, Boolean>();

    /**
     * Cache for no night shifts before weekend per employee.
     */
    private Map<Employee, Boolean> noNightShiftsBeforeWeekends = new HashMap<Employee, Boolean>();

    /**
     * Cache for unwanted patterns deviation per employee.
     */
    private Map<Employee, Float> unwantedPatternsDeviations = new HashMap<Employee, Float>();

    /**
     * Returns the List of DayRoster instances.
     * @return List of DayRoster instances
     */
    public List<DayRoster> getDayRosters() {
        return roster;
    }

    /**
     * Returns true, if this individual is a feasible solution, i.e. all
     * hard constraints are satisfied.
     * @return True, if this individual is a feasible solution
     */
    public boolean isFeasible() {
        // check, if hard  constraints for each planned day are satisfied
        for (DayRoster dayRoster: roster) {
            Map<ShiftType, List<Employee>> plannedEmployees = new HashMap<ShiftType, List<Employee>>();
            Map<ShiftType, Integer> preferredCounts = new HashMap<ShiftType, Integer>();

            for (Map<ShiftType, Employee> map: dayRoster.getDayRoster()) {
                for (Map.Entry<ShiftType, Employee> roster : map.entrySet()) {
                    ShiftType shiftType = roster.getKey();
                    Employee employee = roster.getValue();

                    if (plannedEmployees.containsKey(shiftType)) {
                        // only add, if not in list already
                        if (!plannedEmployees.get(shiftType).contains(employee)) {
                            plannedEmployees.get(shiftType).add(employee);
                        }
                    } else {
                        List<Employee> employees = new ArrayList<Employee>();
                        employees.add(employee);
                        plannedEmployees.put(shiftType, employees);
                    }

                    if (!preferredCounts.containsKey(shiftType)) {
                        preferredCounts.put(shiftType, period.getPreferredEmployeeCount(shiftType, dayRoster.getDate()));
                    }
                }
            }

            // check, if size of each planned employees
            for (Map.Entry<ShiftType, List<Employee>> planned: plannedEmployees.entrySet()) {
                ShiftType shiftType1 = planned.getKey();
                List<Employee> employees = planned.getValue();

                if (!preferredCounts.containsKey(shiftType1) || !(employees.size() == preferredCounts.get(shiftType1))) {
                    return false;
                }
            }

            // check if demand for the day is satisfied
            if (!dayRoster.isDemandedShiftAssignedToNurse()) {
                return false;
            }
        }

        // no hard constraint is unsatisfied, this is a feasible solution
        return true;
    }

    /**
     * Returns the fitness value for this individual. The lower the value, the better.
     * @return Fitness value
     */
    public float getFitness() {
        // if fitness value is not calculated, calculate now
        if (fitness == null) {
            fitness = fitnessCalculator.calculate(this, period);
        }

        return fitness;
    }

    /**
     * Setter for SchedulingPeriod instance.
     * @param period SchedulingPeriod instance
     */
    public void setSchedulingPeriod(SchedulingPeriod period) {
        this.period = period;
    }

    /**
     * Returns a deep copy of this instance.
     * @return Individual deep copy instance
     */
    static Individual copy(Individual individual) {
        Individual copyInstance = new Individual();
        copyInstance.fitness = individual.fitness;
        copyInstance.period = individual.period;

        // deep copy day roster
        for (DayRoster dayRoster: individual.roster) {
            copyInstance.roster.add(DayRoster.copy(dayRoster));
        }

        return copyInstance;
    }

    /**
     * Adds a DayRoster instance to all rosters and takes care of updating
     * TimeUnit instances.
     * @param dayRoster DayRoster instance
     */
    public void addDayRoster(DayRoster dayRoster) {
        roster.add(dayRoster);
    }

    /**
     * Returns the number of assignments per employee.
     * @param employee Employee instance
     * @return Number of assignments
     */
    public int getNumAssignments(Employee employee) {
        // check, if number of assignments is already calculated
        if (numAssignments.containsKey(employee)) {
            return numAssignments.get(employee);
        }

        // calculate number of assignments and store in cache
        int assignments = 0;
        for (DayRoster dayRoster: roster) {
            if (dayRoster.isEmployeePlanned(employee)) {
                assignments++;
            }
        }
        numAssignments.put(employee, assignments);

        return assignments;
    }

    /**
     * Returns a map of DayRoster -> Boolean where Boolean is True, if employee is working that day.
     * @param employee Employee instance
     * @return Map with True/False values for each day of an employee
     */
    private Map<DayRoster, Boolean> getAssignments(Employee employee) {
        // return assignments, if already calculated
        if (assignments.containsKey(employee)) {
            return assignments.get(employee);
        }

        // build a map for each day roster with employee planned booleans
        Map<DayRoster, Boolean> assigned = new LinkedHashMap<DayRoster, Boolean>();
        for (DayRoster dayRoster: roster) {
            assigned.put(dayRoster, dayRoster.isEmployeePlanned(employee));
        }

        // store assignments in cache and return result
        assignments.put(employee, assigned);
        return assigned;
    }

    /**
     * Calculates the consecutive days information per employee.
     * @param employee Employee instance
     */
    private void calculateConsecutiveDays(Employee employee) {
        // calculate number of consecutive events and store in caches
        int consecutiveWork = 0, consecutiveFree = 0,
                lastMinConsecutiveWork = 0, lastMaxConsecutiveWork = 0,
                lastMinConsecutiveFree = 0, lastMaxConsecutiveFree = 0;
        boolean yesterdayWork = false, yesterdayFree = false;
        for (Map.Entry<DayRoster, Boolean> entry: getAssignments(employee).entrySet()) {
            if (entry.getValue()) {
                // employee works today, check if employee also worked yesterday
                if (yesterdayWork) {
                    consecutiveWork++;
                }

                // employee has not free today, update last numbers of consecutive free days
                if (consecutiveFree > lastMaxConsecutiveFree) {
                    lastMaxConsecutiveFree = consecutiveFree;
                }
                if (lastMinConsecutiveFree == 0
                        || consecutiveFree > 0 && consecutiveFree < lastMinConsecutiveFree) {
                    lastMinConsecutiveFree = consecutiveFree;
                }

                consecutiveFree = 0;
                yesterdayWork = true;
                yesterdayFree = false;
            } else {
                // employee has free today, check if employee had free yesterday
                if (yesterdayFree) {
                    consecutiveFree++;
                }

                // employee does not work today, update last numbers of consecutive assignments
                if (consecutiveWork > lastMaxConsecutiveWork) {
                    lastMaxConsecutiveWork = consecutiveWork;
                }
                if (lastMinConsecutiveWork == 0
                        || consecutiveWork > 0 && consecutiveWork < lastMinConsecutiveWork) {
                    lastMinConsecutiveWork = consecutiveWork;
                }

                consecutiveWork = 0;
                yesterdayWork = false;
                yesterdayFree = true;
            }
        }

        numConsecutiveMinWork.put(employee, lastMinConsecutiveWork);
        numConsecutiveMaxWork.put(employee, lastMaxConsecutiveWork);
        numConsecutiveMinFree.put(employee, lastMinConsecutiveFree);
        numConsecutiveMaxFree.put(employee, lastMaxConsecutiveFree);
    }

    /**
     * Returns the requested consecutive event per employee.
     * @param employee Employee instance
     * @param map Map instance
     * @return Integer of consecutive events
     */
    private int getDayInformation(Employee employee, Map map) {
        // check, if number (maximum/minimum) of consecutive events are already calculated
        if (map.containsKey(employee)) {
            return (Integer) map.get(employee);
        }

        // we have no consecutive days information, calculate and return info
        calculateConsecutiveDays(employee);

        // re-run this method to return the calculated values
        return getDayInformation(employee, map);
    }

    /**
     * Returns the deviation for unsatisfied unwanted patterns per employee.
     * @param employee Employee instance.
     * @return Float of deviation for unwanted patterns per employee
     */
    private float getUnwantedPatternInformation(Employee employee) {
        if (unwantedPatternsDeviations.containsKey(employee)) {
            return unwantedPatternsDeviations.get(employee);
        }

        // we have no deviation information, calculate and add
        unwantedPatternsDeviations.put(employee, calculateUnwantedPatternsDeviation(employee));

        // re-run this method to return the calculated values
        return getUnwantedPatternInformation(employee);
    }

    /**
     * Returns the minimum number of consecutive working days per employee.
     * @param employee Employee instance
     * @return Minimum number of consecutive working days per employee
     */
    public int getNumMinConsecutiveWork(Employee employee) {
        return getDayInformation(employee, numConsecutiveMinWork);
    }

    /**
     * Returns the maximum number of consecutive working days per employee.
     * @param employee Employee instance
     * @return Maximum number of consecutive working days per employee
     */
    public int getNumMaxConsecutiveWork(Employee employee) {
        return getDayInformation(employee, numConsecutiveMaxWork);
    }

    /**
     * Returns the minimum number of consecutive free days per employee.
     * @param employee Employee instance
     * @return Minimum number of consecutive free days per employee
     */
    public int getNumMinConsecutiveFree(Employee employee) {
        return getDayInformation(employee, numConsecutiveMinFree);
    }

    /**
     * Returns the maximum number of consecutive free days per employee.
     * @param employee Employee instance
     * @return Maximum number of consecutive free days per employee
     */
    public int getNumMaxConsecutiveFree(Employee employee) {
        return getDayInformation(employee, numConsecutiveMaxFree);
    }

    /**
     * Calculates the unwanted pattern information per employee
     * @param employee Employee instance
     */
    private float calculateUnwantedPatternsDeviation(Employee employee) {
        float deviation = 0;

        // calculate for every unwanted pattern per employee
        for (Pattern pattern: employee.getContract().getUnwantedPatterns()) {
            Boolean satisfied = true;
            //PatternEntry[] patternEntries = (PatternEntry[]) pattern.getEntries().values().toArray();
            List<PatternEntry> patternEntries = new ArrayList<PatternEntry>(pattern.getEntries().values());
            int peIndex = 0; // pattern entry index

            // check for each day
            for (Map.Entry<DayRoster, Boolean> dayRosterEntry: getAssignments(employee).entrySet()) {
                DayRoster dayRoster = dayRosterEntry.getKey();
                Boolean working = dayRosterEntry.getValue();
                PatternEntry patternEntry = patternEntries.get(peIndex);

                // if current day matches pattern entry day or is any day
                if (patternEntry.isDayAny()
                        || patternEntry.getDay().equals(dayRoster.getDay())) {

                    // If
                    //  shift type of employee matches pattern entry shift type
                    //  or is any shift type
                    //  or employee is working and should not have a shift assigned
                    ShiftType shiftType = dayRoster.getShiftTypeForEmployee(employee);
                    if ((working && patternEntry.isShiftTypeNone())
                            || patternEntry.isShiftTypeAny()
                            || (shiftType != null && patternEntry.getShiftType().equals(shiftType))) {

                        // Increase peIndex to check next pattern element.
                        // If all pattern elements matched, this unwanted pattern soft-constraint
                        // is unsatisfied.
                        if (++peIndex >= patternEntries.size()) {
                            satisfied = false;
                            break;
                        }
                    } else {
                        peIndex = 0; // reset peIndex, as unwanted pattern is not complete
                    }
                } else {
                    peIndex = 0; // reset peIndex, as unwanted pattern is not complete
                }
            }

            // if unwanted pattern is unsatisfied, add deviation
            if (!satisfied) {
                deviation += pattern.getWeight();
            }
        }

        return deviation;
    }

    /**
     * Calculates the weekend information per employee.
     * @param employee Employee instance
     */
    private void calculateWeekends(Employee employee) {
        // calculate number of consecutive events and store in caches
        int consecutiveWork = 0, totalWork = 0,
                lastMinConsecutiveWeekendWork = 0, lastMaxConsecutiveWeekendWork = 0;

        boolean lastWeekendWork = false,
                completeWeekend = true,
                identicalShiftTypes = true,
                noNight = true;

        ShiftType lastShift = null;

        for (Map.Entry<DayRoster, Boolean> entry: getAssignments(employee).entrySet()) {
            DayRoster dayRoster = entry.getKey();
            Boolean working = entry.getValue();

            if (!DateTimeHelper.getInstance().isWeekend(dayRoster.getDate(), employee.getContract())) {
                // this day is not a weekend as of weekend definition in contract, go on..
                continue;
            }

            if (working) {
                // this is a weekend day and the employee is working
                totalWork++;

                // if employee worked last weekend day, increase consecutiveWork
                if (lastWeekendWork) {
                    consecutiveWork++;
                }

                lastWeekendWork = true;

                // check identical shift types
                if (lastShift == null) {
                    lastShift = dayRoster.getShiftTypeForEmployee(employee);
                } else if (lastShift != dayRoster.getShiftTypeForEmployee(employee)) {
                    // there was another shift type, identicalShiftTypes is not true anymore
                    identicalShiftTypes = false;
                }
            } else {
                // employee does not work today, update last numbers of consecutive events
                if (consecutiveWork > lastMaxConsecutiveWeekendWork) {
                    lastMaxConsecutiveWeekendWork = consecutiveWork;
                }
                if (lastMinConsecutiveWeekendWork == 0
                        || consecutiveWork > 0 && consecutiveWork < lastMinConsecutiveWeekendWork) {
                    lastMinConsecutiveWeekendWork = consecutiveWork;
                }
                // check, if last shift type was not a night shift
                if (lastShift != null && lastShift.isNight()) {
                    noNight = false;
                }

                consecutiveWork = 0;
                lastWeekendWork = false;
                completeWeekend = false;
            }
        }

        numWeekendsTotal.put(employee, totalWork);
        numConsecutiveWeekendsMin.put(employee, lastMinConsecutiveWeekendWork);
        numConsecutiveWeekendsMax.put(employee, lastMaxConsecutiveWeekendWork);
        completeWeekends.put(employee, completeWeekend);
        identicalShiftTypesDuringWeekend.put(employee, identicalShiftTypes);
        noNightShiftsBeforeWeekends.put(employee, noNight);
    }

    /**
     * Returns the requested weekend information per employee.
     * @param employee Employee instance
     * @param map Map instance
     * @return Integer|Boolean of weekend information
     * */
    private Object getWeekendInformation(Employee employee, Map map) {
        if (map.containsKey(employee)) {
            return map.get(employee);
        }

        // we have no weekend information, calculate and return info
        calculateWeekends(employee);

        // re-run this method to return the calculated value
        return getWeekendInformation(employee, map);
    }

    /**
     * Returns the total number of working weekends per employee.
     * @param employee Employee instance
     * @return Total number of working weekends per employee
     */
    public int getNumTotalWeekendsWork(Employee employee) {
        return (Integer) getWeekendInformation(employee, numWeekendsTotal);
    }

    /**
     * Returns the minimum number of consecutive working weekends per employee.
     * @param employee Employee instance
     * @return Minimum number of consecutive working weekends per employee
     */
    public int getNumMinConsecutiveWeekendsWork(Employee employee) {
        return (Integer) getWeekendInformation(employee, numConsecutiveWeekendsMin);
    }

    /**
     * Returns the maximum number of consecutive working weekends per employee.
     * @param employee Employee instance
     * @return Maximum number of consecutive working weekends per employee
     */
    public int getNumMaxConsecutiveWeekendsWork(Employee employee) {
        return (Integer) getWeekendInformation(employee, numConsecutiveWeekendsMax);
    }

    /**
     * Returns true, if employee has to work completely on weekends.
     * @param employee Employee instance
     * @return True, if employee has to work completely on weekends, otherwise false
     */
    public boolean isCompleteWeekends(Employee employee) {
        return (Boolean) getWeekendInformation(employee, completeWeekends);
    }

    /**
     * Returns the deviation for unsatisfied unwanted patterns per employee.
     * @param employee Employee instance
     * @return Float of deviation for unsatisfied unwanted patterns per employee
     */
    public float getUnwantedPatternDeviation(Employee employee) {
        return getUnwantedPatternInformation(employee);
    }

    /**
     * Returns true, if employee has only identical shift types on weekends.
     * @param employee Employee instance
     * @return True, if employee has only identical shift types on weekends, otherwise false
     */
    public boolean isIdenticalShiftType(Employee employee) {
        return (Boolean) getWeekendInformation(employee, identicalShiftTypesDuringWeekend);
    }

    /**
     * Returns true, if employee has no night shifts before a free weekends.
     * @param employee Employee instance
     * @return True, if employee has no night shifts before a free weekends, otherwise false
     */
    public boolean isNoNightShiftsBeforeFreeWeekend(Employee employee) {
        return (Boolean) getWeekendInformation(employee, noNightShiftsBeforeWeekends);
    }

    @Override
    public String toString() {
        String nl = System.getProperty("line.separator");
        StringBuilder out = new StringBuilder();

        out.append("Individual ID: ").append(id).append(", fitness: ").append(fitness).append(", feasible: ").append(isFeasible()).append(nl);

        for (DayRoster dayRoster: roster) {
            out.append(dayRoster).append(nl);
        }

        return out.toString();
    }
}
