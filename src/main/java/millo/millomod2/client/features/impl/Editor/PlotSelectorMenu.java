package millo.millomod2.client.features.impl.Editor;

import millo.millomod2.menu.PopUpMenu;
import millo.millomod2.menu.elements.TextFieldElement;
import millo.millomod2.menu.elements.buttons.ButtonElement;
import millo.millomod2.menu.elements.flex.CrossAxisAlignment;
import millo.millomod2.menu.elements.flex.ElementDirection;
import millo.millomod2.menu.elements.flex.FlexElement;
import millo.millomod2.menu.elements.flex.MainAxisAlignment;
import net.minecraft.network.chat.Component;

public class PlotSelectorMenu extends PopUpMenu {

    private final EditorMenu parent;
    public PlotSelectorMenu(EditorMenu parent) {
        super(parent);
        this.parent = parent;
    }

    @Override
    protected void init() {

        FlexElement<?> screenFlex = FlexElement.create(width, height)
                .mainAlign(MainAxisAlignment.CENTER)
                .crossAlign(CrossAxisAlignment.CENTER)
                .padding(0)
                .gap(0);
        addRenderableWidget(screenFlex);

        FlexElement<?> centerFlex = FlexElement.create(200, 100)
                .background(0x80000000)
                .direction(ElementDirection.COLUMN)
                .mainAlign(MainAxisAlignment.CENTER)
                .crossAlign(CrossAxisAlignment.STRETCH)
                .padding(4)
                .gap(4);
        screenFlex.addChild(centerFlex);

        TextFieldElement plotIdField = new TextFieldElement(192, 20, Component.literal(""));
        plotIdField.setHint(Component.literal("Enter Plot ID..."));
        centerFlex.addChild(plotIdField);

        FlexElement<?> buttonFlex = FlexElement.create(200, 20)
                .direction(ElementDirection.ROW)
                .mainAlign(MainAxisAlignment.SPACE_BETWEEN)
                .crossAlign(CrossAxisAlignment.CENTER)
                .padding(0)
                .gap(4);
        centerFlex.addChild(buttonFlex);
        buttonFlex.addChild(
                ButtonElement.create(70, 20)
                        .message(Component.literal("Cancel"))
                        .onPress((button) -> {
                            onClose();
                        })
        );
        buttonFlex.addChild(
                ButtonElement.create(70, 20)
                        .message(Component.literal("Load"))
                        .onPress((button) -> {
                            String input = plotIdField.getValue();
                            try {
                                int plotId = Integer.parseInt(input);
                                parent.loadPlot(plotId);
                                onClose();
                            } catch (NumberFormatException e) {
                                // Invalid input handling
                            }
                        })
        );


    }
}
