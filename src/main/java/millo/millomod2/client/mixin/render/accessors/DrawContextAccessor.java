package millo.millomod2.client.mixin.render.accessors;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.render.state.GuiRenderState;
import org.joml.Matrix3x2fStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(DrawContext.class)
public interface DrawContextAccessor {

    @Invoker("<init>")
    static DrawContext createDrawContext(MinecraftClient client, Matrix3x2fStack matrices, GuiRenderState state, int mouseX, int mouseY) {
        throw new UnsupportedOperationException("Invoker not implemented");
    }

}
