package millo.millomod2.client.hypercube.model.codeblocks;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import millo.millomod2.client.hypercube.model.arguments.ArgumentModel;
import millo.millomod2.client.hypercube.model.codefields.CodeFields;

import java.util.ArrayList;

public class BlockCodeBlockModel extends CodeBlockModel<BlockCodeBlockModel> {

    private String block;
    private ArrayList<ArgumentModel<?>> args;
    private CodeFields codeFields;


    @Override
    BlockCodeBlockModel self() {
        return this;
    }

    @Override
    public String id() {
        return "block";
    }

    @Override
    public BlockCodeBlockModel deserialize(JsonObject jsonObject) {
        this.block = jsonObject.get("block").getAsString();

        if (jsonObject.has("args")) {
            this.args = new ArrayList<>();
            for (var arg : jsonObject.getAsJsonObject("args").getAsJsonArray("items")) {
                this.args.add(ArgumentModel.deserializeItemArgument(arg.getAsJsonObject()));
            }
        }
        this.codeFields = CodeFields.deserialize(jsonObject);
        return this;
    }

    @Override
    public JsonObject serialize() {
        JsonObject root = super.serialize();
        root.addProperty("block", block);
        if (args != null) {
            JsonObject argsJson = new JsonObject();
            JsonArray itemsJson = new JsonArray();
            for (ArgumentModel<?> arg : args) {
                itemsJson.add(arg.serialize());
            }
            argsJson.add("items", itemsJson);
            root.add("args", argsJson);
        }
        codeFields.serializeOn(root);
        return root;
    }


    public String getBlock() {
        return block;
    }

    public CodeFields getCodeFields() {
        return codeFields;
    }

    public ArrayList<ArgumentModel<?>> getArgs() {
        return args;
    }
}
