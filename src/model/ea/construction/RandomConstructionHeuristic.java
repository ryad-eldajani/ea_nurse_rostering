package model.ea.construction;

import helper.DateTimeHelper;
import helper.RandomHelper;
import model.ea.Individual;
import model.schedule.DayRoster;
import model.schedule.Employee;
import model.schedule.SchedulingPeriod;
import model.schedule.ShiftType;
import java.util.Date;
import java.util.Map;

/**
 * Concrete construction heuristic by assigning nurses randomly.
 */
public class RandomConstructionHeuristic implements IConstructionHeuristic {
    @Override
    public Individual getIndividual(SchedulingPeriod period) {
        Individual individual = new Individual();
        individual.setSchedulingPeriod(period);
        int numberOfEmployees = period.getEmployees().size();

        // iterate over every day
        for (int dayNumber = 0; dayNumber < DateTimeHelper.getInstance().getNumberOfDays(period); dayNumber++) {
            Date currentDate = DateTimeHelper.getInstance().getDateByNumber(period, dayNumber);
            DayRoster dayRoster = new DayRoster();
            dayRoster.setDate(currentDate);
            for (Map.Entry<ShiftType, Integer> cover : period.getCoversByDate(currentDate).entrySet()) {
                // try to find random employees which have required skills and are unique per day
                ShiftType shiftType = cover.getKey();
                Integer preferredEmployeeCount = cover.getValue();
                for (int employeeNumber = 0; employeeNumber < preferredEmployeeCount; employeeNumber++) {
                    Employee employee;
                    do {
                        int randomEmployeeId = RandomHelper.getInstance().getInt(numberOfEmployees);
                        employee = period.getEmployeeById(randomEmployeeId);
                    } while (!employee.hasRequiredSkillsForShiftType(shiftType)
                            || dayRoster.isEmployeePlanned(employee));

                    // the random chosen employee fits the criteria, add to day roster
                    dayRoster.addToDayRoster(shiftType, employee);
                }
            }
            // finally add the generated day roster to the individual
            individual.addDayRoster(dayRoster);
        }

        return individual;
    }
}
