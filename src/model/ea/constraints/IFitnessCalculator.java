package model.ea.constraints;

import model.ea.Individual;
import model.schedule.SchedulingPeriod;

/**
 * Defines the general interface for fitness calculation.
 */
public interface IFitnessCalculator {
    /**
     * Calculates the fitness for an individual.
     * @param individual Individual instance
     * @param period SchedulingPeriod instance
     * @return Fitness
     */
    float calculate(Individual individual, SchedulingPeriod period);
}
