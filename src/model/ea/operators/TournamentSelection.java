/**
 * 
 */
package model.ea.operators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import helper.ConfigurationHelper;
import model.ea.Individual;
import model.ea.Population;

/**
 * @author nicolasmaeke
 *
 */
public class TournamentSelection implements IMatingSelectionOperator {
	private int numberOfParents = ConfigurationHelper.getInstance().getPropertyInteger("NumberOfParents", 6);
	private int numberOfDirectDuels = ConfigurationHelper.getInstance().getPropertyInteger("NumberOfDirectDuels", 3);
	
	@Override
	public List<Individual> select(Population population) {
		List<Individual> selection = new ArrayList<Individual>();
		for (int i = 0; i < numberOfParents; i++) {
			int index = new Random().nextInt(population.getPool().size());
			for (int j = 2; j < numberOfDirectDuels ; j++) {
				int u = new Random().nextInt(population.getPool().size());
				if (population.getPool().get(u).getFitness() > population.getPool().get(index).getFitness()){
					index = u;
				}
			selection.add(population.getPool().get(index));
			}
		}
		return selection;
	}

}
