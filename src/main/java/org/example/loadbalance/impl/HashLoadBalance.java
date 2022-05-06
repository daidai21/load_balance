package org.example.loadbalance.impl;

import org.example.loadbalance.SourceNode;
import org.example.loadbalance.TargetNode;

public class HashLoadBalance extends AbstractLoadBalance {
    @Override
    public TargetNode select(SourceNode sourceNode) {
        lock.lock();
        try {
            int idx = (int) sourceNode.hashValue() % targets.size();
            return targets.get(idx);
        } finally {
            lock.unlock();
        }
    }

    @Override
    protected void nodeChanged() {
    }
}
