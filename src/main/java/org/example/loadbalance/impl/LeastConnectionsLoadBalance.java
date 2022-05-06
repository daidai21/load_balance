package org.example.loadbalance.impl;

import org.example.loadbalance.SourceNode;
import org.example.loadbalance.TargetNode;

public class LeastConnectionsLoadBalance extends AbstractLoadBalance {
    @Override
    public TargetNode select(SourceNode sourceNode) {
        lock.lock();
        try {
            int idx = 0;
            TargetNode targetNode = targets.get(0);
            for (int i = 1; i < targets.size(); ++i) {
                if (targets.get(i).getConnects() < targetNode.getConnects()) {
                    targetNode = targets.get(i);
                    idx = i;
                }
            }
            targets.get(idx).incConnects();
            return targetNode;
        } finally {
            lock.unlock();
        }
    }

    @Override
    protected void nodeChanged() {
    }
}
