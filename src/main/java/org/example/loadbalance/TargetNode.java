package org.example.loadbalance;

import java.util.concurrent.atomic.AtomicLong;

public abstract class TargetNode {

    private final AtomicLong connects = new AtomicLong(0);

    /**
     * [1,n)
     */
    private int weight = 0;

    public long getConnects() {
        return connects.get();
    }

    public void incConnects() {
        connects.incrementAndGet();
    }

    public void decConnects() {
        connects.decrementAndGet();
    }

    public int getWeight() {
        return this.weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

}
