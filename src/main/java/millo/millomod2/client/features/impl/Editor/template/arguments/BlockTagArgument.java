package millo.millomod2.client.features.impl.Editor.template.arguments;

import millo.millomod2.client.features.impl.Editor.template.Argument;
import millo.millomod2.client.hypercube.template.ArgumentItemData;
import millo.millomod2.client.util.style.Styles;
import net.minecraft.text.Text;

public class BlockTagArgument extends Argument<BlockTagArgument> {

    private String option;
    private String tag;
    private String action;
    private String block;

    @Override
    public Text getDisplayText() {
        return Text.literal(option).setStyle(Styles.BLOCK_TAG.getStyle());
    }

    @Override
    public Text getTooltip() {
        return Text.literal(tag).setStyle(Styles.COMMENT.getStyle());
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
