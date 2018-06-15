package nl.quintor.staq.java10.httpclient.websocket;

import jdk.incubator.http.HttpClient;
import jdk.incubator.http.WebSocket;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.Scanner;
import java.util.concurrent.*;

public class WebSocketChatExercise {

    // A WebSocket service on remy-ws.glitch.me provides an example chat-like service as part of a collection of
    // HTML5 demos at https://html5demos.com/. Try it yourself at https://html5demos.com/web-socket/
    // Every message that is sent, is broadcasted to all other connected participants.
    // In this exercise, we connect with Java HTTP Client instead of our browser!
    //
    // This service sends two kinds of messages:
    // - connected user count: digits only
    //    2
    // - chat messages: text enclosed between ""
    //    "Hello world"
    // Chat clients are expected to send chat messages only, properly enclosed between ""
    //
    // Reading + encoding, and decoding + printing of messages has already been implemented.
    // It is up to you to implement the WebSocket service connection!
    //
    // To be done:
    //
    // 1. Connect to the websocket service by implementing connect(...) using the httpClient.
    //    Other can now see that you are connected, but you cannot yet send or receive messages.
    //    Point your browser at https://html5demos.com/web-socket/ to verify this: every time you reconnect, the number
    //    of connected users should increase.
    //
    // 2. Send chat messages over the websocket by implementing sendChatMessage(...)
    //    Others can receive your messages, but you cannot yet read theirs.
    //    Point your browser at https://html5demos.com/web-socket/ to verify that you are sending messages.
    //
    // 3. Implement closing the websocket connection by implementing quit(...)
    //    When this is done, you can quit the application by typing the /quit command to the program.
    //    Verify this by giving it a try. Does the Java process finish when you type /quit and press enter?
    //
    // 4. Implement a listener that receives and prints the received messages and events.
    //    When this is done, you can finally read what others are saying.
    //    Verify this by spawning two application instances, and see if you can talk to yourself!
    //
    // 5. Noticed that the connection dies after some time? The server disconnects if you don't send frequently enough.
    //    This can be prevented by sending "ping" messages every once in a while. Modify ChatWebsocketListener such
    //    that it will send a ping on the WebSocket every 15 seconds; and stop sending pings when the connection closes
    //    or dies.
    //    Hint: use `keepaliveExecutorService` to do the scheduling for you. Cancel the ScheduledFuture on disconnect.
    //    Hint: no need to send any data with your ping message. ByteBuffer.allocate(0) will allocate an empty buffer.
    //
    // If you completed these five steps, you should have a fully functional chat client. Good luck!


    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ScheduledExecutorService keepaliveExecutorService = Executors.newSingleThreadScheduledExecutor();

    public static void main(String... args) {
        var chatClient = new WebSocketChatExercise();
        chatClient.connectAndRun("wss://remy-ws.glitch.me/").join();
    }

    // 1. Connect to the websocket service and register a listener
    private CompletableFuture<WebSocket> connect(String url) {
        // Use the httpClient to connect asynchronously to the given URL.
        // As a listener, specify the ChatWebsocketListener that will be implemented later.
        return CompletableFuture.failedFuture(new UnsupportedOperationException("not implemented"));
    }

    // 2. Start sending chat messages over the websocket
    private CompletableFuture<WebSocket> sendChatMessage(WebSocket webSocket, String message) {
        var formattedMessage = '"' + message + '"';
        // Send the text message over the WebSocket.
        // Hint: take a close look a the documentation of the relevant method. What do the parameters mean?
        return CompletableFuture.failedFuture(new UnsupportedOperationException("not implemented"));
    }

    // 3. Allow to close the websocket connection.
    private CompletableFuture<Void> quit(WebSocket webSocket) {
        // Close the websocket. Because all interactions with the websocket are asynchronous, don't wait for close to
        // succeed here!
        // Hint: You can use WebSocket.NORMAL_CLOSURE as statusCode and leave the reason empty.
        var sendClose = CompletableFuture.failedFuture(new UnsupportedOperationException("not implemented"));
        return sendClose
                .thenRun(keepaliveExecutorService::shutdown);
    }

    // 4. Implement the WebSocket Listener to receive messages from others.
    // Hint: remember that HTTP Client WebSockets are reactive - they won't give you anything that you don't ask for!
    private class ChatWebsocketListener implements WebSocket.Listener {

        @Override
        public void onOpen(WebSocket webSocket) {
            printConnectionOpened();
        }

        @Override
        public CompletionStage<?> onText(WebSocket webSocket, CharSequence message, WebSocket.MessagePart part) {
            // That's strange! We're already invoking `printReceivedChatMessage` here, but no messages gets printed...
            // Looks like the WebSocket is a bit lazy. Or maybe just reactive?
            // Can you figure out what is missing here? Where else is it missing?
            printReceivedChatMessage(message);
            return null;
        }

        @Override
        public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
            printConnectionClosed();
            return null;
        }

        @Override
        public void onError(WebSocket webSocket, Throwable error) {
            printConnectionError(error);
        }
    }

    private void printConnectionOpened() {
        System.out.println("# Connection opened");
    }

    private void printConnectionClosed() {
        System.out.println("# Connection closed");
    }

    private void printConnectionError(Throwable error) {
        System.out.println("# Connection error: " + error.toString());
    }

    private void printReceivedChatMessage(CharSequence message) {
        if (message.charAt(0) == '"') {
            var chatMessage = message.subSequence(1, message.length() - 1);
            System.out.println("<Anonymous>\t" + chatMessage);
        } else if (message.codePoints().allMatch(Character::isDigit)) {
            System.out.println("# " + message + " users connected");
        }
    }

    public CompletableFuture<Void> connectAndRun(String url) {
        var webSocket = connect(url).join();
        return run(webSocket);
    }

    private CompletableFuture<Void> run(WebSocket webSocket) {
        final Scanner scanner = new Scanner(System.in);
        return processNextLines(webSocket, scanner);
    }

    private CompletableFuture<Void> processNextLines(WebSocket webSocket, Scanner scanner) {
        final String line = scanner.nextLine();
        if ("/quit".equals(line)) {
            return quit(webSocket);
        }
        return sendChatMessage(webSocket, line)
                .thenRun(() -> processNextLines(webSocket, scanner));
    }
}
