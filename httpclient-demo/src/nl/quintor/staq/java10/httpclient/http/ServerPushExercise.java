package nl.quintor.staq.java10.httpclient.http;

import jdk.incubator.http.HttpClient;
import jdk.incubator.http.HttpRequest;
import jdk.incubator.http.HttpResponse;

import java.net.URI;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ServerPushExercise {

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public static class BodyAndPushed {
        public String body;
        public Map<String, byte[]> pushed;
    }

    public CompletableFuture<BodyAndPushed> serverPushResponses(final String url) {
        // 1. Make an asynchronous request to "url", and return a future of a BodyAndPushed object, where "body" is
        // the body of the page returned, and "pushed" are the pushed resources as a map from url to body as a byte
        // array.
        // Hint: use MultiSubscriber.asMap
        // Hint: do not wait forever for futures. Make them time out and discard them. The future should complete when
        // all responses have either been completed or have timed out.
        // BONUS: Can you implement your own MultiSubscriber to do this more efficiently?

        // var httpRequest = ...;
        // var multiSubscriber = HttpResponse.MultiSubscriber.asMap(...);
        // var responseFuture = httpClient.sendAsync(httpRequest, multiSubscriber);
        //
        // var allFutures = ...
        // return allFutures.thenApply(v -> {
        //     ...
        //     var bodyAndPushed = new BodyAndPushed();
        //     bodyAndPushed.body = ...
        //     bodyAndPushed.pushed = ...
        //     return bodyAndPushed;
        // }
        throw new UnsupportedOperationException();
    }
}
