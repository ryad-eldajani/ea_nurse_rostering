package model.schedule;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Defines a pattern.
 */
public class Pattern {
    /**
     * Identifier of this pattern.
     */
    private int id;

    /**
     * Weight for constraint deviation.
     */
    private int weight;

    /**
     * Map identifier -> PatternEntry
     */
    Map<Integer, PatternEntry> entries = new LinkedHashMap<Integer, PatternEntry>();

    /**
     * Getter identifier.
     * @return Identifier
     */
    public int getId() {
        return id;
    }

    /**
     * Setter identifier
     * @param id Identifier
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter weight.
     * @return Weight
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Setter weight.
     * @param weight Weight
     */
    public void setWeight(int weight) {
        this.weight = weight;
    }

    /**
     * Getter entries.
     * @return Pattern entries
     */
    public Map<Integer, PatternEntry> getEntries() {
        return entries;
    }

    /**
     * Adds a pattern entry.
     * @param entry PatternEntry instance
     */
    public void addPatternEntry(PatternEntry entry) {
        entries.put(entry.getId(), entry);
    }
}
