package org.example.loadbalance.impl;

import org.example.loadbalance.SourceNode;
import org.example.loadbalance.TargetNode;
import org.example.loadbalance.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Ref   http://xueliang.org/article/detail/20200327131948809
public class WeightedRoundRobinLoadBalance extends AbstractLoadBalance {

    private final Random random = new Random();

    /**
     * TargetNode, currentWeight
     */
    private List<Pair<TargetNode, Integer>> weightTargets = new ArrayList<>();

    @Override
    public TargetNode select(SourceNode sourceNode) {
        lock.lock();
        try {
            TargetNode selected = null;
            int maxWeight = Integer.MIN_VALUE;
            int totalWeight = 0;
            int selectedIdx = 0;
            for (int i = 0; i < weightTargets.size(); ++i) {
                Pair<TargetNode, Integer> tmp = weightTargets.get(i);
                tmp.setRight(tmp.getRight() + tmp.getLeft().getWeight());
                if (tmp.getRight() > maxWeight) {
                    maxWeight = tmp.getRight();
                    selected = tmp.getLeft();
                    selectedIdx = i;
                }
                totalWeight += tmp.getLeft().getWeight();
            }
            if (selected != null) {
                weightTargets.get(selectedIdx).setRight(weightTargets.get(selectedIdx).getRight() - totalWeight);
                return selected;
            }
            return weightTargets.get(0).getLeft();
        } finally {
            lock.unlock();
        }
    }

    @Override
    protected void nodeChanged() {
        lock.lock();
        try {
            // FIXME: should not update all nodes
            weightTargets = new ArrayList<>();
            for (TargetNode tmp : targets) {
                weightTargets.add(new Pair<>(tmp, 0));
            }
        } finally {
            lock.unlock();
        }
    }
}
