package millo.millomod2.client.hypercube.model.codefields;

import com.google.gson.JsonObject;

public abstract class CodeFields {

    abstract public void serializeOn(JsonObject jsonObject);

    public static CodeFields deserialize(JsonObject jsonObject) {
        if (jsonObject.has("data")) {
            return new DynamicCodeFields(jsonObject.get("data").getAsString());
        }
        if (jsonObject.has("action")) {
            String action = jsonObject.get("action").getAsString();
            String attribute = jsonObject.has("attribute") ? jsonObject.get("attribute").getAsString() : null;
            String target = jsonObject.has("target") ? jsonObject.get("target").getAsString() : null;
            if (jsonObject.has("subAction")) {
                String subAction = jsonObject.get("subAction").getAsString();
                return new SubActionCodeFields(action, target, attribute, subAction);
            }
            return new ActionCodeFields(action, target, attribute);
        }
        return new EmptyCodeFields();
    }
}
