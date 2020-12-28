package com.imitee.chainblock.repository;

import com.imitee.chainblock.dao.MsgChannel;
import com.imitee.chainblock.entity.NodeBean;
import org.jetbrains.annotations.NotNull;

import java.net.Socket;
import java.util.HashMap;

/**
 * @Description: 保存整个拓扑网络结点的信息
 * @Author 11740
 * @Date 2020/12/7 11:35
 */
public class P2PNodeListRepository {
    private static HashMap<String, NodeBean> nodesList = new HashMap<>();

    /**
     * 添加拓扑网络中的新节点信息
     * @param nodeBean 结点信息
     */
    public void addNodeMsg(@NotNull NodeBean nodeBean) {
        nodesList.put(nodeBean.getNodeId(), nodeBean);
    }

    public static void connectNode(@NotNull String beanId) {
        connectNode(nodesList.get(beanId));
    }

    /**
     * 主动发起连接
     * @param bean 要进行连接的结点
     */
    public static void connectNode(@NotNull NodeBean bean) {
        prepareConnection(bean);
    }

    /**
     * 当以这个方法进行连接时，本结点是作为订阅者进行连接的
     * @param bean 要连接的目标结点信息
     * @return 通信信道
     */
    private static MsgChannel prepareConnection(NodeBean bean) {
        // 创建的信道
        MsgChannel msgChannel = new MsgChannel(bean);
        return msgChannel;
    }
}
