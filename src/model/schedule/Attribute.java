package model.schedule;

/**
 * Attribute model.
 */
public class Attribute {
    /**
     * On definition (if any).
     */
    private Integer on = null;

    /**
     * Penalty weight, if assigned constrained is unsatisfied.
     */
    private Integer weight = null;

    /**
     * Value object (Boolean or Integer).
     */
    private Object value = null;

    /**
     * Attribute name.
     */
    private String name;

    /**
     * Sets the correct (first char lower case) attribute name.
     * @param name Attribute name.
     */
    public void setName(String name) {
        this.name = Character.toLowerCase(name.charAt(0)) + name.substring(1);;
    }

    /**
     * Sets the value with correct data type.
     * @param value Attribute value.
     */
    public void setValue(String value) {
        if (value.equals("true")) {
            this.value = true;
        } else if (value.equals("false")) {
            this.value = false;
        } else {
            this.value = Integer.parseInt(value);
        }
    }

    /**
     * Returns on value.
     * @return On value (if any).
     */
    public Integer getOn() {
        return on;
    }

    /**
     * Sets on value.
     * @param on On value.
     */
    public void setOn(Integer on) {
        this.on = on;
    }

    /**
     * Returns penalty weight.
     * @return Penalty weight.
     */
    public Integer getWeight() {
        return weight;
    }

    /**
     * Sets penalty weight.
     * @param weight Penalty weight.
     */
    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    /**
     * Returns attribute name.
     * @return Attribute name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the value as integer.
     * @return Value as integer
     */
    public int getValueInt() {
        try {
            return (Integer) value;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Returns the value as boolean.
     * @return Value as boolean
     */
    public boolean getValueBoolean() {
        try {
            return (Boolean) value;
        } catch (Exception e) {
            return false;
        }
    }
}
