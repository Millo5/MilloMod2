package millo.millomod2.client.features.impl;

import millo.millomod2.client.MilloMod;
import millo.millomod2.client.features.Feature;
import millo.millomod2.client.features.addons.ContainerMod;
import millo.millomod2.client.features.addons.Keybound;
import millo.millomod2.client.util.KeyUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class SlotIndex extends Feature implements Keybound, ContainerMod {

    @Override
    public String getId() {
        return "slot_index";
    }

    @Override
    public void containerDrawSlot(DrawContext context, Slot slot, int mouseX, int mouseY, CallbackInfo ci) {
        if (!KeyUtil.isKeyDown(getKeybind())) return;

        context.fill(slot.x, slot.y, slot.x + 16, slot.y + 16, 0xa0000000);
        context.drawCenteredTextWithShadow(MilloMod.MC.textRenderer, String.valueOf(slot.getIndex() + 1), slot.x + 8, slot.y + 4, 0xFFFFFFFF);
    }
}
