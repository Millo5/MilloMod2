package millo.millomod2.client.hypercube.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import millo.millomod2.client.hypercube.model.codeblocks.BlockCodeBlockModel;
import millo.millomod2.client.hypercube.model.codeblocks.CodeBlockModel;
import millo.millomod2.client.hypercube.model.codefields.ActionCodeFields;
import millo.millomod2.client.hypercube.model.codefields.DynamicCodeFields;
import millo.millomod2.client.hypercube.template.MethodType;
import millo.millomod2.client.util.MilloLog;
import net.minecraft.item.ItemStack;

import java.io.IOException;
import java.util.ArrayList;

public class TemplateModel implements JsonSerializable<TemplateModel> {

    private ArrayList<CodeBlockModel<?>> blocks;

    @Override
    public TemplateModel deserialize(JsonObject jsonObject) {
        blocks = new ArrayList<>();
        for (JsonElement jsonElement : jsonObject.get("blocks").getAsJsonArray()) {
            blocks.add(CodeBlockModel.deserializeCodeBlock(jsonElement.getAsJsonObject()));
        }
        return this;
    }

    @Override
    public JsonObject serialize() {
        JsonObject root = new JsonObject();
        JsonArray blocksJson = new JsonArray();
        for (CodeBlockModel<?> block : blocks) {
            blocksJson.add(block.serialize());
        }
        root.add("blocks", blocksJson);
        return root;
    }

    //

    public String getName() {
        if (blocks == null || blocks.isEmpty()) return "Empty Template";
        if (!(blocks.getFirst() instanceof BlockCodeBlockModel block)) return "Empty Template";
        if (block.getCodeFields() instanceof DynamicCodeFields dyn) {
            String name = dyn.getData();
            if (name.isEmpty()) return block.getBlock() + "_empty";
            return name;
        }
        if (block.getCodeFields() instanceof ActionCodeFields ac) {
            return ac.getAction();
        }
        return block.getBlock();
    }

    public MethodType getMethodType() {
        try {
            return MethodType.valueOf(((BlockCodeBlockModel) blocks.getFirst()).getBlock().toUpperCase());
        } catch (Exception e) {
            MilloLog.logWarning("Failed to get method type for template " + getName() + ", defaulting to FUNC");
            return MethodType.FUNC;
        }
    }

    public String getFileName() {
        return getMethodType().suffixString(getName());
    }

    public ItemStack getItem() {
        try {
            return ModelUtil.createTemplateItem(ModelUtil.compress(serialize().toString()));
        } catch (IOException e) {
            throw MilloLog.throwError("Failed to create template item: " + e.getMessage());
        }
    }

    public ArrayList<CodeBlockModel<?>> getBlocks() {
        return blocks;
    }
}
