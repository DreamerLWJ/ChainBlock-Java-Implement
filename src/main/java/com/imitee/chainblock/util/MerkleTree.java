package com.imitee.chainblock.util;

import com.imitee.chainblock.core.transaction.Transaction;

/**
 * @Description: TODO(请添加描述)
 * @Author 11740
 * @Date 2020/12/7 17:03
 */
public class MerkleTree {
    /**
     * 树节点
     */
    public class MerkleNode {
        // 本结点 hash 值
        private String hash;
    }

    /**
     * 树节点保存的位置
     */
    private final MerkleNode[] merkleNodes;

    /**
     * 树的高度
     */
    private int height;

    /**
     * 树的结点数目
     */
    private int size;
    /**
     * merkle root
     */
    private final MerkleNode root;

    public MerkleTree(Transaction[] transactions) {
        // 保证 size 为偶数
        size = transactions.length % 2 == 0 ? transactions.length : transactions.length + 1;
        height = (int) (Math.log(size + 1) / Math.log(2)); // 计算高度
    }

    private void createMerkleRoot(Transaction[] transactions) {

    }
}
