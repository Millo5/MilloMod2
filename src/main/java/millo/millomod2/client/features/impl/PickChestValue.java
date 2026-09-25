package millo.millomod2.client.features.impl;

import millo.millomod2.client.MilloMod;
import millo.millomod2.client.features.Feature;
import millo.millomod2.client.features.PacketEventBus;
import millo.millomod2.client.features.addons.Keybound;
import millo.millomod2.client.features.addons.PacketEventSubscriber;
import millo.millomod2.client.util.HypercubeAPI;
import millo.millomod2.client.util.PlayerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import java.util.Iterator;


public class PickChestValue extends Feature implements Keybound, PacketEventSubscriber {

    private boolean requested = false;

    @Override
    public String getId() {
        return "pick_chest_value";
    }

    @Override
    public void subscribePackets(PacketEventBus eventBus) {
        eventBus.subscribeReceive(ClientboundContainerSetSlotPacket.class, this::receive);
    }

    public boolean receive(ClientboundContainerSetSlotPacket packet) {
        if (MilloMod.net() == null) return false;
        if (!requested) return false;

        ItemStack stack = packet.getItem();
        ItemContainerContents container = stack.get(DataComponents.CONTAINER);
        if (container == null) return false;

        requested = false;

        Iterator<ItemStack> itemIterator = container.nonEmptyItems().iterator();
        if (!itemIterator.hasNext()) return true;
        ItemStack item = itemIterator.next();

        MilloMod.schedule(() -> PlayerUtil.setInventorySlot(packet.getSlot(), item), 50);

        return true;
    }

    @Override
    public void onTick() {
        while (getKeybind().consumeClick()) {
            LocalPlayer player = player();
            Minecraft mc = MilloMod.MC;
            if (HypercubeAPI.getMode() != HypercubeAPI.Mode.DEV) return;
            if (mc.level == null || player == null || MilloMod.net() == null) return;
            if (!(MilloMod.MC.hitResult instanceof BlockHitResult block)) return;
            if (block.getType() != HitResult.Type.BLOCK) return;

            BlockPos pos = block.getBlockPos();
            if (!(mc.level.getBlockEntity(pos) instanceof ChestBlockEntity)) return;

            if (MilloMod.net() == null) return;
            requested = true;

            if (MilloMod.MC.gameMode == null) return;

            MilloMod.MC.gameMode.handlePickItemFromBlock(pos, true);
        }
    }
}
