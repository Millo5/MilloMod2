package millo.millomod2.client.features.impl;

import millo.millomod2.client.MilloMod;
import millo.millomod2.client.config.FeatureConfig;
import millo.millomod2.client.features.Feature;
import millo.millomod2.client.features.PacketEventBus;
import millo.millomod2.client.features.addons.Configurable;
import millo.millomod2.client.features.addons.ContainerMod;
import millo.millomod2.client.features.addons.PacketEventSubscriber;
import millo.millomod2.client.features.addons.Toggleable;
import millo.millomod2.client.hypercube.data.ValueType;
import millo.millomod2.client.mixin.render.accessors.HandledScreenAccessor;
import millo.millomod2.client.util.ItemUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemLore;
import org.joml.Vector2f;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ArgumentDisplay extends Feature implements Toggleable, Configurable, ContainerMod, PacketEventSubscriber {

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

    @Override
    public void subscribePackets(PacketEventBus eventBus) {
        eventBus.subscribeReceive(ClientboundContainerSetSlotPacket.class, this::onReceivePacket);
    }

    public boolean onReceivePacket(ClientboundContainerSetSlotPacket packet) {
        if (!isEnabled()) return false;

        if (packet.getItem().getItem() == Items.WRITTEN_BOOK) {
            Map<String, Object> tags = ItemUtil.getItemTags(packet.getItem());
            if (tags == null) return false;
            if (!tags.containsKey("hypercube:item_instance")) return false;
            Object tag = tags.get("hypercube:item_instance");
            if (!(tag instanceof StringTag tagStr)) return false;
            if (tagStr.asString().isEmpty()) return false;
            if (!tagStr.asString().get().equals("reference_book")) return false;

            arguments.clear();
            ArgumentInfo lastArg = null;

            ItemLore lore = ItemUtil.getLore(packet.getItem());
            List<Component> lines = lore.styledLines();
            for (Component line : lines) {
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
    public void containerDrawSlot(GuiGraphics context, Slot slot, int mouseX, int mouseY, CallbackInfo ci) {
        if (!isEnabled()) return;
        if (slot.container instanceof Inventory) return;
        if (!(MilloMod.MC.screen instanceof AbstractContainerScreen<?> handledScreen)) return;

        List<ArgumentInfo> arguments = List.copyOf(this.arguments);

        int idx = slot.getContainerSlot();
        if (idx >= arguments.size()) return;
        HandledScreenAccessor container = (HandledScreenAccessor) handledScreen;

        if (slot.getItem().isEmpty() && config.getBoolean("show_icons")) {
            context.renderItem(arguments.get(idx).type.getIcon(), slot.x , slot.y);
            context.fill(slot.x, slot.y, slot.x + 16, slot.y + 16, 0x50000000);
        }

        if (!config.getBoolean("show_text")) return;

        Vector2f pos = context.pose().transformPosition(slot.x, slot.y, new Vector2f());
        boolean mouseOver = mouseX >= pos.x && mouseX <= pos.x + 16 && mouseY >= pos.y && mouseY <= pos.y + 16;
        if (!mouseOver) return;

        Component name = arguments.get(slot.getContainerSlot()).name;
        int height = MilloMod.MC.font.wordWrapHeight(name, container.getBackgroundWidth());

        context.drawWordWrap(MilloMod.MC.font, name, 0, -height, container.getBackgroundWidth(), 0xFFFFFFFF, true);
    }

    private static class ArgumentInfo {
        public Component name;
        public ValueType type;
    }

}
