package millo.millomod2.client.menus;

import millo.millomod2.client.MilloMod;
import millo.millomod2.client.net.UpdateService;
import millo.millomod2.menu.Menu;
import millo.millomod2.menu.elements.ListElement;
import millo.millomod2.menu.elements.buttons.ButtonElement;
import millo.millomod2.menu.elements.flex.CrossAxisAlignment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class UpdateMenu extends Menu {

    private ListElement main;

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

        UpdateService.update().thenAccept(result -> {
            if (result == UpdateService.UpdateResult.SUCCESS) {
                if (exit) {
                    Executors.newSingleThreadScheduledExecutor()
                            .schedule(() -> {
                                MilloMod.MC.execute(() -> {
                                    System.exit(0);
                                });
                            }, 3000, TimeUnit.MILLISECONDS);
                    return;
                }
                close();
            } else {
                setButtonStates(true);
            }
        });
    }

    protected void applyBlur(DrawContext context) {}

}
