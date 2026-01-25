package millo.millomod2.menu.elements;

import millo.millomod2.client.MilloMod;
import millo.millomod2.client.util.PlayerUtil;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.GameMode;
import org.joml.Vector2f;

public class ItemStackElement extends ClickableElement<ItemStackElement> {

    private final ItemStack stack;
    private final boolean drawOverlay;
    private final boolean hasTooltip;

    public ItemStackElement(int x, int y, int width, int height, Text message, ItemStack stack, boolean drawOverlay, boolean hasTooltip) {
        super(x, y, width, height, message);

        this.stack = stack;
        this.drawOverlay = drawOverlay;
        this.hasTooltip = hasTooltip;
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {

        context.drawItem(stack, getX() - 4, getY() - 4, 0);
        if (drawOverlay) {
            context.drawStackOverlay(getTextRenderer(), stack, getX(), getY(), null);
        }

        if (isMouseOver(mouseX, mouseY)) {
            var pos = context.getMatrices().transformPosition(mouseX, mouseY, new Vector2f());
            context.drawItemTooltip(getTextRenderer(), stack, (int) pos.x, (int) pos.y);
        }
    }

    @Override
    public void onClick(Click click, boolean doubled) {
        ClientPlayerEntity player = MilloMod.player();
        if (player != null && player.getGameMode() == GameMode.CREATIVE) {
            PlayerUtil.giveItem(stack);
        }
    }
}
