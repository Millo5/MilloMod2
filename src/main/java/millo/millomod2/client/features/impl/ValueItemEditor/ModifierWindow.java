package millo.millomod2.client.features.impl.ValueItemEditor;

import millo.millomod2.menu.elements.ClickableElement;
import millo.millomod2.menu.elements.ListElement;
import millo.millomod2.menu.elements.TextElement;
import millo.millomod2.menu.elements.flex.CrossAxisAlignment;
import millo.millomod2.menu.elements.flex.ElementDirection;
import millo.millomod2.menu.elements.flex.FlexElement;
import millo.millomod2.menu.elements.flex.MainAxisAlignment;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.world.item.ItemStack;

public abstract class ModifierWindow {

    protected abstract AbstractWidget getElement();
    protected abstract String getTitle();
    protected abstract AbstractWidget getDefaultFocus();

    public abstract void applyToItem(ItemStack stack);


    public final ListElement getWindow() {
        ListElement listElement = ListElement.create(200, 20)
                .direction(ElementDirection.COLUMN)
                .crossAlign(CrossAxisAlignment.STRETCH)
                .background(0xA0000000)
                .border(new ClickableElement.Border().full(0xFFFFFFFF))
                .gap(1)
                .padding(3);

        listElement.addChild(TextElement.create(getTitle())
                .align(TextElement.TextAlignment.CENTER));
        listElement.addChild(getElement());

        listElement.setFocus(getDefaultFocus());

        return listElement;
    }

    protected AbstractWidget encase(ListElement parent, String label, AbstractWidget widget) {
        FlexElement<? extends FlexElement<?>> flex = FlexElement.create(parent.getWidth(), widget.getHeight())
                .crossAlign(CrossAxisAlignment.CENTER)
                .mainAlign(MainAxisAlignment.SPACE_BETWEEN)
                .gap(1);

        TextElement text = TextElement.create(label);
        flex.addChild(text);
        widget.setWidth(parent.getWidth() - text.getWidth());
        flex.addChild(widget);

        return flex;
    }

}
