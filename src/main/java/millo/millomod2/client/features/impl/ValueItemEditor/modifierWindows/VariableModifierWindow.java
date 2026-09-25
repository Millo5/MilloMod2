package millo.millomod2.client.features.impl.ValueItemEditor.modifierWindows;

import millo.millomod2.client.MilloMod;
import millo.millomod2.client.features.impl.ValueItemEditor.ModifierWindow;
import millo.millomod2.client.hypercube.data.VariableScope;
import millo.millomod2.client.hypercube.model.arguments.VariableArgumentModel;
import millo.millomod2.client.util.style.Styles;
import millo.millomod2.menu.elements.ClickableElement;
import millo.millomod2.menu.elements.ListElement;
import millo.millomod2.menu.elements.TextFieldElement;
import millo.millomod2.menu.elements.buttons.ButtonElement;
import millo.millomod2.menu.elements.flex.CrossAxisAlignment;
import millo.millomod2.menu.elements.flex.ElementDirection;
import millo.millomod2.menu.elements.flex.FlexElement;
import millo.millomod2.menu.elements.flex.MainAxisAlignment;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;
import java.util.ArrayList;

public class VariableModifierWindow extends ModifierWindow {

    private final VariableArgumentModel value;
    private TextFieldElement name;

    public VariableModifierWindow(VariableArgumentModel value) {
        this.value = value;
    }

    @Override
    protected AbstractWidget getElement() {
        ListElement list = ListElement.create(100, 20)
                .direction(ElementDirection.COLUMN)
                .crossAlign(CrossAxisAlignment.STRETCH)
                .gap(5);

        name = new TextFieldElement(100, 20, Component.literal(value.getName()));
        name.setMaxLength(10000);
        name.setResponder(value::setName);

        FlexElement<?> scope = FlexElement.create(175, 20)
                .mainAlign(MainAxisAlignment.SPACE_BETWEEN)
                .crossAlign(CrossAxisAlignment.CENTER)
                .gap(1);

        Font textRenderer = MilloMod.MC.font;
        for (VariableScope variableScope : VariableScope.values()) {
            ButtonElement button = ButtonElement.create(textRenderer.width(variableScope.name()) + 10, 20)
                    .message(Component.literal(variableScope.name()).setStyle(variableScope.getStyle().withItalic(false)))
                    .hoverBackground(0xA0FFFFFF)
                    .onPress((b) -> {
                        value.setScope(variableScope);
                        for (AbstractWidget child : scope.getChildren()) {
                            if (child instanceof ButtonElement btn) {
                                btn.border(new ClickableElement.Border());
                            }
                        }
                        b.border(new ClickableElement.Border().full(0xFFFFFFFF));
                    });
            if (variableScope == value.getScope()) {
                button.border(new ClickableElement.Border().full(0xFFFFFFFF));
            }
            scope.addChild(button);
        }

        list.addChildren(name, scope);

        return list;
    }

    @Override
    protected String getTitle() {
        return "Variable";
    }

    @Override
    public void applyToItem(ItemStack stack) {
        stack.set(DataComponents.CUSTOM_NAME, Component.literal(value.getName()).setStyle(Styles.DEFAULT.getStyle().withItalic(false)));

        ItemLore lore = stack.get(DataComponents.LORE);
        if (lore == null) return;

        var lines = new ArrayList<>(lore.styledLines());
        lines.set(0, Component.literal(value.getScope().name()).setStyle(value.getScope().getStyle().withItalic(false)));

        stack.set(DataComponents.LORE, new ItemLore(lines));
    }

    @Override
    protected AbstractWidget getDefaultFocus() {
        return name;
    }
}
