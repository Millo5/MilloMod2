package millo.millomod2.client.features.impl.Editor.template.arguments;

import millo.millomod2.client.features.impl.Editor.template.Argument;
import millo.millomod2.client.hypercube.template.ArgumentItemData;
import millo.millomod2.client.util.style.Styles;
import net.minecraft.text.Text;

public class SoundArgument extends Argument<SoundArgument> {

    private double pitch;
    private double volume;

    @Override
    protected Text getDisplayText() {
        return Text.literal("Sound(pitch: " + pitch + ", volume: " + volume + ")").setStyle(Styles.SOUND.getStyle());
    }

    @Override
    public SoundArgument from(ArgumentItemData data) {
        this.pitch = data.pitch;
        this.volume = data.vol;
        return this;
    }
}
