package model.ea.operators;

import model.ea.Individual;
import model.ea.Population;

import java.util.List;

/**
 * Defines the general interface for a mating selection operator.
 */
public interface IMatingSelectionOperator {
    /**
     * Get a mating selection for a specific population.
     */
    List<Individual> select(Population population);
}
