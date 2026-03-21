package millo.millomod2.client.websocket;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import millo.millomod2.client.MilloMod;
import millo.millomod2.client.hypercube.model.ModelUtil;
import millo.millomod2.client.hypercube.model.TemplateModel;
import millo.millomod2.client.util.ItemUtil;
import millo.millomod2.client.util.PlayerUtil;
import millo.millomod2.client.util.style.Styles;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

public class MilloWebSocketServer extends WebSocketServer {

    public MilloWebSocketServer(InetSocketAddress address) {
        super(address);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {}

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {}

    @Override
    public void onMessage(WebSocket conn, String message) {
        String res = accept(message);
        if (res != null) conn.send(res);
    }

    private enum MessageType {
        INFO,
        ERROR,
        SUCCESS
    }
    private static void message(MessageType type, String source, String message) {
        MutableText text = switch (type) {
            case INFO -> Text.literal(" » ").setStyle(Styles.LINE_NUM.getStyle());
            case ERROR -> Text.literal(" » ").setStyle(Styles.SCARY.getStyle());
            case SUCCESS -> Text.literal(" » ").setStyle(Styles.TRUE.getStyle());
        };

        String name = source == null ? "WSS" : source;

        if (type != MessageType.INFO) text.append(Text.literal("["+name+"] ").setStyle(Styles.NAME.getStyle()));
        text.append(Text.literal(message).setStyle(Styles.DEFAULT.getStyle()));

        MilloMod.player().sendMessage(text, false);
    }

    private static String accept(String message) {
        JsonObject result = new JsonObject();
        if (message == null) return null;

        JsonObject dataJson;
        try {
            dataJson = JsonParser.parseString(message).getAsJsonObject();
        } catch (JsonSyntaxException e) {
            message(MessageType.ERROR, null, "Failed to parse provided JSON data.");
            message(MessageType.INFO, null, e.getMessage());
            message(MessageType.INFO, null, message);
            return null;
        }

        String type = dataJson.get("type").getAsString();
        String data = dataJson.get("data").getAsString();
        String source = dataJson.get("source").getAsString();

        if (source.isEmpty()) {
            message(MessageType.ERROR, null, "No source provided!");
            return null;
        }

        if (type.equals("item")) {
            ItemStack stack;
            try {
                stack = ItemUtil.fromNbt(data);
            } catch (Exception e) {
                message(MessageType.ERROR, source, "Failed to parse provided NBT data.");
                message(MessageType.INFO, source, e.getMessage());
                return null;
            }
            PlayerUtil.giveItem(stack);
            message(MessageType.SUCCESS, source, "Received " + stack.getName().getString() + "!");
        }

        if (type.equals("template")) {
//            Template template = Template.parseBase64(data);
            TemplateModel template = ModelUtil.parseFromGzip(data);
            if (template == null) {
                message(MessageType.ERROR, source, "Failed to parse provided template data.");
                return null;
            }

            PlayerUtil.giveItem(template.getItem());
            message(MessageType.SUCCESS, source, "Received " + template.getName() + "!");
        }

        return result.toString();
    }


    @Override
    public void onError(WebSocket conn, Exception ex) {}

    @Override
    public void onStart() {}


}
