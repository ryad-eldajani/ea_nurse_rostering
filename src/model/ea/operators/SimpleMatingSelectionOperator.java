package model.ea.operators;

import model.ea.Individual;
import model.ea.Population;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements a simple mating selection operator.
 */
public class SimpleMatingSelectionOperator implements IMatingSelectionOperator {
    /**
     * Selects the first 50% of the population.
     * @param population The parent population.
     * @return List of individuals for the new generation.
     */
    @Override
    public List<Individual> select(Population population) {
        List<Individual> selection = new ArrayList<Individual>();

        for (int i = 0; i < population.getPool().size() * 0.5; i++) {
            selection.add(population.getPool().get(i));
        }

        return selection;
    }
}
