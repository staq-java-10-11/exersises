package nl.quintor.staq.java10.httpclient.http;

import jdk.incubator.http.HttpClient;
import jdk.incubator.http.HttpRequest;
import jdk.incubator.http.HttpResponse;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class BasicExercise {

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public CompletableFuture<String> simple(final String url) {
        // Make an asynchronous request to url and return the response body as a future of a string
        // var httpRequest = ...
        // return ...
        throw new UnsupportedOperationException();
    }

    public CompletableFuture<List<String>> manySimpleResponses(final String url, final int times) {
        var before = Thread.activeCount();
        var httpClient = HttpClient.newBuilder()
                // Cached thread pool is HttpClient's default executor
                .executor(Executors.newCachedThreadPool())
                .build();
        var futures = new ArrayList<CompletableFuture<HttpResponse<String>>>();
        // 1. Make many requests to "url" asynchronously. Store the futures of the responses in "futures"
        for (int i = 0; i < times; i++) {
            // var httpRequest = ...
            // var responseFuture = ...
            // futures.add(responseFuture);
        }

        // 2. Try to lower the thread count by replacing the executor. Does it work?
        System.out.println((Thread.activeCount() - before) + " more threads are active");

        // 3. Return a future of a list of strings
        // return ...
        throw new UnsupportedOperationException();
    }
}
