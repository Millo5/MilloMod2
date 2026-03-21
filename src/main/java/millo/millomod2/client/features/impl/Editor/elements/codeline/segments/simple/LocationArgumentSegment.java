package millo.millomod2.client.features.impl.Editor.elements.codeline.segments.simple;

import millo.millomod2.client.hypercube.model.arguments.LocationArgumentModel;
import millo.millomod2.client.util.style.Styles;
import millo.millomod2.menu.elements.TextElement;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.text.DecimalFormat;

public class LocationArgumentSegment extends SimpleSegment<LocationArgumentModel> {

    private final static DecimalFormat DF = new DecimalFormat("#.##");

    public LocationArgumentSegment(LocationArgumentModel model) {
        super(model);
    }

    @Override
    TextElement createContent(LocationArgumentModel model) {
        String x = DF.format(model.getX());
        String y = DF.format(model.getY());
        String z = DF.format(model.getZ());

        String cmd = "/ptp " + x + " " + y + " " + z;
        MutableText text = Text.literal("[").setStyle(Styles.LOCATION.getStyle());
        text.append(Text.literal(x + ", " + y + ", " + z).setStyle(Styles.DEFAULT.getStyle()));
        if (!model.isBlock()) {
            String pitch = DF.format(model.getPitch());
            String yaw = DF.format(model.getYaw());
            cmd += " " + pitch + " " + yaw;
            text.append(Text.literal(", " + pitch + ", " + yaw).setStyle(Styles.DEFAULT.getStyle()));
        }
        text.append(Text.literal("]").setStyle(Styles.LOCATION.getStyle()));

        return new SimpleArgumentBuilder(text)
                .onClickCmd(cmd)
                .build();
    }

    @Override
    public Class<LocationArgumentModel> getModelClass() {
        return LocationArgumentModel.class;
    }

}
