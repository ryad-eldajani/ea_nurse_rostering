package parser;

import helper.ArrayHelper;
import helper.ConfigurationHelper;
import helper.DateTimeHelper;
import model.schedule.*;
import model.schedule.Attribute;
import nu.xom.*;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to parse an XML scheduling instance.
 */
@SuppressWarnings("unused")
public class XmlParser implements IParser {
    /**
     * Parses Skills node and returns a List of Skill instances.
     * @param skills Skills node.
     * @return List of Skill instances.
     */
    private List<Skill> parseSkills(Element skills) {
        List<Skill> skillList = new ArrayList<Skill>();

        for (int i = 0; i < skills.getChildCount(); i++) {
            Node skillNode = skills.getChild(i);
            if (skillNode instanceof Element) {
                Element element = (Element) skillNode;
                if (element.getLocalName().equals("Skill") && element.getValue().equals("Nurse")) {
                    skillList.add(Skill.NURSE);
                } else if (element.getLocalName().equals("Skill") && element.getValue().equals("HeadNurse")) {
                    skillList.add(Skill.HEAD_NURSE);
                }
            }
        }

        return skillList;
    }

    /**
     * Parses CoverRequirements node.
     * @param coverRequirements Day covering requirements node.
     * @param period SchedulingPeriod instance.
     */
    private void parseDayCovers(Element coverRequirements, SchedulingPeriod period) throws ParseException, NoSuchFieldException, IllegalAccessException {
        List<Cover> coverList = new ArrayList<Cover>();

        for (int i = 0; i < coverRequirements.getChildCount(); i++) {
            Node requirementNode = coverRequirements.getChild(i);
            if (requirementNode instanceof Element && ((Element) requirementNode).getLocalName().equals("DayOfWeekCover")) {
                Element element = (Element) requirementNode;
                Cover dayCover = new Cover();
                for (int j = 0; j < requirementNode.getChildCount(); j++) {
                    if (element.getChild(j) instanceof Element) {
                        Element requirementInfo = (Element) element.getChild(j);
                        if (requirementInfo.getLocalName().equals("Day")) {
                            dayCover.setDay(DateTimeHelper.getInstance().getDayByName(requirementInfo.getValue()));
                        } else if (requirementInfo.getLocalName().equals("Cover")) {
                            int preferred = Integer.parseInt(
                                            requirementInfo.getChildElements("Preferred").get(0).getValue());
                            ShiftType shiftType = period.getShiftTypeById(
                                            requirementInfo.getChildElements("Shift").get(0).getValue());
                            dayCover.addCover(shiftType, preferred);
                        }
                    }
                }
                coverList.add(dayCover);
            }
        }

        period.setDayCovers(coverList);
    }

    /**
     * Parses UnwantedPatterns node.
     * @param unwantedPatterns UnwantedPatterns node.
     * @param contract Contract instance.
     */
    private void parseUnwantedPatterns(Element unwantedPatterns, Contract contract) {
        List<Integer> unwantedPatternsList = new ArrayList<Integer>();

        for (int i = 0; i < unwantedPatterns.getChildCount(); i++) {
            Node patternNode = unwantedPatterns.getChild(i);
            if (patternNode instanceof Element) {
                Element element = (Element) patternNode;
                if (element.getLocalName().equals("Pattern")) {
                    unwantedPatternsList.add(Integer.valueOf(element.getValue()));
                }
            }
        }

        contract.setUnwantedPatterns(unwantedPatternsList);
    }

