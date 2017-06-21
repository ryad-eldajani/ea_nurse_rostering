package model.ea.operators;

import model.ea.Individual;
import model.ea.Population;

/**
 * takes the first two individuals from the list of parents
 * and creates children with a half of each parent
 */
@SuppressWarnings("unused")
public class SequentialRecombination implements IRecombination {
	@Override
	public Population recombine(Population parents) {
		Population children = new Population();
		
		int numberOfDays = parents.getPool().get(0).getDayRosters().size();
		int half = numberOfDays/2;

		for (int j = 0; j < parents.getPool().size()/2; j++) {
			Individual parent1 = parents.getPool().get(j);
			Individual parent2 = parents.getPool().get(parents.getPool().size()-1-j);

			Individual child1 = Individual.copy(parent1);
			for (int i = half; i < numberOfDays; i++) {
				child1.getDayRosters().set(i, parent2.getDayRosters().get(i));
			}
			children.addIndividualToPool(child1);

			Individual child2 = Individual.copy(parent2);
			for (int i = half; i < numberOfDays; i++) {
				child1.getDayRosters().set(i, parent1.getDayRosters().get(i));
			}
			children.addIndividualToPool(child2);
		}

		return children;
	}
}
