package helper;

/**
 * Helper methods for arrays.
 */
public class ArrayHelper {
    /**
     * Singleton instance.
     */
    private final static ArrayHelper instance = new ArrayHelper();

    /**
     * Returns the singleton instance.
     *
     * @return Singleton instance
     */
    public static ArrayHelper getInstance() {
        return ArrayHelper.instance;
    }

    /**
     * Private constructor to avoid bypassing singleton.
     */
    private ArrayHelper() {}

    /**
     * Returns true, if array contains needle.
     * @param array Array to check.
     * @param needle Needle to search.
     * @return True, if needle is in array.s
     */
    public boolean contains(Object[] array, Object needle) {
        for (Object element: array) {
            if (element.equals(needle)) {
                return true;
            }
        }

        return false;
    }
}
