package millo.millomod2.client.menus;

import millo.millomod2.client.features.FeatureHandler;
import millo.millomod2.client.features.impl.BlueprintLoader.BlueprintLoader;
import millo.millomod2.menu.Menu;
import millo.millomod2.menu.elements.ListElement;
import millo.millomod2.menu.elements.buttons.ButtonElement;
import millo.millomod2.menu.elements.flex.CrossAxisAlignment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.ArrayList;

public class BlueprintsMenu extends Menu {

    private final BlueprintLoader feat;
    private ArrayList<String> found;

    public BlueprintsMenu(Screen parent) {
        super(parent);

        feat = FeatureHandler.get(BlueprintLoader.class);
        found = feat.getFoundBlueprints();
        if (found.isEmpty()) {
            feat.searchForBlueprints();
            found = feat.getFoundBlueprints();
        }
    }

    @Override
    protected void init() {
        ListElement main = ListElement.create(width /4 * 3, height)
                .position(width / 8, 0)
                .maxExpansion(height)
                .crossAlign(CrossAxisAlignment.CENTER)
                .padding(40)
                .gap(10);
        addDrawableChild(main);

        for (String blueprint : found) {
            ButtonElement button = ButtonElement.create(200, 20)
                    .message(Text.literal(blueprint))
                    .onPress(b -> {
                        feat.readBlueprint(blueprint);
                        close();
                    });
            main.addChild(button);
        }

        main.addChildren(ButtonElement.create(200, 20)
                .message(Text.literal("Refresh"))
                .onClick(b -> {
                    feat.searchForBlueprints();
                    found = feat.getFoundBlueprints();
                    clearAndInit();
                }),
            ButtonElement.create(200, 20)
                    .message(Text.literal("Get Template"))
                    .onClick(b -> {
                        feat.giveTemplate();
                    }));
    }
}
