package millo.millomod2.client.features.impl.Editor.template.arguments;

import millo.millomod2.client.features.impl.Editor.template.Argument;
import millo.millomod2.client.hypercube.template.ArgumentItemData;
import millo.millomod2.client.util.style.Styles;
import net.minecraft.text.Text;

public class SoundArgument extends Argument<SoundArgument> {

    private String sound;
    private double pitch;
    private double volume;

    @Override
    public Text getDisplayText() {
        return Text.literal(sound).setStyle(Styles.SOUND.getStyle());
    }

    @Override
    public Text getTooltip() {
//        ArrayList<Text> lines = new ArrayList<>();
//        lines.add(Text.literal("Pitch: " + pitch));
//        lines.add(Text.literal("Volume: " + volume));
//        return lines;
        return Text.literal("Pitch: " + pitch + "\nVolume: " + volume);
    }

    @Override
    public SoundArgument from(ArgumentItemData data) {
        this.sound = data.sound;
        this.pitch = data.pitch;
        this.volume = data.vol;
        return this;
    }
}
