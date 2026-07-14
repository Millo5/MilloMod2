package millo.millomod2.client.menus;

import millo.millomod2.client.MilloMod;
import millo.millomod2.client.net.UpdateService;
import millo.millomod2.client.util.MilloLog;
import millo.millomod2.client.util.style.Styles;
import millo.millomod2.menu.Menu;
import millo.millomod2.menu.elements.ListElement;
import millo.millomod2.menu.elements.TextElement;
import millo.millomod2.menu.elements.buttons.ButtonElement;
import millo.millomod2.menu.elements.flex.CrossAxisAlignment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

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
        addDrawableChild(main);

        status = TextElement.create("Ready");
        status.setWidth(250);
        main.addChild(status);

        main.addChild(ButtonElement.create(200, 20)
                .message(Text.literal("Update Now"))
                .onPress((b) -> {
                    update(true);
                })
                .background(0x80000000)
        );

        main.addChild(ButtonElement.create(200, 20)
                .message(Text.literal("Update on Exit"))
                .onPress((b) -> {
                    update(false);
                })
                .background(0x80000000)
        );

        main.addChild(ButtonElement.create(200, 20)
                .message(Text.literal("Ignore Forever"))
                .onPress((b) -> {
                    UpdateService.ignoreUpdates();
                    close();
                })
                .background(0x80000000)
        );

        main.addChild(ButtonElement.create(200, 20)
                .message(Text.literal("Not Now"))
                .onPress((b) -> {
                    close();
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
        status.setMessage(Text.literal("Downloading and validating update...").setStyle(Styles.COMMENT.getStyle()));

        UpdateService.update().whenComplete((result, throwable) -> MilloMod.MC.execute(() -> {
            if (throwable != null) {
                MilloLog.error("Update failed unexpectedly: " + throwable.getMessage());
                status.setMessage(Text.literal("Update failed. Check the log.").setStyle(Styles.SCARY.getStyle()));
                setButtonStates(true);
                return;
            }
            if (result != UpdateService.UpdateResult.SUCCESS) {
                status.setMessage(Text.literal("Update failed. Check the log.").setStyle(Styles.SCARY.getStyle()));
                setButtonStates(true);
                return;
            }

            if (exit) {
                status.setMessage(Text.literal("Update ready. Restarting...").setStyle(Styles.TRUE.getStyle()));
                MilloMod.schedule(() -> {
                    System.exit(0);
                }, 3000);
            } else {
                close();
            }
        }));
    }

    protected void applyBlur(DrawContext context) {}

}
