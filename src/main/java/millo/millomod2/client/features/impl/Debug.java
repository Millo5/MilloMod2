package millo.millomod2.client.features.impl;

import millo.millomod2.client.config.FeatureConfig;
import millo.millomod2.client.features.Feature;
import millo.millomod2.client.features.addons.Configurable;

public class Debug extends Feature implements Configurable {

    private static Debug INSTANCE;

    public Debug() {
        INSTANCE = this;
    }

    @Override
    public String getId() {
        return "debug";
    }

    @Override
    public void setupConfig(FeatureConfig config) {
        config.addBoolean("hud_info", false);
    }

    public static boolean showHudInfo() {
        return INSTANCE.config.getBoolean("hud_info");
    }

}
