package millo.millomod2.client.features.impl.Editor.elements;

import millo.millomod2.client.MilloMod;
import millo.millomod2.client.util.PlayerUtil;
import millo.millomod2.menu.Menu;
import millo.millomod2.menu.elements.ListElement;
import millo.millomod2.menu.elements.buttons.AbstractButton;
import millo.millomod2.menu.elements.buttons.ButtonElement;
import millo.millomod2.menu.elements.flex.CrossAxisAlignment;
import millo.millomod2.menu.elements.flex.ElementDirection;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public class HierarchyMethodElement extends AbstractButton<HierarchyMethodElement> {

    private final String templateName;
    private final CodeBrowser browser;

    private float highlight = 0f;

    public HierarchyMethodElement(CodeBrowser browser, String templateName, int width, int height) {
        super(0, 0, width, height, Text.empty());

        this.templateName = templateName;
        this.browser = browser;
    }

    @Override
    public void setMessage(Text message) {
        super.setMessage(message);

        int textWidth = getTextRenderer().getWidth(message);
        if (textWidth > getWidth() - 10) {
            setWidth(textWidth + 10);
        }
    }

    @Override
    protected HierarchyMethodElement self() {
        return this;
    }

    @Override
    public void onClick(Click click, boolean doubled) {
        if (click.button() == 1) {
            if (MilloMod.MC.currentScreen instanceof Menu menu) {
                ListElement contextMenu = ListElement.create(100, 20)
                        .background(0xCC222222)
                        .direction(ElementDirection.COLUMN)
                        .crossAlign(CrossAxisAlignment.STRETCH)
                        .gap(0);
                contextMenu.addChild(ButtonElement.create(100, 20)
                        .message(Text.literal("Give Item"))
                        .onPress(button -> {
                            ItemStack item = browser.getHierarchy().getTemplate(templateName).getItem();
                            PlayerUtil.giveItem(item);
                        })
                );
                menu.openContextMenu(contextMenu,
                        (int) MilloMod.MC.mouse.getScaledX(MilloMod.MC.getWindow()),
                        (int) MilloMod.MC.mouse.getScaledY(MilloMod.MC.getWindow())
                );
            }
            return;
        }

        browser.openTemplate(templateName);
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {


        if (highlight > 0f) {
            highlight -= deltaTicks * 0.1f;
            if (highlight < 0f) highlight = 0f;
//            context.fill(getX(), getY(), getRight(), getBottom(), 0x00FFFF00 | ((int) (highlight * 255) << 24));
        }

        int highlightColor = getHighlightColor();
        if (highlightColor != 0) {
            context.fill(getX(), getY(), getRight(), getBottom(), highlightColor);
        }

        super.renderWidget(context, mouseX, mouseY, deltaTicks);
    }

    private int getHighlightColor() {
        Tab tab = browser.getCurrentTab();
        if (tab != null && tab.getTemplate().getFileName().equals(templateName)) {
            highlight = 0f;
            return 0x4000FFFF;
        }

        if (highlight > 0f) {
            return 0x00FFFF00 | ((int) (highlight * 255) << 24);
        }
        return 0;
    }

    public void highlight() {
        highlight = 1f;
    }

    public String getTemplateName() {
        return templateName;
    }
}
