package millo.millomod2.client.features.impl.Editor.template.arguments;

import millo.millomod2.client.features.impl.Editor.template.Argument;
import millo.millomod2.client.hypercube.template.ArgumentItemData;
import millo.millomod2.client.util.style.Styles;
import net.minecraft.text.Text;

public class GameValueArgument extends Argument<GameValueArgument> {

    private String target;
    private String type;

    @Override
    protected Text getDisplayText() {
        String t = String.valueOf(target.charAt(0));
        if (t.equals("D")) t = "";
        return Text.literal(type + " [" + t + "]").setStyle(Styles.GAME_VALUE.getStyle());
    }

    @Override
    public GameValueArgument from(ArgumentItemData data) {
        this.target = data.target;
        this.type = data.type;
        return this;
    }
}
