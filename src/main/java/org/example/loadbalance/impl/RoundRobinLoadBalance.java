package org.example.loadbalance.impl;

import org.example.loadbalance.SourceNode;
import org.example.loadbalance.TargetNode;

public class RoundRobinLoadBalance extends AbstractLoadBalance {

    private int targetIdx = 0;

    @Override
    public TargetNode select(SourceNode sourceNode) {
        lock.lock();
        try {
            return targets.get(targetIdx);
        } finally {
            targetIdx = targetIdx == Integer.MAX_VALUE ? 0 : targetIdx + 1;
            lock.unlock();
        }
    }

    @Override
    protected void nodeChanged() {
    }
}
