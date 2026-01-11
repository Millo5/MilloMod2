package millo.millomod2.client.features.impl.Editor;

import millo.millomod2.client.MilloMod;
import millo.millomod2.client.hypercube.template.Template;
import millo.millomod2.client.util.ItemUtil;
import millo.millomod2.client.util.PlayerUtil;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class LegacyEditorSupport {

    private long lastRequest = 0;
    private final Editor editor;
    private TemplateCallback callback;

    public LegacyEditorSupport(Editor editor) {
        this.editor = editor;
    }

    public void getMethodFromPosition(BlockPos pos, ClientPlayerEntity player, ClientPlayNetworkHandler net, TemplateCallback callback) {
        if (MilloMod.MC.interactionManager == null) return;
        if (System.currentTimeMillis() - lastRequest < 2000) return;
        this.callback = callback;

        lastRequest = System.currentTimeMillis();

        boolean sneaking = player.isSneaking();
        ItemStack item = player.getMainHandStack();

        PlayerUtil.sendHandItem(ItemStack.EMPTY);
        if (!sneaking) PlayerUtil.sendSneak(true);
        MilloMod.MC.interactionManager.interactBlock(player, Hand.MAIN_HAND, new BlockHitResult(
                pos.toCenterPos(), Direction.UP, pos, false
        ));
        if (!sneaking) PlayerUtil.sendSneak(false);
        PlayerUtil.sendHandItem(item);
    }

    public boolean slotUpdate(ScreenHandlerSlotUpdateS2CPacket packet) {
        if (System.currentTimeMillis() - lastRequest > 2000) return false;

        String codeTemplateData = ItemUtil.getPBVString(packet.getStack(), "hypercube:codetemplatedata");
        if (codeTemplateData == null) return false;
        lastRequest = 0;

        if (callback != null) callback.onReceive(Template.parseItemNBT(codeTemplateData));

        MilloMod.schedule(() -> MilloMod.net().sendPacket(new CreativeInventoryActionC2SPacket(packet.getSlot(), ItemStack.EMPTY)), 50);

        return true;
    }

    public interface TemplateCallback {
        void onReceive(Template template);
    }
}
