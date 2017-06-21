package model.ea.operators;

import model.ea.Population;

/**
 * Defines the general interface for a recombination operator.
 */
public interface IRecombination {
    /**
     * Recombines individuals.
     */
    Population recombine(Population parents);
}
