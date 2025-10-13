package millo.millomod2.client.config;

public class ConfigValuePair<T> {
    private final String key;
    private final ConfigValue<T> configValue;

    public ConfigValuePair(String key, ConfigValue<T> configValue) {
        this.key = key;
        this.configValue = configValue;
    }

    public String getKey() {
        return key;
    }

    public T getValue() {
        return configValue.getValue();
    }

    public ConfigValue<T> getConfigValue() {
        return configValue;
    }

    public void setValue(T value) {
        configValue.setValue(value);
    }
}