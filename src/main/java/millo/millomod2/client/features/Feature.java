package millo.millomod2.client.features;

import millo.millomod2.client.MilloMod;
import millo.millomod2.client.config.FeatureConfig;
import millo.millomod2.client.features.addons.Configurable;
import millo.millomod2.client.features.addons.Positional;
import millo.millomod2.client.features.addons.Toggleable;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;

import java.util.HashMap;

public abstract class Feature {

    protected final MinecraftClient MC = MilloMod.MC;
    protected HashMap<String, KeyBinding> keybinds = new HashMap<>();

    protected ClientPlayerEntity player() {
        return MilloMod.player();
    }
    protected ClientPlayNetworkHandler net() {
        return MilloMod.net();
    }


    public abstract String getId();

    public Feature() {

        if (this instanceof Toggleable toggleable) {
            config.addBoolean("enabled", toggleable.enabledByDefault());
            config.addListener("enabled", (from, to) -> toggleable.enabledChanged((Boolean) to));
        }

        if (this instanceof Configurable configurable) {
            configurable.setupConfig(config);
        }

        if (this instanceof Positional positional) {
            position = positional.defaultPosition();
        } else position = null;

    }

    public HashMap<String, KeyBinding> getKeybinds() {
        return keybinds;
    }

    public void onEndTick() {}

    public void onTick() {}


    // Feature addon data

    protected final FeatureConfig config = new FeatureConfig();
    public FeatureConfig getConfig() {
        return config;
    }

    protected final FeaturePosition position;
    public FeaturePosition getPosition() {
        return position;
    }
}
