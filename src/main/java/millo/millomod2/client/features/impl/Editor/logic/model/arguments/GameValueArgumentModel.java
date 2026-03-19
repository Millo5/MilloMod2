package millo.millomod2.client.features.impl.Editor.logic.model.arguments;

import com.google.gson.JsonObject;

public class GameValueArgumentModel extends ArgumentModel<GameValueArgumentModel> {

    private String type;
    private String target;

    @Override
    public GameValueArgumentModel self() {
        return this;
    }

    @Override
    public String id() {
        return "g_val";
    }

    @Override
    protected void deserializeItem(JsonObject jsonObject) {
        type = jsonObject.get("type").getAsString();
        target = jsonObject.get("target").getAsString();
    }

    @Override
    protected void serializeItem(JsonObject jsonObject) {
        jsonObject.addProperty("type", type);
        jsonObject.addProperty("target", target);
    }
}
