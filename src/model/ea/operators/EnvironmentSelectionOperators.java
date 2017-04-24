/**
 * 
 */
package model.ea.operators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.ea.Individual;
import model.ea.Population;

/**
 * @author nicolasmaeke
 *
 */
public class EnvironmentSelectionOperators {
/**
 * 
 * @param numberOfDirectTournaments: the number of randomly chosen equaly distributed enemies per individual
 * @param currentPopulation: the population after mutation and recombination
 * @param numberOfSelections: how many individuals have to be selected for the next generation
 * @return
 */
	public Population qStepwiseTournamentSelection(int numberOfDirectDuels, Population currentPopulation, int numberOfSelections) {
		
		Population newPopulation = new Population(); // the new Population for the next generation
		List<Individual> individuals = new ArrayList<Individual>(); // the selected individuals are written in this list
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
		
		for (int j = 0; j < numberOfSelections; j++) { // the number of needed individuals for the next generation are taken from the current population
			individuals.add(currentPopulation.getPool().get(getIndexOfMax(scores))); // the individual with the highest score are selected		
			}
		}
		newPopulation.addIndividualsToPool(individuals); 
		return newPopulation;
	}
	
	
	/**
	 * helping method for finding the array index with the largest value 
	 * @param scores
	 * @return
	 */	
	private int getIndexOfMax(int scores[]) {
	    int max = scores[0];
	    int index = 0;
	    for(int i=1; i < scores.length; i++) {
	        if (max < scores[i]) {
	            index = i;
	            max = scores[i];
	        }
	    }
	    return index;
	}
}
