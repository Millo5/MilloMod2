package millo.millomod2.client.hypercube.model.arguments;

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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
