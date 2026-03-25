package millo.millomod2.client.mixin.screen;

import millo.millomod2.client.MilloMod;
import millo.millomod2.client.mixin.render.accessors.ScreenAccessor;
import millo.millomod2.client.net.UpdateService;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
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
    protected abstract Text getMultiplayerDisabledText();


    @Inject(method = "addNormalWidgets", at = @At("RETURN"))
    void addUpdateButton(int y, int spacingY, CallbackInfoReturnable<Integer> cir) {

        UpdateService.checkForUpdates().thenAccept(result -> {

            if (!result.outdated()) return;
            ScreenAccessor accessor = (ScreenAccessor)this;
            accessor.iAddDrawableChild(
                    (ButtonWidget.builder(Text.literal("Update MilloMod (" + MilloMod.MOD_VERSION + " -> " + result.latestVersion() + ")"),
                            (button) -> UpdateService.openUpdateScreen())
                            .dimensions(accessor.getWidth() / 2 - 100, y + spacingY * 3, 200, 20)
                            .tooltip(null)
                            .build()
                    )).active = getMultiplayerDisabledText() == null;

        });

    }

}
