package millo.millomod2.client.features.impl.Editor.logic.model.codefields;


import com.google.gson.JsonObject;

public class DynamicCodeFields extends CodeFields {

    private final String code;

    public DynamicCodeFields(String code) {
        this.code = code;
    }

    @Override
    public void serializeOn(JsonObject jsonObject) {
        jsonObject.addProperty("data", code);
    }
}
