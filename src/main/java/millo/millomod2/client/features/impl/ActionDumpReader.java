package millo.millomod2.client.features.impl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import millo.millomod2.client.features.Feature;
import millo.millomod2.client.features.PacketEventBus;
import millo.millomod2.client.features.addons.PacketEventSubscriber;
import millo.millomod2.client.util.FileUtil;
import millo.millomod2.client.util.PlayerUtil;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.text.Text;

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
        player().sendMessage(Text.of("Reading action dump..."), false);
    }

    @Override
    public void subscribePackets(PacketEventBus eventBus) {
        eventBus.subscribeReceive(GameMessageS2CPacket.class, this::onChat);
    }

    public boolean onChat(GameMessageS2CPacket message) {
        if (!reading) return false;
        String content = message.content().getString();

        if (content.startsWith("Error:")) {
            reading = false;
            player().sendMessage(Text.of("Error while reading action dump!"), false);
            return false;
        }

        fullDump.append(content.trim());

        reading = !content.equals("}");
        if (!reading) {
            JsonObject json = JsonParser.parseString(fullDump.toString()).getAsJsonObject();
            FileUtil.writeJson("action_dump.json", json);

            player().sendMessage(Text.of("Action dump saved!"), false);
        }
        return true;
    }

}
