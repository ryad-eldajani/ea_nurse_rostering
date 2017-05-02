package writer;

import helper.ConfigurationHelper;
import helper.DateTimeHelper;
import helper.FilesystemHelper;
import model.ea.Individual;
import model.schedule.*;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Serializer;

import java.io.*;
import java.util.Map;

/**
 * Class to write an XML solution file.
 */
@SuppressWarnings("unused")
public class XmlWriter implements IWriter {
    /**
     * Returns the solution as a XOM Document instance.
     * @param individual Individual
     * @return Document instance
     */
    private Document getDocument(Individual individual) {
        Element root = new Element("Solution");
        Element periodId = new Element("SchedulingPeriodID");
        Element competitor = new Element("Competitor");
        Element penalty = new Element("SoftConstraintsPenalty");

        periodId.appendChild(String.valueOf(individual.getId()));
        competitor.appendChild(ConfigurationHelper.getInstance().getProperty("CompetitorName"));
        penalty.appendChild(String.valueOf(individual.getFitness()));

        root.appendChild(periodId);
        root.appendChild(competitor);
        root.appendChild(penalty);

        for (DayRoster dayRoster: individual.getDayRosters()) {
            for (Map<ShiftType, Employee> map: dayRoster.getDayRoster()) {
                for (Map.Entry<ShiftType, Employee> entry: map.entrySet()) {
                    ShiftType shiftType = entry.getKey();
                    Employee employee = entry.getValue();

                    Element assignment = new Element("Assignment");
                    Element dateElement = new Element("Date");
                    Element employeeElement = new Element("Employee");
                    Element shiftTypeElement = new Element("ShiftType");

                    dateElement.appendChild(
                            DateTimeHelper.getInstance().getDateStringReversed(dayRoster.getDate(), "-"));
                    employeeElement.appendChild(String.valueOf(employee.getId()));
                    shiftTypeElement.appendChild(shiftType.getId());

                    assignment.appendChild(dateElement);
                    assignment.appendChild(employeeElement);
                    assignment.appendChild(shiftTypeElement);

                    root.appendChild(assignment);
                }
            }
        }

        return new Document(root);
    }

    /**
     * Writes a solution file in XML format.
     * @param individual Individual instance
     * @return Full path
     * @throws Exception Exception on fail
     */
    @Override
    public String writeFile(Individual individual) throws Exception {
        String filename = ConfigurationHelper.getInstance().getProperty("SolutionFilename", "solution.xml");
        String fullPath = FilesystemHelper.getInstance().getFullPath(filename);

        FileOutputStream fileOutputStream = new FileOutputStream(fullPath);
        Serializer serializer = new Serializer(fileOutputStream, "UTF-8");
        serializer.setIndent(2);
        serializer.write(getDocument(individual));


        // return full path, if written successfully, otherwise null
        return fullPath;
    }
}
