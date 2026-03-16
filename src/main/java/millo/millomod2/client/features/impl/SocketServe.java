package millo.millomod2.client.features.impl;

import millo.millomod2.client.features.Feature;
import millo.millomod2.client.features.addons.Toggleable;
import millo.millomod2.client.features.impl.Notifications.Notifications;
import millo.millomod2.client.util.style.Styles;
import millo.millomod2.client.websocket.MilloWebSocketServer;
import net.minecraft.text.Text;

import java.net.InetSocketAddress;

public class SocketServe extends Feature implements Toggleable {

    private MilloWebSocketServer webSocketServer;

    @Override
    public String getId() {
        return "socket_serve";
    }

    public SocketServe() {
        if (!isEnabled()) return;
        startServer();
    }

    @Override
    public void enabledChanged(boolean enabled) {
        if (enabled) {
            startServer();
            return;
        }

        if (webSocketServer == null) return;
        try {
            webSocketServer.stop();
        } catch (Exception ignored) {
            Notifications.notify(Text.literal("Failed to stop WebSocket server").setStyle(Styles.SCARY.getStyle()));
        }

        webSocketServer = null;
    }

    private void startServer() {
        if (webSocketServer == null) {
            webSocketServer = new MilloWebSocketServer(new InetSocketAddress("localhost", 31321));
            new Thread(webSocketServer, "Millo-Websocket-Thread").start();
            Notifications.notify(Text.literal("WebSocket server started on port 31321").setStyle(Styles.ADDED.getStyle()));
        }
    }
}
