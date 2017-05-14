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
public class StochasticUniversalSampling implements IMatingSelectionOperator {
	private int numberOfParents = ConfigurationHelper.getInstance().getPropertyInteger("NumberOfParents", 6);
	
	@Override
	public List<Individual> select(Population population) {
		
		int populationSize = population.getPool().size();
		float[] cumulatedFitness = new float[populationSize];
		cumulatedFitness[0] = population.getPool().get(0).getFitness(); 
		// Die Fitness vom ersten Individuum muss vor der Schleife berechnet werden, da
		// sonst in der Schleife eine ArrayOutOfBounds Exception auftritt. Diese beginnt daher erst bei i = 1.
		for (int i = 1; i < populationSize ; i++) {
			cumulatedFitness[i] = cumulatedFitness[i-1] + population.getPool().get(i).getFitness(); 
		}
		int u = new Random().nextInt((int) (cumulatedFitness[populationSize-1] + 1)/numberOfParents);
		int j = 0;
		List<Individual> selection = new ArrayList<Individual>();
		for (int i = 0; i < numberOfParents; i++) {
			while (cumulatedFitness[j] < u){
				j ++;
			}
			u = (int) (u + (cumulatedFitness[populationSize-1]/numberOfParents));
			selection.add(population.getPool().get(j));
		}
		return selection;
	}

}
