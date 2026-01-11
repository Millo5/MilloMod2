package millo.millomod2.client.features.impl;

import millo.millomod2.client.features.Feature;
import millo.millomod2.client.features.addons.Keybound;
import millo.millomod2.client.util.PlayerUtil;

public class SpectatorToggle extends Feature implements Keybound {

    @Override
    public String getId() {
        return "spectator_toggle";
    }

    @Override
    public void onTick() {
        while (getKeybind("key").wasPressed()) {
            if (player() == null) return;
            if (player().isCreative()) {
                PlayerUtil.sendCommand("gmsp");
            } else {
                PlayerUtil.sendCommand("gmc");
            }
            player().sendAbilitiesUpdate();
        }
    }

}
