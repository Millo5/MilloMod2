package millo.millomod2.client.features.impl.Editor.elements.codeline.segments.simple;

import millo.millomod2.client.hypercube.actiondump.Sound;
import millo.millomod2.client.hypercube.actiondump.SoundVariant;
import millo.millomod2.client.hypercube.actiondump.readable.ActionDump;
import millo.millomod2.client.hypercube.model.arguments.SoundArgumentModel;
import millo.millomod2.client.util.MilloLog;
import millo.millomod2.client.util.SoundUtil;
import millo.millomod2.client.util.style.Styles;
import millo.millomod2.menu.elements.TextElement;
import net.minecraft.text.Text;

public class SoundArgumentSegment extends SimpleSegment<SoundArgumentModel> {

    public SoundArgumentSegment(SoundArgumentModel model) {
        super(model);
    }

    @Override
    TextElement createContent(SoundArgumentModel model) {
        String name = model.getSound() == null ? model.getKey() : model.getSound();

        var tooltip = Text.literal("Pitch: " + model.getPitch() + "\nVolume: " + model.getVolume());
        if (model.getVariant() != null && !model.getVariant().isBlank()) tooltip.append(Text.literal("\nVariant: " + model.getVariant()));

        return new SimpleArgumentBuilder(name)
                .style(Styles.SOUND)
                .tooltip(tooltip)
                .onClick(this::onClick)
                .build();
    }

    private Boolean onClick() {
        ActionDump dump = ActionDump.getActionDump().orElseThrow();
        Sound sound = dump.getSoundFromName(model.getSound());

        float volume = (float) model.getVolume();
        float pitch = (float) model.getPitch();

        if (model.getKey() != null) {
            SoundUtil.playSound(model.getKey(), volume, pitch);
            return false;
        }

        String variant = model.getVariant();
        if (variant != null && !variant.isBlank()) {
            for (SoundVariant v : sound.variants) {
                if (v.id.equals(variant)) {
                    SoundUtil.playSoundVariant(sound.soundId, volume, pitch, v.seed);
                    return false;
                }
            }
            MilloLog.logInGame("Could not find variant: " + variant);
            return true;
        }

        SoundUtil.playSound(sound.soundId, volume, pitch);
        return false;
    }

    @Override
    public Class<SoundArgumentModel> getModelClass() {
        return SoundArgumentModel.class;
    }

}
