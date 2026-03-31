package millo.millomod2.client.features.impl;

import millo.millomod2.client.MilloMod;
import millo.millomod2.client.config.FeatureConfig;
import millo.millomod2.client.features.Feature;
import millo.millomod2.client.features.addons.Configurable;
import millo.millomod2.client.features.addons.ContainerMod;
import millo.millomod2.client.features.addons.OnReceivePacket;
import millo.millomod2.client.features.addons.Toggleable;
import millo.millomod2.client.hypercube.data.ValueType;
import millo.millomod2.client.mixin.render.accessors.HandledScreenAccessor;
import millo.millomod2.client.util.ItemUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtString;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import org.joml.Vector2f;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ArgumentDisplay extends Feature implements Toggleable, Configurable, ContainerMod {

    private final ArrayList<ArgumentInfo> arguments = new ArrayList<>();

    @Override
    public String getId() {
        return "argument_display";
    }

    @Override
    public void setupConfig(FeatureConfig config) {
        config.addBoolean("show_icons", true);
        config.addBoolean("show_text", true);
    }

    @OnReceivePacket
    public boolean onReceivePacket(ScreenHandlerSlotUpdateS2CPacket packet) {
        if (!isEnabled()) return false;

        if (packet.getStack().getItem() == Items.WRITTEN_BOOK) {
            Map<String, Object> tags = ItemUtil.getItemTags(packet.getStack());
            if (tags == null) return false;
            if (!tags.containsKey("hypercube:item_instance")) return false;
            Object tag = tags.get("hypercube:item_instance");
            if (!(tag instanceof NbtString tagStr)) return false;
            if (tagStr.asString().isEmpty()) return false;
            if (!tagStr.asString().get().equals("reference_book")) return false;

            arguments.clear();
            ArgumentInfo lastArg = null;

            LoreComponent lore = ItemUtil.getLore(packet.getStack());
            List<Text> lines = lore.styledLines();
            for (Text line : lines) {
                String str = line.getString();
                if (str.equals("Returns Value:")) break;

                int colonIndex = str.indexOf(" - ");
                if (!str.isEmpty() && str.charAt(0) != '⏵' && colonIndex != -1) {
                    String typeStr = str.substring(0, colonIndex);
                    ValueType type = ValueType.fromString(typeStr);
                    lastArg = new ArgumentInfo();
                    lastArg.name = line;
                    lastArg.type = type;
                    arguments.add(lastArg);
                    continue;
                }

                if (str.isBlank() || lastArg == null) continue;
                lastArg.name = lastArg.name.copy().append("\n").append(line);
            }
        }
        return false;
    }

    @Override
    public void containerDrawSlot(DrawContext context, Slot slot, int mouseX, int mouseY, CallbackInfo ci) {
        if (!isEnabled()) return;
        if (slot.getIndex() >= arguments.size()) return;
        if (slot.inventory instanceof PlayerInventory) return;
        if (!(MilloMod.MC.currentScreen instanceof HandledScreen<?> handledScreen)) return;
        HandledScreenAccessor container = (HandledScreenAccessor) handledScreen;

        if (slot.getStack().isEmpty() && config.getBoolean("show_icons")) {
            context.drawItem(arguments.get(slot.getIndex()).type.getIcon(), slot.x , slot.y);
            context.fill(slot.x, slot.y, slot.x + 16, slot.y + 16, 0x50000000);
        }

        if (!config.getBoolean("show_text")) return;

        Vector2f pos = context.getMatrices().transformPosition(slot.x, slot.y, new Vector2f());
        boolean mouseOver = mouseX >= pos.x && mouseX <= pos.x + 16 && mouseY >= pos.y && mouseY <= pos.y + 16;
        if (!mouseOver) return;

        Text name = arguments.get(slot.getIndex()).name;
        int height = MilloMod.MC.textRenderer.getWrappedLinesHeight(name, container.getBackgroundWidth());

        context.drawWrappedText(MilloMod.MC.textRenderer, name, 0, -height, container.getBackgroundWidth(), 0xFFFFFFFF, true);
    }

    private static class ArgumentInfo {
        public Text name;
        public ValueType type;
    }

}
