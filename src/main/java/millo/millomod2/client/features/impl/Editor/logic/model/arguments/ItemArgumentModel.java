package millo.millomod2.client.features.impl.Editor.logic.model.arguments;

import com.google.gson.JsonObject;

public class ItemArgumentModel extends ArgumentModel<ItemArgumentModel> {

    private String value;

    @Override
    public ItemArgumentModel self() {
        return this;
    }

    @Override
    public String id() {
        return "item";
    }

    @Override
    protected void deserializeItem(JsonObject jsonObject) {
        value = jsonObject.get("item").getAsString();
    }

    @Override
    protected void serializeItem(JsonObject jsonObject) {
        jsonObject.addProperty("item", value);
    }
}
