package model.ea.operators;

import model.ea.Individual;
import model.ea.Population;

import java.util.List;

/**
 * Defines the general interface for an environmental selection operator.
 */
public interface IEnvironmentSelectionOperator {
    /**
     * Returns an environmental selection for a population.
     */
    void select(Population population);
}
