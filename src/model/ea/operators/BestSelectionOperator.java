package model.ea.operators;

import helper.ConfigurationHelper;
import model.ea.Population;

@SuppressWarnings("unused")
public class BestSelectionOperator implements IEnvironmentSelectionOperator {
	 private int numberOfSelections = ConfigurationHelper.getInstance().getPropertyInteger("IndividualsPerPopulation", 10);
	 
	/**
	 * Takes the population after Recombination and Mutation selects
	 * the number of needed best individuals from parents and children.
	 * @param population Population instance
	 */
	@Override
	public void select(Population population) {
		population.sortByFitness();
		int poolSize = population.getPool().size();
		for (int i = poolSize-1; i >= numberOfSelections; i--) {
			population.getPool().remove(i);
		}
	}
}
