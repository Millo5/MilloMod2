package millo.millomod2.client.mixin.render.accessors;

import net.minecraft.client.gui.DrawContext;
import org.joml.Matrix3x2fStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DrawContext.class)
public interface DrawContextAccessor {

    @Mutable
    @Accessor("matrices")
    void setMatrices(Matrix3x2fStack matrices);

}
