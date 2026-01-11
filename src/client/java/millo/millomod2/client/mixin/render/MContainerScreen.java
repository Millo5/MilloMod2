package millo.millomod2.client.mixin.render;

import millo.millomod2.client.features.FeatureHandler;
import millo.millomod2.client.features.addons.ContainerMod;
import millo.millomod2.client.util.RenderInfo;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.input.KeyInput;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HandledScreen.class)
public abstract class MContainerScreen<T extends ScreenHandler> extends Screen {

    protected MContainerScreen(Text title) {
        super(title);
    }

    @Unique private long lastFrameTime = System.currentTimeMillis();
    @Shadow public abstract T getScreenHandler();

    @Shadow @Final protected T handler;
    @Shadow protected int y;
    @Shadow protected int x;

    @Inject(method = "init()V", at = @At("RETURN"))
    private void init(CallbackInfo ci) {
        FeatureHandler.forEach(f -> {
            if (f instanceof ContainerMod rendered) {
                rendered.containerInit((HandledScreen<? extends ScreenHandler>) (Object) this, ci);
            }
        });
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        FeatureHandler.forEach(f -> {
            if (f instanceof ContainerMod rendered) {
                rendered.containerTick(ci);
            }
        });
    }

    @Inject(method = "render", at = @At("RETURN"))
    private void render(DrawContext context, int mouseX, int mouseY, float deltaTicks, CallbackInfo ci) {
        long currentTime = System.currentTimeMillis();
        long deltaTime = currentTime - lastFrameTime;
        lastFrameTime = currentTime;

        RenderInfo info = new RenderInfo(context, deltaTime / 1000f, mouseX, mouseY);

        context.getMatrices().pushMatrix();
        context.getMatrices().translate(x, y);
        FeatureHandler.forEach(f -> {
            if (f instanceof ContainerMod rendered) {
                rendered.containerRender(this.handler, info);
            }
        });
        context.getMatrices().popMatrix();
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void mouseClicked(Click click, boolean doubled, CallbackInfoReturnable<Boolean> cir) {
        FeatureHandler.forEach(f -> {
            if (f instanceof ContainerMod rendered) {
                rendered.containerMouseClicked(click, doubled, cir);
            }
        });
    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void keyPressedInject(KeyInput input, CallbackInfoReturnable<Boolean> cir) {
        FeatureHandler.forEach(f -> {
            if (f instanceof ContainerMod rendered) {
                rendered.containerKeyPressed(input, cir);
            }
        });
    }

    @Inject(method = "close", at = @At("HEAD"))
    private void close(CallbackInfo ci) {
        FeatureHandler.forEach(f -> {
            if (f instanceof ContainerMod rendered) {
                rendered.containerClose(ci);
            }
        });
    }

    @Inject(method="drawSlot", at = @At("TAIL"))
    private void drawSlotInject(DrawContext context, Slot slot, int mouseX, int mouseY, CallbackInfo ci) {
        FeatureHandler.forEach(f -> {
            if (f instanceof ContainerMod rendered) {
                rendered.containerDrawSlot(context, slot, mouseX, mouseY, ci);
            }
        });
    }


}

//public interface MContainerScreenAccessor {
//    HandledScreen<?> getSelf();
//}

