package model.schedule;

/**
 * Skill enumeration.
 */
public enum Skill {
    NURSE, HEAD_NURSE;

    @Override
    public String toString() {
        return this == NURSE ? "N" : "HN";
    }
}
