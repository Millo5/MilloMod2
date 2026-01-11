package millo.millomod2.client.features.impl.Editor.template.arguments;

import millo.millomod2.client.features.impl.Editor.template.Argument;
import millo.millomod2.client.hypercube.template.ArgumentItemData;
import millo.millomod2.client.util.style.Styles;
import net.minecraft.text.Text;

import java.text.DecimalFormat;

public class VectorArgument extends Argument<VectorArgument> {

    private double x;
    private double y;
    private double z;

    @Override
    public Text getDisplayText() {
        DecimalFormat df = new DecimalFormat("#.##");
        String x = df.format(this.x);
        String y = df.format(this.y);
        String z = df.format(this.z);

        return Text.literal("<" + x + ", " + y + ", " + z + ">").setStyle(Styles.VECTOR.getStyle());
    }

    @Override
    public VectorArgument from(ArgumentItemData data) {
        x = data.x;
        y = data.y;
        z = data.z;
        return this;
    }
}
