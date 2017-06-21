package model.ea.operators;

import java.util.ArrayList;
import java.util.List;

import helper.ArrayHelper;
import helper.ConfigurationHelper;
import helper.RandomHelper;
import model.ea.Population;

@SuppressWarnings("unused")
public class TournamentSelectionOperator implements IMatingSelectionOperator {
	private int numberOfParents = ConfigurationHelper.getInstance().getPropertyInteger("NumberOfParents", 6);
	private int numberOfDirectDuels = ConfigurationHelper.getInstance().getPropertyInteger("NumberOfDirectDuels", 3);
	
	@Override
	public Population select(Population population) {
		Population selection = new Population();
		List<Integer> usedIndices = new ArrayList<Integer>();

		for (int i = 0; i < numberOfParents; i++) {
		    int index;
		    do {
                index = RandomHelper.getInstance().getInt(population.getPool().size());
            } while (usedIndices.contains(index));
		    usedIndices.add(index);

			for (int j = 2; j < numberOfDirectDuels ; j++) {
			    int u;
			    do {
                    u = RandomHelper.getInstance().getInt(population.getPool().size());
                } while (usedIndices.contains(u));

			    if (population.getPool().get(u).getFitness() < population.getPool().get(index).getFitness()) {
                    ArrayHelper.getInstance().removeValue(usedIndices, index);
                    index = u;
                    usedIndices.add(index);
				}

				selection.addIndividualToPool(population.getPool().get(index));
			}
		}

		return selection;
	}
}
