package millo.millomod2.client.features.addons;

import com.mojang.blaze3d.platform.InputConstants;
import java.util.HashMap;
import net.minecraft.client.KeyMapping;

public interface Keybound {

    HashMap<String, KeyMapping> getKeybinds();

    default void registerKeybind(String id, KeyMapping key) {
        getKeybinds().put(id, key);
    }

    default KeyMapping getKeybind(String id) {
        return getKeybinds().get(id);
    }

    /***
     * Gets the default keybind with the id "key", not always present
     * @return The default keybind
     */
    default KeyMapping getKeybind() {
        if (!getKeybinds().containsKey("key")) {
            throw new IllegalStateException("No default keybind registered for " + getId());
        }
        return getKeybind("key");
    }

    String getId();
    default String[] getKeybindIds() {
        return new String[] {"key"};
    }

    default InputConstants.Type getDefaultType() {
        return InputConstants.Type.KEYSYM;
    }
    default int getDefaultCode() {
        return -1;
    }

}
