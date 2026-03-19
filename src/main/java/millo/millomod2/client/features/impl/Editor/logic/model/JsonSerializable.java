package millo.millomod2.client.features.impl.Editor.logic.model;

import com.google.gson.JsonObject;

public interface JsonSerializable<T> {
    T deserialize(JsonObject jsonObject);
    JsonObject serialize();
}
