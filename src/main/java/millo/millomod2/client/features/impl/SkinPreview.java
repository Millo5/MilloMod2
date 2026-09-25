package millo.millomod2.client.features.impl;

import com.mojang.authlib.GameProfile;
import millo.millomod2.client.MilloMod;
import millo.millomod2.client.features.Feature;
import millo.millomod2.client.features.addons.ContainerMod;
import millo.millomod2.client.features.addons.Toggleable;
import millo.millomod2.client.mixin.render.accessors.HandledScreenAccessor;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.PlayerModelType;
import net.minecraft.world.entity.player.PlayerSkin;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ResolvableProfile;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

public class SkinPreview extends Feature implements Toggleable, ContainerMod {

    private PlayerSkin skin;
    private PlayerModel model;
    private UUID uuid;

    @Override
    public String getId() {
        return "skin_preview";
    }

    @Override
    public void containerDrawSlot(GuiGraphics context, Slot slot, int mouseX, int mouseY, CallbackInfo ci) {
        if (!isEnabled()) return;
        Screen screen = MilloMod.MC.screen;
        if (screen == null) return;

        var handledScreen = (HandledScreenAccessor) screen;

        Slot focusedSlot = handledScreen.getFocusedSlot();
        if (slot != focusedSlot) {
            this.uuid = null;
            return;
        }

        ItemStack item = slot.getItem();

        if (!item.getItem().equals(Items.PLAYER_HEAD)) return;
        ResolvableProfile profile = item.get(DataComponents.PROFILE);

        if (profile == null) return;
        GameProfile gameProfile = profile.partialProfile();
        UUID uuid = gameProfile.id();

        if (!uuid.equals(this.uuid)) {
            this.uuid = uuid;
            MilloMod.MC.getSkinManager().get(gameProfile).thenAccept(textures -> {
                if (textures.isEmpty()) return;
                this.skin = textures.get();
                boolean slim = skin.model() == PlayerModelType.SLIM;
                var modelData = PlayerModel.createMesh(CubeDeformation.NONE, slim);
                model = new PlayerModel(modelData.getRoot().bake(64, 64), slim);
            });
        }

        int x = handledScreen.getX() - 100;
        int y = handledScreen.getY();

        if (model != null && skin != null) {
            context.submitSkinRenderState(
                    model, skin.body().texturePath(), 38f, -15f, -15f, 0f, x, y, x + 100, y + 100
            );
        }
    }
}
