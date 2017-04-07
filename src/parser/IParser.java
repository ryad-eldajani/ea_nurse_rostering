package parser;

import model.schedule.SchedulingPeriod;

/**
 * Defines the general interface for a file parser containing period definition.
 */
public interface IParser {
    /**
     * Parses a scheduling period definition file.
     *
     * @param path Path to the scheduling period definition file.
     * @return Scheduling period configuration.
     */
    SchedulingPeriod loadFile(String path);
}
