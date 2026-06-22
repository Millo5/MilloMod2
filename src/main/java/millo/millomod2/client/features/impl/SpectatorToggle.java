package millo.millomod2.client.features.impl;

import millo.millomod2.client.config.FeatureConfig;
import millo.millomod2.client.features.Feature;
import millo.millomod2.client.features.addons.Configurable;
import millo.millomod2.client.features.addons.Keybound;
import millo.millomod2.client.util.PlayerUtil;

public class SpectatorToggle extends Feature implements Keybound, Configurable {

    @Override
    public String getId() {
        return "spectator_toggle";
    }

    @Override
    public void setupConfig(FeatureConfig config) {
        config.addBoolean("hold_toggle", false);
    }

    private boolean wasPressed = false;

    @Override
    public void onTick() {
        if (player() == null) return;
        if (getConfig().getBoolean("hold_toggle")) {
            if (player().isCreative() && getKeybind().isPressed() && !wasPressed) {
                PlayerUtil.sendCommand("gmsp");
                wasPressed = true;
                return;
            }
            if (!player().isCreative() && !getKeybind().isPressed() && wasPressed) {
                PlayerUtil.sendCommand("gmc");
                wasPressed = false;
            }
            return;
        }

        while (getKeybind("key").wasPressed()) {
            if (player().isCreative()) {
                PlayerUtil.sendCommand("gmsp");
            } else {
                PlayerUtil.sendCommand("gmc");
            }
            player().sendAbilitiesUpdate();
        }
    }

}
