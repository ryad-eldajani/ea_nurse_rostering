package model.ea.operators;

import model.ea.Population;

/**
 * Defines the general interface for an environmental selection operator.
 */
public interface IEnvironmentSelectionOperator {
    /**
     * Returns an environmental selection for a population.
     * @param population Old generation Population instance
     */
    void select(Population population);
}
