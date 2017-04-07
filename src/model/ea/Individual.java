package model.ea;

import model.schedule.DayRoster;
import model.schedule.Employee;

import java.util.ArrayList;
import java.util.List;

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
    private long id = Individual.getNextId();

    /**
     * Returns the last global identifier.
     * @return Identifier
     */
    private static long getNextId() {
        return lastId++;
    }

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
    private Double fitness = null;

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
        boolean feasible = true;

        // check, if hard  constraints for each planned day are satisfied
        for (DayRoster dayRoster: roster) {
            List<Employee> plannedEmployees = new ArrayList<Employee>();
            for (Employee employee: dayRoster.getDayRoster().values()) {
                // if the employee already has a shift this day, this solution is not feasible
                if (plannedEmployees.contains(employee)) {
                    feasible = false;
                    break;
                }

                plannedEmployees.add(employee);
            }

            // check if demand for the day is satisfied
            if (!dayRoster.isDemandedShiftAssignedToNurse()) {
                feasible = false;
            }

            // if last planned day has unsatisfied feasibility, we can abort right now
            if (!feasible) {
                break;
            }
        }

        return feasible;
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
        fitness = 0d;
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
