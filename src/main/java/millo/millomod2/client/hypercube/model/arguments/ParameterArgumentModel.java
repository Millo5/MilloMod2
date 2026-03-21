package millo.millomod2.client.hypercube.model.arguments;

import com.google.gson.JsonObject;

public class ParameterArgumentModel extends ArgumentModel<ParameterArgumentModel> {

    private String name;
    private String type;
    private boolean plural;
    private boolean optional;
    private ArgumentModel<?> defaultValue;

    private String description;
    private String note;

    @Override
    public ParameterArgumentModel self() {
        return this;
    }

    @Override
    public String id() {
        return "pn_el";
    }

    @Override
    protected void deserializeItem(JsonObject jsonObject) {
        name = jsonObject.get("name").getAsString();
        type = jsonObject.get("type").getAsString();
        plural = jsonObject.get("plural").getAsBoolean();
        optional = jsonObject.get("optional").getAsBoolean();
        if (jsonObject.has("default_value")) {
            defaultValue = ArgumentModel.deserializeDefaultArgument(jsonObject.getAsJsonObject("default_value"));
        }
        description = jsonObject.has("description") ? jsonObject.get("description").getAsString() : null;
        note = jsonObject.has("note") ? jsonObject.get("note").getAsString() : null;
    }

    @Override
    protected void serializeItem(JsonObject jsonObject) {
        jsonObject.addProperty("name", name);
        jsonObject.addProperty("type", type);
        jsonObject.addProperty("plural", plural);
        jsonObject.addProperty("optional", optional);

        if (defaultValue != null) {
            JsonObject defaultValueJson = new JsonObject();
            defaultValueJson.addProperty("id", defaultValue.id());
            JsonObject dataObject = new JsonObject();
            defaultValue.serializeItem(dataObject);
            defaultValueJson.add("data", dataObject);
            jsonObject.add("default_value", defaultValueJson);
        }

        if (description != null) jsonObject.addProperty("description", description);
        if (note != null) jsonObject.addProperty("note", note);
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public ArgumentModel<?> getDefaultValue() {
        return defaultValue;
    }

    public String getDescription() {
        return description;
    }

    public String getNote() {
        return note;
    }

    public boolean isOptional() {
        return optional;
    }

    public boolean isPlural() {
        return plural;
    }
}
