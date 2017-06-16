package model.ea.operators;

import model.ea.Population;

import java.util.List;

/**
 * Defines the general interface for a recombination operator.
 */
public interface IRecombinationOperator {
    /**
     * Recombines individuals.
     */
    Population recombine(Population parents);
}
