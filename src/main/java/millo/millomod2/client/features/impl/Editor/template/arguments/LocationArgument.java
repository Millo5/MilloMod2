package millo.millomod2.client.features.impl.Editor.template.arguments;

import millo.millomod2.client.features.impl.Editor.template.Argument;
import millo.millomod2.client.hypercube.template.ArgumentItemData;
import millo.millomod2.client.hypercube.template.LocationData;
import millo.millomod2.client.util.style.Styles;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.text.DecimalFormat;

public class LocationArgument extends Argument<LocationArgument> {

    private boolean isBlock;
    private double x;
    private double y;
    private double z;
    private double pitch;
    private double yaw;


    @Override
    public Text getDisplayText() {
        DecimalFormat df = new DecimalFormat("#.##");
        String x = df.format(this.x);
        String y = df.format(this.y);
        String z = df.format(this.z);

        MutableText text = Text.literal("[").setStyle(Styles.LOCATION.getStyle());
        text.append(Text.literal(x + ", " + y + ", " + z).setStyle(Styles.DEFAULT.getStyle()));
        if (!isBlock) {
            String pitch = df.format(this.pitch);
            String yaw = df.format(this.yaw);
            text.append(Text.literal(", " + pitch + ", " + yaw).setStyle(Styles.DEFAULT.getStyle()));
        }
        text.append(Text.literal("]").setStyle(Styles.LOCATION.getStyle()));
        return text;
    }

    @Override
    public LocationArgument from(ArgumentItemData data) {
        LocationData loc = data.loc;
        this.isBlock = data.isBlock;
        this.x = loc.x;
        this.y = loc.y;
        this.z = loc.z;
        if (!isBlock) {
            this.pitch = loc.pitch;
            this.yaw = loc.yaw;
        }

        return this;
    }
}
