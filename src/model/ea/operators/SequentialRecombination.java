/**
 * 
 */
package model.ea.operators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.ea.Individual;
import model.schedule.DayRoster;

/**
 * takes the first two individuals from the list of parents
 * and creates children with a half of each parent
 * @return a List of new created children
 */
public class SequentialRecombination implements IRecombinationOperator {

	@Override
	public List<Individual> recombine(List<Individual> parents) {
		
        List<Individual> children = new ArrayList<Individual>();
		
		int numberOfDays = parents.get(0).getDayRosters().size();
		int half = numberOfDays/2;
		
		Individual parent1 = parents.get(0);
		Individual parent2 = parents.get(1);
		
		Individual child1 = new Individual();
		child1 = parent1.copy(parent1);
		for (int i = half; i < numberOfDays; i++) {
			child1.getDayRosters().set(i, parent2.getDayRosters().get(i)); 
		}
	    children.add(child1); 
	    
	    Individual child2 = new Individual();
	    child2 = parent2.copy(parent2);
	    for (int i = half; i < numberOfDays; i++) {
			child1.getDayRosters().set(i, parent1.getDayRosters().get(i)); 
		}
	    children.add(child2);
		
		return children;
	}

}
