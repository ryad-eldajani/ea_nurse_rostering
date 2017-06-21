package model.ea.operators;

import java.util.ArrayList;

import helper.ConfigurationHelper;
import model.ea.Population;

public class GoodBadMatingSelection implements IMatingSelection {
	private int numberOfParents = ConfigurationHelper.getInstance().getPropertyInteger("NumberOfParents", 6);
	
	/**
	 * For the first half of needed parents take the best individuals and for the second half take the worst.
     * @param population: the initial population of the generation
     * @return selection: a list of the selected individuals to become parents
     */
	@Override
	public Population select(Population population) {
		population.sortByFitness();
		int poolSize = population.getPool().size()-1;
		Population selection = new Population();
		for (int i = 0; i < numberOfParents/2; i++) {
			selection.addIndividualToPool(population.getPool().get(i));
			selection.addIndividualToPool(population.getPool().get(poolSize-i));
		}
		return selection;
	}

}
