package millo.millomod2.client.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class JsonUtil {

    public static void compare(JsonObject self, JsonObject other) {
        compare(self, other, new ArrayList<>());
    }

    private static void compare(JsonElement self, JsonElement other, ArrayList<String> path) {
        if (self.isJsonObject() && other.isJsonObject()) {
            compare(self.getAsJsonObject(), other.getAsJsonObject(), path);
        } else if (self.isJsonArray() && other.isJsonArray()) {
            compare(self.getAsJsonArray(), other.getAsJsonArray(), path);
        } else if (!self.equals(other)) {
            MilloLog.logWarning(self + " does not match " + other + " at path " + String.join(".", path));
        }
    }

    private static void compare(JsonObject self, JsonObject other, ArrayList<String> path) {
        Set<String> keys = new HashSet<>();
        keys.addAll(self.keySet());
        keys.addAll(other.keySet());

        for (String key : keys) {
            if (!other.has(key)) {
                MilloLog.logWarning("Key " + key + " is missing in other" + " at path " + String.join(".", path) + " -> " + self.get(key));
                continue;
            }
            if (!self.has(key)) {
                MilloLog.logWarning("Key " + key + " is missing in self" + " at path " + String.join(".", path) + " -> " + other.get(key));
                continue;
            }
            ArrayList<String> newPath = new ArrayList<>(path);
            newPath.add(key);
            compare(self.get(key), other.get(key), newPath);
        }
    }

    private static void compare(JsonArray self, JsonArray other, ArrayList<String> path) {
        int maxSize = Math.max(self.size(), other.size());
        for (int i = 0; i < maxSize; i++) {
            if (i >= self.size()) {
                MilloLog.logWarning("Missing element in self at index " + i + " at path " + String.join(".", path) + " -> " + other.get(i));
                continue;
            }
            if (i >= other.size()) {
                MilloLog.logWarning("Missing element in other at index " + i + " at path " + String.join(".", path) + " -> " + self.get(i));
                continue;
            }
            ArrayList<String> newPath = new ArrayList<>(path);
            newPath.add("[" + i + "]");
            compare(self.get(i), other.get(i), newPath);
        }
    }

}
