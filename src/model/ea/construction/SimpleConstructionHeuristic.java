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
@SuppressWarnings("unused")
public class SimpleConstructionHeuristic implements IConstructionHeuristic {
    @Override
    public Individual getIndividual(SchedulingPeriod period) {
        Individual individual = new Individual();
        individual.setSchedulingPeriod(period);

        // iterate over every day
        for (int dayNumber = 0; dayNumber < DateTimeHelper.getInstance().getNumberOfDays(period); dayNumber++) {
            Date currentDate = DateTimeHelper.getInstance().getDateByNumber(period, dayNumber);
            DayRoster dayRoster = new DayRoster();
            dayRoster.setDate(currentDate);
            int employeeId = 0;

            for (Map.Entry<ShiftType, Integer> cover : period.getCoversByDate(currentDate).entrySet()) {
                // try to find employees which have required skills and are unique per day
                ShiftType shiftType = cover.getKey();
                Integer preferredEmployeeCount = cover.getValue();
                for (int employeeNumber = 0; employeeNumber < preferredEmployeeCount; employeeNumber++) {
                    Employee employee;
                    do {
                        employee = period.getEmployeeById(employeeId++);
                    } while (!employee.hasRequiredSkillsForShiftType(shiftType)
                            || dayRoster.isEmployeePlanned(employee));

                    // the random chosen employee fits the criteria, add to day roster
                    dayRoster.addToDayRoster(shiftType, employee);
                }
            }

            individual.addDayRoster(dayRoster);
        }

        return individual;
    }
}
