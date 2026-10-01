package millo.millomod2.client.features.impl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import millo.millomod2.client.features.Feature;
import millo.millomod2.client.features.PacketEventBus;
import millo.millomod2.client.features.addons.PacketEventSubscriber;
import millo.millomod2.client.util.FileUtil;
import millo.millomod2.client.util.logging.MilloLog;
import millo.millomod2.client.util.PlayerUtil;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;

public class ActionDumpReader extends Feature implements PacketEventSubscriber {

    @Override
    public String getId() {
        return "action_dump_reader";
    }

    private boolean reading = false;
    private StringBuilder fullDump;

    public void read() {
        reading = true;
        PlayerUtil.sendCommand("dumpactioninfo");
        fullDump = new StringBuilder();
        MilloLog.logInGame("Reading action dump...");
    }

    @Override
    public void subscribePackets(PacketEventBus eventBus) {
        eventBus.subscribeReceive(ClientboundSystemChatPacket.class, this::onChat);
    }

    public boolean onChat(ClientboundSystemChatPacket message) {
        if (!reading) return false;
        String content = message.content().getString();

        if (content.startsWith("Error:")) {
            reading = false;
            MilloLog.logInGame("Error while reading action dump!");
            return false;
        }

        fullDump.append(content.trim());

        reading = !content.equals("}");
        if (!reading) {
            JsonObject json = JsonParser.parseString(fullDump.toString()).getAsJsonObject();
            FileUtil.writeJson("action_dump.json", json);

            MilloLog.logInGame("Action dump saved!");
        }
        return true;
    }

}
