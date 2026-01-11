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
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.entity.player.PlayerSkinType;
import net.minecraft.entity.player.SkinTextures;
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
    public void containerDrawSlot(DrawContext context, Slot slot, int mouseX, int mouseY, CallbackInfo ci) {
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
        GameProfile gameProfile = profile.getGameProfile();
        UUID uuid = gameProfile.id();

        if (!uuid.equals(this.uuid)) {
            this.uuid = uuid;
            MilloMod.MC.getSkinProvider().fetchSkinTextures(gameProfile).thenAccept(textures -> {
                if (textures.isEmpty()) return;
                this.skin = textures.get();
                boolean slim = skin.model() == PlayerSkinType.SLIM;
                var modelData = PlayerEntityModel.getTexturedModelData(Dilation.NONE, slim);
                model = new PlayerEntityModel(modelData.getRoot().createPart(64, 64), slim);
            });
        }

        int x = handledScreen.getX() - 100;
        int y = handledScreen.getY();

        if (model != null && skin != null) {
            context.addPlayerSkin(
                    model, skin.body().texturePath(), 38f, -15f, -15f, 0f, x, y, x + 100, y + 100
            );
        }
    }
}
