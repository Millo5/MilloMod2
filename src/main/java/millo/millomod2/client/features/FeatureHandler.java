package millo.millomod2.client.features;

import millo.millomod2.client.features.addons.Keybound;
import millo.millomod2.client.features.impl.*;
import millo.millomod2.client.features.impl.CommandWheel.CommandWheel;
import millo.millomod2.client.features.impl.Editor.Editor;
import millo.millomod2.client.features.impl.Notifications.Notifications;
import millo.millomod2.client.features.impl.QuickValueItem.QuickValueItem;
import millo.millomod2.client.util.MilloLog;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * *Cache GUI
 * SoundPreview (actiondump)
 * SideChat
 * *Play/Dev/Build time per plot (or spawn)
 *
 * ? SocketServe
 *
 * SaveStates
 * /colors | /cols
 * /dfgive
 *
 */

public final class FeatureHandler {

    public static FeatureHandler INSTANCE;

    private final PacketHandler packetHandler = new PacketHandler();
    private final KeyBinding.Category KEYBIND_CATEGORY = KeyBinding.Category.create(Identifier.of("category.millomod2"));

    private final ArrayList<String> order = new ArrayList<>();
    private final HashMap<String, Feature> initialFeatureMap = new HashMap<>();
    private final Map<String, Feature> featureMap;

    public static void register() {
        if (INSTANCE != null) return;
        INSTANCE = new FeatureHandler();
    }

    private FeatureHandler() {
        register(
                new Editor(),
                new FlightSpeedToggle(),
                new QuickValueItem(),
                new AngelsGrace(),
                new SpectatorToggle(),
                new LagslayerHUD(),
                new ShowItemTags(),
                new PickChestValue(),
                new ContainerSearch(),
                new CommandWheel(),
                new SocketServe(),
                new ToggleSprintDisplay(),
                new Notifications(),
                new SkinPreview(),
                new ActionDumpReader(),
                new Debug()
        );

        featureMap = Map.copyOf(initialFeatureMap);

        ClientTickEvents.START_CLIENT_TICK.register(client -> forEach(Feature::onTick));
        ClientTickEvents.END_CLIENT_TICK.register(client -> forEach(Feature::onEndTick));
    }

    //

    public static void forEach(FeatureConsumer consumer) {
        INSTANCE.order.forEach(id -> consumer.accept(INSTANCE.featureMap.get(id)));
    }

    @SuppressWarnings("unchecked")
    public static <T extends Feature> T get(String id) {
        return (T) INSTANCE.featureMap.get(id);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Feature> T get(Class<T> clazz) {
        for (Feature feature : INSTANCE.featureMap.values()) {
            if (clazz.isInstance(feature)) {
                return (T) feature;
            }
        }
        throw new IllegalArgumentException("No feature found for class: " + clazz.getName());
    }

    public static PacketHandler getPacketHandler() {
        return INSTANCE.packetHandler;
    }

    private void register(Feature... features) {
        for (Feature feature : features) {
            register(feature);
        }
    }

    private void register(Feature feature) {
        MilloLog.log("Loading feature: " + feature.getId());

        order.add(feature.getId());
        packetHandler.register(feature);
        initialFeatureMap.put(feature.getId(), feature);

        if (feature instanceof Keybound keybound) {
            for (String keybindId : keybound.getKeybindIds()) {
                keybound.registerKeybind(keybindId, KeyBindingHelper.registerKeyBinding(new KeyBinding(
                        "key.millomod2." + feature.getId() + "." + keybindId,
                        keybound.getDefaultType(),
                        keybound.getDefaultCode(),
                        KEYBIND_CATEGORY
                )));
            }
        }

    }

    public interface FeatureConsumer {
        void accept(Feature feature);
    }

}
