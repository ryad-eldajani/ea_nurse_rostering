package start;

import helper.ClassLoaderHelper;
import helper.ConfigurationHelper;
import model.ea.EvolutionaryCycle;
import model.ea.Individual;
import model.ea.Population;
import model.schedule.SchedulingPeriod;
import parser.IParser;
import writer.IWriter;

/**
 * Start class to initialize the application.
 */
public class Start {
    /**
     * Singleton instance.
     */
    private final static Start instance = new Start();

    /**
     * Returns the singleton instance.
     * @return Singleton instance
     */
    public static Start getInstance() {
        return Start.instance;
    }

    /**
     * Private constructor to avoid bypassing singleton.
     */
    private Start() {}

    /**
     * Default main method.
     * @param args Optional arguments for the JVM
     */
    public static void main(String[] args) {
        SchedulingPeriod period = getInstance().parseSchedulingPeriod();
        Population population = (new EvolutionaryCycle()).evolutionize(period);
        Individual best = population.sortByFitness().getPool().get(0);

        System.out.println("Best solution: " + best);
        System.out.println(getInstance().writeSolutionFile(best));
    }

    /**
     * Writes a solution file.
     * @param individual Individual instance
     * @return Full path of written file
     */
    private String writeSolutionFile(Individual individual) {
        IWriter writer = ClassLoaderHelper.getInstance().getWriter();
        try {
            return "Solution file written to: " + writer.writeFile(individual);
        } catch (Exception e) {
            return "Error writing solution file: " + e.getMessage();
        }
    }

    /**
     * Instantiates the parser, loads and returns a scheduling period definitions.
     * @return SchedulingPeriod instance or null.
     */
    private SchedulingPeriod parseSchedulingPeriod() {
        IParser parser = ClassLoaderHelper.getInstance().getParser();

        // try to load the desired scheduling period
        return parser.loadFile("data/"
                + ConfigurationHelper.getInstance().getProperty("PeriodFile", "toy1.xml"));
    }
}
