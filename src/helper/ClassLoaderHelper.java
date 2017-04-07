package helper;

import model.ea.operators.IEnvironmentSelectionOperator;
import model.ea.operators.IMutationOperator;
import model.ea.operators.IRecombinationOperator;
import model.ea.construction.IConstructionHeuristic;
import model.ea.operators.IMatingSelectionOperator;
import parser.IParser;

/**
 * Helper methods for loading correct classes.
 */
public class ClassLoaderHelper {
    /**
     * Singleton instance.
     */
    private final static ClassLoaderHelper instance = new ClassLoaderHelper();

    /**
     * Returns the singleton instance.
     *
     * @return Singleton instance
     */
    public static ClassLoaderHelper getInstance() {
        return ClassLoaderHelper.instance;
    }

    /**
     * Private constructor to avoid bypassing singleton.
     */
    private ClassLoaderHelper() {}

    /**
     * Instantiates a new object from a configuration.
     * @param configurationKey Configuration key
     * @param configurationFallback Fallback configuration
     * @param packagePrefix Prefix for package
     * @return Object instance
     */
    private Object getLoadedClass(String configurationKey, String configurationFallback, String packagePrefix) {
        try {
            // try to instantiate the appropriate object
            Class loadingClass = Class.forName(packagePrefix
                    + ConfigurationHelper.getInstance().getProperty(configurationKey, configurationFallback));
            return loadingClass.newInstance();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Returns the file parser for the scheduling period definitions.
     * @return Instance that implements IParser
     */
    public IParser getParser() {
        Object instance = getLoadedClass("Parser", "XmlParser", "parser.");
        if (instance == null) {
            return null;
        }

        return (IParser) instance;
    }

    /**
     * Returns the implementing construction heuristic.
     * @return Instance that implements IConstructionHeuristic
     */
    public IConstructionHeuristic getConstructionHeuristic() {
        Object instance = getLoadedClass("ConstructionHeuristic", "SimpleConstructionHeuristic", "model.ea.construction.");
        if (instance == null) {
            return null;
        }

        return (IConstructionHeuristic) instance;
    }

    /**
     * Returns the implementing mating selection operator.
     * @return Instance that implements IConstructionHeuristic
     */
    public IMatingSelectionOperator getMatingSelectionOperator() {
        Object instance = getLoadedClass("MatingSelectionOperator", "SimpleMatingSelectionOperator", "model.ea.operators.");
        if (instance == null) {
            return null;
        }

        return (IMatingSelectionOperator) instance;
    }

    /**
     * Returns the implementing recombination operator.
     * @return Instance that implements IRecombinationOperator
     */
    public IRecombinationOperator getRecombinationOperator() {
        Object instance = getLoadedClass("RecombinationOperator", "SimpleRecombinationOperator", "model.ea.operators.");
        if (instance == null) {
            return null;
        }

        return (IRecombinationOperator) instance;
    }

    /**
     * Returns the implementing mutation operator.
     * @return Instance that implements IMutationOperator
     */
    public IMutationOperator getMutationOperator() {
        Object instance = getLoadedClass("MutationOperator", "SimpleMutationOperator", "model.ea.operators.");
        if (instance == null) {
            return null;
        }

        return (IMutationOperator) instance;
    }

    /**
     * Returns the implementing environmental selection operator.
     * @return Instance that implements IEnvironmentSelectionOperator
     */
    public IEnvironmentSelectionOperator getEnvironmentSelectionOperator() {
        Object instance = getLoadedClass("EnvironmentSelectionOperator", "SimpleEnvironmentSelectionOperator", "model.ea.operators.");
        if (instance == null) {
            return null;
        }

        return (IEnvironmentSelectionOperator) instance;
    }
}
