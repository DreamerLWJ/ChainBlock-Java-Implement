package com.imitee.chainblock.entity;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Description: TODO(请添加描述)
 * @Author 11740
 * @Date 2020/12/4 0:30
 */
public class BlockCache {
    /**
     * 当前节点的区块链结构
     */
    private List<Block> blockChain = new CopyOnWriteArrayList<>();

    public List<Block> getBlockChain() {
        return blockChain;
    }

    public void setBlockChain(List<Block> blockChain) {
        this.blockChain = blockChain;
    }
}
