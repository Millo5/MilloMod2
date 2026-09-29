package millo.millomod2.client.mixin.screen;

import millo.millomod2.client.MilloMod;
import millo.millomod2.client.mixin.render.accessors.ScreenAccessor;
import millo.millomod2.client.net.UpdateService;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TitleScreen.class)
public abstract class MTitleScreen {

    @Shadow
    @Nullable
    protected abstract Component getMultiplayerDisabledReason();


    @Inject(method = "createNormalMenuOptions", at = @At("RETURN"))
    void addUpdateButton(int y, int spacingY, CallbackInfoReturnable<Integer> cir) {
        TitleScreen titleScreen = (TitleScreen) (Object) this;

        UpdateService.checkForUpdates().thenAccept(result -> {
            MilloMod.MC.execute(() -> {
                if (!result.outdated() || MilloMod.MC.gui.screen() != titleScreen) return;
                ScreenAccessor accessor = (ScreenAccessor) titleScreen;
                accessor.iAddDrawableChild(
                        (Button.builder(Component.literal("Update MilloMod (" + MilloMod.MOD_VERSION + " -> " + result.latestVersion() + ")"),
                                        (button) -> UpdateService.openUpdateScreen())
                                .bounds(accessor.getWidth() / 2 - 100, y + spacingY * 3, 200, 20)
                                .tooltip(null)
                                .build()
                        )).active = getMultiplayerDisabledReason() == null;
            });
        });

    }

}
