package millo.millomod2.client.features.impl;

import millo.millomod2.client.MilloMod;
import millo.millomod2.client.config.FeatureConfig;
import millo.millomod2.client.features.Feature;
import millo.millomod2.client.features.FeaturePosition;
import millo.millomod2.client.features.addons.Configurable;
import millo.millomod2.client.features.addons.HUDRendered;
import millo.millomod2.client.features.addons.Positional;
import millo.millomod2.client.hypercube.data.HypercubeLocation;
import millo.millomod2.client.hypercube.data.Plot;
import millo.millomod2.client.hypercube.data.Spawn;
import millo.millomod2.client.util.HypercubeAPI;
import millo.millomod2.client.util.RenderInfo;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;

public class Debug extends Feature implements Configurable, HUDRendered, Positional {

    private static Debug INSTANCE;

    public Debug() {
        INSTANCE = this;
    }

    public static boolean logCommands() {
        return INSTANCE.config.getBoolean("log_commands");
    }

    @Override
    public String getId() {
        return "debug";
    }

    @Override
    public FeaturePosition defaultPosition() {
        return new FeaturePosition(20, 20, 40, 40);
    }

    @Override
    public void setupConfig(FeatureConfig config) {
        config.addBoolean("hud_info", false);
        config.addBoolean("show_tracker", false);
        config.addBoolean("log_commands", false);
    }

    public static boolean showHudInfo() {
        return INSTANCE.config.getBoolean("hud_info");
    }

    @Override
    public void HUDRender(RenderInfo renderInfo) {
        if (config.getBoolean("show_tracker")) renderTracker(renderInfo);
    }

    private void renderTracker(RenderInfo renderInfo) {
        boolean inHypercube = HypercubeAPI.isInHypercube();
        HypercubeAPI.Mode mode = HypercubeAPI.getMode();
        Vec3d plotOrigin = HypercubeAPI.getPlotOrigin();
        HypercubeLocation location = HypercubeAPI.getHypercubeLocation();
        TemporaryTracker.Sequence trackerStep = TemporaryTracker.getStep();

        ArrayList<String> lines = new ArrayList<>();
        lines.add("In Hypercube: " + inHypercube);
        lines.add("Tracker Step: " + trackerStep.name());
        lines.add("Mode: " + mode.name());
        lines.add("Plot Origin: " + plotOrigin);
        if (location instanceof Spawn) {
            lines.add("Location: Spawn");
        } else if (location instanceof Plot plot) {
            lines.add("Location: Plot");
            lines.add("  Id: " + plot.getId());
            lines.add("  Name: " + plot.getName());
            lines.add("  Has Underground: " + plot.hasUnderground());
        }
        lines.add("  " + location.toString());

        int yOffset = 0;
        for (String line : lines) {
            renderInfo.context().drawText(MilloMod.MC.textRenderer, line, getPosition().getX(), getPosition().getY() + yOffset, 0xFFFFFFFF, true);
            yOffset += 10;
        }
    }


}
