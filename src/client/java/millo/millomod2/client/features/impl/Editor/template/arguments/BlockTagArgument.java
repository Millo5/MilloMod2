package millo.millomod2.client.features.impl.Editor.template.arguments;

import millo.millomod2.client.features.impl.Editor.template.Argument;
import millo.millomod2.client.hypercube.template.ArgumentItemData;
import net.minecraft.text.Text;

public class BlockTagArgument extends Argument<BlockTagArgument> {

    private String option;
    private String tag;
    private String action;
    private String block;

    @Override
    protected Text getDisplayText() {
        return Text.literal(tag + ":" + option);
    }

    @Override
    public BlockTagArgument from(ArgumentItemData data) {
        this.option = data.option;
        this.tag = data.tag;
        this.action = data.action;
        this.block = data.block;
        return this;
    }
}
