package model.ea.operators;

import helper.RandomHelper;
import model.ea.Individual;
import model.ea.Population;
import model.schedule.DayRoster;

/**
 * Recombines days of two individuals to create a whole new individual.
 */
@SuppressWarnings("unused")
public class RandomRecombinationOperator implements IRecombinationOperator {
	@Override
	public Population recombine(Population parents) {
		Population children = new Population();

		int numIndividuals = parents.getPool().size();
		for (int i = 0; i < numIndividuals; i++) {
            int randomIndividual1;
            int randomIndividual2;
            do {
                randomIndividual1 = RandomHelper.getInstance().getInt(parents.getPool().size()-1);
                randomIndividual2 = RandomHelper.getInstance().getInt(parents.getPool().size()-1);
            } while (randomIndividual1 == randomIndividual2);

            Individual individual1 = parents.getPool().get(randomIndividual1);
            Individual individual2 = parents.getPool().get(randomIndividual2);

            Individual newIndividual = Individual.copy(individual1);
            newIndividual.resetRosters();

            for (int day = 0; day < individual1.getDayRosters().size(); day++) {
                DayRoster randomRoster = RandomHelper.getInstance().getInt(0, 1) == 0
                        ? individual1.getDayRosters().get(day)
                        : individual2.getDayRosters().get(day);

                newIndividual.addDayRoster(randomRoster);
            }
            newIndividual.isFeasible();
            newIndividual.getFitness(true);

            children.addIndividualToPool(newIndividual);
        }

		return children;
	}
}
