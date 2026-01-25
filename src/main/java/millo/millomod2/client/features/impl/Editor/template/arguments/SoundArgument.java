package millo.millomod2.client.features.impl.Editor.template.arguments;

import millo.millomod2.client.features.impl.Editor.template.Argument;
import millo.millomod2.client.hypercube.actiondump.Sound;
import millo.millomod2.client.hypercube.actiondump.SoundVariant;
import millo.millomod2.client.hypercube.actiondump.readable.ActionDump;
import millo.millomod2.client.hypercube.template.ArgumentItemData;
import millo.millomod2.client.util.MilloLog;
import millo.millomod2.client.util.SoundUtil;
import millo.millomod2.client.util.style.Styles;
import net.minecraft.text.Text;

import java.util.function.Supplier;

public class SoundArgument extends Argument<SoundArgument> {

    private String sound;
    private String variant;
    private double pitch;
    private double volume;

    private boolean run() {
        ActionDump dump = ActionDump.getActionDump().orElseThrow();
        Sound sound = dump.getSoundFromName(this.sound);

        if (variant != null && !variant.isBlank()) {
            for (SoundVariant variant : sound.variants) {
                if (variant.id.equals(this.variant)) {
                    SoundUtil.playSoundVariant(sound.soundId, (float) volume, (float) pitch, variant.seed);
                    return false;
                }
            }
            MilloLog.logInGame("Could not find variant: " + variant);
            return true;
        }

        SoundUtil.playSound(sound.soundId, (float) volume, (float) pitch);
        return false;
    }

    @Override
    public Text getDisplayText() {
        return Text.literal(sound).setStyle(Styles.SOUND.getStyle());
    }

    @Override
    public Text getTooltip() {
        var text = Text.literal("Pitch: " + pitch + "\nVolume: " + volume);
        if (variant != null && !variant.isBlank()) text.append(Text.literal("\nVariant: " + variant));
        return text;
    }

    @Override
    public Supplier<Boolean> getOnClick() {
        return this::run;
    }

    @Override
    public SoundArgument from(ArgumentItemData data) {
        this.sound = data.sound;
        this.pitch = data.pitch;
        this.volume = data.vol;
        this.variant = data.variant;
        return this;
    }
}
