package millo.millomod2.client.mixin.render;

import millo.millomod2.client.features.FeatureHandler;
import millo.millomod2.client.features.addons.ContainerMod;
import millo.millomod2.client.util.RenderInfo;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractContainerScreen.class)
public abstract class MContainerScreen<T extends AbstractContainerMenu> extends Screen {

    protected MContainerScreen(Component title) {
        super(title);
    }

    @Unique private long lastFrameTime = System.currentTimeMillis();
    @Shadow public abstract T getMenu();

    @Shadow @Final protected T menu;
    @Shadow protected int topPos;
    @Shadow protected int leftPos;

    @Inject(method = "init()V", at = @At("RETURN"))
    private void init(CallbackInfo ci) {
        FeatureHandler.forEach(f -> {
            if (f instanceof ContainerMod rendered) {
                rendered.containerInit((AbstractContainerScreen<? extends AbstractContainerMenu>) (Object) this, ci);
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

    @Inject(method = "extractRenderState", at = @At("RETURN"))
    private void render(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float deltaTicks, CallbackInfo ci) {
        long currentTime = System.currentTimeMillis();
        long deltaTime = currentTime - lastFrameTime;
        lastFrameTime = currentTime;

        RenderInfo info = new RenderInfo(graphics, deltaTime / 1000f, mouseX, mouseY);

        graphics.pose().pushMatrix();
        graphics.pose().translate(leftPos, topPos);
        FeatureHandler.forEach(f -> {
            if (f instanceof ContainerMod rendered) {
                rendered.containerRender(this.menu, info);
            }
        });
        graphics.pose().popMatrix();
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void mouseClicked(MouseButtonEvent click, boolean doubled, CallbackInfoReturnable<Boolean> cir) {
        FeatureHandler.forEach(f -> {
            if (f instanceof ContainerMod rendered) {
                rendered.containerMouseClicked(click, doubled, cir);
            }
        });
    }

    @Inject(method = "mouseReleased", at = @At("HEAD"), cancellable = true)
    private void mouseReleased(MouseButtonEvent click, CallbackInfoReturnable<Boolean> cir) {
        FeatureHandler.forEach(f -> {
            if (f instanceof ContainerMod rendered) {
                rendered.containerMouseReleased(click, cir);
            }
        });
    }



    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void keyPressedInject(KeyEvent input, CallbackInfoReturnable<Boolean> cir) {
        FeatureHandler.forEach(f -> {
            if (f instanceof ContainerMod rendered) {
                rendered.containerKeyPressed(this.menu, input, cir);
            }
        });
    }

    @Inject(method = "onClose", at = @At("HEAD"))
    private void close(CallbackInfo ci) {
        FeatureHandler.forEach(f -> {
            if (f instanceof ContainerMod rendered) {
                rendered.containerClose(ci);
            }
        });
    }

    @Inject(method="extractSlot", at = @At("TAIL"))
    private void drawSlotInject(GuiGraphicsExtractor graphics, Slot slot, int mouseX, int mouseY, CallbackInfo ci) {
        FeatureHandler.forEach(f -> {
            if (f instanceof ContainerMod rendered) {
                rendered.containerDrawSlot(graphics, slot, mouseX, mouseY, ci);
            }
        });
    }


}

//public interface MContainerScreenAccessor {
//    HandledScreen<?> getSelf();
//}

