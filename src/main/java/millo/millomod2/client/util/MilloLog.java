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

    private static void sendToPlayer(Text text) {
        if (MilloMod.player() == null) return;
        MilloMod.MC.send(() -> MilloMod.player().sendMessage(text, false));
    }

    public static void logInGame(String message, boolean debugOnly) {
        LOGGER.info(message);
        sendToPlayer(Text.literal("[MilloMod2] " + message));
    }

    public static void logInGame(Text text) {
        LOGGER.info(text.getString());
        sendToPlayer(Text.literal("[MilloMod2] ").append(text));
    }

    public static void error(String message) {
        LOGGER.error(message);
    }

    public static void errorInGame(String message) {
        error(message);
        sendToPlayer(Text.literal("[MilloMod2] Error: " + message).setStyle(Styles.SCARY.getStyle()));
    }

    public static void stackTrace(Exception e) {
        LOGGER.error("Exception: ", e);
        if (DEBUG) e.printStackTrace();
    }


    public static RuntimeException throwError(String s) {
        errorInGame(s);
        return new RuntimeException(s);
    }

    public static void logWarning(String s) {
        LOGGER.warn("Warning: {}", s);
        sendToPlayer(Text.literal("[MilloMod2] Warning: " + s).setStyle(Styles.ITEM.getStyle()));
    }
}
