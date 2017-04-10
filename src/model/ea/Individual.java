package model.ea;

import model.schedule.DayRoster;
import model.schedule.Employee;
import model.schedule.ShiftType;
import model.schedule.TimeUnit;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    private Integer fitness = null;

    /**
     * List of all rosters for all days
     */
    private List<DayRoster> roster = new ArrayList<DayRoster>();

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
            List<Employee> plannedEmployees = new ArrayList<Employee>();
            for (Employee employee: dayRoster.getDayRoster().values()) {
                // if the employee already has a shift this day, this solution is not feasible
                if (plannedEmployees.contains(employee)) {
                    return false;
                }

                plannedEmployees.add(employee);
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
    public double getFitness() {
        // if fitness value is not calculated, calculate now
        if (fitness == null) {
            calculateFitness();
        }

        return fitness;
    }

    /**
     * Calculates the fitness value for this individual. The lower the value, the better.
     */
    public void calculateFitness() {
        // TODO: Calculate correct fitness value
        fitness = 0;
    }

    /**
     * Returns a deep copy of this instance.
     * @return Individual deep copy instance
     */
    public static Individual copy(Individual individual) {
        Individual copyInstance = new Individual();
        copyInstance.fitness = individual.fitness;

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
            for (Map.Entry<ShiftType, Employee> entry: dayRoster.getDayRoster().entrySet()) {
                ShiftType shiftType = entry.getKey();
                Employee currentEmployee = entry.getValue();
                TimeUnit timeUnit = TimeUnit.getTimeUnit(dayRoster.getDate(), shiftType);

                if (currentEmployee.getId() == employee.getId()) {
                    timeUnits.add(timeUnit);
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
        for (Map.Entry<ShiftType, Employee> entry: dayRoster.getDayRoster().entrySet()) {
            ShiftType shiftType = entry.getKey();
            Date date = dayRoster.getDate();
            Employee employee = entry.getValue();

            TimeUnit timeUnit = TimeUnit.getTimeUnit(date, shiftType);
            timeUnit.addEmployee(employee);
        }
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
