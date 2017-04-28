package start;

import helper.ClassLoaderHelper;
import helper.ConfigurationHelper;
import helper.TuiHelper;
import model.ea.EvolutionaryCycle;
import model.ea.Individual;
import model.ea.Population;
import model.schedule.Numbering;
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
        // read scheduling period information
        SchedulingPeriod period = getInstance().parseSchedulingPeriod();

        // create and run the evolutionary cycle
        EvolutionaryCycle evolutionaryCycle = (new EvolutionaryCycle());
        Population evolutionizedPopulation = evolutionaryCycle.evolutionize(period);

        // retrieve best individual from initialized and evolutionized populations
        Individual bestInitialized = evolutionaryCycle.getInitPopulation().getBestIndividual();
        Individual best = evolutionizedPopulation.getBestIndividual();

        TuiHelper.getInstance().showEAResult(best, bestInitialized);
        System.out.println(Start.getInstance().writeSolutionFile(best));
    }

    /**
     * Writes a solution file.
     * @param individual Individual instance
     * @return Full path of written file
     */
    private String writeSolutionFile(Individual individual) {
        // check, if we need to write a solution file
        if (!ConfigurationHelper.getInstance().getPropertyBoolean("SolutionWrite")) {
            return "\"SolutionWrite\" in config.properties is set to \"false\", no solution file written.";
        }

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
        SchedulingPeriod period = parser.loadFile("data/"
                + ConfigurationHelper.getInstance().getProperty("PeriodFile", "toy1.xml"));

        // setup the correct numberings for this scheduling period
        Numbering.setupNumberings(period);

        return period;
    }
}
