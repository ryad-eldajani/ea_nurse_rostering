package parser;

import model.SchedulingPeriod;

public interface IParser {
    /**
     * Parses a scheduling period definition file.
     *
     * @param path Path to the scheduling period definition file.
     * @return Scheduling period configuration.
     */
    SchedulingPeriod loadFile(String path);
}
