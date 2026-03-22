package millo.millomod2.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import millo.millomod2.client.commands.CommandHandler;
import millo.millomod2.client.config.saving.ConfigSaving;
import millo.millomod2.client.features.FeatureHandler;
import millo.millomod2.client.util.ModAPI;
import millo.millomod2.client.hypercube.modapi.ModAPIPayload;
import millo.millomod2.client.util.MilloLog;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class MilloMod implements ClientModInitializer {

    public static final String MOD_ID = "millomod2";
    public static final String MOD_VERSION = FabricLoader.getInstance().getModContainer(MOD_ID).get().getMetadata().getVersion().getFriendlyString();
    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();
    public static final Gson GSON_COMPACT = new Gson();

    public static MinecraftClient MC = MinecraftClient.getInstance();
    public static ClientPlayNetworkHandler net() {
        return MC.getNetworkHandler();
    }
    public static ClientPlayerEntity player() {
        return MC.player;
    }

    private static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();


    @Override
    public void onInitializeClient() {
        FeatureHandler.register(); // Initialize features

        try {
            ConfigSaving.getInstance().load();
        } catch (IOException e) {
            MilloLog.error("Failed to load config: " + e.getMessage());
        }

        ClientCommandRegistrationCallback.EVENT.register(CommandHandler::register);

        PayloadTypeRegistry.playS2C().register(ModAPIPayload.ID, ModAPIPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(ModAPIPayload.ID, ModAPIPayload.CODEC);

        ClientPlayNetworking.registerGlobalReceiver(ModAPIPayload.ID, ModAPI::onPayload);

    }

    public static void schedule(Runnable task, long delayMs) {
        executor.schedule(() -> MilloMod.MC.execute(task), delayMs, java.util.concurrent.TimeUnit.MILLISECONDS);
    }

    public static MutableText translatable(String... keys) {
        return Text.translatable("millo." + String.join(".", keys));
    }


    /*

    editor:
        hierarchy
            folders
            saving
        searching
        pinning tabs
        find tab in hierarchy (highlight in hierarchy)
        "else" block looks weird

    ultrakill mode
    airhits in dev

    action reference display
    action argument display (per slot)


    Icon


     */

}
