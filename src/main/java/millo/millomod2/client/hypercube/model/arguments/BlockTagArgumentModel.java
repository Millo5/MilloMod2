package millo.millomod2.client.hypercube.model.arguments;

import com.google.gson.JsonObject;

public class BlockTagArgumentModel extends ArgumentModel<BlockTagArgumentModel> {

    private String option, tag, action, block;
    private VariableArgumentModel variable;

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
        if (jsonObject.has("variable")) {
            variable = (VariableArgumentModel) ArgumentModel.deserializeDefaultArgument(jsonObject.getAsJsonObject("variable"));
        }
    }

    @Override
    protected void serializeItem(JsonObject jsonObject) {
        jsonObject.addProperty("option", option);
        jsonObject.addProperty("tag", tag);
        jsonObject.addProperty("action", action);
        jsonObject.addProperty("block", block);
        if (variable != null) {
            JsonObject defaultValueJson = new JsonObject();
            defaultValueJson.addProperty("id", variable.id());
            JsonObject dataObject = new JsonObject();
            variable.serializeItem(dataObject);
            defaultValueJson.add("data", dataObject);
            jsonObject.add("variable", defaultValueJson);
        }
    }


    public String getOption() {
        return option;
    }

    public String getTag() {
        return tag;
    }

    public String getAction() {
        return action;
    }

    public String getBlock() {
        return block;
    }

    public VariableArgumentModel getVariable() {
        return variable;
    }
}
