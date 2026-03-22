package millo.millomod2.client.config;

import millo.millomod2.client.config.saving.SavedFeature;
import millo.millomod2.client.config.value.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"unused", "unchecked"})
public class FeatureConfig {

    private final ArrayList<String> order = new ArrayList<>();
    private final Map<String, ConfigValuePair<?>> values = new HashMap<>();

    private boolean finalized = false;
    public FeatureConfig() {}
    public void finalizeConfig() {
        finalized = true;
    }

    public void forEach(ConfigValueConsumer consumer) {
        order.forEach(key -> consumer.accept(values.get(key)));
    }

    public <T> void addListener(String key, ConfigValue.ConfigValueChangeListener<T> listener) {
        ConfigValue<T> config = (ConfigValue<T>) values.get(key).getConfigValue();
        config.addListener(listener);
    }

    public void loadFrom(SavedFeature savedFeature) {
        savedFeature.values.forEach((key, value) -> {
            if (!values.containsKey(key)) return;

            ConfigValuePair<?> current = values.get(key);
            current.getConfigValue().deserialize(value);
        });
    }

    public SavedFeature getSave() {
        SavedFeature save = new SavedFeature();
        forEach(pair -> save.put(pair.getKey(), pair.getConfigValue().serialize()));
        return save;
    }


    public interface ConfigValueConsumer {
        void accept(ConfigValuePair<?> value);
    }

    // -- Adders

    private <T> void addValue(String key, ConfigValue<T> value) {
        if (finalized) throw new IllegalStateException("Cannot add config values after finalizing the config.");
        if (values.containsKey(key)) throw new IllegalArgumentException("Config pair with key " + key + " already exists.");
        values.put(key, new ConfigValuePair<>(key, value));
        order.add(key);
    }

    public FeatureConfig addInteger(String key, int def) {
        addValue(key, new IntegerConfigValue(def));
        return this;
    }

    public FeatureConfig addIntegerRange(String key, int def, int min, int max) {
        addValue(key, new IntegerRangeConfigValue(def, min, max));
        return this;
    };

    public FeatureConfig addBoolean(String key, boolean def) {
        addValue(key, new BooleanConfigValue(def));
        return this;
    };

    public void addFloat(String key, float def) {
        addValue(key, new FloatConfigValue(def));
    }

    public FeatureConfig addString(String key, String def) {
        addValue(key, new StringConfigValue(def));
        return this;
    };

    public FeatureConfig addChoice(String key, String def, String[] choices) {
        addValue(key, new ChoiceConfigValue(def, choices));
        return this;
    }

    public FeatureConfig addColor(String color, int def) {
        addValue(color, new ColorConfigValue(def));
        return this;
    }

    // Unique Adders
    public <T extends ConfigValue<?> & Instantiable<T>> FeatureConfig addList(String key, ListConfigValue<T> conf) {
        addValue(key, conf);
        return this;
    }

//    public <T extends ConfigValue<?>> FeatureConfig addList(String key, List<T> def) {
//        addValue(key, new ListConfigValue<>(def));
//        return this;
//    }

    // -- Getters

    public Map<String, ConfigValuePair<?>> getValues() {
        return values;
    }

    public <T extends ConfigValue<?>> T get(String key) {
        return (T) values.get(key).getConfigValue();
    }

    public int getInt(String key) {
        return ((IntegerConfigValue) get(key)).getValue();
    }

    public boolean getBoolean(String key) {
        return ((BooleanConfigValue) get(key)).getValue();
    }

    public float getFloat(String key) {
        return ((FloatConfigValue) get(key)).getValue();
    }

    public String getString(String key) {
        return ((StringConfigValue) get(key)).getValue();
    }

    public String getChoice(String key) {
        return ((ChoiceConfigValue) get(key)).getValue();
    }


}
