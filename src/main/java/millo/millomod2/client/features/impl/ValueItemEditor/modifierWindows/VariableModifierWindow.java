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
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.ArrayList;

public class VariableModifierWindow extends ModifierWindow {

    private final VariableArgumentModel value;
    private TextFieldElement name;

    public VariableModifierWindow(VariableArgumentModel value) {
        this.value = value;
    }

    @Override
    protected ClickableWidget getElement() {
        ListElement list = ListElement.create(100, 20)
                .direction(ElementDirection.COLUMN)
                .crossAlign(CrossAxisAlignment.STRETCH)
                .gap(5);

        name = new TextFieldElement(100, 20, Text.literal(value.getName()));
        name.setChangedListener(value::setName);

        FlexElement<?> scope = FlexElement.create(175, 20)
                .mainAlign(MainAxisAlignment.SPACE_BETWEEN)
                .crossAlign(CrossAxisAlignment.CENTER)
                .gap(1);

        TextRenderer textRenderer = MilloMod.MC.textRenderer;
        for (VariableScope variableScope : VariableScope.values()) {
            ButtonElement button = ButtonElement.create(textRenderer.getWidth(variableScope.name()) + 10, 20)
                    .message(Text.literal(variableScope.name()).setStyle(variableScope.getStyle().withItalic(false)))
                    .hoverBackground(0xA0FFFFFF)
                    .onPress((b) -> {
                        value.setScope(variableScope);
                        for (ClickableWidget child : scope.getChildren()) {
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
        stack.set(DataComponentTypes.CUSTOM_NAME, Text.literal(value.getName()).setStyle(Styles.DEFAULT.getStyle().withItalic(false)));

        LoreComponent lore = stack.get(DataComponentTypes.LORE);
        if (lore == null) return;

        var lines = new ArrayList<>(lore.styledLines());
        lines.set(0, Text.literal(value.getScope().name()).setStyle(value.getScope().getStyle().withItalic(false)));

        stack.set(DataComponentTypes.LORE, new LoreComponent(lines));
    }

    @Override
    protected ClickableWidget getDefaultFocus() {
        return name;
    }
}
