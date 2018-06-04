package nl.quintor.staq.java10.httpclient.websocket;

import jdk.incubator.http.WebSocket;

import java.nio.ByteBuffer;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ChatWebsocketListener implements WebSocket.Listener {

    private final ScheduledExecutorService executorService;
    private ScheduledFuture keepalive;

    public ChatWebsocketListener(ScheduledExecutorService scheduledExecutorService) {
        this.executorService = scheduledExecutorService;
    }

    @Override
    public void onOpen(WebSocket webSocket) {
        System.out.println("# Connection opened");
        webSocket.request(1);

        // Send pings to keep the connection alive
        keepalive = executorService.scheduleAtFixedRate(() -> webSocket.sendPing(ByteBuffer.allocate(0)), 0, 15, TimeUnit.SECONDS);
    }

    @Override
    public CompletionStage<?> onText(WebSocket webSocket, CharSequence message, WebSocket.MessagePart part) {
        if (message.charAt(0) == '"') {
            System.out.println("<Anonymous>\t" + message.subSequence(1, message.length() - 1));
        } else if (message.codePoints().allMatch(Character::isDigit)) {
            System.out.println("# " + message + " users connected");
        }
        webSocket.request(1);
        return null;
    }

    @Override
    public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
        onConnectionClosed();
        System.out.println("# Connection closed");
        return null;
    }

    @Override
    public void onError(WebSocket webSocket, Throwable error) {
        onConnectionClosed();
        System.out.println("# Error: " + error.toString());
    }

    private void onConnectionClosed() {
        keepalive.cancel(false);
    }
}
