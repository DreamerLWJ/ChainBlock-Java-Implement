package com.imitee.chainblock.entity;

import com.imitee.chainblock.NodeType;

/**
 * @Description: TODO(请添加描述)
 * @Author 11740
 * @Date 2020/12/7 11:15
 */
public class NodeBean {
    /**
     * 该节点加入或者接受加入的时间戳
     */
    private long timestamp;

    /**
     * IP 地址
     */
    private String ip;

    /**
     * 该结点的性质：
     * 相对于当前结点而言，是观察者还是被观察者
     */
    private NodeType nodeType;

    public NodeBean(long timestamp, String ip, NodeType nodeType) {
        this.timestamp = timestamp;
        this.ip = ip;
        this.nodeType = nodeType;
    }
}
