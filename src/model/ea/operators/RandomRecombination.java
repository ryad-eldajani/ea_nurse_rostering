package model.ea.operators;

import helper.RandomHelper;
import model.ea.Individual;
import model.ea.Population;
import model.schedule.DayRoster;

@SuppressWarnings("unused")
public class RandomRecombination implements IRecombination {
	
	/**
	 * Recombines days of two individuals to create a whole new individual.
	 * @param parents: the parents selected with the Mating Selection
	 * @return children: a new population instance containing the new created children
	 */
	@Override
	public Population recombine(Population parents) {
		Population children = new Population();

		int numIndividuals = parents.getPool().size();
		for (int i = 0; i < numIndividuals; i++) {
            int randomIndividual1;
            int randomIndividual2;
            do {
                randomIndividual1 = RandomHelper.getInstance().getInt(parents.getPool().size());
                randomIndividual2 = RandomHelper.getInstance().getInt(parents.getPool().size());
            } while (randomIndividual1 == randomIndividual2);

            Individual individual1 = parents.getPool().get(randomIndividual1);
            Individual individual2 = parents.getPool().get(randomIndividual2);

            Individual newIndividual = Individual.copy(individual1);
            newIndividual.resetRosters();

            for (int day = 0; day < individual1.getDayRosters().size(); day++) {
                DayRoster randomRoster = RandomHelper.getInstance().getBoolean()
                        ? individual1.getDayRosters().get(day)
                        : individual2.getDayRosters().get(day);

                newIndividual.addDayRoster(randomRoster);
            }

            // if solution is unfeasible, recombine again
            if (!newIndividual.isFeasible()) {
                return recombine(parents);
            }

            newIndividual.getFitness(true);

            children.addIndividualToPool(newIndividual);
        }

		return children;
	}
}
