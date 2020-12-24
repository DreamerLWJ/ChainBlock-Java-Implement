package com.imitee.chainblock.repository;

import com.imitee.chainblock.entity.NodeBean;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

/**
 * @Description: 保存整个拓扑网络结点的信息
 * @Author 11740
 * @Date 2020/12/7 11:35
 */
public class P2PNodeListRepository {
    private HashMap<String, NodeBean> nodesList = new HashMap<>();

    public void addNodeMsg(@NotNull NodeBean nodeBean) {
        nodesList.put(nodeBean.getNodeId(), nodeBean);
    }

    public void connectNode(@NotNull NodeBean bean) {

    }
}
