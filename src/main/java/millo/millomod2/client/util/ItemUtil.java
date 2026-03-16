package millo.millomod2.client.util;

import com.mojang.brigadier.StringReader;
import com.mojang.serialization.DataResult;
import millo.millomod2.client.MilloMod;
import net.minecraft.command.argument.ItemStackArgument;
import net.minecraft.command.argument.ItemStringReader;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;

import java.util.HashMap;
import java.util.Map;

public class ItemUtil {

    public static NbtCompound getPBV(ItemStack stack) {
        ComponentMap components = stack.getComponents();
        if (components == null) return null;

        NbtComponent custom_data = components.get(DataComponentTypes.CUSTOM_DATA);
        if (custom_data == null) return null;

        NbtCompound nbt = custom_data.copyNbt();
        return nbt.getCompound("PublicBukkitValues").orElse(null);
    }

    public static Map<String, Object> getItemTags(ItemStack item) {

        NbtCompound pbv = getPBV(item);
        if (pbv == null) return null;

        HashMap<String, Object> result = new HashMap<>();

        pbv.getKeys().forEach(key -> {
            Object value = pbv.get(key);
            result.put(key, value);
        });

        return result;

    }

    public static ItemStack fromNbt(String data) {
        if (MilloMod.MC.world == null) return ItemStack.EMPTY;

        try {
            NbtCompound nbt = StringNbtReader.readCompound(data);
            DataResult<ItemStack> result = ItemStack.CODEC.parse(MilloMod.MC.world.getRegistryManager().getOps(NbtOps.INSTANCE), nbt);
            return result.getOrThrow();
        } catch (Exception e) {
            try {
                ItemStringReader stringReader = new ItemStringReader(MilloMod.MC.world.getRegistryManager());
                ItemStringReader.ItemResult result = stringReader.consume(new StringReader(data));
                return new ItemStackArgument(result.item(), result.components()).createStack(1, false);
            } catch (Exception e2) {
                System.out.println("Error parsing item NBT: " + e2.getMessage());
            }
            System.out.println("Unexpected error parsing item NBT: " + e.getMessage());
            return ItemStack.EMPTY;
        }
    }


    public static String getPBVString(ItemStack stack, String key) {
        NbtCompound pbv = getPBV(stack);
        if (pbv == null) return null;

        if (!pbv.contains(key)) return null;

        return pbv.getString(key).orElse(null);
    }
}
