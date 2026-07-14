package millo.millomod2.client.websocket;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import millo.millomod2.client.MilloMod;
import millo.millomod2.client.hypercube.model.ModelUtil;
import millo.millomod2.client.hypercube.model.TemplateModel;
import millo.millomod2.client.util.ItemUtil;
import millo.millomod2.client.util.MilloLog;
import millo.millomod2.client.util.PlayerUtil;
import millo.millomod2.client.util.style.Styles;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class MilloWebSocketServer extends WebSocketServer {

    private static final Map<String, MessageHandler> MESSAGE_HANDLERS = Map.of(
            "item", new ItemMessageHandler(),
            "template", new TemplateMessageHandler()
    );

    private final Consumer<MilloWebSocketServer> startedCallback;
    private final BiConsumer<MilloWebSocketServer, Exception> failedCallback;

    public MilloWebSocketServer(InetSocketAddress address, Consumer<MilloWebSocketServer> startedCallback, BiConsumer<MilloWebSocketServer, Exception> failedCallback) {
        super(address);
        this.startedCallback = startedCallback;
        this.failedCallback = failedCallback;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {}

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {}

    @Override
    public void onMessage(WebSocket conn, String message) {
        MilloMod.MC.execute(() -> {
            try {
                String response = accept(message);
                if (response != null && conn.isOpen()) conn.send(response);
            } catch (Exception e) {
                MilloLog.error("Failed to handle WebSocket request: " + e.getMessage());
                if (conn.isOpen()) conn.send(errorResponse("Request failed"));
            }
        });
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

        if (MilloMod.player() != null) MilloMod.player().sendMessage(text, false);
    }

    private static String accept(String message) {
        JsonObject result = new JsonObject();
        if (message == null) return null;

        JsonObject dataJson;
        try {
            dataJson = JsonParser.parseString(message).getAsJsonObject();
        } catch (JsonSyntaxException | IllegalStateException e) {
            message(MessageType.ERROR, null, "Failed to parse provided JSON data.");
            message(MessageType.INFO, null, e.getMessage());
            return null;
        }

        if (!dataJson.has("type") || !dataJson.has("data") || !dataJson.has("source")
                || !dataJson.get("type").isJsonPrimitive()
                || !dataJson.get("data").isJsonPrimitive()
                || !dataJson.get("source").isJsonPrimitive()) {
            message(MessageType.ERROR, null, "Missing or invalid message fields.");
            return null;
        }

        String type = dataJson.get("type").getAsString();
        String data = dataJson.get("data").getAsString();
        String source = dataJson.get("source").getAsString();

        if (source.isBlank()) {
            message(MessageType.ERROR, null, "No source provided!");
            return null;
        }
        if (source.length() > 256) {
            message(MessageType.ERROR, null, "Source name is too long!");
            return null;
        }

        MessageHandler handler = MESSAGE_HANDLERS.get(type);
        if (handler == null) {
            message(MessageType.ERROR, source, "Unknown message type: " + type);
            return null;
        }
        if (!handler.handle(source, data)) return null;

        return result.toString();
    }

    private interface MessageHandler {
        boolean handle(String source, String data);
    }

    private static class ItemMessageHandler implements MessageHandler {

        @Override
        public boolean handle(String source, String data) {
            ItemStack stack;
            try {
                stack = ItemUtil.fromNbt(data);
            } catch (Exception e) {
                message(MessageType.ERROR, source, "Failed to parse provided NBT data.");
                message(MessageType.INFO, source, e.getMessage());
                return false;
            }
            PlayerUtil.giveItem(stack);
            message(MessageType.SUCCESS, source, "Received " + stack.getName().getString() + "!");
            return true;
        }
    }

    private static class TemplateMessageHandler implements MessageHandler {

        @Override
        public boolean handle(String source, String data) {
            TemplateModel template = ModelUtil.parseFromGzip(data);
            if (template == null) {
                message(MessageType.ERROR, source, "Failed to parse provided template data.");
                return false;
            }

            PlayerUtil.giveItem(template.getItem());
            message(MessageType.SUCCESS, source, "Received " + template.getName() + "!");
            return true;
        }
    }

    private static String errorResponse(String message) {
        JsonObject response = new JsonObject();
        response.addProperty("error", message);
        return response.toString();
    }


    @Override
    public void onError(WebSocket conn, Exception ex) {
        MilloLog.error("WebSocket server error: " + ex.getMessage());
        if (conn == null) failedCallback.accept(this, ex);
    }

    @Override
    public void onStart() {
        startedCallback.accept(this);
    }


}
