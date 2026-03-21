package millo.millomod2.client.features.impl.Editor.elements.codeline.segments.simple;

import millo.millomod2.client.hypercube.model.arguments.PotionArgumentModel;
import millo.millomod2.client.util.style.Styles;
import millo.millomod2.menu.elements.TextElement;
import net.minecraft.text.Text;

public class PotionArgumentSegment extends SimpleSegment<PotionArgumentModel> {

    public PotionArgumentSegment(PotionArgumentModel model) {
        super(model);
    }

    @Override
    TextElement createContent(PotionArgumentModel model) {
        return new SimpleArgumentBuilder(model.getPotion())
                .style(Styles.POTION)
                .tooltip(Text.literal("Amplifier: " + model.getAmplifier() + "\nDuration: " + model.getDuration() + " ticks").setStyle(Styles.COMMENT.getStyle()))
                .onClickCmd("/pot get " + model.getPotion().replaceAll(" ", "_").toLowerCase() + " " + model.getAmplifier() + " " + model.getDuration())
                .build();
    }

    @Override
    public Class<PotionArgumentModel> getModelClass() {
        return PotionArgumentModel.class;
    }

}
