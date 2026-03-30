package millo.millomod2.client.features.impl.Editor;

import millo.millomod2.menu.PopUpMenu;
import millo.millomod2.menu.elements.TextFieldElement;
import millo.millomod2.menu.elements.buttons.ButtonElement;
import millo.millomod2.menu.elements.flex.CrossAxisAlignment;
import millo.millomod2.menu.elements.flex.ElementDirection;
import millo.millomod2.menu.elements.flex.FlexElement;
import millo.millomod2.menu.elements.flex.MainAxisAlignment;
import net.minecraft.text.Text;

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
        addDrawableChild(screenFlex);

        FlexElement<?> centerFlex = FlexElement.create(200, 100)
                .background(0x80000000)
                .direction(ElementDirection.COLUMN)
                .mainAlign(MainAxisAlignment.CENTER)
                .crossAlign(CrossAxisAlignment.STRETCH)
                .padding(4)
                .gap(4);
        screenFlex.addChild(centerFlex);

        TextFieldElement plotIdField = new TextFieldElement(192, 20, Text.literal(""));
        plotIdField.setPlaceholder(Text.literal("Enter Plot ID..."));
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
                        .message(Text.literal("Cancel"))
                        .onPress((button) -> {
                            close();
                        })
        );
        buttonFlex.addChild(
                ButtonElement.create(70, 20)
                        .message(Text.literal("Load"))
                        .onPress((button) -> {
                            String input = plotIdField.getText();
                            try {
                                int plotId = Integer.parseInt(input);
                                parent.loadPlot(plotId);
                                close();
                            } catch (NumberFormatException e) {
                                // Invalid input handling
                            }
                        })
        );


    }
}
