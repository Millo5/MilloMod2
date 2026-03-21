package millo.millomod2.client.hypercube.model.arguments;

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

    public String getValue() {
        return value;
    }
}
