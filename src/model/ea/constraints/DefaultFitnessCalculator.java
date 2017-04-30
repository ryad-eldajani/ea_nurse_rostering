package model.ea.constraints;

import model.ea.Individual;
import model.schedule.Attribute;
import model.schedule.Employee;
import model.schedule.SchedulingPeriod;
import org.w3c.dom.Attr;

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
        fitness += getDeviationNumAssignments();
        fitness += getDeviationNumConsecutiveDays();

        return fitness;
    }

    /**
     * Calculates the deviation for Min/MaxNumAssignments for each employee.
     * @return Deviation
     */
    private float getDeviationNumAssignments() {
       float deviation = 0;

       // calculate for each employee
       for (Employee employee: period.getEmployees()) {
           Attribute minNumAssignments = employee.getContract().getMinNumAssignments();
           Attribute maxNumAssignments = employee.getContract().getMaxNumAssignments();
           int weightMin = minNumAssignments.getWeight();
           int valueMin = minNumAssignments.getValueInt();
           int weightMax = maxNumAssignments.getWeight();
           int valueMax = maxNumAssignments.getValueInt();
           int numAssignments = individual.getNumAssignments(employee);

           // if soft-constraints are unsatisfied, add deviation
           if (numAssignments < valueMin) {
               deviation += (valueMin - numAssignments) * weightMin;
           }
           if (numAssignments > valueMax) {
               deviation += (numAssignments - valueMax) * weightMax;
           }
       }

       return deviation;
    }

    /**
     * Calculates the deviations for Min/MaxNumConsecutive(Free)Days for each employee.
     * @return Deviation
     */
    private float getDeviationNumConsecutiveDays() {
        float deviation = 0;

        // calculate for each employee
        for (Employee employee: period.getEmployees()) {
            Attribute minNumConsecutiveWorkDays = employee.getContract().getMinNumAssignments();
            Attribute maxNumConsecutiveWorkDays = employee.getContract().getMaxNumAssignments();
            Attribute minNumConsecutiveFreeDays = employee.getContract().getMinConsecutiveFreeDays();
            Attribute maxNumConsecutiveFreeDays = employee.getContract().getMaxConsecutiveFreeDays();

            int weightMinWork = minNumConsecutiveWorkDays.getWeight();
            int valueMinWork = minNumConsecutiveWorkDays.getValueInt();
            int weightMaxWork = maxNumConsecutiveWorkDays.getWeight();
            int valueMaxWork = maxNumConsecutiveWorkDays.getValueInt();
            int weightMinFree = minNumConsecutiveFreeDays.getWeight();
            int valueMinFree = minNumConsecutiveFreeDays.getValueInt();
            int weightMaxFree = maxNumConsecutiveFreeDays.getWeight();
            int valueMaxFree = maxNumConsecutiveFreeDays.getValueInt();

            int numConsecutiveWorkDaysMin = individual.getNumConsecutiveDays(employee, false, false);
            int numConsecutiveWorkDaysMax = individual.getNumConsecutiveDays(employee, true, false);
            int numConsecutiveFreeDaysMin = individual.getNumConsecutiveDays(employee, false, true);
            int numConsecutiveFreeDaysMax = individual.getNumConsecutiveDays(employee, true, true);

            // if soft-constraints are unsatisfied, add deviation
            if (numConsecutiveWorkDaysMin < valueMinWork) {
                deviation += (valueMinWork - numConsecutiveWorkDaysMin) * weightMinWork;
            }
            if (numConsecutiveWorkDaysMax > valueMinWork) {
                deviation += (numConsecutiveWorkDaysMax - valueMaxWork) * weightMaxWork;
            }
            if (numConsecutiveFreeDaysMin < valueMinFree) {
                deviation += (valueMinFree - numConsecutiveFreeDaysMin) * weightMinFree;
            }
            if (numConsecutiveFreeDaysMax > valueMinFree) {
                deviation += (numConsecutiveFreeDaysMax - valueMaxFree) * weightMaxFree;
            }
        }

        return deviation;
    }
}
