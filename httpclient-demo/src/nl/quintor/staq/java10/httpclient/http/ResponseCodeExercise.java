package nl.quintor.staq.java10.httpclient.http;

import jdk.incubator.http.HttpClient;
import jdk.incubator.http.HttpRequest;
import jdk.incubator.http.HttpResponse;

import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Flow;

public class ResponseCodeExercise {

    private final HttpClient httpClient = HttpClient.newHttpClient();

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
        // 2. Make an asynchronous request to "url". Once you know the response code, if it is 200, read the
        // response body as an Optional of a String. If it is not 200, do not read the response body and return an empty
        // Optional
        // BONUS: Make your own BodySubscriber to implement this
        var httpRequest = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();

        // This is a simple solution, but it duplicates checking for the response code.
        /*
        var responseFuture = httpClient.sendAsync(httpRequest, (status, httpHeaders) -> status == 200
                ? HttpResponse.BodySubscriber.asString(Charset.defaultCharset())
                : HttpResponse.BodySubscriber.discard(""));
        return responseFuture.thenApply(response -> response.statusCode() == 200
                ? Optional.of(response.body())
                : Optional.empty());
        */

        // Here we use a customer BodySubscriber that wraps the body in an Optional.
        /*
        var responseFuture = httpClient.sendAsync(httpRequest, (status, httpHeaders) -> status == 200
                ? OptionalWrappingBodySubscriber.fromBodySubscriber(HttpResponse.BodySubscriber.asString(Charset.defaultCharset()))
                : HttpResponse.BodySubscriber.discard(Optional.empty()));
        return responseFuture.thenApply(HttpResponse::body);
        */

        throw new UnsupportedOperationException();
    }

    static class OptionalWrappingBodySubscriber<T> implements HttpResponse.BodySubscriber<Optional<T>> {

        private final HttpResponse.BodySubscriber<T> bodySubscriber;

        private OptionalWrappingBodySubscriber(HttpResponse.BodySubscriber<T> bodySubscriber) {
            this.bodySubscriber = bodySubscriber;
        }

        @Override
        public CompletionStage<Optional<T>> getBody() {
            return bodySubscriber.getBody().thenApply(Optional::of);
        }

        @Override
        public void onSubscribe(Flow.Subscription subscription) {
            bodySubscriber.onSubscribe(subscription);
        }

        @Override
        public void onNext(List<ByteBuffer> item) {
            bodySubscriber.onNext(item);
        }

        @Override
        public void onError(Throwable throwable) {
            bodySubscriber.onError(throwable);
        }

        @Override
        public void onComplete() {
            bodySubscriber.onComplete();
        }

        public static <T> HttpResponse.BodySubscriber<Optional<T>> fromBodySubscriber(HttpResponse.BodySubscriber<T> bodySubscriber) {
            return new OptionalWrappingBodySubscriber<>(bodySubscriber);
        }
    }

    public CompletableFuture<String> failedFutureIfNot200(final String url) {
        // 3. Make an asynchronous request to "url". Once you know the response code, if it is 200, read the
        // response body as a future String. If it is not 200, do not read the response body and make the resulting
        // future fail with an exceptional result.
        // BONUS: Make your own BodySubscriber to implement this
        var httpRequest = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
        var responseFuture = httpClient.sendAsync(httpRequest, (status, httpHeaders) -> status == 200
                ? HttpResponse.BodySubscriber.asString(Charset.defaultCharset())
                : new FailingBodySubscriber<>(new IOException("status not 200")));
        return responseFuture.thenApply(HttpResponse::body);
    }

    static class FailingBodySubscriber<T> implements HttpResponse.BodySubscriber<T> {

        private final CompletableFuture<T> bodyFuture = new CompletableFuture<>();
        private final Throwable failure;

        FailingBodySubscriber(Throwable failure) {
            this.failure = failure;
        }

        @Override
        public CompletionStage<T> getBody() {
            return bodyFuture;
        }

        @Override
        public void onSubscribe(Flow.Subscription subscription) {
            subscription.request(Long.MAX_VALUE);
        }

        @Override
        public void onNext(List<ByteBuffer> items) {
        }

        @Override
        public void onError(Throwable throwable) {
            bodyFuture.completeExceptionally(throwable);
        }

        @Override
        public void onComplete() {
            bodyFuture.completeExceptionally(failure);
        }
    }
}
