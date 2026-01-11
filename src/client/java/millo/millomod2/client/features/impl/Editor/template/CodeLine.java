package millo.millomod2.client.features.impl.Editor.template;

import net.minecraft.text.Text;

public interface CodeLine extends CodeEntry{
    Text DOT = Text.literal(".");
    Text SPACE = Text.literal(" ");

    Text getDisplayText();
}
