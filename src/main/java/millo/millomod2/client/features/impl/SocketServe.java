package millo.millomod2.client.features.impl;

import millo.millomod2.client.features.Feature;
import millo.millomod2.client.features.addons.Toggleable;
import millo.millomod2.client.websocket.MilloWebSocketServer;

import java.net.InetSocketAddress;

public class SocketServe extends Feature implements Toggleable {

    private MilloWebSocketServer webSocketServer;

    @Override
    public String getId() {
        return "socket_serve";
    }

    public SocketServe() {
        if (!isEnabled()) return;
        webSocketServer = new MilloWebSocketServer(new InetSocketAddress("localhost", 31321));
        new Thread(webSocketServer, "Millo-Websocket-Thread").start();
    }

    @Override
    public void enabledChanged(boolean enabled) {
        if (enabled) {
            if (webSocketServer == null) {
                webSocketServer = new MilloWebSocketServer(new InetSocketAddress("localhost", 31321));
                new Thread(webSocketServer, "Millo-Websocket-Thread").start();
            }
        } else {
            if (webSocketServer != null) {
                try {
                    webSocketServer.stop();
                } catch (Exception ignored) {}
                webSocketServer = null;
            }
        }
    }
}
