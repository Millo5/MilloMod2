package millo.millomod2.client.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.brigadier.StringReader;
import com.mojang.serialization.DataResult;
import millo.millomod2.client.MilloMod;
import net.minecraft.commands.arguments.item.ItemInput;
import net.minecraft.commands.arguments.item.ItemParser;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.TagParser;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemLore;
import java.util.HashMap;
import java.util.Map;

public class ItemUtil {

    public static CompoundTag getPBV(ItemStack stack) {
        DataComponentMap components = stack.getComponents();
        if (components == null) return null;

        CustomData custom_data = components.get(DataComponents.CUSTOM_DATA);
        if (custom_data == null) return null;

        CompoundTag nbt = custom_data.copyTag();
        return nbt.getCompound("PublicBukkitValues").orElse(null);
    }

    public static Map<String, Object> getItemTags(ItemStack item) {

        CompoundTag pbv = getPBV(item);
        if (pbv == null) return null;

        HashMap<String, Object> result = new HashMap<>();

        pbv.keySet().forEach(key -> {
            Object value = pbv.get(key);
            result.put(key, value);
        });

        return result;

    }

    public static ItemStack fromNbt(String data) {
        if (MilloMod.MC.level == null) return ItemStack.EMPTY;

        try {
            CompoundTag nbt = TagParser.parseCompoundFully(data);
            DataResult<ItemStack> result = ItemStack.CODEC.parse(MilloMod.MC.level.registryAccess().createSerializationContext(NbtOps.INSTANCE), nbt);
            return result.getOrThrow();
        } catch (Exception e) {
            try {
                ItemParser stringReader = new ItemParser(MilloMod.MC.level.registryAccess());
                ItemParser.ItemResult result = stringReader.parse(new StringReader(data));
                return new ItemInput(result.item(), result.components()).createItemStack(1, false);
            } catch (Exception e2) {
                System.out.println("Error parsing item NBT: " + e2.getMessage());
            }
            System.out.println("Unexpected error parsing item NBT: " + e.getMessage());
            return ItemStack.EMPTY;
        }
    }


    public static String getPBVString(ItemStack stack, String key) {
        CompoundTag pbv = getPBV(stack);
        if (pbv == null) return null;

        if (!pbv.contains(key)) return null;

        return pbv.getString(key).orElse(null);
    }

    public static ItemLore getLore(ItemStack stack) {
        return stack.get(DataComponents.LORE);
    }

    public static String getItemTagAsString(ItemStack stack, String tag) {
        return (String) getItemTags(stack).get(tag);
    }

    public static JsonObject getVarItem(ItemStack stack) {
        String varitem = ItemUtil.getPBVString(stack, "hypercube:varitem");
        if (varitem == null) return null;

        return JsonParser.parseString(varitem).getAsJsonObject();
    }

    public static void setVarItem(ItemStack stack, JsonObject varItem) {
        DataComponentMap components = stack.getComponents();
        if (components == null) return;

        CustomData custom_data = components.get(DataComponents.CUSTOM_DATA);
        if (custom_data == null) return;

        CompoundTag nbt = custom_data.copyTag();
        CompoundTag pbv = nbt.getCompound("PublicBukkitValues").orElse(null);
        if (pbv == null) return;

        pbv.putString("hypercube:varitem", varItem.toString());
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(nbt));
    }
}
