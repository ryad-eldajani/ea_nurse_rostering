package model.ea.operators;

import java.util.Random;

import helper.ConfigurationHelper;
import model.ea.Population;

@SuppressWarnings("unused")
public class FitnessProportionalMatingSelection implements IMatingSelection {
	private int numberOfParents = ConfigurationHelper.getInstance().getPropertyInteger("NumberOfParents", 6);

	@Override
	public Population select(Population population) {
		int populationSize = population.getPool().size();
		float[] cumulatedFitness = new float[populationSize];
		cumulatedFitness[0] = population.getPool().get(0).getFitness(); 
		// Die Fitness vom ersten Individuum muss vor der Schleife berechnet werden, da
		// sonst in der Schleife eine ArrayOutOfBounds Exception auftritt. Diese beginnt daher erst bei i = 1.
		for (int i = 1; i < populationSize ; i++) {
			cumulatedFitness[i] = cumulatedFitness[i-1] + population.getPool().get(i).getFitness(); 
		}

        Population selection = new Population();
		for (int i = 0; i < numberOfParents; i++) {
			int j = 0;
			int u = new Random().nextInt((int) cumulatedFitness[populationSize-1] + 1);
			while (cumulatedFitness[j] < u) {
				j ++;
			    selection.addIndividualToPool(population.getPool().get(j));
			}
		}

		return selection;
	}
}
