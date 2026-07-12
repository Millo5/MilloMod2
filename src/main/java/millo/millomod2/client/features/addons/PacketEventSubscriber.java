package millo.millomod2.client.features.addons;

import millo.millomod2.client.features.PacketEventBus;

public interface PacketEventSubscriber {
    void subscribePackets(PacketEventBus eventBus);
}
