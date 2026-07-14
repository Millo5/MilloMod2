package millo.millomod2.client.features.impl;

import millo.millomod2.client.features.Feature;
import millo.millomod2.client.features.addons.Toggleable;
import millo.millomod2.client.MilloMod;
import millo.millomod2.client.features.impl.Notifications.Notifications;
import millo.millomod2.client.util.style.Styles;
import millo.millomod2.client.websocket.MilloWebSocketServer;
import net.minecraft.text.Text;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServe extends Feature implements Toggleable {

    private enum ServerState {
        STOPPED,
        STARTING,
        RUNNING,
        STOPPING
    }

    private final ExecutorService lifecycleExecutor = Executors.newSingleThreadExecutor(runnable -> {
        Thread thread = new Thread(runnable, "Millo-SocketServe");
        thread.setDaemon(true);
        return thread;
    });
    private volatile MilloWebSocketServer webSocketServer;
    private volatile ServerState serverState = ServerState.STOPPED;
    private volatile boolean lifecycleInitialized;
    private volatile boolean shouldRun;
    private volatile String lastError = "None";

    @Override
    public String getId() {
        return "socket_serve";
    }

    @Override
    public void onTick() {
        if (lifecycleInitialized) return;
        enabledChanged(isEnabled());
    }

    @Override
    public void enabledChanged(boolean enabled) {
        lifecycleInitialized = true;
        shouldRun = enabled;
        lifecycleExecutor.execute(this::applyState);
    }

    private void applyState() {
        if (shouldRun && serverState == ServerState.STOPPED) {
            MilloWebSocketServer server = new MilloWebSocketServer(
                    new InetSocketAddress("localhost", 31321),
                    started -> lifecycleExecutor.execute(() -> handleStarted(started)),
                    (failed, exception) -> lifecycleExecutor.execute(() -> handleFailure(failed, exception))
            );
            webSocketServer = server;
            serverState = ServerState.STARTING;
            try {
                server.start();
            } catch (Exception e) {
                handleFailure(server, e);
            }
            return;
        }

        if (!shouldRun && (serverState == ServerState.STARTING || serverState == ServerState.RUNNING)) {
            MilloWebSocketServer server = webSocketServer;
            serverState = ServerState.STOPPING;
            try {
                server.stop(1000);
                if (webSocketServer == server) {
                    webSocketServer = null;
                    serverState = ServerState.STOPPED;
                }
                lastError = "None";
                notify(Text.literal("WebSocket server stopped").setStyle(Styles.REMOVED.getStyle()));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                serverState = ServerState.RUNNING;
                lastError = message(e);
                notify(Text.literal("Failed to stop WebSocket server: " + message(e)).setStyle(Styles.SCARY.getStyle()));
            }
        }
    }

    private void handleStarted(MilloWebSocketServer server) {
        if (webSocketServer != server || serverState != ServerState.STARTING) return;
        serverState = ServerState.RUNNING;
        lastError = "None";
        if (shouldRun) {
            notify(Text.literal("WebSocket server started on port 31321").setStyle(Styles.ADDED.getStyle()));
        } else {
            applyState();
        }
    }

    private void handleFailure(MilloWebSocketServer server, Exception exception) {
        if (webSocketServer != server || serverState == ServerState.STOPPING) return;
        boolean wasRunning = serverState == ServerState.RUNNING;
        webSocketServer = null;
        serverState = ServerState.STOPPED;
        lastError = message(exception);
        String prefix = wasRunning ? "WebSocket server stopped unexpectedly: " : "Failed to start WebSocket server: ";
        notify(Text.literal(prefix + message(exception) + ". Toggle it to retry.")
                .setStyle(Styles.SCARY.getStyle()));
    }

    private String message(Exception exception) {
        return exception.getMessage() == null ? exception.getClass().getSimpleName() : exception.getMessage();
    }

    private void notify(Text message) {
        MilloMod.MC.execute(() -> Notifications.notify(message));
    }

    public DebugInfo getDebugInfo() {
        MilloWebSocketServer server = webSocketServer;
        int connections = server == null ? 0 : server.getConnections().size();
        return new DebugInfo(
                isEnabled(),
                lifecycleInitialized,
                shouldRun,
                serverState.name(),
                server != null,
                connections,
                lifecycleExecutor.isShutdown(),
                lastError
        );
    }

    public record DebugInfo(
            boolean enabled,
            boolean initialized,
            boolean shouldRun,
            String state,
            boolean serverPresent,
            int connections,
            boolean executorShutdown,
            String lastError
    ) {}
}
