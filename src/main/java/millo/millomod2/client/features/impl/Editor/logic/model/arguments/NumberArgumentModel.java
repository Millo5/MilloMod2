package millo.millomod2.client.features.impl.Editor.logic.model.arguments;

import com.google.gson.JsonObject;

public class NumberArgumentModel extends ArgumentModel<NumberArgumentModel> {

    // "%math() %var()" its a string not a number.
    private String value;

    @Override
    public NumberArgumentModel self() {
        return this;
    }

    @Override
    public String id() {
        return "num";
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
