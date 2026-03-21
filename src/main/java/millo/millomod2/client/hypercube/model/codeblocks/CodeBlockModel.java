package millo.millomod2.client.hypercube.model.codeblocks;

import com.google.gson.JsonObject;
import millo.millomod2.client.hypercube.model.JsonSerializable;

import java.util.HashMap;
import java.util.function.Supplier;

public abstract class CodeBlockModel<T extends CodeBlockModel<T>> implements JsonSerializable<T> {

    abstract T self();
    public abstract String id();

    @Override
    public JsonObject serialize() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", id());
        return jsonObject;
    }

    ///

    private static final HashMap<String, Supplier<CodeBlockModel<?>>> codeblocks = new HashMap<>();
    static {
        register(BlockCodeBlockModel::new);
        register(BracketCodeBlockModel::new);
    }

    private static void register(Supplier<CodeBlockModel<?>> supplier) {
        CodeBlockModel<?> codeBlockModel = supplier.get();
        codeblocks.put(codeBlockModel.id(), supplier);
    }

    public static CodeBlockModel<?> deserializeCodeBlock(JsonObject jsonObject) {
        String id = jsonObject.get("id").getAsString();
        Supplier<CodeBlockModel<?>> supplier = codeblocks.get(id);
        if (supplier == null) throw new IllegalArgumentException("Unknown code block id: " + id);
        return supplier.get().deserialize(jsonObject);
    }

}
