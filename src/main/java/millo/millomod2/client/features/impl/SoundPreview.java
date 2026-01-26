package millo.millomod2.client.features.impl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import millo.millomod2.client.features.Feature;
import millo.millomod2.client.features.addons.ContainerMod;
import millo.millomod2.client.features.addons.Toggleable;
import millo.millomod2.client.hypercube.actiondump.Sound;
import millo.millomod2.client.hypercube.actiondump.SoundVariant;
import millo.millomod2.client.hypercube.actiondump.readable.ActionDump;
import millo.millomod2.client.mixin.render.accessors.HandledScreenAccessor;
import millo.millomod2.client.mixin.render.accessors.ScreenAccessor;
import millo.millomod2.client.util.ItemUtil;
import millo.millomod2.client.util.SoundUtil;
import millo.millomod2.menu.elements.buttons.ButtonElement;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class SoundPreview extends Feature implements Toggleable, ContainerMod {
    @Override
    public String getId() {
        return "sound_preview";
    }

    @Override
    public <T extends ScreenHandler> void containerInit(HandledScreen<T> handledScreen, CallbackInfo ci) {
        if (!isEnabled()) return;

        if (!(handledScreen.getScreenHandler() instanceof GenericContainerScreenHandler containerHandler)) return;
        if (!(containerHandler.getInventory() instanceof SimpleInventory)) return;

        HandledScreenAccessor hsAccessor = (HandledScreenAccessor) handledScreen;

        ButtonElement button = ButtonElement.create(20, 20)
                .message(Text.of("P"))
                .position(hsAccessor.getX() + hsAccessor.getBackgroundWidth() + 5, hsAccessor.getY())
                .background(0xffffffff)
                .onPress((b) -> {
                    Inventory inv = containerHandler.getInventory();
                    for (int i = 0; i < inv.size(); i++) {
                        var stack = inv.getStack(i);
                        if (stack.isEmpty()) continue;
                        previewSound(stack);
                    }
                });

        ScreenAccessor accessor = (ScreenAccessor) handledScreen;
        accessor.iAddDrawableChild(button);
    }

    private void previewSound(ItemStack item) {
        String varitem = ItemUtil.getPBVString(item, "hypercube:varitem");
        if (varitem == null) return;

        var json = JsonParser.parseString(varitem);


        JsonObject obj = json.getAsJsonObject();
        String id = obj.get("id").getAsString();
        if (!id.equals("snd")) return;

        JsonObject data = obj.getAsJsonObject("data");

        double pitch = data.get("pitch").getAsDouble();
        double vol = data.get("vol").getAsDouble();

        if (data.has("key")) {
            SoundUtil.playSound(data.get("key").getAsString(), (float) vol, (float) pitch);
            return;
        }

        String sound = data.get("sound").getAsString();
        String variant;
        if (data.has("variant")) {
            variant = data.get("variant").getAsString();
        } else {
            variant = "";
        }

        ActionDump actionDump = ActionDump.getActionDump().orElseThrow();

        Sound adSound = actionDump.getSoundFromName(sound);
        if (adSound == null) return;

        String soundId = adSound.soundId;
        if (soundId == null) return;

        if (!variant.isEmpty()) {
            for (SoundVariant soundVariant : adSound.variants) {
                if (soundVariant.id.equals(variant)) {
                    SoundUtil.playSoundVariant(soundId, (float) vol, (float) pitch, soundVariant.seed);
                    return;
                }
            }
        }
        SoundUtil.playSound(soundId, (float) vol, (float) pitch);
    }
}
