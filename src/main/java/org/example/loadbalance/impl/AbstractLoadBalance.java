package org.example.loadbalance.impl;

import org.example.loadbalance.LoadBalance;
import org.example.loadbalance.TargetNode;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class AbstractLoadBalance implements LoadBalance {

    protected final List<TargetNode> targets = new LinkedList<>();

    protected final Lock lock = new ReentrantLock();

    public void removeNode(TargetNode targetNode) {
        lock.lock();
        try {
            targets.remove(targetNode);
            nodeChanged();
        } finally {
            lock.unlock();
        }
    }

    public void addNode(TargetNode targetNode) {
        lock.lock();
        try {
            if (!targets.contains(targetNode)) targets.add(targetNode);
            nodeChanged();
        } finally {
            lock.unlock();
        }
    }

    protected abstract void nodeChanged();
}
