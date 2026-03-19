package millo.millomod2.client.features.impl.Editor.logic.model.arguments;

import com.google.gson.JsonObject;
import millo.millomod2.client.hypercube.data.VariableScope;

public class VariableArgumentModel extends ArgumentModel<VariableArgumentModel> {

    private VariableScope scope;
    private String name;


    @Override
    public VariableArgumentModel self() {
        return this;
    }

    @Override
    public String id() {
        return "var";
    }

    @Override
    protected void deserializeItem(JsonObject jsonObject) {
        scope = VariableScope.valueOf(jsonObject.get("scope").getAsString().toUpperCase());
        name = jsonObject.get("name").getAsString();
    }

    @Override
    protected void serializeItem(JsonObject jsonObject) {
        jsonObject.addProperty("scope", scope.name().toLowerCase());
        jsonObject.addProperty("name", name);
    }
}
