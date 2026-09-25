package millo.millomod2.client.features.impl.Editor;

import millo.millomod2.client.MilloMod;
import millo.millomod2.client.hypercube.model.ModelUtil;
import millo.millomod2.client.hypercube.model.TemplateModel;
import millo.millomod2.client.util.ItemUtil;
import millo.millomod2.client.util.PlayerUtil;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.network.protocol.game.ServerboundSetCreativeModeSlotPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;

public class LegacyEditorSupport {

    private long lastRequest = 0;
    private final Editor editor;
    private TemplateCallback callback;

    public LegacyEditorSupport(Editor editor) {
        this.editor = editor;
    }

    public void getMethodFromPosition(BlockPos pos, LocalPlayer player, ClientPacketListener net, TemplateCallback callback) {
        if (MilloMod.MC.gameMode == null) return;
        if (System.currentTimeMillis() - lastRequest < 2000) return;
        this.callback = callback;

        lastRequest = System.currentTimeMillis();

        boolean sneaking = player.isShiftKeyDown();

        if (!sneaking) PlayerUtil.sendSneak(true);
        MilloMod.MC.gameMode.useItemOn(player, InteractionHand.MAIN_HAND, new BlockHitResult(
                pos.getCenter(), Direction.UP, pos, false
        ));
        if (!sneaking) PlayerUtil.sendSneak(false);
    }

    public boolean slotUpdate(ClientboundContainerSetSlotPacket packet) {
        var currentTime = System.currentTimeMillis();
        if (currentTime - lastRequest > 2000) return false;
        if (currentTime - lastRequest > 1950) return true;

        String codeTemplateData = ItemUtil.getPBVString(packet.getItem(), "hypercube:codetemplatedata");
        if (codeTemplateData == null) return false;
        lastRequest = currentTime - 1950;

        if (callback != null) callback.onReceive(ModelUtil.parseFromItemNBT(codeTemplateData));

        MilloMod.schedule(() -> MilloMod.net().send(new ServerboundSetCreativeModeSlotPacket(packet.getSlot(), ItemStack.EMPTY)), 50);

        return true;
    }

    public interface TemplateCallback {
        void onReceive(TemplateModel template);
    }
}
