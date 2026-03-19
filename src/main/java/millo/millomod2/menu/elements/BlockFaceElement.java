package millo.millomod2.menu.elements;

import millo.millomod2.client.MilloMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.model.BlockStateModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class BlockFaceElement extends ClickableElement<BlockFaceElement> {

    private final Sprite sprite;
    private float rotation = 0;

    public BlockFaceElement(Identifier id, int x, int y, int width, int height) {
        super(x, y, width, height, Text.empty());

        Block block = Registries.BLOCK.get(id);
        BlockState state = block.getDefaultState();

        BlockRenderManager brm = MilloMod.MC.getBlockRenderManager();

        BlockStateModel model = brm.getModel(state);
        sprite = model.particleSprite();
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        context.getMatrices().pushMatrix();
        context.getMatrices().translate(getX() + getWidth() / 2f, getY() + getHeight() / 2f);
        if (rotation != 0) context.getMatrices().rotate(rotation);
        context.getMatrices().translate(-getWidth() / 2f, -getHeight() / 2f);
        context.drawSpriteStretched(RenderPipelines.GUI_TEXTURED, sprite, 0, 0, getWidth(), getHeight(),0xFFFFFFFF);
        context.getMatrices().popMatrix();
    }

    public void rotate(float amount) {
        rotation = amount;
    }
}
