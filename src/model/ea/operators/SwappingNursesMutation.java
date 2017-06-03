/**
 * 
 */
package model.ea.operators;

import java.util.List;
import java.util.Random;

import model.ea.Individual;
import model.schedule.Employee;
import model.schedule.ShiftType;

/**
 * for every individual of the selection swap the first nurse of shift x on a random day
 * with the first nurse of shift x on another random day
 *
 */
public class SwappingNursesMutation implements IMutationOperator {

	@Override
	public List<Individual> mutate(List<Individual> selection) {
		
		int numberOfDays = selection.get(0).getDayRosters().size();
		int randDay1 = new Random().nextInt(numberOfDays - 1); // random number between zero and the number of days in a schedule
		int randDay2 = new Random().nextInt(numberOfDays - 1);
		
		for (int i = 0; i < selection.size(); i++) {
			
			int numberOfShifts = selection.get(i).getDayRosters().get(randDay1).getDayRoster().size();
			int randShift = new Random().nextInt(numberOfShifts - 1); // random number between zero and the number of shifts in a day roster
			 
			// the nurse of a random shift on a random day
			Employee nurse1 = selection.get(i).getDayRosters().get(randDay1).getDayRoster().get(randShift).get(0);
			// the nurse of the same shift on another random day
			Employee nurse2 = selection.get(i).getDayRosters().get(randDay2).getDayRoster().get(randShift).get(0);
			ShiftType st = selection.get(i).getDayRosters().get(randDay1).getShiftTypeForEmployee(nurse1);
			
			// swap the nurses
			selection.get(i).getDayRosters().get(randDay1).getDayRoster().get(randShift).replace(st, nurse2);
			selection.get(i).getDayRosters().get(randDay2).getDayRoster().get(randShift).replace(st, nurse1);
			
			// swap back if solution isn't feasible anymore
			if(selection.get(i).isFeasible() != true){
				selection.get(i).getDayRosters().get(randDay1).getDayRoster().get(randShift).replace(st, nurse1);
				selection.get(i).getDayRosters().get(randDay2).getDayRoster().get(randShift).replace(st, nurse2);
			}
		}
		return selection;
	}

}
