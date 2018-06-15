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

        // var httpRequest = ...
        // var responseFuture = httpClient.sendAsync(httpRequest, ...);
        // return responseFuture.thenApply(HttpResponse::body);
        throw new UnsupportedOperationException();
    }

    public CompletableFuture<Optional<String>> emptyOptionalIfNot200(final String url) {
        // 2. Make an asynchronous request to "url". Once you know the response code, if it is 200, read the
        // response body as an Optional of a String. If it is not 200, do not read the response body and return an empty
        // Optional

        // var httpRequest = ...
        // HttpResponse.BodyHandler<String> bodyHandler = ...
        // var responseFuture = httpClient.sendAsync(httpRequest, bodyHandler);
        // return responseFuture.thenApply(...);

        // BONUS: To avoid checking the response code twice, achieve the same by implementing and using
        // OptionalWrappingBodySubscriber (see below)

        // var httpRequest = ...
        // HttpResponse.BodyHandler<Optional<String>> bodyHandler = ...
        // var responseFuture = httpClient.sendAsync(httpRequest, bodyHandler);
        // return responseFuture.thenApply(HttpResponse::body);

        throw new UnsupportedOperationException();
    }

    static class OptionalWrappingBodySubscriber<T> implements HttpResponse.BodySubscriber<Optional<T>> {

        private HttpResponse.BodySubscriber<T> bodySubscriber;

        @Override
        public CompletionStage<Optional<T>> getBody() {
            // return ...;
            throw new UnsupportedOperationException();
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
    }

    public CompletableFuture<String> failedFutureIfNot200(final String url) {
        // 3. Make an asynchronous request to "url". Once you know the response code, if it is 200, read the
        // response body as a future String. If it is not 200, do not read the response body and make the resulting
        // future fail with an exceptional result.
        // BONUS: Make your own BodySubscriber to implement this

        // var httpRequest = ...;
        // var responseFuture = httpClient.sendAsync(httpRequest, ...);
        // return responseFuture.thenApply(HttpResponse::body);

        throw new UnsupportedOperationException();
    }
}
