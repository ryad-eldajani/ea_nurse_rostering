package start;

import helper.ConfigurationHelper;
import model.SchedulingPeriod;
import parser.IParser;

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
     *
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
     *
     * @param args Optional arguments for the JVM
     */
    public static void main(String[] args) {
        SchedulingPeriod period = getInstance().parseSchedulingPeriod();
        System.out.print(period);
    }

    /**
     * Instantiates the parser, loads and returns a scheduling period definitions.
     * @return SchedulingPeriod instance or null.
     */
    private SchedulingPeriod parseSchedulingPeriod() {
        try {
            // try to instantiate the parser object
            Class parserClass = Class.forName("parser."
                    + ConfigurationHelper.getInstance().getProperty("Parser", "XmlParser"));
            IParser parser = (IParser) parserClass.newInstance();

            // try to load the desired scheduling period
            return parser.loadFile("data/"
                    + ConfigurationHelper.getInstance().getProperty("PeriodFile", "toy1.xml"));
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return null;
    }
}
