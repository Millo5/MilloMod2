package millo.millomod2.menu.elements;

import millo.millomod2.client.MilloMod;
import millo.millomod2.client.util.PlayerUtil;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import org.joml.Vector2f;

public class ItemStackElement extends ClickableElement<ItemStackElement> {

    private final ItemStack stack;
    private final boolean drawOverlay;
    private final boolean hasTooltip;

    public ItemStackElement(int x, int y, int width, int height, Component message, ItemStack stack, boolean drawOverlay, boolean hasTooltip) {
        super(x, y, width, height, message);

        this.stack = stack;
        this.drawOverlay = drawOverlay;
        this.hasTooltip = hasTooltip;
    }

    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float deltaTicks) {

        graphics.item(stack, getX() - 4, getY() - 4, 0);
        if (drawOverlay) {
            graphics.itemDecorations(getTextRenderer(), stack, getX(), getY(), null);
        }

        if (isMouseOver(mouseX, mouseY)) {
            var pos = graphics.pose().transformPosition(mouseX, mouseY, new Vector2f());
            graphics.setTooltipForNextFrame(getTextRenderer(), stack, (int) pos.x, (int) pos.y);
        }
    }

    @Override
    public void onClick(MouseButtonEvent click, boolean doubled) {
        LocalPlayer player = MilloMod.player();
        if (player != null && player.gameMode() == GameType.CREATIVE) {
            PlayerUtil.giveItem(stack);
        }
    }
}
