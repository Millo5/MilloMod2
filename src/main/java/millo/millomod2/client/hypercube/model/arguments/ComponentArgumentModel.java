package millo.millomod2.client.hypercube.model.arguments;

import com.google.gson.JsonObject;

public class ComponentArgumentModel extends ArgumentModel<ComponentArgumentModel> {

    private String value;

    @Override
    public ComponentArgumentModel self() {
        return this;
    }

    @Override
    public String id() {
        return "comp";
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
