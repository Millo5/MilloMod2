package millo.millomod2.client.mixin.render;

import millo.millomod2.client.rendering.world.Renderer;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class MGameRenderer {

    @Inject(method = "close", at = @At("RETURN"))
    private void close(CallbackInfo ci) {
        Renderer.close();
    }

}