    /**
     * Parses Patterns node.
     * @param patterns Patterns node.
     * @param period SchedulingPeriod instance.
     */
    private void parsePatterns(Element patterns, SchedulingPeriod period) throws ParseException, NoSuchFieldException, IllegalAccessException {
        for (int i = 0; i < patterns.getChildCount(); i++) {
            Node patternNode = patterns.getChild(i);
            if (patternNode instanceof Element && ((Element) patternNode).getLocalName().equals("Pattern")) {
                Element element = (Element) patternNode;
                Pattern pattern = new Pattern();
                pattern.setId(Integer.parseInt(element.getAttributeValue("ID")));
                pattern.setWeight(Integer.parseInt(element.getAttributeValue("weight")));
                for (int j = 0; j < patternNode.getChildCount(); j++) {
                    if (element.getChild(j) instanceof Element) {
                        Element patternEntries = (Element) element.getChild(j);
                        for (int k = 0; k < patternEntries.getChildCount(); k++) {
                            if (patternEntries.getChild(k) instanceof Element) {
                                Element patternEntryInfo = (Element) patternEntries.getChild(k);
                                PatternEntry patternEntry = new PatternEntry();
                                patternEntry.setId(Integer.parseInt(patternEntryInfo.getAttributeValue("index")));
                                String shiftTypeValue = patternEntryInfo.getChildElements("ShiftType").get(0).getValue();
                                String dayValue = patternEntryInfo.getChildElements("Day").get(0).getValue();

                                if (shiftTypeValue.equals("None")) {
                                    patternEntry.setShiftTypeNone(true);
                                } else if (shiftTypeValue.equals("Any")) {
                                    patternEntry.setShiftTypeAny(true);
                                } else {
                                    patternEntry.setShiftType(period.getShiftTypeById(shiftTypeValue));
                                }

                                if (dayValue.equals("Any")) {
                                    patternEntry.setDayAny(true);
                                } else {
                                    patternEntry.setDay(DateTimeHelper.getInstance().getDayByName(dayValue));
                                }

                                pattern.addPatternEntry(patternEntry);
                            }
                        }
                        period.addPattern(pattern);
                    }
                }
            }
        }
    }

    /**
     * Parses Contracts node.
     * @param contracts Contracts node.
     * @param period SchedulingPeriod instance.
     */
    private void parseContracts(Element contracts, SchedulingPeriod period) throws ParseException, NoSuchFieldException, IllegalAccessException {
        List<Contract> contractList = new ArrayList<Contract>();

        for (int i = 0; i < contracts.getChildCount(); i++) {
            Node contractNode = contracts.getChild(i);
            if (contractNode instanceof Element && ((Element) contractNode).getLocalName().equals("Contract")) {
                Element element = (Element) contractNode;
                Contract contract = new Contract();
                contract.setId(Integer.parseInt(element.getAttributeValue("ID")));
                for (int j = 0; j < contractNode.getChildCount(); j++) {
                    if (element.getChild(j) instanceof Element) {
                        Element contractInfo = (Element) element.getChild(j);
                        // handle weighted attributes
                        if (ArrayHelper.getInstance().contains(
                                ConfigurationHelper.getInstance().getPropertyArray("WeightedAttributes"),
                                contractInfo.getLocalName())) {
                            Attribute attribute = new Attribute();
                            attribute.setName(contractInfo.getLocalName());
                            attribute.setValue(contractInfo.getValue());
                            if (contractInfo.getAttribute("weight") != null) {
                                attribute.setWeight(Integer.valueOf(contractInfo.getAttributeValue("weight")));
                            } else if (contractInfo.getAttribute("on") != null) {
                                attribute.setOn(Integer.valueOf(contractInfo.getAttributeValue("on")));
                            }
                            contract.setAttribute(attribute);
                        } else if (contractInfo.getLocalName().equals("Description")) {
                            contract.setDescription(contractInfo.getValue());
                        } else if (contractInfo.getLocalName().equals("UnwantedPatterns")) {
                            parseUnwantedPatterns(contractInfo, contract);
                        } else if (contractInfo.getLocalName().equals("WeekendDefinition")) {
                            contract.setWeekendDefinition(
                                    DateTimeHelper.getInstance().getDayListFromString(contractInfo.getValue()));
                        }
                    }
                }
                contractList.add(contract);
            }
        }

        period.setContracts(contractList);
    }

