/**
 * 
 */
package model.ea.operators;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import model.ea.Individual;
import model.ea.Population;
import model.schedule.SchedulingPeriod;

/**
 * @author nicolasmaeke
 *
 */
public class qStepwiseTournamentSelection implements IEnvironmentSelectionOperator{
/**
 * 
 * @param numberOfDirectTournaments: the number of randomly chosen equally distributed enemies per individual
 * @param currentPopulation: the population after mutation and recombination
 * @param numberOfSelections: how many individuals have to be selected for the next generation
 * @return
 */
	@Override
	public void select (Population currentPopulation, Map<String, Object> optionalArguments) { // int numberOfDirectDuels, int numberOfSelections, SchedulingPeriod sp
		
		Population newPopulation = currentPopulation; // the new Population for the next generation
		List<Individual> toRemove = new ArrayList<Individual>(); // the non-selected individuals are written in this list
		int[] scores = new int[currentPopulation.getPool().size()]; // the number of wins per individual are written in this list
		
		for (int i = 0; i < currentPopulation.getPool().size(); i++ ){ // every individual of the population has to fight a tournament
			int wins = 0; 
			
			for (int j = 0; j < numberOfDirectDuels; j++) { // every tournament has the same number of direct Duels
				int enemy = j;
				
				while (enemy == j){ 
					enemy = new Random().nextInt(currentPopulation.getPool().size()) + 1; // the enemy is selected randomly from the population
				}
				
				if (currentPopulation.getPool().get(i).getFitness() > currentPopulation.getPool().get(enemy).getFitness()){ 
					wins++;	// the individual gets a win if its fitness is better than the enemies
				}
			scores[i] = wins; // all wins of the individual are summed up to its score
			}
		
		for (int j = 0; j < currentPopulation.getPool().size()-numberOfSelections; j++) { // the number of not needed individuals for the next generation are taken from the current population
			toRemove.add(currentPopulation.getPool().get(getIndexOfMin(scores))); // the individual with the lowest score are selected		
			}
		}
		newPopulation.getPool().removeAll(toRemove);
		return;
	}
	
	
	/**
	 * helping method for finding the array index with the lowest value 
	 * @param scores
	 * @return
	 */	
	private int getIndexOfMin(int scores[]) {
	    int min = scores[0];
	    int index = 0;
	    for(int i = 1; i < scores.length; i++) {
	        if (min > scores[i]) {
	            index = i;
	            min = scores[i];
	        }
	    }
	    return index;
	}


}
