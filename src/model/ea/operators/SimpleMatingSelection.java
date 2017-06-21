package model.ea.operators;

import model.ea.Population;

/**
 * Implements a simple mating selection operator.
 */
@SuppressWarnings("unused")
public class SimpleMatingSelection implements IMatingSelection {
    /**
     * Selects the first 50% of the population.
     * @param population The parent population.
     * @return List of individuals for the new generation.
     */
    @Override
    public Population select(Population population) {
        Population selection = new Population();

        for (int i = 0; i < population.sortByFitness().getPool().size() * 0.5; i++) {
            selection.addIndividualToPool(population.getPool().get(i));
        }

        return selection;
    }
}
