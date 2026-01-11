package millo.millomod2.client.config.saving;


import java.util.HashMap;
import java.util.Map;

// for GSON deserialization
public class SavedFeature {

    public Map<String, Object> values = new HashMap<>();

    public void put(String key, Object value) {
        values.put(key, value);
    }

}
