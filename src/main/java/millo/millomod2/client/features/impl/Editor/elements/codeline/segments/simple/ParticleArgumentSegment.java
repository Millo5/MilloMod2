package millo.millomod2.client.features.impl.Editor.elements.codeline.segments.simple;

import millo.millomod2.client.hypercube.model.arguments.ParticleArgumentModel;
import millo.millomod2.client.hypercube.model.arguments.particle.ParticleField;
import millo.millomod2.client.util.style.Styles;
import millo.millomod2.menu.elements.TextElement;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.lang.reflect.Field;

public class ParticleArgumentSegment extends SimpleSegment<ParticleArgumentModel> {

    public ParticleArgumentSegment(ParticleArgumentModel model) {
        super(model);
    }

    @Override
    TextElement createContent(ParticleArgumentModel model) {

        MutableText tooltip = Text.empty();
        tooltip.append("Amount: " + model.getAmount() + "\n");
        tooltip.append("Spread: " + model.getHorizontal() + " " + model.getVertical() + "\n");

        for (ParticleField<?> dataField : model.getDataFields()) {
            tooltip.append(dataField.id() + ": ");
            Field[] fields = dataField.getClass().getDeclaredFields();
            if (fields.length == 1) {
                fields[0].setAccessible(true);
                try {
                    Object value = fields[0].get(dataField);
                    tooltip.append(value + "\n");
                } catch (IllegalAccessException ignored) {}
                continue;
            }

            for (Field declaredField : fields) {
                declaredField.setAccessible(true);
                try {
                    Object value = declaredField.get(dataField);
                    tooltip.append("  " + declaredField.getName() + ": " + value + "\n");
                } catch (IllegalAccessException ignored) {}
            }
        }

        return new SimpleArgumentBuilder(model.getParticle())
                .style(Styles.PARTICLE)
                .tooltip(tooltip)
                .build();
    }

    @Override
    public Class<ParticleArgumentModel> getModelClass() {
        return ParticleArgumentModel.class;
    }

}
