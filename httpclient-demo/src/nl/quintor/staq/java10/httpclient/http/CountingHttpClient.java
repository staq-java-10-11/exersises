package nl.quintor.staq.java10.httpclient.http;

import jdk.incubator.http.HttpClient;
import jdk.incubator.http.HttpRequest;
import jdk.incubator.http.HttpResponse;

import java.net.URI;
import java.time.Duration;

public class CountingHttpClient {

    private final HttpClient httpClient = HttpClient.newHttpClient();

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
