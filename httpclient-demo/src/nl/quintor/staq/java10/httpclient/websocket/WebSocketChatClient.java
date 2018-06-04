package nl.quintor.staq.java10.httpclient.websocket;

import jdk.incubator.http.HttpClient;
import jdk.incubator.http.WebSocket;

import java.net.URI;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class WebSocketChatClient {

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    public CompletableFuture<WebSocket> connect(String url) {
        return httpClient.newWebSocketBuilder()
                .buildAsync(URI.create(url), new ChatWebsocketListener(executorService));
    }

    public CompletableFuture<Void> run(WebSocket webSocket) {
        final Scanner scanner = new Scanner(System.in);
        return processNextLines(webSocket, scanner);
    }

    private CompletableFuture<Void> processNextLines(WebSocket webSocket, Scanner scanner) {
        final String line = scanner.nextLine();
        if ("/quit" .equals(line)) {
            return quit(webSocket);
        }
        return webSocket.sendText('"' + line + '"', true)
                .thenRun(() -> processNextLines(webSocket, scanner));
    }

    private CompletableFuture<Void> quit(WebSocket webSocket) {
        var sendClose = webSocket.sendClose(WebSocket.NORMAL_CLOSURE, "User quit");
        return sendClose
                .thenRun(executorService::shutdown);
    }

    public static void main(String... args) {
        var chatClient = new WebSocketChatClient();
        chatClient.connect("wss://remy-ws.glitch.me/")
                .thenAccept(chatClient::run)
                .join();
    }
}
