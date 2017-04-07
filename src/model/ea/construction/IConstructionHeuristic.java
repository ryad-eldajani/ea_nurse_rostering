package model.ea.construction;

import model.ea.Individual;
import model.schedule.SchedulingPeriod;

/**
 * Defines the general interface for construction heuristic generating a sub-optimal,
 * but feasible solution for the nurse rostering problem.
 */
public interface IConstructionHeuristic {
    /**
     * Returns an individual (i.e. a feasible solution) for a concrete scheduling period.
     * @return Individual instance
     */
    Individual getIndividual(SchedulingPeriod period);
}
