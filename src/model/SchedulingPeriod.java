package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Scheduling period model.
 */
public class SchedulingPeriod {
    /**
     * Identifier.
     */
    private String id;

    /**
     * Path to source file.
     */
    private String sourceFile;

    /**
     * Start date of schedule period.
     */
    private Date startDate;

    /**
     * End date of schedule period.
     */
    private Date endDate;

    /**
     * List of available skills.
     */
    private List<Skill> skills = new ArrayList<Skill>();

    /**
     * List of required shift types.
     */
    private List<ShiftType> shiftTypes = new ArrayList<ShiftType>();

    /**
     * List of available contracts.
     */
    private List<Contract> contracts = new ArrayList<Contract>();

    /**
     * List of available employees.
     */
    private List<Employee> employees = new ArrayList<Employee>();

    /**
     * List of required coverings for each days.
     */
    private List<Cover> dayCovers = new ArrayList<Cover>();

    /**
     * Sets the source file path.
     * @param path Source file path.
     */
    public void setSourceFile(String path) {
        sourceFile = path;
    }

    /**
     * Returns the source file path.
     * @return Source file path.
     */
    public String getSourceFile() {
        return sourceFile;
    }

    /**
     * Returns a contract by its identifier.
     * @param id Contract identifier.
     * @return Contract instance or null.
     */
    public Contract getContractById(int id) {
        for (Contract contract: contracts) {
            if (contract.getId() == id) {
                return contract;
            }
        }

        return null;
    }

    /**
     * Returns a ShiftType instance by identifier.
     * @param id ShiftType identifier.
     * @return ShiftType instance or null.
     */
    public ShiftType getShiftTypeById(String id) {
        for (ShiftType shift: shiftTypes) {
            if (shift.getId().equals(id)) {
                return shift;
            }
        }

        return null;
    }

    /**
     * Returns an Employee instance by identifier.
     * @param id Employee identifier.
     * @return Employee instance or null.
     */
    public Employee getEmployeeById(String id) {
        for (Employee employee: employees) {
            if (employee.getId() == Integer.parseInt(id)) {
                return employee;
            }
        }

        return null;
    }

    /**
     * Adds an employee.
     * @param employee Employee instance.
     */
    public void addEmployee(Employee employee) {
        employees.add(employee);
    }

    /**
     * Adds a shift type.
     * @param shiftType ShiftType instance.
     */
    public void addShiftType(ShiftType shiftType) {
        shiftTypes.add(shiftType);
    }

    /***
     * Following getters and setters are trivial and self explanatory, therefore not documented further.
     ***/

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public List<ShiftType> getShiftTypes() {
        return shiftTypes;
    }

    public void setShiftTypes(List<ShiftType> shiftTypes) {
        this.shiftTypes = shiftTypes;
    }

    public List<Contract> getContracts() {
        return contracts;
    }

    public void setContracts(List<Contract> contracts) {
        this.contracts = contracts;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public List<Cover> getDayCovers() {
        return dayCovers;
    }

    public void setDayCovers(List<Cover> dayCovers) {
        this.dayCovers = dayCovers;
    }

    @Override
    public String toString() {
        String nl = System.getProperty("line.separator");
        StringBuilder out = new StringBuilder();
        out.append("Scheduling period information").append(nl)
                .append(" ID: ").append(id).append(nl)
                .append(" Start date: ").append(startDate).append(nl)
                .append(" End date: ").append(endDate).append(nl)
                .append(" Source file: ").append(sourceFile).append(nl);

        // add required skills, if any
        if (skills.size() > 0) {
            out.append(" Required skills: ").append(skills.toString()).append(nl);
        }

        // add shift types, if any
        if (shiftTypes.size() > 0) {
            out.append(" Shift types: ").append(nl);
            for (ShiftType shiftType: shiftTypes) {
                out.append(" - ").append(shiftType.toString()).append(nl);
            }
        }

        // add contracts, if any
        if (contracts.size() > 0) {
            out.append(" Contracts: ").append(nl);
            for (Contract contract: contracts) {
                out.append(" - ").append(contract.toString()).append(nl);
            }
        }

        // add employees, if any
        if (employees.size() > 0) {
            out.append(" Employees: ").append(nl);
            for (Employee employee: employees) {
                out.append(" - ").append(employee.toString()).append(nl);
            }
        }

        // add day covers, if any
        if (dayCovers.size() > 0) {
            out.append(" Day covers: ").append(nl);
            for (Cover dayCover: dayCovers) {
                out.append(" - ").append(dayCover.toString()).append(nl);
            }
        }

        return out.toString();
    }
}
