package millo.millomod2.client.menus;

import millo.millomod2.client.config.PositionalElement;
import millo.millomod2.client.features.FeatureHandler;
import millo.millomod2.client.features.FeaturePosition;
import millo.millomod2.client.features.addons.Positional;
import millo.millomod2.client.features.addons.Toggleable;
import millo.millomod2.menu.Menu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;

public class ConfigPositionMenu extends Menu {

    public ConfigPositionMenu(Screen parent) {
        super(parent);
    }

    @Override
    protected void init() {
        FeatureHandler.forEach((feat) -> {
            if (feat instanceof Positional positional) {
                if (feat instanceof Toggleable tog && !tog.isEnabled()) return;
                FeaturePosition pos = positional.getPosition();
                addRenderableWidget(new PositionalElement(pos, feat));
            }
        });
    }

    @Override
    protected void renderBlurredBackground(GuiGraphics context) {
    }

    @Override
    public void onClose() {
        FeatureHandler.forEach((feat) -> {
            if (feat instanceof Positional positional) {
//                feat.getConfig().
            }
        });
        super.onClose();
    }
}
