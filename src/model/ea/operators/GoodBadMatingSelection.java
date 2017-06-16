package model.ea.operators;

import java.util.ArrayList;

import helper.ConfigurationHelper;
import model.ea.Population;

public class GoodBadMatingSelection implements IMatingSelectionOperator {
	private int numberOfParents = ConfigurationHelper.getInstance().getPropertyInteger("NumberOfParents", 6);
	
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
