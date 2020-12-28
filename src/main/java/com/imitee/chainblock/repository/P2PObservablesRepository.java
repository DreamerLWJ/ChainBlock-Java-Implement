package com.imitee.chainblock.repository;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.imitee.chainblock.core.block.Block;
import com.imitee.chainblock.dao.MsgChannel;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @Description: TODO(请添加描述)
 * @Author 11740
 * @Date 2020/12/6 0:34
 */
public class P2PObservablesRepository {
    /**
     * 存放与目标结点连接的安全信道
     */
    private static HashMap<String, MsgChannel> msgChannelHashMap = new HashMap<>();

    public static void addChannel(String beanId, MsgChannel msgChannel) {
        // TODO 进行校验操作，用于校验目标结点是否可信

        // 存放入仓库中
        msgChannelHashMap.put(beanId, msgChannel);
    }

    public static void publishBlock(Block block) {
        msgChannelHashMap.entrySet().forEach(new Consumer<Map.Entry<String, MsgChannel>>() {
            @Override
            public void accept(Map.Entry<String, MsgChannel> stringMsgChannelEntry) {
                // 对每个信道进行发送区块
                stringMsgChannelEntry.getValue().pushBlock(block);
            }
        });
    }

    private static String getBLockJSON(Block block) {
        // 创建JSON对象
        return JSONObject.toJSONString(block);
    }
}
