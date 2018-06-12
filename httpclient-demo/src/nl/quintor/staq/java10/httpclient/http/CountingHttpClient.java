package nl.quintor.staq.java10.httpclient.http;

import jdk.incubator.http.HttpClient;
import jdk.incubator.http.HttpRequest;
import jdk.incubator.http.HttpResponse;

import java.net.URI;
import java.nio.charset.Charset;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class CountingHttpClient {

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public CompletableFuture<String> simple(final String url) {
        // Make an asynchronous request to url and return the response body as a future of a string
        var httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        return httpClient.sendAsync(httpRequest, HttpResponse.BodyHandler.asString())
                .thenApply(HttpResponse::body);
    }

    public CompletableFuture<List<String>> manySimpleResponses(final String url) {
        var before = Thread.activeCount();
        var httpClient = HttpClient.newBuilder()
                .build();
        var futures = new ArrayList<CompletableFuture<HttpResponse<String>>>();
        // 1. Make 100 requests to "url" asynchronously. Store the futures of the responses in "futures"
        for (int i = 0; i < 20; i++) {
            var httpRequest = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
            var responseFuture = httpClient.sendAsync(httpRequest, HttpResponse.BodyHandler.asString());
            futures.add(responseFuture);
        }

        // 2. Try to lower the thread count. Does it work?
        System.out.println((Thread.activeCount() - before) + " more threads are active");

        // 3. Return a future of a list of strings
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .map(HttpResponse::body)
                        .collect(Collectors.toList())
                );
    }

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
        var httpRequest = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
        var multiSubscriber = HttpResponse.MultiSubscriber.asMap(
                r -> Optional.of(HttpResponse.BodyHandler.asByteArray()),
                false
        );
        var responseFuture = httpClient.sendAsync(httpRequest, multiSubscriber);

        var bodyFuture = responseFuture.thenCompose(map -> map.get(httpRequest)).thenApply(HttpResponse::body);
        var pushedFuture = responseFuture.thenApply(map -> map.entrySet().stream()
                .filter(entry -> !entry.getKey().equals(httpRequest))
                .collect(Collectors.toMap(
                        entry -> entry.getKey().uri().getPath(),
                        entry -> entry.getValue().completeOnTimeout(null, 3, TimeUnit.SECONDS)
                )));

        var allFutures = responseFuture.thenCompose(v ->
                CompletableFuture.allOf(pushedFuture.join().values().toArray(new CompletableFuture[0])));

        return allFutures.thenApply(v -> {
            var body = bodyFuture.join();
            var pushedFutures = pushedFuture.join();
            var pushed = pushedFutures.entrySet().stream()
                    .filter(entry -> entry.getValue().join() != null)
                    .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().join().body()));
            var bodyAndPushed = new BodyAndPushed();
            bodyAndPushed.body = new String(body);
            bodyAndPushed.pushed = pushed;
            return bodyAndPushed;
        });
    }

    public CompletableFuture<String> emptyStringIfNot200(final String url) {
        // 1. Make an asynchronous request to "url". If the response code is 200, use BodySubscriber.asString to read
        // the body. If it is not 200, do not use BodySubscriber.asString and always return an empty string.
        // BONUS: Can you get the correct Charset to use from the headers?
        var httpRequest = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
        var responseFuture = httpClient.sendAsync(httpRequest, (status, httpHeaders) -> status == 200
                ? HttpResponse.BodySubscriber.asString(Charset.defaultCharset())
                : HttpResponse.BodySubscriber.discard(""));
        return responseFuture.thenApply(HttpResponse::body);
    }

    public CompletableFuture<Optional<String>> emptyOptionalIfNot200(final String url) {
        // BONUS: Make an asynchronous request to "url". Once you know the response code, if it is 200, read the
        // response body as an Optional of a String. If it is not 200, do not read the response body and return an empty
        // Optional
        // Hint: Make your own BodySubscriber to implement this
        throw new UnsupportedOperationException();
    }

    public void request(final String url) {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .timeout(Duration.ofSeconds(10))
                .uri(URI.create(url))
                .build();

        var okBodySubscriber = HttpResponse.BodySubscriber.fromSubscriber(new ByteCountingSubscriber(), ByteCountingSubscriber::getReceivedByteCount);

        var responseFuture = httpClient.sendAsync(httpRequest, (status, httpHeaders) ->
                status == 200
                        ? HttpResponse.BodySubscriber.buffering(okBodySubscriber, 10_000)
                        : HttpResponse.BodySubscriber.discard(0L)
        );
        var response = responseFuture.join();
        System.out.println("CountingHttpClient is done -  status was " + response.statusCode() + "; response reported " + response.body() + " bytes");
    }

    public static void main(String... args) {
        CountingHttpClient client = new CountingHttpClient();
        var simpleResponse = client.simple("https://www.google.com/").join();
        System.out.println(!simpleResponse.isEmpty());
        var manySimpleResponses = client.manySimpleResponses("https://www.milanboers.nl/").join();
        System.out.println(manySimpleResponses.size());
        var serverPushResponses = client.serverPushResponses("https://http2.akamai.com/demo/h2_demo_frame_sp2.html?pushnum=1").join();
        System.out.println(serverPushResponses.pushed.size());
        var emptyStringIfNot200 = client.emptyStringIfNot200("https://www.google.com/404.html").join();
        System.out.println("".equals(emptyStringIfNot200));

        //client.request("http://speedtest.xs4all.net/files/1mb.bin");
    }
}
