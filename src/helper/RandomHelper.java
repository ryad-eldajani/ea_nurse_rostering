package helper;

import java.util.Random;

/**
 * Helper methods for arrays.
 */
public class RandomHelper {
    /**
     * Singleton instance.
     */
    private final static RandomHelper instance = new RandomHelper();

    /**
     * Returns the singleton instance.
     *
     * @return Singleton instance
     */
    public static RandomHelper getInstance() {
        return RandomHelper.instance;
    }

    /**
     * Random instance
     */
    private Random random;

    /**
     * Private constructor to avoid bypassing singleton.
     */
    private RandomHelper() {
        long seed = ConfigurationHelper.getInstance().getPropertyLong("RandomSeed", System.nanoTime());
        random = new Random(seed);
    }

    /**
     * Returns a random integer between min (inclusive) and max (exclusive).
     * @param min Minimum random integer
     * @param max Maximum random integer
     * @return Random integer between min and max
     */
    public int getInt(int min, int max) {
        return random.nextInt(max - min) + min;
    }

    /**
     * Returns a random integer between 0 (inclusive) and max (exclusive).
     * @param max Maximum random integer
     * @return Random integer between 0 and max
     */
    public int getInt(int max) {
        return getInt(0, max);
    }
}
