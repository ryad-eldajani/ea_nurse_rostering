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
                    ShiftType shiftType = shiftType = roster.getKey();
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
    public static Individual copy(Individual individual) {
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
     * Returns a list of TimeUnit instances for an employee.
     * This correspondents to Definition 3 (event) of Burke et al. (2001)
     * "Fitness Evaluation for Nurse Scheduling Problems".
     * @param employee Employee to get time units
     * @return List of TimeUnit instances
     */
    public List<TimeUnit> getTimeUnitsForEmployee(Employee employee) {
        List<TimeUnit> timeUnits = new ArrayList<TimeUnit>();
        for (DayRoster dayRoster: roster) {
            for (Map<ShiftType, Employee> map: dayRoster.getDayRoster()) {
                for (Map.Entry<ShiftType, Employee> entry: map.entrySet()) {
                    ShiftType shiftType = entry.getKey();
                    Employee currentEmployee = entry.getValue();
                    TimeUnit timeUnit = TimeUnit.getTimeUnit(dayRoster.getDate(), shiftType);

                    if (currentEmployee.getId() == employee.getId()) {
                        timeUnits.add(timeUnit);
                    }
                }
            }
        }

        return timeUnits;
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
     * Returns the number of assignments per employee
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
