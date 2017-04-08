package writer;

import model.ea.Individual;

/**
 * Defines the general interface for a file parser containing period definition.
 */
public interface IWriter {
    /**
     * Writes a solution file.
     * @param individual Individual instance
     * @return Full path to solution file, if written successfully
     */
    String writeFile(Individual individual) throws Exception;
}
