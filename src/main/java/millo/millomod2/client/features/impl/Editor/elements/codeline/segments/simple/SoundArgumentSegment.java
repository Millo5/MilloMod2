package millo.millomod2.client.features.impl.Editor.elements.codeline.segments.simple;

import millo.millomod2.client.hypercube.model.arguments.SoundArgumentModel;
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
        model.play();
        return false; // false so it doesn't play the default click sound
    }

    @Override
    public Class<SoundArgumentModel> getModelClass() {
        return SoundArgumentModel.class;
    }

}
