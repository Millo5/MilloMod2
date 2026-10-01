package millo.millomod2.client.menus;

import millo.millomod2.client.MilloMod;
import millo.millomod2.client.net.UpdateService;
import millo.millomod2.client.util.logging.MilloLog;
import millo.millomod2.client.util.style.Styles;
import millo.millomod2.menu.Menu;
import millo.millomod2.menu.elements.ListElement;
import millo.millomod2.menu.elements.TextElement;
import millo.millomod2.menu.elements.buttons.ButtonElement;
import millo.millomod2.menu.elements.flex.CrossAxisAlignment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class UpdateMenu extends Menu {

    private ListElement main;
    private TextElement status;

    public UpdateMenu(Screen parent) {
        super(parent);
    }

    @Override
    protected void init() {
        main = ListElement.create(width /4 * 3, height)
                .position(width / 8, 0)
                .maxExpansion(height)
                .crossAlign(CrossAxisAlignment.CENTER)
                .padding(40)
                .gap(10);
        addRenderableWidget(main);

        status = TextElement.create("Ready");
        status.setWidth(250);
        main.addChild(status);

        main.addChild(ButtonElement.create(200, 20)
                .message(Component.literal("Update Now"))
                .onPress((b) -> {
                    update(true);
                })
                .background(0x80000000)
        );

        main.addChild(ButtonElement.create(200, 20)
                .message(Component.literal("Update on Exit"))
                .onPress((b) -> {
                    update(false);
                })
                .background(0x80000000)
        );

        main.addChild(ButtonElement.create(200, 20)
                .message(Component.literal("Ignore Forever"))
                .onPress((b) -> {
                    UpdateService.ignoreUpdates();
                    onClose();
                })
                .background(0x80000000)
        );

        main.addChild(ButtonElement.create(200, 20)
                .message(Component.literal("Not Now"))
                .onPress((b) -> {
                    onClose();
                })
                .background(0x80000000)
        );

    }

    public void setButtonStates(boolean active) {
        for (var child : main.getChildren()) {
            if (child instanceof ButtonElement button) {
                button.active = active;
            }
        }
    }

    private void update(boolean exit) {
        setButtonStates(false);
        status.setMessage(Component.literal("Downloading and validating update...").setStyle(Styles.COMMENT.getStyle()));

        UpdateService.update().whenComplete((result, throwable) -> MilloMod.MC.execute(() -> {
            if (throwable != null) {
                MilloLog.error("Update failed unexpectedly: " + throwable.getMessage());
                status.setMessage(Component.literal("Update failed. Check the log.").setStyle(Styles.SCARY.getStyle()));
                setButtonStates(true);
                return;
            }
            if (result != UpdateService.UpdateResult.SUCCESS) {
                status.setMessage(Component.literal("Update failed. Check the log.").setStyle(Styles.SCARY.getStyle()));
                setButtonStates(true);
                return;
            }

            if (exit) {
                status.setMessage(Component.literal("Update ready. Restarting...").setStyle(Styles.TRUE.getStyle()));
                MilloMod.schedule(() -> {
                    System.exit(0);
                }, 3000);
            } else {
                onClose();
            }
        }));
    }

    protected void extractBlurredBackground(GuiGraphicsExtractor graphics) {}

}
