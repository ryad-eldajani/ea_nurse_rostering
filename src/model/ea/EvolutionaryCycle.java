package model.ea;

import helper.ClassLoaderHelper;
import helper.ConfigurationHelper;
import helper.TuiHelper;
import model.ea.operators.IEnvironmentSelectionOperator;
import model.ea.operators.IMatingSelectionOperator;
import model.ea.operators.IMutationOperator;
import model.ea.operators.IRecombinationOperator;
import model.schedule.SchedulingPeriod;

/**
 * This class implements an evolutionary cycle for the evolutionary algorithm.
 */
public class EvolutionaryCycle {
    /**
     * Current iteration.
     */
    private int iteration = 0;

    /**
     * Maximum number of iterations.
     */
    private int maxIterations = ConfigurationHelper.getInstance().getPropertyInteger("MaxIterations", 1000);

    /**
     * The recombination operator is used, if true.
     */
    private boolean useRecombination = ConfigurationHelper.getInstance().getPropertyBoolean("UseRecombination");

    /**
     * The mutation operator is used, if true.
     */
    private boolean useMutation = ConfigurationHelper.getInstance().getPropertyBoolean("UseMutation");

    /**
     * Holds the mating selection operator instance.
     */
    private IMatingSelectionOperator matingSelectionOperator = ClassLoaderHelper.getInstance().getMatingSelectionOperator();

    /**
     * Holds the recombination operator instance.
     */
    private IRecombinationOperator recombinationOperator = ClassLoaderHelper.getInstance().getRecombinationOperator();

    /**
     * Holds the mutation operator instance.
     */
    private IMutationOperator mutationOperator = ClassLoaderHelper.getInstance().getMutationOperator();

    /**
     * Holds the environment selection operator instance.
     */
    private IEnvironmentSelectionOperator environmentSelectionOperator = ClassLoaderHelper.getInstance().getEnvironmentSelectionOperator();

    /**
     * Holds the initializing population (for benchmarking purposes against last solutions).
     */
    private Population initPopulation = null;

    /**
     * Returns the initializing population.
     * @return Initialization population instance
     */
    public Population getInitPopulation() {
        return initPopulation;
    }

    /**
     * Runs the evolutionary cycle.
     * @param period SchedulingPeriod instance
     * @return Population instance
     */
    public Population evolutionize(SchedulingPeriod period) {
        initPopulation = generateInitializationPopulation(period);
        initPopulation.benchmark();

        Population population = Population.copy(initPopulation);
        Population children;

        // evolutionize cycle, while termination condition is not met
        while (!isTerminationCondition()) {
            // get selection of mating individuals
            Population parents = matingSelectionOperator.select(population);
            children = new Population();

            // recombine individuals (if used)
            if (useRecombination) {
                 children = recombinationOperator.recombine(parents);
            }

            // mutate individuals (if used)
            if (useMutation) {
            	children = mutationOperator.mutate(children);
            }

            // benchmark new generation
            population.addIndividualsToPool(children.getPool());
            population.benchmark();

            // get environmental selection from new generation
            environmentSelectionOperator.select(population);
        }

        // cycle is terminated, return the latest solution
        return population;
    }

    /**
     * Returns true, if the termination condition is met.
     * @return True, if termination condition is met
     */
    private boolean isTerminationCondition() {
        TuiHelper.getInstance().showProgress(iteration, maxIterations);
        return ++iteration > maxIterations;
    }

    /**
     * Returns a generated initialization population for a specific scheduling period.
     * @param period SchedulingPeriod instance
     * @return Population instance
     */
    private Population generateInitializationPopulation(SchedulingPeriod period) {
        Population population = new Population();

        for (int i = 0; i < ConfigurationHelper.getInstance().getPropertyInteger("IndividualsPerPopulation", 10); i++) {
            Individual individual = ClassLoaderHelper.getInstance().getConstructionHeuristic().getIndividual(period);
            population.addIndividualToPool(individual);
        }

        return population;
    }
}
