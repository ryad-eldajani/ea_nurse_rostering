package writer;

import model.ea.Individual;

/**
 * Class to write a text solution file.
 */
@SuppressWarnings("unused")
public class TextWriter implements IWriter {
    @Override
    public String writeFile(Individual individual) {
        throw new RuntimeException(TextWriter.class.toString() + " not implemented yet.");
    }
}
