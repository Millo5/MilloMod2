package millo.millomod2.client.util;

import millo.millomod2.client.MilloMod;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.util.HashMap;

public class KeyUtil {

    public static boolean isKeyDown(KeyBinding keyBind) {
        try {
            String cname = FabricLoader.getInstance().isDevelopmentEnvironment() ? "boundKey" : "field_1655";
            int keycode = ((InputUtil.Key) FieldUtils.getField(KeyBinding.class, cname, true).get(keyBind)).getCode();
            if (keycode == -1) return false;
            return InputUtil.isKeyPressed(MilloMod.MC.getWindow(), keycode);
        } catch (IllegalAccessException e) {
            return false;
        }
    }

    private static final HashMap<KeyBinding, Integer> keyDuration = new HashMap<>();
    public static boolean isKeyPressed(KeyBinding keyBind) {
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
