package millo.millomod2.client.features.impl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import millo.millomod2.client.features.Feature;
import millo.millomod2.client.features.addons.ChatSendInjector;
import millo.millomod2.client.features.addons.Toggleable;
import millo.millomod2.client.util.ItemUtil;
import millo.millomod2.client.util.PlayerUtil;
import net.minecraft.item.ItemStack;

public class ParticleColorShorthand extends Feature implements Toggleable, ChatSendInjector {

    @Override
    public String getId() {
        return "particle_color_shorthand";
    }

    @Override
    public boolean onSendMessage(String msg) {
        if (!isEnabled() || player() == null) return false;

        ItemStack heldItem = player().getMainHandStack();
        String varitem = ItemUtil.getPBVString(heldItem, "hypercube:varitem");
        if (varitem == null) return false;

        var json = JsonParser.parseString(varitem);
        JsonObject obj = json.getAsJsonObject();
        String id = obj.get("id").getAsString();
        if (!id.equals("part")) return false;

        if (msg.matches("^<?#[0-9a-fA-F]{6}>?$")) {
            msg = msg.replaceAll("[<>]", "");
            PlayerUtil.sendCommand("par color " + msg);
            return true;
        }

        if (!msg.matches("^f<?#[0-9a-fA-F]{6}>?$")) return false;
        msg = msg.replaceAll("[<>]", "").substring(1);
        PlayerUtil.sendCommand("par fade " + msg);

        return true;
    }
}
