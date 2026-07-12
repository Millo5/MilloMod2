package millo.millomod2.client.features;

import millo.millomod2.client.util.MilloLog;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BundleS2CPacket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class PacketEventBus {

    private final Map<Class<?>, List<PacketListener<?>>> receiveSubscribers = new HashMap<>();
    private final Map<Class<?>, List<PacketListener<?>>> sendSubscribers = new HashMap<>();

    public <P extends Packet<?>> void subscribeReceive(Class<P> packetType, PacketListener<P> listener) {
        subscribe(receiveSubscribers, packetType, listener);
    }

    public <P extends Packet<?>> void subscribeSend(Class<P> packetType, PacketListener<P> listener) {
        subscribe(sendSubscribers, packetType, listener);
    }

    private <P extends Packet<?>> void subscribe(
            Map<Class<?>, List<PacketListener<?>>> subscribers,
            Class<P> packetType,
            PacketListener<P> listener
    ) {
        subscribers.computeIfAbsent(packetType, ignored -> new ArrayList<>()).add(listener);
    }

    public boolean postReceive(Packet<?> packet) {
        if (packet instanceof BundleS2CPacket bundle) {
            bundle.getPackets().forEach(this::postReceive);
            return false;
        }
        return post(receiveSubscribers, packet);
    }

    public boolean postSend(Packet<?> packet) {
        return post(sendSubscribers, packet);
    }

    @SuppressWarnings("unchecked")
    private <P extends Packet<?>> boolean post(Map<Class<?>, List<PacketListener<?>>> subscribers, P packet) {
        List<PacketListener<?>> listeners = subscribers.get(packet.getClass());
        if (listeners == null) return false;

        boolean cancelled = false;
        for (PacketListener<?> listener : listeners) {
            try {
                cancelled |= ((PacketListener<P>) listener).onPacket(packet);
            } catch (Exception e) {
                MilloLog.error("Error handling packet " + packet.getClass().getName());
                MilloLog.stackTrace(e);
            }
        }
        return cancelled;
    }

    @FunctionalInterface
    public interface PacketListener<P extends Packet<?>> {
        boolean onPacket(P packet);
    }
}
