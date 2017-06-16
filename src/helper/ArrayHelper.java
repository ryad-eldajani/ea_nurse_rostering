package helper;

import java.util.List;

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

    /**
     * Reverses an array. See: http://stackoverflow.com/a/2137791
     * @param array Array to reverses
     * @return Reversed array
     */
    Object[] reverse(Object[] array) {
        for (int i = 0; i < array.length / 2; i++) {
            Object temp = array[i];
            array[i] = array[array.length - i - 1];
            array[array.length - i - 1] = temp;
        }

        return array;
    }

    /**
     * Outputs an array as a string separated by separator
     * @param array Array to output
     * @param separator Separator
     * @return Array as string
     */
    String getString(Object[] array, String separator) {
        StringBuilder out = new StringBuilder();

        for (int i = 0; i < array.length; i++) {
            out.append(array[i].toString());
            if (i < array.length - 1) {
                out.append(separator);
            }
        }

        return out.toString();
    }

    /**
     * Removes an element from a list by value.
     * @param list List instance
     * @param value Value to remove
     */
    public void removeValue(List list, Object value) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(value)) {
                list.remove(i);
                return;
            }
        }
    }
}
