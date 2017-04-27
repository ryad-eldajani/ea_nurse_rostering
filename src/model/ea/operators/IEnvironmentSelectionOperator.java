package model.ea.operators;

import model.ea.Population;

import java.util.Map;


/**
 * Defines the general interface for an environmental selection operator.
 */
public interface IEnvironmentSelectionOperator {
    /**
     * Returns an environmental selection for a population.
     * @param population Old generation Population instance
     * @param optionalArguments optional Arguments
     */
    void select(Population population, Map<String, Object> optionalArguments);
}
