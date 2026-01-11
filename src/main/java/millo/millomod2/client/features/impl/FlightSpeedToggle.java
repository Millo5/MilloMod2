package millo.millomod2.client.features.impl;

import millo.millomod2.client.config.FeatureConfig;
import millo.millomod2.client.features.Feature;
import millo.millomod2.client.features.addons.Configurable;
import millo.millomod2.client.features.addons.Keybound;
import millo.millomod2.client.util.PlayerUtil;

public class FlightSpeedToggle extends Feature implements Keybound, Configurable {


    @Override
    public String getId() {
        return "flight_speed_toggle";
    }

    @Override
    public void onTick() {
        while (getKeybind().wasPressed()) {
            if (MC.player != null && MC.player.getAbilities().allowFlying) {
                if (MC.player.getAbilities().getFlySpeed() > 0.05f) {
                    PlayerUtil.sendCommand("fs 100");
                } else {
                    PlayerUtil.sendCommand("fs " + config.getInt("speed"));
                }
            }
        }
    }

    @Override
    public void setupConfig(FeatureConfig config) {
        config.addIntegerRange("speed", 300, 0, 1000);
    }

}
