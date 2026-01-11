package millo.millomod2.client.features.addons;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

import java.util.HashMap;

public interface Keybound {

    HashMap<String, KeyBinding> getKeybinds();

    default void registerKeybind(String id, KeyBinding key) {
        getKeybinds().put(id, key);
    }

    default KeyBinding getKeybind(String id) {
        return getKeybinds().get(id);
    }

    /***
     * Gets the default keybind with the id "key", not always present
     * @return The default keybind
     */
    default KeyBinding getKeybind() {
        if (!getKeybinds().containsKey("key")) {
            throw new IllegalStateException("No default keybind registered for " + getId());
        }
        return getKeybind("key");
    }

    String getId();
    default String[] getKeybindIds() {
        return new String[] {"key"};
    }

    default InputUtil.Type getDefaultType() {
        return InputUtil.Type.KEYSYM;
    }
    default int getDefaultCode() {
        return -1;
    }

}
