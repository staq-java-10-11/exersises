package nl.quintor.staq.java10.httpclient.http;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.Flow;
import java.util.concurrent.atomic.AtomicLong;

public class ByteCountingSubscriber implements Flow.Subscriber<List<ByteBuffer>> {

    private Flow.Subscription subscription;
    private final AtomicLong received = new AtomicLong();

    public long getReceivedByteCount() {
        return received.get();
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1);
    }

    @Override
    public void onNext(List<ByteBuffer> item) {
        subscription.request(1);
        final long bytes = item.stream().mapToLong(ByteBuffer::remaining).sum();
        received.addAndGet(bytes);
        System.out.println("ByteCountingSubscriber received " + bytes + " bytes");
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
