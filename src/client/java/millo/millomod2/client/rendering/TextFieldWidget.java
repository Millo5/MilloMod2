package millo.millomod2.client.rendering;

import millo.millomod2.client.MilloMod;
import net.minecraft.client.gui.DrawContext;
import net.sapfii.sapscreens.screens.widgets.Widget;

public class TextFieldWidget extends Widget<TextFieldWidget> {

    net.minecraft.client.gui.widget.TextFieldWidget textField;

    public TextFieldWidget(int x, int y, int width, int height) {
        textField = new net.minecraft.client.gui.widget.TextFieldWidget(MilloMod.MC.textRenderer, x, y, width, height, null);
        textField.setMaxLength(256);
        textField.setDrawsBackground(false);
    }

    public net.minecraft.client.gui.widget.TextFieldWidget getTextField() {
        return textField;
    }

    @Override
    public void render(DrawContext drawContext, int i, int i1, float v) {
        textField.render(drawContext, i, i1, v);
    }

    @Override
    public TextFieldWidget getThis() {
        return this;
    }
}
