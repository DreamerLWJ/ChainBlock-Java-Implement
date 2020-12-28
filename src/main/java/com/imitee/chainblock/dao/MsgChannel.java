package com.imitee.chainblock.dao;

import com.imitee.chainblock.core.block.Block;
import com.imitee.chainblock.entity.NodeBean;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @Description: TODO(请添加描述)
 * @Author 11740
 * @Date 2020/12/24 23:49
 */
public class MsgChannel {
    private final String ip;
    private final String openid;
    private final int port;
    private Socket socket;

    @NotNull
    public MsgChannel(String ip, String nodeId, int port) {
        this.ip = ip;
        this.openid = nodeId;
        this.port = port;
        prepareConnection();
    }

    @NotNull
    public MsgChannel(NodeBean bean) {
        this(bean.getIp(), bean.getNodeId(), bean.getPort());
    }

    private void prepareConnection() {
        // TODO 线程池
        socket = new Socket();
    }

    public void connect() {
        try {
            // 直接创建的连接
            socket.connect(new InetSocketAddress(ip, port), 5000);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pushBlock(Block block) {

    }
}
