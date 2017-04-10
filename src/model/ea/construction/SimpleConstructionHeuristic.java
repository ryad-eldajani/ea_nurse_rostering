package model.ea.construction;

import helper.DateTimeHelper;
import model.ea.Individual;
import model.schedule.DayRoster;
import model.schedule.Employee;
import model.schedule.SchedulingPeriod;
import model.schedule.ShiftType;

import java.util.*;

/**
 * Concrete construction heuristic by assigning first available nurses.
 */
public class SimpleConstructionHeuristic implements IConstructionHeuristic {
    @Override
    public Individual getIndividual(SchedulingPeriod period) {
        Individual individual = new Individual();

        // iterate over every day
        for (int dayNumber = 0; dayNumber < DateTimeHelper.getInstance().getNumberOfDays(period); dayNumber++) {
            Date currentDate = DateTimeHelper.getInstance().getDateByNumber(period, dayNumber);
            DayRoster dayRoster = new DayRoster();
            dayRoster.setDate(currentDate);
            for (ShiftType shiftType: period.getCoversByDate(currentDate).keySet()) {
                for (Employee employee: period.getEmployees()) {
                    // only assign, if not planned already
                    if (dayRoster.isEmployeePlanned(employee)) {
                        continue;
                    }

                    if (employee.hasRequiredSkillsForShiftType(shiftType)) {
                        // the employee has the required skills for this shift type, add to roster
                        dayRoster.addToDayRoster(shiftType, employee);
                        break;
                    }
                }
            }

            individual.addDayRoster(dayRoster);
        }

        return individual;
    }
}
