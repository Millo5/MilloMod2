package millo.millomod2.client.features.impl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import millo.millomod2.client.features.Feature;
import millo.millomod2.client.features.addons.ContainerMod;
import millo.millomod2.client.features.addons.Toggleable;
import millo.millomod2.client.hypercube.actiondump.Sound;
import millo.millomod2.client.hypercube.actiondump.SoundVariant;
import millo.millomod2.client.hypercube.actiondump.readable.ActionDump;
import millo.millomod2.client.hypercube.model.arguments.SoundArgumentModel;
import millo.millomod2.client.mixin.render.accessors.HandledScreenAccessor;
import millo.millomod2.client.mixin.render.accessors.ScreenAccessor;
import millo.millomod2.client.util.ItemUtil;
import millo.millomod2.client.util.KeyUtil;
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

import java.util.ArrayList;

public class SoundPreview extends Feature implements Toggleable, ContainerMod {
    @Override
    public String getId() {
        return "sound_preview";
    }

    private final ArrayList<SoundInstance> previewQueue = new ArrayList<>();

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

                    int delay = 0;

                    if (!KeyUtil.isKeyDown(42)) {
                        for (ItemStack itemStack : inv) {
                            if (itemStack.isEmpty()) continue;
                            int num = numberFromItemValue(itemStack);
                            if (num >= 0) {
                                delay = num;
                                break;
                            }
                        }
                    }

                    int currentDelay = 0;
                    for (int i = 0; i < inv.size(); i++) {
                        ItemStack itemStack = inv.getStack(i);
                        if (itemStack.isEmpty()) continue;

                        try {
                            SoundInstance instance = new SoundInstance(itemStack);
                            instance.setDelay(currentDelay);
                            previewQueue.add(instance);
                            currentDelay += delay;
                        } catch (IllegalArgumentException e) {
                        }
                    }
                });

        ScreenAccessor accessor = (ScreenAccessor) handledScreen;
        accessor.iAddDrawableChild(button);
    }

    @Override
    public void onTick() {
        if (previewQueue.isEmpty()) return;

        ArrayList<SoundInstance> playedThisTick = new ArrayList<>();
        for (SoundInstance instance : previewQueue) {
            instance.setDelay(instance.getDelay() - 1);
            if (instance.getDelay() <= 0) {
                instance.play();
                playedThisTick.add(instance);
            }
        }
        previewQueue.removeAll(playedThisTick);
    }

    private int numberFromItemValue(ItemStack item) {
        String varitem = ItemUtil.getPBVString(item, "hypercube:varitem");
        if (varitem == null) return -1;

        var json = JsonParser.parseString(varitem);

        JsonObject obj = json.getAsJsonObject();
        String id = obj.get("id").getAsString();
        if (!id.equals("num")) return -1;

        JsonObject data = obj.getAsJsonObject("data");
        return data.get("name").getAsInt();
    }

    private void previewSound(ItemStack item) {
        try {
            SoundInstance instance = new SoundInstance(item);
            instance.play();
        } catch (IllegalArgumentException e) {
            // Not a sound, ignore
        }
    }

    public void queueSound(SoundInstance inst) {
        previewQueue.add(inst);
    }

    public static class SoundInstance {
        private final String soundId;
        private final float volume;
        private final float pitch;
        private final String variant;
        private final String customKey;

        private int delay = 0;

        public SoundInstance(String soundId, float volume, float pitch, String variant, String customKey) {
            this.soundId = soundId;
            this.volume = volume;
            this.pitch = pitch;
            this.variant = variant;
            this.customKey = customKey;
        }

        public SoundInstance(ItemStack item) {
            String varitem = ItemUtil.getPBVString(item, "hypercube:varitem");
            if (varitem == null) throw new IllegalArgumentException("Item does not contain a varitem");

            var json = JsonParser.parseString(varitem);
            JsonObject obj = json.getAsJsonObject();
            String id = obj.get("id").getAsString();
            if (!id.equals("snd")) throw new IllegalArgumentException("Varitem is not a sound");

            JsonObject data = obj.getAsJsonObject("data");
            this.volume = (float) data.get("vol").getAsDouble();
            this.pitch = (float) data.get("pitch").getAsDouble();
            this.variant = data.has("variant") ? data.get("variant").getAsString() : "";

            customKey = data.has("key") ? data.get("key").getAsString() : null;

            ActionDump actionDump = ActionDump.getActionDump().orElseThrow();
            Sound adSound = actionDump.getSoundFromName(data.get("sound").getAsString());
            if (adSound == null) throw new IllegalArgumentException("Sound not found in action dump");
            this.soundId = adSound.soundId;
        }

        public SoundInstance(SoundArgumentModel sound) {
            this.volume = (float) sound.getVolume();
            this.pitch = (float) sound.getPitch();
            this.variant = sound.getVariant() != null ? sound.getVariant() : "";
            this.customKey = sound.getKey();

            ActionDump actionDump = ActionDump.getActionDump().orElseThrow();
            Sound adSound = actionDump.getSoundFromName(sound.getSound());
            if (adSound == null) throw new IllegalArgumentException("Sound not found in action dump");
            this.soundId = adSound.soundId;
        }

        public int getDelay() {
            return delay;
        }

        public void setDelay(int delay) {
            this.delay = delay;
        }

        public void play() {
            if (customKey != null) {
                SoundUtil.playSound(customKey, volume, pitch);
                return;
            }

            if (!variant.isEmpty()) {
                ActionDump actionDump = ActionDump.getActionDump().orElseThrow();
                Sound adSound = actionDump.getSoundFromName(soundId);
                if (adSound == null) return;

                for (SoundVariant soundVariant : adSound.variants) {
                    if (soundVariant.id.equals(variant)) {
                        SoundUtil.playSoundVariant(soundId, volume, pitch, soundVariant.seed);
                        return;
                    }
                }
            }
            SoundUtil.playSound(soundId, volume, pitch);
        }
    }
}
