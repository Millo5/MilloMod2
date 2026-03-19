package millo.millomod2.client.features.impl.Editor.logic.model.arguments;

import com.google.gson.JsonObject;
import millo.millomod2.client.features.impl.Editor.logic.model.JsonSerializable;
import millo.millomod2.client.util.MilloLog;

import java.util.HashMap;
import java.util.function.Supplier;

public abstract class ArgumentModel<T extends ArgumentModel<T>> implements JsonSerializable<T> {

    private int slot;

    public abstract T self();
    public abstract String id();

    @Override
    public final JsonObject serialize() {
        JsonObject jsonObject = new JsonObject();
        if (slot >= 0) jsonObject.addProperty("slot", slot);

        JsonObject itemObject = new JsonObject();
        itemObject.addProperty("id", id());
        JsonObject dataObject = new JsonObject();
        serializeItem(dataObject);
        itemObject.add("data", dataObject);
        jsonObject.add("item", itemObject);

        return jsonObject;
    }

    @Override
    public final T deserialize(JsonObject jsonObject) {
        this.slot = jsonObject.get("slot").getAsInt();
        deserializeItem(jsonObject.getAsJsonObject("item").getAsJsonObject("data"));
        return self();
    }

    abstract protected void serializeItem(JsonObject jsonObject);
    abstract protected void deserializeItem(JsonObject jsonObject);


    public void setSlot(int slot) {
        this.slot = slot;
    }

    public int getSlot() {
        return slot;
    }

    ///

    private static final HashMap<String, Supplier<ArgumentModel<?>>> arguments = new HashMap<>();
    static {
        register(BlockTagArgumentModel::new);
        register(ComponentArgumentModel::new);
        register(GameValueArgumentModel::new);
        register(HintArgumentModel::new);
        register(ItemArgumentModel::new);
        register(LocationArgumentModel::new);
        register(NumberArgumentModel::new);
        register(ParameterArgumentModel::new);
        register(ParticleArgumentModel::new);
        register(PotionArgumentModel::new);
        register(SoundArgumentModel::new);
        register(TextArgumentModel::new);
        register(VariableArgumentModel::new);
        register(VectorArgumentModel::new);
    }

    private static void register(Supplier<ArgumentModel<?>> supp) {
        ArgumentModel<?> arg = supp.get();
        arguments.put(arg.id(), supp);
    }

    public static ArgumentModel<?> deserializeArgument(JsonObject jsonObject) {
        String id = jsonObject.getAsJsonObject("item").get("id").getAsString();
        Supplier<ArgumentModel<?>> supp = arguments.get(id);
        if (supp == null) throw MilloLog.throwError("Unknown argument id: " + id);
        return supp.get().deserialize(jsonObject);
    }

    public static ArgumentModel<?> deserializeDefaultArgument(JsonObject jsonObject) {
        String id = jsonObject.get("id").getAsString();
        Supplier<ArgumentModel<?>> supp = arguments.get(id);
        if (supp == null) throw MilloLog.throwError("Unknown argument id: " + id);
        ArgumentModel<? extends ArgumentModel<?>> arg = supp.get();
        arg.deserializeItem(jsonObject.getAsJsonObject("data"));
        arg.setSlot(-1);
        return arg;
    }

}
