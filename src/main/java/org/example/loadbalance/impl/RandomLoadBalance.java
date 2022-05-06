package org.example.loadbalance.impl;

import org.example.loadbalance.SourceNode;
import org.example.loadbalance.TargetNode;

import java.util.Random;

public class RandomLoadBalance extends AbstractLoadBalance {

    private Random random = new Random();

    @Override
    protected void nodeChanged() {
    }

    @Override
    public TargetNode select(SourceNode sourceNode) {
        lock.lock();
        try {
            int idx = random.nextInt(targets.size());
            return targets.get(idx);
        } finally {
            lock.unlock();
        }
    }
}
