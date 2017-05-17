package model.ea.operators;

import model.ea.Individual;

import java.util.List;

/**
 * Implements a simple mutation operator.
 */
public class SimpleMutationOperator implements IMutationOperator {
    @Override
    public List<Individual> mutate(List<Individual> individuals) {
        for (Individual individual: individuals) {
            // TODO: Implement mutation
            Object nothing = null;
        }
		return null;
    }
}
