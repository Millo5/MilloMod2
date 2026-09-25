package millo.millomod2.client.features.impl.Editor.elements;

import millo.millomod2.client.MilloMod;
import millo.millomod2.client.features.FeatureHandler;
import millo.millomod2.client.features.impl.Editor.Editor;
import millo.millomod2.client.features.impl.Editor.EditorMenu;
import millo.millomod2.client.features.impl.Editor.logic.EditorPlot;
import millo.millomod2.client.menus.GuideMenu;
import millo.millomod2.menu.elements.ClickableElement;
import millo.millomod2.menu.elements.TextElement;
import millo.millomod2.menu.elements.buttons.ButtonElement;
import millo.millomod2.menu.elements.buttons.DropDownElement;
import millo.millomod2.menu.elements.flex.CrossAxisAlignment;
import millo.millomod2.menu.elements.flex.ElementDirection;
import millo.millomod2.menu.elements.flex.FlexElement;
import millo.millomod2.menu.elements.flex.MainAxisAlignment;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class TitleBar extends FlexElement<TitleBar> {

    private final EditorMenu menu;

    private final FlexElement<?> left;
    private final FlexElement<?> right;

    private DropDownElement currentPlotDropDown;

    protected TitleBar(int x, int y, int width, int height, Component message, EditorMenu menu) {
        super(x, y, width, height, message);
        this.menu = menu;

        left = FlexElement.create(width/2, 20)
                .direction(ElementDirection.ROW)
                .gap(5)
                .padding(10)
                .crossAlign(CrossAxisAlignment.CENTER);
        right = FlexElement.create(width/2, 20)
                .direction(ElementDirection.ROW)
                .mainAlign(MainAxisAlignment.END)
                .gap(5)
                .padding(5)
                .crossAlign(CrossAxisAlignment.CENTER);


        currentPlotDropDown = DropDownElement.create(100, 15)
                .offsetY(3)
                .background(0x33000000)
                .message(Component.literal("No Plot Loaded"))
                .addOption(Component.literal("Open..."), menu::openPlotSelector);

        if (!menu.getRecentPlots().isEmpty()) {
            currentPlotDropDown
                    .addSpacer()
                    .addHeader(Component.literal("Recent Plots").withColor(0x33AAAAAA));

            for (EditorPlot.Metadata recentPlot : menu.getRecentPlots()) {
                currentPlotDropDown
                    .addOption(Component.literal(recentPlot.name() + " (" + recentPlot.id() + ")"), (button) -> menu.loadPlot(recentPlot));
            }
        }


        ///

        left.addChildren(
                TextElement.create("Millo Mod \"Editor\""),
                currentPlotDropDown
        );


        right.addChild(
                ButtonElement.create(60, 15)
                        .message(Component.literal("Fetch All"))
                        .onPress(this::getAllTemplates)
        );

        right.addChildren(
//                ButtonElement.create(60, 15)
//                        .message(Text.literal("Clear"))
//                        .onPress(menu::clear),
                ButtonElement.create(60, 15)
                        .message(Component.literal("Search"))
                        .onPress(button -> menu.getMain().focusCodeBrowserSearch()),
                ButtonElement.create(15, 15)
                        .message(Component.literal("?"))
                        .onPress(button -> new GuideMenu(menu, "Editor").open()),
                ButtonElement.create(15, 15)
                        .message(Component.literal("X"))
                        .background(0xAAFF0000)
                        .onPress(button -> menu.onClose())
        );

        this.addChildren(
                left,
                right
        );
    }

    private void getAllTemplates(ButtonElement button) {
        Editor editor = FeatureHandler.get(Editor.class);
        editor.getAllTemplates();
    }

    public TitleBar(EditorMenu menu) {
        this(0, 0, menu.width, 20, Component.empty(), menu);

        direction(ElementDirection.ROW);
        mainAlign(MainAxisAlignment.SPACE_BETWEEN);
        crossAlign(CrossAxisAlignment.CENTER);
        border(new ClickableElement.Border().bottom(0xFFFFFFFF));
        background(0x33333333);
    }


    public void setLoadedPlot(EditorPlot plot) {
        if (plot == null) {
            MutableComponent text = Component.literal("No Plot Loaded");
            currentPlotDropDown.message(text);
            currentPlotDropDown.setWidth(Math.max(100, MilloMod.MC.font.width(text) + 10));
            return;
        }
        MutableComponent text = Component.literal(plot.getMetadata().name() + " (" + plot.getPlotId() + ")");
        currentPlotDropDown.message(text);
        currentPlotDropDown.setWidth(Math.max(150, MilloMod.MC.font.width(text) + 10));
    }
}
