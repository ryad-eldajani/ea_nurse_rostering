package model.ea.operators;

import helper.RandomHelper;
import model.ea.Individual;
import model.ea.Population;
import model.schedule.DayRoster;

/**
 * Takes the first two individuals from the list of parents
 * and swaps one randomly selected day roster.
 */
@SuppressWarnings("unused")
public class SingleDayRecombinationOperator implements IRecombinationOperator {

	@Override
	public Population recombine(Population parents) {
	    parents.sortByFitness();
		Population children = Population.copy(parents);
		
		int numberOfDays = parents.getPool().get(0).getDayRosters().size();
		
		Individual parent1 = parents.getPool().get(0);
		Individual parent2 = parents.getPool().get(1);

		// select a random number between zero and the number of days in the schedule
		int r = RandomHelper.getInstance().getInt(numberOfDays - 1);
		
		DayRoster drParent1 = parent1.getDayRosters().get(r); // save the day rosters of day r of both parents
		DayRoster drParent2 = parent2.getDayRosters().get(r);
		
		Individual child1 = parents.getPool().get(0); 		// create a new individual with the properties of parent1
	    child1.getDayRosters().set(r, drParent2); 	// replace one property of this child with a property of parent2
	    children.addIndividualToPool(child1); 						// add the new created individual to the population
	    
	    Individual child2 = parents.getPool().get(1);
	    child2.getDayRosters().set(r, drParent1);
	    children.addIndividualToPool(child2);
		
		return children;
	}

}
