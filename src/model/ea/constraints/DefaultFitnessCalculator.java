package model.ea.constraints;

import model.ea.Individual;
import model.schedule.Attribute;
import model.schedule.Employee;
import model.schedule.SchedulingPeriod;

/**
 * Implements the default fitness calculator which uses algorithms for each
 * soft constraint to calculate the overall fitness.
 */
public class DefaultFitnessCalculator implements IFitnessCalculator {
    /**
     * Holds the current individual instance.
     */
    private Individual individual;

    /**
     * Holds the scheduling period instance.
     */
    private SchedulingPeriod period;

    @Override
    public float calculate(Individual individual, SchedulingPeriod period) {
        float fitness = 0;
        this.individual = individual;
        this.period = period;

        // calculate each deviations
        fitness += getDeviationMaxNumAssignments();
        fitness += getDeviationMinNumAssignments();

        return fitness;
    }

    /**
     * Calculates the deviation for MaxNumAssignments for each employee.
     * @return Deviation
     */
    private float getDeviationMaxNumAssignments() {
       float deviation = 0;

       // calculate for each employee
       for (Employee employee: period.getEmployees()) {
           Attribute maxNumAssignments = employee.getContract().getMaxNumAssignments();
           int weight = maxNumAssignments.getWeight();
           int value = maxNumAssignments.getValueInt();
           int numAssignments = individual.getNumAssignments(employee);

           // soft-constraint is unsatisfied, add deviation
           if (numAssignments > value) {
               deviation += (numAssignments - value) * weight;
           }
       }

       return deviation;
    }

    /**
     * Calculates the deviation for MaxNumAssignments for each employee.
     * @return Deviation
     */
    private float getDeviationMinNumAssignments() {
        float deviation = 0;

        // calculate for each employee
        for (Employee employee: period.getEmployees()) {
            Attribute minNumAssignments = employee.getContract().getMinNumAssignments();
            int weight = minNumAssignments.getWeight();
            int value = minNumAssignments.getValueInt();
            int numAssignments = individual.getNumAssignments(employee);

            // soft-constraint is unsatisfied, add deviation
            if (numAssignments < value) {
                deviation += (value - numAssignments) * weight;
            }
        }

        return deviation;
    }
}
