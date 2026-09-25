package millo.millomod2.client.features.impl;

import millo.millomod2.client.features.Feature;
import millo.millomod2.client.features.PacketEventBus;
import millo.millomod2.client.features.addons.PacketEventSubscriber;
import millo.millomod2.client.util.PlayerUtil;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.network.protocol.game.ServerboundAcceptTeleportationPacket;
import net.minecraft.world.entity.Relative;
import net.minecraft.world.phys.Vec3;
import java.util.function.Consumer;

public class TeleportHandler extends Feature implements PacketEventSubscriber {

    private static TeleportHandler instance;

    private boolean active = false;
    private boolean cancel = false;
    private Consumer<ClientboundPlayerPositionPacket> callback;
    private Vec3 target;



    @Override
    public String getId() {
        return "teleport_handler";
    }

    public TeleportHandler() {
        instance = this;
    }

    @Override
    public void subscribePackets(PacketEventBus eventBus) {
        eventBus.subscribeReceive(ClientboundPlayerPositionPacket.class, this::positionLook);
    }

    public boolean positionLook(ClientboundPlayerPositionPacket packet) {
        if (!active) return false;
        if (net() == null || player() == null) return false;

        boolean handle = target != null && target.equals(packet.change().position());
        if (target == null &&
                (!packet.relatives().contains(Relative.X_ROT) &&
                        !packet.relatives().contains(Relative.Y_ROT) &&
                        packet.change().xRot() == 0 && packet.change().yRot() == 0
                )) {
            handle = true;
        }

        if (!handle) return false;

        if (callback != null) callback.accept(packet);
        if (cancel) net().send(new ServerboundAcceptTeleportationPacket(packet.id()));
        callback = null;
        active = false;
        return cancel;
    }

    public static void teleportTo(Vec3 position) {
        teleportTo(position, false, false);
    }

    public static void teleportTo(Vec3 target, boolean cancel) {
        teleportTo(target, cancel, false);
    }

    public static void teleportTo(Vec3 target, boolean cancel, boolean devmode) {
        PlayerUtil.sendCommand("p tp " + target.x + " " + target.y + " " + target.z + (devmode ? " -d" : ""));
        instance.active = true;
        instance.cancel = cancel;
        instance.target = target;
        instance.callback = null;
    }

    public static void teleportToMethod(String methodName, boolean cancel, Consumer<Vec3> callback) {
        PlayerUtil.sendCommand("ctp " + methodName);

        instance.active = true;
        instance.cancel = cancel;
        instance.target = null;
        instance.callback = (packet) -> {
            Vec3 pos = packet.change().position();
            callback.accept(pos);
        };

    }

}
