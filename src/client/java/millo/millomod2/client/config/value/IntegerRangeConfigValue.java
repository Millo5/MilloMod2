package millo.millomod2.client.config.value;

public class IntegerRangeConfigValue extends IntegerConfigValue {

    private final int min, max;

    public IntegerRangeConfigValue(Integer defaultValue, int min, int max) {
        super(defaultValue);
        this.min = min;
        this.max = max;
    }

    @Override
    public void setValue(Integer value) {
        super.setValue(Math.max(min, Math.min(max, value)));
    }

}
