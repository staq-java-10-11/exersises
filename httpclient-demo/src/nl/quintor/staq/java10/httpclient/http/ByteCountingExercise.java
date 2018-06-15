package nl.quintor.staq.java10.httpclient.http;

import jdk.incubator.http.HttpClient;
import jdk.incubator.http.HttpRequest;
import jdk.incubator.http.HttpResponse;

import java.net.URI;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.Flow;
import java.util.concurrent.atomic.AtomicLong;

public class ByteCountingExercise {

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public long countBytes(final String url) {
        // 1. Make an asynchronous request to the url
        // Count the number of returned bytes, without persisting the entire response in-memory.
        // Use HttpResponse.BodyHandler.fromSubscriber to wire up your ByteCountingSubscriber as a BodyHandler, and let
        // obtain the "body" from the Subscriber's byte count.
        // BONUS: what happens when you wrap this BodyHandler into BodyHandler.buffering(...)?

        // var responseFuture = ...
        // var response = ...
        // System.out.println("ByteCountingExercise is done -  status was " + response.statusCode() + "; response reported " + response.body() + " bytes");
        // return response.body();
        throw new UnsupportedOperationException();
    }

    /**
     * A Flow Subscriber that counts the number of bytes it receives.
     */
    static class ByteCountingSubscriber implements Flow.Subscriber<List<ByteBuffer>> {

        private Flow.Subscription subscription;
        private final AtomicLong received = new AtomicLong();

        long getReceivedByteCount() {
            // 1. Return the number of bytes received so far.
            throw new UnsupportedOperationException();
        }

        @Override
        public void onSubscribe(Flow.Subscription subscription) {
            this.subscription = subscription;
            // What happens if you remove this line?
            subscription.request(1);
        }

        @Override
        public void onNext(List<ByteBuffer> item) {
            // 2. Count the number of received bytes.
            // Hint: the remaining() method on ByteBuffer gives the number of bytes in the buffer.

            // final long bytes = ...
            // received.addAndGet(bytes);
            // System.out.println("ByteCountingSubscriber received " + bytes + " bytes");

            // What happens if you remove this line?
            subscription.request(1);
        }

        @Override
        public void onError(Throwable throwable) {
            System.out.println("ByteCountingSubscriber received an error: " + throwable.toString());
        }

        @Override
        public void onComplete() {
            System.out.println("ByteCountingSubscriber completed after " + received + " bytes");
        }
    }
}
