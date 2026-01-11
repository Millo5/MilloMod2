package millo.millomod2.client.features.addons;

import millo.millomod2.client.config.FeatureConfig;

/***
 * Marker interface for features that can be toggled on and off.
 */
public interface Toggleable {

    FeatureConfig getConfig();

    default boolean isEnabled() {
        return getConfig().getBoolean("enabled");
    }

    default boolean enabledByDefault() {
        return true;
    }

    default void enabledChanged(boolean enabled) {
        // Optional method to handle changes in enabled state.
    }

}
