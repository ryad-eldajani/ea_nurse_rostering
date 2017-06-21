package model.ea.operators;

import java.util.Random;

import helper.ConfigurationHelper;
import model.ea.Population;

public class StochasticMatingSelection implements IMatingSelection {
	private int numberOfParents = ConfigurationHelper.getInstance().getPropertyInteger("NumberOfParents", 6);
	
	/**
     * @param population: the initial population of the generation
     * @return selection: a list of the selected individuals to become parents
     * Algorithm matches the Stochastic Universal Sampling
     */
	@Override
	public Population select(Population population) {
		
		int populationSize = population.getPool().size();
		float[] cumulatedFitness = new float[populationSize];
		cumulatedFitness[0] = population.getPool().get(0).getFitness();

		// We have to calculated the fitness of the individual before the loop, otherwise we get an
		// ArrayOutOfBounds exception. Therefore we begin with i = 1.
		for (int i = 1; i < populationSize ; i++) {
			cumulatedFitness[i] = cumulatedFitness[i-1] + population.getPool().get(i).getFitness();
		}

		int u = new Random().nextInt((int) (cumulatedFitness[populationSize-1] + 1)/numberOfParents);
		int j = 0;

		Population selection = new Population();
		for (int i = 0; i < numberOfParents; i++) {
			while (cumulatedFitness[j] < u){
				j ++;
			}
			u = (int) (u + (cumulatedFitness[populationSize-1]/numberOfParents));

			selection.addIndividualToPool(population.getPool().get(j));
		}

		return selection;
	}
}
