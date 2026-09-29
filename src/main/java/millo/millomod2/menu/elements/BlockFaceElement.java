package millo.millomod2.menu.elements;

import millo.millomod2.client.MilloMod;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class BlockFaceElement extends ClickableElement<BlockFaceElement> {

    private final TextureAtlasSprite sprite;
    private float rotation = 0;

    public BlockFaceElement(Identifier id, int x, int y, int width, int height) {
        super(x, y, width, height, Component.empty());

        Block block = BuiltInRegistries.BLOCK.getValue(id);
        BlockState state = block.defaultBlockState();

        sprite = MilloMod.MC
                .getModelManager()
                .getBlockStateModelSet()
                .get(state)
                .particleMaterial()
                .sprite();
    }

    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float deltaTicks) {
        graphics.pose().pushMatrix();
        graphics.pose().translate(getX() + getWidth() / 2f, getY() + getHeight() / 2f);
        if (rotation != 0) graphics.pose().rotate(rotation);
        graphics.pose().translate(-getWidth() / 2f, -getHeight() / 2f);
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, 0, 0, getWidth(), getHeight(),0xFFFFFFFF);
        graphics.pose().popMatrix();
    }

    public void rotate(float amount) {
        rotation = amount;
    }
}
