package millo.millomod2.client.util;

import com.mojang.blaze3d.platform.InputConstants;
import millo.millomod2.client.MilloMod;
import millo.millomod2.client.mixin.core.KeyMappingAccessor;
import net.minecraft.client.KeyMapping;

import java.util.HashMap;

public class KeyUtil {

    public static boolean isKeyDown(KeyMapping keyBind) {
        return isKeyDown(((KeyMappingAccessor) keyBind).getKey().getValue());
    }

    public static boolean isKeyDown(int keycode) {
        if (keycode == -1) return false;
        return InputConstants.isKeyDown(MilloMod.MC.getWindow(), keycode);
    }

    private static final HashMap<KeyMapping, Integer> keyDuration = new HashMap<>();
    public static boolean isKeyPressed(KeyMapping keyBind) {
        if (!keyDuration.containsKey(keyBind)) keyDuration.put(keyBind, 0);
        if (isKeyDown(keyBind)) {
            int dur = keyDuration.get(keyBind) + 1;
            keyDuration.put(keyBind, dur);
            return dur == 1;
        }
        keyDuration.put(keyBind, 0);
        return false;
    }

}
