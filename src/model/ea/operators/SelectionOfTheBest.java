/**
 * 
 */
package model.ea.operators;

import java.util.ArrayList;
import java.util.List;

import helper.ConfigurationHelper;
import model.ea.Individual;
import model.ea.Population;

/**
 * @author nicolasmaeke
 *
 */
public class SelectionOfTheBest implements IEnvironmentSelectionOperator {
	 private int numberOfSelections = ConfigurationHelper.getInstance().getPropertyInteger("IndividualsPerPopulation", 10);
	 
	/** 
	 *@param takes the population after Recombination and Mutation
	 *selects the number of needed best individuals from parents and children
	 *@return thereby the new Generation of the population is created
	 */
	@Override
	public void select(Population population) {
		List <Individual> newGeneration = new ArrayList<Individual>();
		population.sortByFitness();
		for (int i = 0; i < numberOfSelections; i++) {
			newGeneration.add(population.getPool().get(i));
		}
		population.getPool().clear();
		population.getPool().addAll(newGeneration);
	}
}
