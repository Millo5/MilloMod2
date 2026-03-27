package millo.millomod2.client.hypercube.model.arguments;

import com.google.gson.JsonObject;
import millo.millomod2.client.hypercube.actiondump.Sound;
import millo.millomod2.client.hypercube.actiondump.SoundVariant;
import millo.millomod2.client.hypercube.actiondump.readable.ActionDump;
import millo.millomod2.client.util.MilloLog;
import millo.millomod2.client.util.SoundUtil;

public class SoundArgumentModel extends ArgumentModel<SoundArgumentModel> {

    private double volume, pitch;
    private String sound, key, variant;

    @Override
    public SoundArgumentModel self() {
        return this;
    }

    @Override
    public String id() {
        return "snd";
    }

    @Override
    protected void deserializeItem(JsonObject jsonObject) {
        sound = jsonObject.has("sound") ? jsonObject.get("sound").getAsString() : null;
        volume = jsonObject.get("vol").getAsDouble();
        pitch = jsonObject.get("pitch").getAsDouble();
        key = jsonObject.has("key") ? jsonObject.get("key").getAsString() : null;
        variant = jsonObject.has("variant") ? jsonObject.get("variant").getAsString() : null;
    }

    @Override
    protected void serializeItem(JsonObject jsonObject) {
        jsonObject.addProperty("pitch", pitch);
        jsonObject.addProperty("vol", volume);
        if (sound != null) jsonObject.addProperty("sound", sound);
        if (variant != null) jsonObject.addProperty("variant", variant);
        if (key != null) jsonObject.addProperty("key", key);
    }

    public double getVolume() {
        return volume;
    }

    public double getPitch() {
        return pitch;
    }

    public String getSound() {
        return sound;
    }

    public String getVariant() {
        return variant;
    }

    public String getKey() {
        return key;
    }

    public void play() {
        ActionDump dump = ActionDump.getActionDump().orElseThrow();
        Sound sound = dump.getSoundFromName(getSound());

        float volume = (float) getVolume();
        float pitch = (float) getPitch();

        if (getKey() != null) {
            SoundUtil.playSound(getKey(), volume, pitch);
            return;
        }

        String variant = getVariant();
        if (variant != null && !variant.isBlank()) {
            for (SoundVariant v : sound.variants) {
                if (v.id.equals(variant)) {
                    SoundUtil.playSoundVariant(sound.soundId, volume, pitch, v.seed);
                    return;
                }
            }
            MilloLog.logInGame("Could not find variant: " + variant);
            return;
        }

        SoundUtil.playSound(sound.soundId, volume, pitch);
    }
}
