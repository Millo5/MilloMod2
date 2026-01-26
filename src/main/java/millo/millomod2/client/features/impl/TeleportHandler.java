package millo.millomod2.client.features.impl;

import millo.millomod2.client.features.Feature;
import millo.millomod2.client.features.addons.OnReceivePacket;
import millo.millomod2.client.util.PlayerUtil;
import net.minecraft.network.packet.c2s.play.TeleportConfirmC2SPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.util.math.Vec3d;

import java.util.function.Consumer;

public class TeleportHandler extends Feature {

    private static TeleportHandler instance;

    private boolean active = false;
    private boolean cancel = false;
    private Consumer<PlayerPositionLookS2CPacket> callback;
    private Vec3d target;


    @Override
    public String getId() {
        return "teleport_handler";
    }

    public TeleportHandler() {
        instance = this;
    }

    @OnReceivePacket
    public boolean positionLook(PlayerPositionLookS2CPacket packet) {
        if (!active) return false;
        if (net() == null || player() == null) return false;

        boolean handle = target != null && target.equals(packet.change().position());
        if (target == null &&
                (!packet.relatives().contains(PositionFlag.X_ROT) &&
                        !packet.relatives().contains(PositionFlag.Y_ROT) &&
                        packet.change().pitch() == 0 && packet.change().yaw() == 0
                )) {
            handle = true;
        }

        if (!handle) return false;

        if (callback != null) callback.accept(packet);
        if (cancel) net().sendPacket(new TeleportConfirmC2SPacket(packet.teleportId()));
        callback = null;
        active = false;
        return cancel;
    }

    public static void teleportTo(Vec3d target, boolean cancel) {
        PlayerUtil.sendCommand("p tp " + target.x + " " + target.y + " " + target.z);
        instance.active = true;
        instance.cancel = cancel;
        instance.target = target;
    }

    public static void teleportToMethod(String methodName, boolean cancel, Consumer<Vec3d> callback) {
        PlayerUtil.sendCommand("ctp " + methodName);

        instance.active = true;
        instance.cancel = cancel;
        instance.target = null;
        instance.callback = (packet) -> {
            Vec3d pos = packet.change().position();
            callback.accept(pos);
        };

    }

}
