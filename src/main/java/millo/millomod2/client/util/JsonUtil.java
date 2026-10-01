package millo.millomod2.client.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import millo.millomod2.client.util.logging.MilloLogger;

import java.util.HashSet;
import java.util.Set;

public class JsonUtil {

    public static void compare(JsonObject self, JsonObject other, MilloLogger logger) {
        compareInternal(self, other, logger);
    }

    private static void compareInternal(JsonElement self, JsonElement other, MilloLogger logger) {
        if (self.isJsonObject() && other.isJsonObject()) {
            compareInternal(self.getAsJsonObject(), other.getAsJsonObject(), logger);
        } else if (self.isJsonArray() && other.isJsonArray()) {
            compareInternal(self.getAsJsonArray(), other.getAsJsonArray(), logger);
        } else if (!self.equals(other)) {
            logger.warn(self + " does not match " + other);
        }
    }

    private static void compareInternal(JsonObject self, JsonObject other, MilloLogger logger) {
        Set<String> keys = new HashSet<>();
        keys.addAll(self.keySet());
        keys.addAll(other.keySet());

        for (String key : keys) {
            logger.push(key);
            if (!other.has(key)) {
                logger.warn("Key is missing in other, value: " + self.get(key));
                continue;
            }
            if (!self.has(key)) {
                logger.warn("Key is missing in self, value: " + other.get(key));
                continue;
            }
            compareInternal(self.get(key), other.get(key), logger);
            logger.pop();
        }
    }

    private static void compareInternal(JsonArray self, JsonArray other, MilloLogger logger) {
        int maxSize = Math.max(self.size(), other.size());
        for (int i = 0; i < maxSize; i++) {
            logger.push("[" + i + "]");
            if (i >= self.size()) {
                logger.warn("Index missing in self, value: " + other.get(i));
                continue;
            }
            if (i >= other.size()) {
                logger.warn("Index missing in other, value: " + self.get(i));
                continue;
            }
            compareInternal(self.get(i), other.get(i), logger);
            logger.pop();
        }
    }

}
