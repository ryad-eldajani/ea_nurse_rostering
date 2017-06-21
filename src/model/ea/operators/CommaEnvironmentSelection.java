package model.ea.operators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import helper.ConfigurationHelper;
import model.ea.Individual;
import model.ea.Population;

public class CommaEnvironmentSelection implements IEnvironmentSelection {
	 private int individualsPerPopulation = ConfigurationHelper.getInstance().getPropertyInteger("IndividualsPerPopulation", 10);
	 
	 /** 
	  *@param takes the population after Recombination and Mutation
	  *selects the number of needed best individuals from children only
	  *requires that the number of children is at least as big as individuals needed
	  *@return thereby the new Generation of the population is created
      */
	@Override
	public void select(Population population) {
		List<Individual> children = population.getPool().subList(individualsPerPopulation, population.getPool().size());
		List <Individual> newGeneration = new ArrayList<Individual>();
		Collections.sort(children, new Comparator<Individual>() {
            @Override
            public int compare(Individual individualA, Individual individualB) {
                return individualA.getFitness() < individualB.getFitness() ? -1
                        : individualA.getFitness() == individualB.getFitness() ? 0
                        : 1;
            }
        });
		for (int i = 0; i < individualsPerPopulation; i++) {
			if(children.isEmpty()){break;}
			newGeneration.add(children.get(i));
		}
		population.getPool().clear();
		population.getPool().addAll(newGeneration);
	}

}
