package com.imitee.chainblock.entity;

import com.alibaba.fastjson.JSONObject;
import com.imitee.chainblock.NodeType;
import com.imitee.chainblock.exception.JSONStateIllegalException;
import com.imitee.chainblock.util.StringUtil;
import org.jetbrains.annotations.NotNull;

/**
 * @Description: TODO(请添加描述)
 * @Author 11740
 * @Date 2020/12/7 11:15
 */
public class NodeBean {
    /**
     * 该节点加入或者接受加入的时间戳
     */
    private final long timestamp;

    /**
     * IP 地址
     */
    private final String ip;

    /**
     * 该结点的性质：
     * 相对于当前结点而言，是观察者还是被观察者
     */
    private final NodeType nodeType;

    private final String nodeId;

    /**
     * 用于通信的端口
     */
    private int port;

    public NodeBean(long timestamp, String ip, int port, NodeType nodeType) {
        this.timestamp = timestamp;
        this.ip = ip;
        this.nodeType = nodeType;
        // 生成结点唯一的标识信息
        this.nodeId = generateNodeId();
    }

    public NodeBean(@NotNull JSONObject nodeS, NodeType nodeType) {
        this(Long.parseLong(
                nodeS.getString("timestamp")),
                nodeS.getString("ip"),
                nodeS.getInteger("port"),
                nodeType
        );
    }

    public NodeBean(String jsonBean, NodeType nodeType) {
        this(JSONObject.parseObject(jsonBean), nodeType);
    }

    public String getIp() {
        return ip;
    }

    private String generateNodeId() {
        return StringUtil.applySha256(toString());
    }

    @Override
    public String toString() {
        return timestamp + ip + nodeType;
    }

    public String getNodeId() {
        return nodeId;
    }

    public int getPort() {
        return port;
    }
}

