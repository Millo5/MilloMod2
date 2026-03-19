package millo.millomod2.client.features.impl.Editor.logic.model.arguments;

import com.google.gson.JsonObject;

public class TextArgumentModel extends ArgumentModel<TextArgumentModel> {

    private String value;

    @Override
    public TextArgumentModel self() {
        return this;
    }

    @Override
    public String id() {
        return "txt";
    }

    @Override
    protected void deserializeItem(JsonObject jsonObject) {
        value = jsonObject.get("name").getAsString();
    }

    @Override
    protected void serializeItem(JsonObject jsonObject) {
        jsonObject.addProperty("name", value);
    }
}
