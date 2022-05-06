package org.example.loadbalance.impl;

import org.example.loadbalance.SourceNode;
import org.example.loadbalance.TargetNode;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

// https://www.xuxueli.com/blog/?blog=./notebook/6-%E7%AE%97%E6%B3%95/%E4%B8%80%E8%87%B4%E6%80%A7Hash%E7%AE%97%E6%B3%95.md
public class ConsistentHashLoadBalance extends AbstractLoadBalance {

    public ConsistentHashLoadBalance() {
        replicas = 3;
    }

    public ConsistentHashLoadBalance(int replicasValue) {
        replicas = replicasValue;
    }

    private final int replicas;

    private final TreeMap<Integer, TargetNode> virtualTargets = new TreeMap<>();

    @Override
    protected void nodeChanged() {
        lock.lock();
        try {
            for (TargetNode targetNode : targets) {
                for (int i = 0; i < replicas; ++i) {
                    int hashCode = hash("SHARD-" + targetNode + "-NODE-" + i);
                    virtualTargets.put(hashCode, targetNode);
                }
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public TargetNode select(SourceNode sourceNode) {
        lock.lock();
        try {
            int sourceNodeHash = hash(sourceNode.toString());
            SortedMap<Integer, TargetNode> tailMap = virtualTargets.tailMap(sourceNodeHash);
            return virtualTargets.get(tailMap.firstKey());
        } finally {
            lock.unlock();
        }
    }

    /**
     * md5
     *
     * @param input
     * @return
     */
    @SuppressWarnings("unchecked")
    private static int hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            return Integer.parseInt(String.valueOf(no));
        } catch (NoSuchAlgorithmException e) {
        }
        return 0;
    }
}
