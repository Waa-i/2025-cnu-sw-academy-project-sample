package edu.cnu.swacademy.exchange.engine.adapter;

import lombok.RequiredArgsConstructor;
import org.jctools.queues.MessagePassingQueue;

import java.util.concurrent.locks.LockSupport;

@RequiredArgsConstructor
public class MpscQueue<T> {
    private static final int SPIN_COUNT = 1000;
    private static final long DEFAULT_WAIT_TIME_NANOS = 1_000_000L;
    private final MessagePassingQueue<T> delegate;

    public void offer(T t) {
        delegate.relaxedOffer(t);
    }
    public T take() throws InterruptedException {
        while (true) {
            if(Thread.interrupted()) {
                throw new InterruptedException();
            }

            for(int i = 0; i < SPIN_COUNT; i++) {
                T item = delegate.relaxedPoll();
                if(item != null) {
                    return item;
                }
                Thread.onSpinWait();
            }
            LockSupport.parkNanos(DEFAULT_WAIT_TIME_NANOS);
        }
    }
}
