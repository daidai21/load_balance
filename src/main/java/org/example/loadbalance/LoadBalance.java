package org.example.loadbalance;


public interface LoadBalance {

    /**
     * @param sourceNode
     * @return
     */
    TargetNode select(SourceNode sourceNode);
}
