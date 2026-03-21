package millo.millomod2.client.hypercube.model.arguments;

import com.google.gson.JsonObject;

public class HintArgumentModel extends ArgumentModel<HintArgumentModel> {

    private String id;

    @Override
    public HintArgumentModel self() {
        return this;
    }

    @Override
    public String id() {
        return "hint";
    }

    @Override
    protected void deserializeItem(JsonObject jsonObject) {
            id = jsonObject.get("id").getAsString();
    }

    @Override
    protected void serializeItem(JsonObject jsonObject) {
        jsonObject.addProperty("id", id);
    }

}
