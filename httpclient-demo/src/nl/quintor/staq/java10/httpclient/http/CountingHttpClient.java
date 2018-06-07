package nl.quintor.staq.java10.httpclient.http;

import jdk.incubator.http.HttpClient;
import jdk.incubator.http.HttpRequest;
import jdk.incubator.http.HttpResponse;

import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
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
        var futures = new ArrayList<CompletableFuture<HttpResponse<String>>>();
        // 1. Make 100 requests to "url" asynchronously. Store the futures of the responses in "futures"
        for (int i = 0; i < 100; i++) {
            var httpRequest = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
            var responseFuture = httpClient.sendAsync(httpRequest, HttpResponse.BodyHandler.asString());
            futures.add(responseFuture);
        }

        // 2. Try to lower the thread count. Does it work?
        System.out.println(Thread.activeCount() + " threads are active");

        // 3. Return a future of a list of strings
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .map(HttpResponse::body)
                        .collect(Collectors.toList())
                );
    }

    public CompletableFuture<String> serverPushResponses(final String url) {
        // 1. Make an asynchronous request to "url", and return a future of all response bodies concatenated.
        // Hint: use MultiSubscriber.asMap
        // BONUS: Can you implement your own MultiSubscriber using a StringBuilder to do this more efficiently?
        var httpRequest = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
        var multiSubscriber = HttpResponse.MultiSubscriber.asMap(r -> Optional.of(HttpResponse.BodyHandler.asString()));
        var responseFuture = httpClient.sendAsync(httpRequest, multiSubscriber);
        return responseFuture.thenApply(map -> map.values().stream()
                .map(CompletableFuture::join)
                .map(HttpResponse::body)
                .collect(Collectors.joining())
        );
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
        client.request("http://speedtest.xs4all.net/files/1mb.bin");
    }
}
