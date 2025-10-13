package millo.millomod2.client.features.impl.Editor.template.arguments;

import millo.millomod2.client.features.impl.Editor.template.Argument;
import millo.millomod2.client.hypercube.template.ArgumentItemData;
import millo.millomod2.client.util.style.Styles;
import net.minecraft.text.Text;

public class PotionArgument extends Argument<PotionArgument> {

    private int amplifier;
    private int duration;

    @Override
    protected Text getDisplayText() {
        return Text.literal("Potion(amplifier: " + amplifier + ", duration: " + duration + ")").setStyle(Styles.POTION.getStyle());
    }

    @Override
    public PotionArgument from(ArgumentItemData data) {
        this.amplifier = data.amp;
        this.duration = data.dur;
        return this;
    }
}