    /**
     * Parses ShiftTypes node.
     * @param shiftTypes ShiftTypes node.
     * @param period SchedulePeriod instance.
     */
    private void parseShifts(Element shiftTypes, SchedulingPeriod period) throws ParseException {
        for (int i = 0; i < shiftTypes.getChildCount(); i++) {
            Node shiftNode = shiftTypes.getChild(i);
            if (shiftNode instanceof Element) {
                Element element = (Element) shiftNode;
                if (element.getLocalName().equals("Shift")) {
                    ShiftType shiftType = new ShiftType();
                    shiftType.setId(element.getAttributeValue("ID"));
                    for (int j = 0; j < shiftNode.getChildCount(); j++) {
                        if (element.getChild(j) instanceof Element) {
                            Element shiftInfo = (Element) element.getChild(j);
                            if (shiftInfo.getLocalName().equals("StartTime")) {
                                shiftType.setStartTime(DateTimeHelper.getInstance().parseTime(shiftInfo.getValue()));
                            } else if (shiftInfo.getLocalName().equals("EndTime")) {
                                shiftType.setEndTime(DateTimeHelper.getInstance().parseTime(shiftInfo.getValue()));
                            } else if (shiftInfo.getLocalName().equals("Description")) {
                                shiftType.setDescription(shiftInfo.getValue());
                            } else if (shiftInfo.getLocalName().equals("Skills")) {
                                shiftType.setRequiredSkills(parseSkills(shiftInfo));
                            }
                        }
                    }
                    period.addShiftType(shiftType);
                }
            }
        }
    }

    /**
     * Returns a parsed Employees node as a List.
     * @param employees Employees node.
     * @param period SchedulingPeriod instance.
     */
    private void parseEmployees(Element employees, SchedulingPeriod period) throws ParseException {
        for (int i = 0; i < employees.getChildCount(); i++) {
            Node employeeNode = employees.getChild(i);
            if (employeeNode instanceof Element) {
                Element element = (Element) employeeNode;
                if (element.getLocalName().equals("Employee")) {
                    Employee employee = new Employee();
                    employee.setId(Integer.parseInt(element.getAttributeValue("ID")));
                    for (int j = 0; j < employeeNode.getChildCount(); j++) {
                        if (element.getChild(j) instanceof Element) {
                            Element employeeInfo = (Element) element.getChild(j);
                            if (employeeInfo.getLocalName().equals("ContractID")) {
                                employee.setContract(period.getContractById(Integer.parseInt(employeeInfo.getValue())));
                            } else if (employeeInfo.getLocalName().equals("Name")) {
                                employee.setName(employeeInfo.getValue());
                            } else if (employeeInfo.getLocalName().equals("Skills")) {
                                employee.setSkills(parseSkills(employeeInfo));
                            }
                        }
                    }
                    period.addEmployee(employee);
                }
            }
        }
    }

    /**
     * Enriches employees with their day off requests.
     * @param dayOffRequests DayOffRequests node.
     * @param period SchedulingPeriod instance.
     */
    private void parseDayOffRequests(Element dayOffRequests, SchedulingPeriod period) throws ParseException {
        for (int i = 0; i < dayOffRequests.getChildCount(); i++) {
            Node dayOffRequest = dayOffRequests.getChild(i);
            if (dayOffRequest instanceof Element) {
                Element element = (Element) dayOffRequest;
                if (element.getLocalName().equals("DayOff")) {
                    Employee employee = null;
                    DayOff dayOff = new DayOff();
                    for (int j = 0; j < dayOffRequest.getChildCount(); j++) {
                        if (element.getChild(j) instanceof Element) {
                            Element dayOffInfo = (Element) element.getChild(j);
                            if (dayOffInfo.getLocalName().equals("EmployeeID")) {
                                employee = period.getEmployeeById(dayOffInfo.getValue());
                            } else if (dayOffInfo.getLocalName().equals("Date")) {
                                dayOff.setDate(DateTimeHelper.getInstance().parseDate(dayOffInfo.getValue()));
                            }
                        }
                    }
                    // Add shift off request to requests of employee if found.
                    if (employee != null) {
                        employee.addDayOffRequest(dayOff);
                    }
                }
            }
        }
    }

