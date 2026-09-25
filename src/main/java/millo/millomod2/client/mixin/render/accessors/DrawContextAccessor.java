package millo.millomod2.client.mixin.render.accessors;

import net.minecraft.client.gui.GuiGraphics;
import org.joml.Matrix3x2fStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GuiGraphics.class)
public interface DrawContextAccessor {

    @Mutable
    @Accessor("pose")
    void setMatrices(Matrix3x2fStack matrices);

}
