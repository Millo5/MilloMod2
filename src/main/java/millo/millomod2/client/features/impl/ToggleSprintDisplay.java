package millo.millomod2.client.features.impl;

import millo.millomod2.client.MilloMod;
import millo.millomod2.client.config.FeatureConfig;
import millo.millomod2.client.features.Feature;
import millo.millomod2.client.features.FeaturePosition;
import millo.millomod2.client.features.addons.Configurable;
import millo.millomod2.client.features.addons.HUDRendered;
import millo.millomod2.client.features.addons.Positional;
import millo.millomod2.client.features.addons.Toggleable;
import millo.millomod2.client.util.RenderInfo;
import millo.millomod2.client.util.style.Styles;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;

import java.awt.*;

public class ToggleSprintDisplay extends Feature implements Toggleable, Configurable, Positional, HUDRendered {

    private Component displayText = Component.literal("Sprint Toggled").setStyle(Styles.COMMENT.getStyle());

    @Override
    public String getId() {
        return "toggle_sprint_display";
    }

    @Override
    public void setupConfig(FeatureConfig config) {
        config.addString("text", "Sprint Toggled");
        config.addListener("text", (from, to) -> {
            displayText = Component.literal((String) to).setStyle(Styles.COMMENT.getStyle());
        });
    }

    @Override
    public boolean enabledByDefault() {
        return false;
    }

    @Override
    public FeaturePosition defaultPosition() {
        return new FeaturePosition(5, 5, 100, 10, FeaturePosition.Anchor.BOTTOM_LEFT);
    }

    @Override
    public void HUDRender(RenderInfo renderInfo) {
        if (!isEnabled()) return;
        GuiGraphicsExtractor graphics = renderInfo.graphics();

        if (player().input.keyPresses.sprint()) {
            int x = position.getX();
            int y = position.getY();
            graphics.text(MilloMod.MC.font, displayText, x, y, Color.WHITE.hashCode(), true);
        }
    }

}
