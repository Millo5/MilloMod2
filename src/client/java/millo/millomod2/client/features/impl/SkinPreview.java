package millo.millomod2.client.features.impl;

import com.mojang.authlib.GameProfile;
import millo.millomod2.client.MilloMod;
import millo.millomod2.client.features.Feature;
import millo.millomod2.client.features.addons.ContainerMod;
import millo.millomod2.client.features.addons.Toggleable;
import millo.millomod2.client.mixin.render.accessors.HandledScreenAccessor;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

public class SkinPreview extends Feature implements Toggleable, ContainerMod {

    private SkinTextures skin;
    private PlayerEntityModel model;
    private UUID uuid;

    @Override
    public String getId() {
        return "skin_preview";
    }

    @Override
    public void containerDrawSlot(DrawContext context, Slot slot, CallbackInfo ci) {
        if (!isEnabled()) return;
        Screen screen = MilloMod.MC.currentScreen;
        if (screen == null) return;

        var handledScreen = (HandledScreenAccessor) screen;

        Slot focusedSlot = handledScreen.getFocusedSlot();
        if (slot != focusedSlot) {
            this.uuid = null;
            return;
        }

        ItemStack item = slot.getStack();

        if (!item.getItem().equals(Items.PLAYER_HEAD)) return;
        ProfileComponent profile = item.get(DataComponentTypes.PROFILE);

        if (profile == null) return;
        GameProfile gameProfile = profile.gameProfile();
        UUID uuid = gameProfile.getId();

        boolean slim;
        if (!uuid.equals(this.uuid)) {
            this.uuid = uuid;
            skin = MilloMod.MC.getSkinProvider().getSkinTextures(gameProfile);
            slim = skin.model() == SkinTextures.Model.SLIM;
            ModelData modelData = PlayerEntityModel.getTexturedModelData(Dilation.NONE, slim);
            model = new PlayerEntityModel(modelData.getRoot().createPart(64, 64), slim);
        }

        int x = handledScreen.getX() - 100;
        int y = handledScreen.getY();

        context.addPlayerSkin(
                model, skin.texture(), 38f, -15f, -15f, 0f, x, y,x + 100, y + 100
        );
    }
}
