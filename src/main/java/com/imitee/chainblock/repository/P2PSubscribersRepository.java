package com.imitee.chainblock.repository;

import com.imitee.chainblock.dao.MsgChannel;

import java.util.HashMap;

/**
 * @Description: 用于保存图中的结点的
 * @Author 11740
 * @Date 2020/12/6 0:35
 */
public class P2PSubscribersRepository {
    /**
     * 存放与目标结点连接的安全信道
     */
    private static HashMap<String, MsgChannel> msgChannelHashMap = new HashMap<>();

    public static void addChannel(String beanId, MsgChannel msgChannel) {
        // TODO 进行校验操作，用于校验目标结点是否可信

        // 存放入仓库中
        msgChannelHashMap.put(beanId, msgChannel);
    }
}
