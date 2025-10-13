package millo.millomod2.client.features.impl;

import millo.millomod2.client.MilloMod;
import millo.millomod2.client.features.Feature;
import millo.millomod2.client.features.addons.Keybound;
import millo.millomod2.client.features.addons.OnReceivePacket;
import millo.millomod2.client.util.HypercubeInfo;
import millo.millomod2.client.util.MilloLog;
import millo.millomod2.client.util.PlayerUtil;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

import java.util.Iterator;


public class PickChestValue extends Feature implements Keybound {

    private boolean requested = false;

    @Override
    public String getId() {
        return "pick_chest_value";
    }

    @OnReceivePacket
    public boolean receive(ScreenHandlerSlotUpdateS2CPacket packet) {
        if (MilloMod.net() == null) return false;
        if (!requested) return false;

        ItemStack stack = packet.getStack();
        ContainerComponent container = stack.get(DataComponentTypes.CONTAINER);
        if (container == null) return false;

        requested = false;

        Iterator<ItemStack> itemIterator = container.iterateNonEmpty().iterator();
        if (!itemIterator.hasNext()) return true;
        ItemStack item = itemIterator.next();

        MilloMod.schedule(() -> PlayerUtil.setInventorySlot(packet.getSlot(), item), 50);

        return true;
    }

    @Override
    public void onTick() {
        while (getKeybind().wasPressed()) {
            ClientPlayerEntity player = player();
            MinecraftClient mc = MilloMod.MC;
            if (HypercubeInfo.getMode() != HypercubeInfo.Mode.DEV) return;
            if (mc.world == null || player == null || MilloMod.net() == null) return;
            if (!(MilloMod.MC.crosshairTarget instanceof BlockHitResult block)) return;
            if (block.getType() != HitResult.Type.BLOCK) return;

            BlockPos pos = block.getBlockPos();
            if (!(mc.world.getBlockEntity(pos) instanceof ChestBlockEntity)) return;

            if (MilloMod.net() == null) return;
            requested = true;

            if (MilloMod.MC.interactionManager == null) return;

            MilloMod.MC.interactionManager.pickItemFromBlock(pos, true);
        }
    }
}