    /**
     * Enriches employees with their shift off requests.
     * @param shiftOffRequests ShiftOffRequests node.
     * @param period SchedulingPeriod instance.
     */
    private void parseShiftOffRequests(Element shiftOffRequests, SchedulingPeriod period) throws ParseException {
        for (int i = 0; i < shiftOffRequests.getChildCount(); i++) {
            Node shiftOffRequest = shiftOffRequests.getChild(i);
            if (shiftOffRequest instanceof Element) {
                Element element = (Element) shiftOffRequest;
                if (element.getLocalName().equals("ShiftOff")) {
                    Employee employee = null;
                    ShiftOff shiftOff = new ShiftOff();
                    shiftOff.setWeight(Integer.parseInt(element.getAttributeValue("weight")));
                    for (int j = 0; j < shiftOffRequest.getChildCount(); j++) {
                        if (element.getChild(j) instanceof Element) {
                            Element shiftOffInfo = (Element) element.getChild(j);
                            if (shiftOffInfo.getLocalName().equals("ShiftTypeID")) {
                                shiftOff.setShiftType(period.getShiftTypeById(shiftOffInfo.getValue()));
                            } else if (shiftOffInfo.getLocalName().equals("EmployeeID")) {
                                employee = period.getEmployeeById(shiftOffInfo.getValue());
                            } else if (shiftOffInfo.getLocalName().equals("Date")) {
                                shiftOff.setDate(DateTimeHelper.getInstance().parseDate(shiftOffInfo.getValue()));
                            }
                        }
                    }
                    // Add shift off request to requests of employee if found.
                    if (employee != null) {
                        employee.addShiftOffRequest(shiftOff);
                    }
                }
            }
        }
    }

    /**
     * Parses the whole period definition.
     * @param root Root element (SchedulingPeriod)
     * @return Compiled SchedulingPeriod instance
     */
    private SchedulingPeriod parseSchedulingPeriod(Element root, SchedulingPeriod period) throws Throwable {
        period.setId(root.getAttributeValue("ID"));
        for (int i = 0; i < root.getChildCount(); i++) {
            Node node = root.getChild(i);
            if (node instanceof Element) {
                Element element = (Element) node;
                if (element.getLocalName().equals("StartDate")) {
                    period.setStartDate(DateTimeHelper.getInstance().parseDate(element.getValue()));
                } else if (element.getLocalName().equals("EndDate")) {
                    period.setEndDate(DateTimeHelper.getInstance().parseDate(element.getValue()));
                } else if (element.getLocalName().equals("Skills")) {
                    period.setSkills(parseSkills(element));
                } else if (element.getLocalName().equals("ShiftTypes")) {
                    parseShifts(element, period);
                } else if (element.getLocalName().equals("Patterns")) {
                    parsePatterns(element, period);
                } else if (element.getLocalName().equals("Contracts")) {
                    parseContracts(element, period);
                } else if (element.getLocalName().equals("Employees")) {
                    parseEmployees(element, period);
                } else if (element.getLocalName().equals("CoverRequirements")) {
                    parseDayCovers(element, period);
                } else if (element.getLocalName().equals("DayOffRequests")) {
                    parseDayOffRequests(element, period);
                } else if (element.getLocalName().equals("ShiftOffRequests")) {
                    parseShiftOffRequests(element, period);
                }
            }
        }
        return period;
    }

    @Override
    public SchedulingPeriod loadFile(String path) {
        try {
            File xmlFile = new File(path);
            SchedulingPeriod schedulingPeriod = new SchedulingPeriod();
            schedulingPeriod.setSourceFile(xmlFile.getAbsolutePath());

            Builder parser = new Builder();
            Document doc = parser.build(xmlFile);

            return parseSchedulingPeriod(doc.getRootElement(), schedulingPeriod);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return null;
    }
}
