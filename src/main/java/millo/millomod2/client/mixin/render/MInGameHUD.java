package millo.millomod2.client.mixin.render;

import millo.millomod2.client.features.FeatureHandler;
import millo.millomod2.client.features.addons.HUDRendered;
import millo.millomod2.client.util.RenderInfo;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class MInGameHUD {

    @Unique private long lastFrameTime = System.currentTimeMillis();

    @Inject(method = "render", at = @At("RETURN"))
    private void render(GuiGraphics context, DeltaTracker tickCounter, CallbackInfo ci) {
        long currentTime = System.currentTimeMillis();
        long deltaTime = currentTime - lastFrameTime;
        lastFrameTime = currentTime;

        RenderInfo renderInfo = new RenderInfo(context, deltaTime / 1000f * 20f);

        FeatureHandler.forEach(feature -> {
            if (feature instanceof HUDRendered hud) {
                hud.HUDRender(renderInfo);
            }
        });
    }
}
