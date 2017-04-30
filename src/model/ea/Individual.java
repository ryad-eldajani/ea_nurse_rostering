package model.ea;

import helper.ClassLoaderHelper;
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
     * Cache for number of assignments per employee.
     */
    private Map<Employee, Integer> numAssignments = new HashMap<Employee, Integer>();

    /**
     * Cache for maximum number of consecutive assignments per employee.
     */
    private Map<Employee, Integer> numConsecutiveDaysMaxWork = new HashMap<Employee, Integer>();

    /**
     * Cache for minimum number of consecutive assignments per employee.
     */
    private Map<Employee, Integer> numConsecutiveDaysMinWork = new HashMap<Employee, Integer>();


    /**
     * Cache for maximum number of consecutive free days per employee.
     */
    private Map<Employee, Integer> numConsecutiveDaysMaxFree = new HashMap<Employee, Integer>();

    /**
     * Cache for minimum number of consecutive free days per employee.
     */
    private Map<Employee, Integer> numConsecutiveDaysMinFree = new HashMap<Employee, Integer>();

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
    boolean isFeasible() {
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

        // update time units
        for (Map<ShiftType, Employee> map: dayRoster.getDayRoster()) {
            for (Map.Entry<ShiftType, Employee> entry: map.entrySet()) {
                ShiftType shiftType = entry.getKey();
                Date date = dayRoster.getDate();
                Employee employee = entry.getValue();

                TimeUnit timeUnit = TimeUnit.getTimeUnit(date, shiftType);
                timeUnit.addEmployee(employee);
            }
        }
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
     * Returns the (minimum/maximum) number of consecutive assignments/free days per employee.
     * @param employee Employee instance
     * @param max If true, returns the maximum number of consecutive events, otherwise minimum
     * @param free If true, returns the minimum/maximum number of consecutive free days, otherwise assignments
     * @return Number of consecutive assignments
     */
    public int getNumConsecutiveDays(Employee employee, boolean max, boolean free) {
        // check, if number (maximum/minimum) of consecutive events are already calculated
        if (!free && !max && numConsecutiveDaysMinWork.containsKey(employee)) {
            return numConsecutiveDaysMinWork.get(employee);
        } else if (!free && max && numConsecutiveDaysMaxWork.containsKey(employee)) {
            return numConsecutiveDaysMaxWork.get(employee);
        } else if (free & !max && numConsecutiveDaysMinFree.containsKey(employee)) {
            return numConsecutiveDaysMinFree.get(employee);
        } else if (free && max && numConsecutiveDaysMaxFree.containsKey(employee)) {
            return numConsecutiveDaysMaxFree.get(employee);
        }

        // build a map for each day roster with employee planned booleans
        Map<DayRoster, Boolean> assigned = new HashMap<DayRoster, Boolean>();
        for (DayRoster dayRoster: roster) {
            assigned.put(dayRoster, dayRoster.isEmployeePlanned(employee));
        }

        // calculate number of consecutive events and store in caches
        int consecutiveWork = 0, consecutiveFree = 0,
                lastMinConsecutiveWork = 0, lastMaxConsecutiveWork = 0,
                lastMinConsecutiveFree = 0, lastMaxConsecutiveFree = 0;
        boolean yesterdayWork = false, yesterdayFree = false;
        for (Map.Entry<DayRoster, Boolean> entry: assigned.entrySet()) {
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

        numConsecutiveDaysMinWork.put(employee, lastMinConsecutiveWork);
        numConsecutiveDaysMaxWork.put(employee, lastMaxConsecutiveWork);
        numConsecutiveDaysMinFree.put(employee, lastMinConsecutiveFree);
        numConsecutiveDaysMaxFree.put(employee, lastMaxConsecutiveFree);

        // return minimum or maximum number of consecutive assignments by min
        return max ? lastMinConsecutiveWork : lastMaxConsecutiveWork;
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
