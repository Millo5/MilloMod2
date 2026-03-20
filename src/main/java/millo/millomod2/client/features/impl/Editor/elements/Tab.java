package millo.millomod2.client.features.impl.Editor.elements;

import millo.millomod2.client.MilloMod;
import millo.millomod2.client.features.impl.Editor.logic.model.TemplateModel;
import millo.millomod2.menu.elements.buttons.ButtonElement;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class Tab extends ButtonElement {

    private final ButtonElement closeButton;
    private final CodeBrowser browser;
    private final int textWidth;
    private boolean selected = false;
    private TemplateModel template;

    public Tab(CodeBrowser browser, String displayName, TemplateModel template) {
        super(0, 0, 100, 20, Text.literal(displayName));
        this.template = template;
        this.browser = browser;

        textWidth = getTextRenderer().getWidth(displayName);
        setWidth(textWidth + 22);

        hoverBackground(0x30ffffff);

        closeButton = ButtonElement.create(16, 16)
                .position(textWidth + 4, 2)
                .hoverBackground(0x30ffffff)
                .message(Text.literal("x"))
                .onPress((button) -> browser.closeTab(this));
    }

    @Override
    public void onClick(Click click, boolean doubled) {
        if (closeButton.mouseClicked(click, doubled)) return;

        if (click.button() == 2) {
            browser.closeTab(this);
            return;
        }

        browser.openTemplate(template);
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        boolean hovered = isMouseOver(mouseX, mouseY) && !closeButton.isMouseOver(mouseX, mouseY);

        int background = this.background;
        if (getHoverBackgroundColor() >= 0) background(hovered ? getHoverBackgroundColor() : getBackground());
        if (selected) background(0x2000ffff);
        context.fill(getX(), getY(), getRight(), getBottom(), getFade().getColor(getBackground()));
        getBorder().render(context, this);
        this.background = background;

        int textColor = hovered ? 0xFFFFFFAA : 0xFFFFFFFF;
        context.drawTextWithShadow(MilloMod.MC.textRenderer, getMessage(), getX() + 4, getY() + (getHeight() - 8) / 2, textColor);

        closeButton.setX(getX() + textWidth + 4);
        closeButton.render(context, mouseX, mouseY, deltaTicks);
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public TemplateModel getTemplate() {
        return template;
    }

    public void setTemplate(TemplateModel template) {
        this.template = template;
    }
}
