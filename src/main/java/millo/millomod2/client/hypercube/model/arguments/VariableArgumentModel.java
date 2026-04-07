package millo.millomod2.client.hypercube.model.arguments;

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

    public VariableScope getScope() {
        return scope;
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setScope(VariableScope scope) {
        this.scope = scope;
    }

}
