package model.ea.operators;

import model.ea.Individual;

import java.util.List;

/**
 * Defines the general interface for a mutation operator.
 */
public interface IMutationOperator {
    /**
     * Mutates individuals.
     */
    void mutate(List<Individual> individuals);
}
