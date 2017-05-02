package model.ea.constraints;

import model.ea.Individual;
import model.schedule.Attribute;
import model.schedule.Employee;
import model.schedule.SchedulingPeriod;

/**
 * Implements the default fitness calculator which uses algorithms for each
 * soft constraint to calculate the overall fitness.
 */
@SuppressWarnings("unused")
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
        fitness += getDeviationWorkingWeekends();

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
            Attribute minNumConsecutiveWorkDays = employee.getContract().getMinNumAssignments(),
                    maxNumConsecutiveWorkDays = employee.getContract().getMaxNumAssignments(),
                    minNumConsecutiveFreeDays = employee.getContract().getMinConsecutiveFreeDays(),
                    maxNumConsecutiveFreeDays = employee.getContract().getMaxConsecutiveFreeDays();

            int weightMinWork = minNumConsecutiveWorkDays.getWeight(),
                    weightMaxWork = maxNumConsecutiveWorkDays.getWeight(),
                    weightMinFree = minNumConsecutiveFreeDays.getWeight(),
                    weightMaxFree = maxNumConsecutiveFreeDays.getWeight();

            int valueMinWork = minNumConsecutiveWorkDays.getValueInt(),
                    valueMaxWork = maxNumConsecutiveWorkDays.getValueInt(),
                    valueMinFree = minNumConsecutiveFreeDays.getValueInt(),
                    valueMaxFree = maxNumConsecutiveFreeDays.getValueInt();

            int numMinConsecutiveWork = individual.getNumMinConsecutiveWork(employee),
                    numMaxConsecutiveWork = individual.getNumMaxConsecutiveWork(employee),
                    numMinConsecutiveFree = individual.getNumMinConsecutiveFree(employee),
                    numMaxConsecutiveFree = individual.getNumMaxConsecutiveFree(employee);

            // if soft-constraints are unsatisfied, add deviation
            if (numMinConsecutiveWork < valueMinWork) {
                deviation += (valueMinWork - numMinConsecutiveWork) * weightMinWork;
            }
            if (numMaxConsecutiveWork > valueMinWork) {
                deviation += (numMaxConsecutiveWork - valueMaxWork) * weightMaxWork;
            }
            if (numMinConsecutiveFree < valueMinFree) {
                deviation += (valueMinFree - numMinConsecutiveFree) * weightMinFree;
            }
            if (numMaxConsecutiveFree > valueMinFree) {
                deviation += (numMaxConsecutiveFree - valueMaxFree) * weightMaxFree;
            }
        }

        return deviation;
    }

    /**
     * Calculates the deviations for (consecutive) working weekends for each employee.
     * @return Deviation
     */
    private float getDeviationWorkingWeekends() {
        float deviation = 0;

        // calculate for each employee
        for (Employee employee: period.getEmployees()) {
            Attribute totalNumWorkWeekends = employee.getContract().getMaxWorkingWeekendsInFourWeeks(),
                    minNumConsecutiveWorkWeekends = employee.getContract().getMinConsecutiveWorkingWeekends(),
                    maxNumConsecutiveWorkWeekends = employee.getContract().getMaxConsecutiveWorkingWeekends(),
                    completeWeekends = employee.getContract().getCompleteWeekends(),
                    identicalShiftTypes = employee.getContract().getIdenticalShiftTypesDuringWeekend(),
                    noNightShiftsBeforeWeekends = employee.getContract().getNoNightShiftBeforeFreeWeekend();

            int weightTotalWork = totalNumWorkWeekends.getWeight(),
                    weightMinConsecutiveWork = minNumConsecutiveWorkWeekends.getWeight(),
                    weightMaxConsecutiveWork = maxNumConsecutiveWorkWeekends.getWeight(),
                    weightCompleteWeekends = completeWeekends.getWeight(),
                    weightIdenticalShiftTypes = identicalShiftTypes.getWeight(),
                    weightNoNightShiftsBeforeWeekends = noNightShiftsBeforeWeekends.getWeight();

            int valueMinTotal = totalNumWorkWeekends.getValueInt(),
                    valueMinConsecutiveWork = minNumConsecutiveWorkWeekends.getValueInt(),
                    valueMaxConsecutiveWork = maxNumConsecutiveWorkWeekends.getValueInt();

            boolean valueCompleteWeekends = completeWeekends.getValueBoolean(),
                    valueIdenticalShiftTypes = identicalShiftTypes.getValueBoolean(),
                    valueNoNightShiftsBeforeWeekends = noNightShiftsBeforeWeekends.getValueBoolean();

            int numTotalWeekendsWork = individual.getNumTotalWeekendsWork(employee),
                    numMinConsecutiveWeekendsWork = individual.getNumMinConsecutiveWeekendsWork(employee),
                    numMaxConsecutiveWeekendsWork = individual.getNumMaxConsecutiveWeekendsWork(employee);

            // if soft-constraints are unsatisfied, add deviation
            if (numTotalWeekendsWork < valueMinTotal) {
                deviation += (valueMinTotal - numTotalWeekendsWork) * weightTotalWork;
            }
            if (numTotalWeekendsWork > valueMinTotal) {
                deviation += (numTotalWeekendsWork - valueMinTotal) * weightTotalWork;
            }
            if (numMinConsecutiveWeekendsWork < valueMinConsecutiveWork) {
                deviation += (valueMinConsecutiveWork - numMinConsecutiveWeekendsWork) * weightMinConsecutiveWork;
            }
            if (numMaxConsecutiveWeekendsWork > valueMaxConsecutiveWork) {
                deviation += (numMaxConsecutiveWeekendsWork - valueMaxConsecutiveWork) * weightMaxConsecutiveWork;
            }
            if (valueCompleteWeekends != individual.isCompleteWeekends(employee)) {
                deviation += weightCompleteWeekends;
            }
            if (valueIdenticalShiftTypes != individual.isIdenticalShiftType(employee)) {
                deviation += weightIdenticalShiftTypes;
            }
            if (valueNoNightShiftsBeforeWeekends != individual.isNoNightShiftsBeforeFreeWeekend(employee)) {
                deviation += weightNoNightShiftsBeforeWeekends;
            }
        }

        return deviation;
    }
}
