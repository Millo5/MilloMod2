package millo.millomod2.client.features.impl.Editor.logic.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import millo.millomod2.client.features.impl.Editor.logic.model.codeblocks.CodeBlockModel;
import millo.millomod2.client.util.MilloLog;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class TemplateModel implements JsonSerializable<TemplateModel> {

    private ArrayList<CodeBlockModel<?>> blocks;

    @Override
    public TemplateModel deserialize(JsonObject jsonObject) {
        blocks = new ArrayList<>();
        for (JsonElement jsonElement : jsonObject.get("blocks").getAsJsonArray()) {
            blocks.add(CodeBlockModel.deserializeCodeBlock(jsonElement.getAsJsonObject()));
        }
        return this;
    }

    @Override
    public JsonObject serialize() {
        JsonObject root = new JsonObject();
        JsonArray blocksJson = new JsonArray();
        for (CodeBlockModel<?> block : blocks) {
            blocksJson.add(block.serialize());
        }
        root.add("blocks", blocksJson);
        return root;
    }

    public void compare(JsonObject other) {
        JsonObject thisJson = this.serialize();
        compare(thisJson, other, new ArrayList<>());
    }

    private void compare(JsonElement self, JsonElement other, ArrayList<String> path) {
        if (self.isJsonObject() && other.isJsonObject()) {
            compare(self.getAsJsonObject(), other.getAsJsonObject(), path);
        } else if (self.isJsonArray() && other.isJsonArray()) {
            compare(self.getAsJsonArray(), other.getAsJsonArray(), path);
        } else if (!self.equals(other)) {
            MilloLog.logWarning(self + " does not match " + other + " at path " + String.join(".", path));
        }
    }

    private void compare(JsonObject self, JsonObject other, ArrayList<String> path) {
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

    private void compare(JsonArray self, JsonArray other, ArrayList<String> path) {
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
