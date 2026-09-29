package millo.millomod2.client.menus;

import millo.millomod2.client.MilloMod;
import millo.millomod2.client.features.FeatureHandler;
import millo.millomod2.client.features.impl.Waypoints.Waypoint;
import millo.millomod2.client.features.impl.Waypoints.Waypoints;
import millo.millomod2.menu.Menu;
import millo.millomod2.menu.elements.ListElement;
import millo.millomod2.menu.elements.TextElement;
import millo.millomod2.menu.elements.buttons.ButtonElement;
import millo.millomod2.menu.elements.flex.CrossAxisAlignment;
import millo.millomod2.menu.elements.flex.ElementDirection;
import millo.millomod2.menu.elements.flex.FlexElement;
import millo.millomod2.menu.elements.flex.MainAxisAlignment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class WaypointMenu extends Menu {

    private final Waypoints feature;

    public WaypointMenu(Screen parent) {
        super(parent);
        feature = FeatureHandler.get(Waypoints.class);
    }

    @Override
    protected void init() {
        ListElement main = ListElement.create(width /4 * 3, height)
                .position(width / 8, 0)
                .maxExpansion(height)
                .crossAlign(CrossAxisAlignment.CENTER)
                .padding(40)
                .gap(10);
        addRenderableWidget(main);

        main.addChild(ButtonElement.create(200, 20)
                .message(Component.literal("Help"))
                .onPress(button -> new GuideMenu(this, "Waypoints").open())
                .background(0x80000000)
        );

        for (Waypoint waypoint : feature.getWaypoints()) {
            FlexElement<?> box = FlexElement.create(width / 2, 20)
                    .background(0x80000000)
                    .direction(ElementDirection.ROW)
                    .mainAlign(MainAxisAlignment.SPACE_BETWEEN)
                    .crossAlign(CrossAxisAlignment.CENTER)
                    .padding(20)
                    .gap(5);

            FlexElement<?> wpButtons = FlexElement.create(40, 14)
                    .direction(ElementDirection.ROW)
                    .mainAlign(MainAxisAlignment.SPACE_BETWEEN)
                    .crossAlign(CrossAxisAlignment.CENTER)
                    .gap(5);
            wpButtons.addChildren(
                    ButtonElement.create(18, 14)
                            .background(0x80000000)
                            .message(Component.literal("TP"))
                            .onPress((button) -> waypoint.teleport()),
                    ButtonElement.create(14, 14)
                            .background(0x80ff0000)
                            .message(Component.literal("x"))
                            .onPress((button) -> {
                                feature.removeWaypoint(waypoint);
                                rebuildWidgets();
                            })
            );

            var wpLabel = TextElement.create(waypoint.label());
            box.addChildren(wpLabel, wpButtons);

            main.addChild(box);
        }

        main.addChild(ButtonElement.create(200, 20)
                .message(Component.literal("Add Waypoint"))
                .onClick((click) -> {
                    if (MilloMod.player() == null) return;
                    if (click.click().button() == 1) {
                        feature.addTemporaryWaypoint(MilloMod.player().position());
                        onClose();
                        return;
                    }
                    new AddWaypointMenu(this, MilloMod.player().position()).open();
                })
                .background(0x80000000)
        );


    }

    protected void extractBlurredBackground(GuiGraphicsExtractor graphics) {}

}
