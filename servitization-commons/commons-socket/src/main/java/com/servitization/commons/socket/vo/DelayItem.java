package com.servitization.commons.socket.vo;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class DelayItem<K, V> implements Delayed {
    /**
     * Base of nanosecond timings, to avoid wrapping
     */
    private static final long NANO_ORIGIN = System.nanoTime();

    /**
     * Returns nanosecond time offset by origin
     */
    final static long now() {
        return System.nanoTime() - NANO_ORIGIN;
    }

    /**
     * Sequence number to break scheduling ties, and in turn to guarantee FIFO order among tied
     * entries.
     */
    private static final AtomicLong sequencer = new AtomicLong(0);

    /**
     * Sequence number to break ties FIFO
     */
    private final long sequenceNumber;

    /**
     * The time the task is enabled to execute in nanoTime units
     */
    private final long time;

    private final K key;
    private final V value;

    public DelayItem(K k, V v, long timeout) {
        this.time = now() + timeout;
        this.key = k;
        this.value = v;
        this.sequenceNumber = sequencer.getAndIncrement();
    }

    public K getKey() {
        return this.key;
    }

    public V getValue() {
        return this.value;
    }

    public long getDelay(TimeUnit unit) {
        long d = unit.convert(time - now(), TimeUnit.NANOSECONDS);
        return d;
    }

    @SuppressWarnings("rawtypes")
    public int compareTo(Delayed other) {
        if (other == this) // compare zero ONLY if same object
            return 0;
        if (other instanceof DelayItem) {
            DelayItem x = (DelayItem) other;
            long diff = time - x.time;
            if (diff < 0)
                return -1;
            else if (diff > 0)
                return 1;
            else if (sequenceNumber < x.sequenceNumber)
                return -1;
            else
                return 1;
        }
        long d = (getDelay(TimeUnit.NANOSECONDS) - other.getDelay(TimeUnit.NANOSECONDS));
        return (d == 0) ? 0 : ((d < 0) ? -1 : 1);
    }
}
