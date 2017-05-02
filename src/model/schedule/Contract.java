package model.schedule;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Model for a contract.
 */
@SuppressWarnings("unused")
public class Contract {
    /***
     * Following fields are self explanatory, therefore not documented further.
     ***/

    private int id;
    private String description;
    private Attribute singleAssignmentPerDay;
    private Attribute minNumAssignments;
    private Attribute maxNumAssignments;
    private Attribute minConsecutiveWorkingDays;
    private Attribute maxConsecutiveWorkingDays;
    private Attribute minConsecutiveFreeDays;
    private Attribute maxConsecutiveFreeDays;
    private Attribute minConsecutiveWorkingWeekends;
    private Attribute maxConsecutiveWorkingWeekends;
    private Attribute maxWorkingWeekendsInFourWeeks;
    private List<Day> weekendDefinition = new ArrayList<Day>();
    private Attribute completeWeekends;
    private Attribute identicalShiftTypesDuringWeekend;
    private Attribute noNightShiftBeforeFreeWeekend;
    private Attribute alternativeSkillCategory;
    private List<Integer> unwantedPatterns = new ArrayList<Integer>();

    /**
     * Returns a weighted attribute by name.
     * @param name Attribute name.
     * @return Attribute instance.
     */
    public Attribute getAttribute(String name)
            throws NoSuchFieldException, IllegalAccessException {
        String attributeName = Character.toLowerCase(name.charAt(0)) + name.substring(1);
        Field field = getClass().getDeclaredField(attributeName);
        return (Attribute) field.get(this);
    }

    /**
     * Sets an attribute by uppercase name.
     * @param attribute Attribute instance.
     */
    public void setAttribute(Attribute attribute)
            throws IllegalAccessException, NoSuchFieldException {
        Field field = getClass().getDeclaredField(attribute.getName());
        field.set(this, attribute);
    }

    /***
     * Following getters and setters are trivial and self explanatory, therefore not documented further.
     ***/

    public void setId(int id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUnwantedPatterns(List<Integer> unwantedPatterns) {
        this.unwantedPatterns = unwantedPatterns;
    }

    public void setWeekendDefinition(List<Day> weekendDefinition) {
        this.weekendDefinition = weekendDefinition;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Attribute getSingleAssignmentPerDay() {
        return singleAssignmentPerDay;
    }

    public Attribute getMinNumAssignments() {
        return minNumAssignments;
    }

    public Attribute getMaxNumAssignments() {
        return maxNumAssignments;
    }

    public Attribute getMinConsecutiveWorkingDays() {
        return minConsecutiveWorkingDays;
    }

    public Attribute getMaxConsecutiveWorkingDays() {
        return maxConsecutiveWorkingDays;
    }

    public Attribute getMinConsecutiveFreeDays() {
        return minConsecutiveFreeDays;
    }

    public Attribute getMaxConsecutiveFreeDays() {
        return maxConsecutiveFreeDays;
    }

    public Attribute getMinConsecutiveWorkingWeekends() {
        return minConsecutiveWorkingWeekends;
    }

    public Attribute getMaxConsecutiveWorkingWeekends() {
        return maxConsecutiveWorkingWeekends;
    }

    public Attribute getMaxWorkingWeekendsInFourWeeks() {
        return maxWorkingWeekendsInFourWeeks;
    }

    public List<Day> getWeekendDefinition() {
        return weekendDefinition;
    }

    public Attribute getCompleteWeekends() {
        return completeWeekends;
    }

    public Attribute getIdenticalShiftTypesDuringWeekend() {
        return identicalShiftTypesDuringWeekend;
    }

    public Attribute getNoNightShiftBeforeFreeWeekend() {
        return noNightShiftBeforeFreeWeekend;
    }

    public Attribute getAlternativeSkillCategory() {
        return alternativeSkillCategory;
    }

    public List<Integer> getUnwantedPatterns() {
        return unwantedPatterns;
    }

    @Override
    public String toString() {
        return "ID: " + String.valueOf(id) + ", "
                + "description: " + description + ", "
                + "weekends: " + weekendDefinition;
    }
}
