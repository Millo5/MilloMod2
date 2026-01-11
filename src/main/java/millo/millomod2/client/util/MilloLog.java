package millo.millomod2.client.util;

import millo.millomod2.client.MilloMod;
import millo.millomod2.client.util.style.Styles;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MilloLog {

    private static final boolean DEBUG = FabricLoader.getInstance().isDevelopmentEnvironment();
    public static final Logger LOGGER = LoggerFactory.getLogger("MilloMod");

    public static void log(String message) {
        if (!DEBUG) return;
        LOGGER.info(message);
    }

    public static void logInGame(String message) {
        logInGame(message, true);
    }

    public static void logInGame(String message, boolean debugOnly) {
        LOGGER.info(message);
        if (debugOnly && !DEBUG) return;
        MilloMod.player().sendMessage(Text.literal("[MilloMod2] " + message), false);
    }

    public static void error(String message) {
        LOGGER.error(message);
    }

    public static void errorInGame(String message) {
        error(message);
        MilloMod.player().sendMessage(Text.literal("[MilloMod2] Error: " + message).setStyle(Styles.SCARY.getStyle()), false);
    }

    public static void stackTrace(Exception e) {
        LOGGER.error("Exception: ", e);
        if (DEBUG) e.printStackTrace();
    }
}
