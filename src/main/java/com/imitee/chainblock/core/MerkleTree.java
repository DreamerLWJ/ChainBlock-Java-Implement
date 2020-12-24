package com.imitee.chainblock.core;

import com.imitee.chainblock.core.transaction.Transaction;
import org.apache.juli.logging.Log;

import java.util.ArrayList;

import static com.imitee.chainblock.util.StringUtil.applySha256;

/**
 * @Description: merkleTree 实现算法
 * @Author 11740
 * @Date 2020/12/7 17:03
 */
public class MerkleTree {
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
    private String root;
    /**
     * 保存所有交易有效性，但可能不进行持久化
     */
    private ArrayList<Transaction> transactions;

    public MerkleTree(ArrayList<Transaction> transactions) {
        this.transactions = transactions;

        // 计算并保证size一定为偶数
        if (transactions.size() % 2 == 0) {
            size = transactions.size();
        } else {
            size = transactions.size() + 1;
            transactions.add(transactions.get(size - 1));
        }
        height = (int) (Math.log(size + 1) / Math.log(2)); // 计算高度

        // 计算 Merkle Root
        root = createMerkleRoot(transactions);
    }

    /**
     * 直接根据交易输入创建 merkle root
     * @param transactions 输入的交易
     * @return 返回 hash 值
     */
    public static String justRoot(ArrayList<Transaction> transactions) {
        return createMerkleRoot(transactions);
    }


    /**
     * 用于计算 MerkleRoot 的函数
     */
    private static String createMerkleRoot(ArrayList<Transaction> transactions) {
        // 保存每一层的结点数目
        int count = transactions.size();
        ArrayList<String> previousTreeLayer = new ArrayList<>();
        // 创建交易结点
        for (Transaction transaction : transactions) {
            previousTreeLayer.add(transaction.transactionId);
        }

        // 指向当前层次的指针
        ArrayList<String> treeLayer = previousTreeLayer;
        while (count != 1) {
            // 如果结点的个数不是 1
            treeLayer = new ArrayList<>();
            for (int i = 1; i < previousTreeLayer.size(); i += 2) {
                // 两两计算它们的 hash
                treeLayer.add(applySha256(previousTreeLayer.get(i - 1) + previousTreeLayer.get(i)));
            }
            // 记录新的层次的 hash 值
            count = treeLayer.size();
            previousTreeLayer = treeLayer;
        }
        // 保存根节点
        return treeLayer.get(0);
    }

    private String getRoot() {
        return root;
    }

    /**
     * @deprecated 尚未完成的方法
     * @param target
     * @return
     */
    private boolean verifyTransaction(Transaction target) {
        // 获取交易的 transactionId
        // TODO 验证交易是否属于一个区块的
        String transactionId = target.transactionId;
        return false;
    }
}
