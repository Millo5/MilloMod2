package millo.millomod2.client.features.impl.Editor.logic.model.arguments;

import com.google.gson.JsonObject;

public class BlockTagArgumentModel extends ArgumentModel<BlockTagArgumentModel> {

    private String option, tag, action, block;

    @Override
    public BlockTagArgumentModel self() {
        return this;
    }

    @Override
    public String id() {
        return "bl_tag";
    }

    @Override
    protected void deserializeItem(JsonObject jsonObject) {
        this.option = jsonObject.get("option").getAsString();
        this.tag = jsonObject.get("tag").getAsString();
        this.action = jsonObject.get("action").getAsString();
        this.block = jsonObject.get("block").getAsString();
    }

    @Override
    protected void serializeItem(JsonObject jsonObject) {
        jsonObject.addProperty("option", option);
        jsonObject.addProperty("tag", tag);
        jsonObject.addProperty("action", action);
        jsonObject.addProperty("block", block);
    }

}
