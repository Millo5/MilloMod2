package millo.millomod2.client.features.impl.Editor.logic.model.codeblocks;

import com.google.gson.JsonObject;

public class BracketCodeBlockModel extends CodeBlockModel<BracketCodeBlockModel> {

    private BracketDirection direction;
    private BracketType type;


    @Override
    BracketCodeBlockModel self() {
        return this;
    }

    @Override
    public String id() {
        return "bracket";
    }

    @Override
    public BracketCodeBlockModel deserialize(JsonObject jsonObject) {
        this.direction = BracketDirection.valueOf(jsonObject.get("direct").getAsString().toUpperCase());
        this.type = BracketType.valueOf(jsonObject.get("type").getAsString().toUpperCase());
        return this;
    }

    @Override
    public JsonObject serialize() {
        JsonObject jsonObject = super.serialize();
        jsonObject.addProperty("direct", direction.name().toLowerCase());
        jsonObject.addProperty("type", type.name().toLowerCase());
        return jsonObject;
    }

    public enum BracketDirection {
        OPEN, CLOSE
    }

    public enum BracketType {
        NORM, REPEAT
    }


}
