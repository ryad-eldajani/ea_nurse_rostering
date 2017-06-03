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
 * and swaps one randomly selected day roster
 * @return a List of new created children
 */
public class SingleDayRecombination implements IRecombinationOperator {

	@Override
	public List<Individual> recombine(List<Individual> parents) {
		
		List<Individual> children = new ArrayList<Individual>();
		
		int numberOfDays = parents.get(0).getDayRosters().size();
		
		Individual parent1 = parents.get(0);
		Individual parent2 = parents.get(1);
		
		int r = new Random().nextInt(numberOfDays - 1); // select a random number between zero and the number of days in the schedule
		
		DayRoster drParent1 = parent1.getDayRosters().get(r); // save the day rosters of day r of both parents
		DayRoster drParent2 = parent2.getDayRosters().get(r);
		
		Individual child1 = parents.get(0); // create a new individual with the properties of parent1
	    child1.getDayRosters().set(r, drParent2); // replace one property of this child with a property of parent2
	    children.add(child1); // add the new created individual to the population
	    
	    Individual child2 = parents.get(1);
	    child2.getDayRosters().set(r, drParent1);
	    children.add(child2);
		
		return children;
	}

}
