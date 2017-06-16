package model.ea.operators;

import java.util.List;
import java.util.Map;

import helper.RandomHelper;
import model.ea.Individual;
import model.ea.Population;
import model.schedule.Employee;
import model.schedule.ShiftType;

/**
 * For every individual of the selection swap the first nurse of shift x on a random day
 * with the first nurse of shift x on another random day.
 */
@SuppressWarnings("unused")
public class SwappingNursesMutation implements IMutationOperator {
	@Override
	public Population mutate(Population selection) {
		int numberOfDays = selection.getPool().get(0).getDayRosters().size();

		// random number between zero and the number of days in a schedule
		int randDay1 = RandomHelper.getInstance().getInt(numberOfDays - 1);
		int randDay2 = RandomHelper.getInstance().getInt(numberOfDays - 1);

		for (Individual individual : selection.getPool()) {
            List<Map<ShiftType, Employee>> dayRoster1 = individual.getDayRosters().get(randDay1).getDayRoster();
            List<Map<ShiftType, Employee>> dayRoster2 = individual.getDayRosters().get(randDay2).getDayRoster();

            // get number of (maximum) shifts
            int numberOfShifts = dayRoster1.size() <= dayRoster2.size()
                    ? dayRoster1.size() : dayRoster2.size();

			// random number between zero and the number of shifts in a day roster
			int randShift = RandomHelper.getInstance().getInt(numberOfShifts - 1);

            // the nurse of a random shift on a random day
            Employee nurse1 = dayRoster1.get(randShift).entrySet().iterator().next().getValue();
            // the nurse of the same shift on another random day
            Employee nurse2 = dayRoster2.get(randShift).entrySet().iterator().next().getValue();

            ShiftType st = individual.getDayRosters().get(randDay1).getShiftTypeForEmployee(nurse1);

            // swap the nurses
            dayRoster1.get(randShift).replace(st, nurse2);
            dayRoster2.get(randShift).replace(st, nurse1);

            // swap back if solution isn't feasible anymore
            if (!individual.isFeasible()) {
                dayRoster1.get(randShift).replace(st, nurse1);
                dayRoster2.get(randShift).replace(st, nurse2);
            }
		}

		return selection;
	}
}
