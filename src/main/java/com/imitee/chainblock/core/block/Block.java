package com.imitee.chainblock.core.block;

import com.imitee.chainblock.core.transaction.Transaction;
import com.imitee.chainblock.util.StringUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * @Description: 定义区块链的类块
 * @Author 11740
 * @Date 2020/12/4 0:30
 */
public class Block {
    /**
     * 生成区块的时间戳
     */
    private final long timestamp;

    /**
     * 当前区块的 hash 值，区块唯一标识
     * 每一个区块不仅包含前一个区块的 hash 值
     * 自身的 hash 值是通过之前的 hash 值和数据 data 通过 hash 计算出来的
     */
    public String hash;
    /**
     * 前一个区块的hash值
     */
    public String previousHash;
    /**
     * 工作量证明，计算正确 hash 值的次数
     */
    private int nonce;

    public String merkleRoot;
    /**
     * 当前区块存储的业务数据集合
     */
    public ArrayList<Transaction> transactions = new ArrayList<>();

    public Block(String previousHash) {
        this.previousHash = previousHash;
        this.timestamp = new Date().getTime();
        // 计算当前区块的 hash
        this.hash = calculateHash();
    }

    public String calculateHash() {
        return StringUtil.applySha256(
                previousHash +
                        timestamp +
                        nonce +
                        merkleRoot
        );
    }

    public void mineBlock(int difficulty) {
        // merkle 树来计算所有交易信息
        merkleRoot = StringUtil.getMerkleRoot(transactions);
        String target = new String(new char[difficulty]).replace('\0', '0');
        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }
        System.out.println("Block Mined!!! : " + hash);
    }

    // 将交易添加到区块中
    public boolean addTransaction(Transaction transaction) {
        if (transaction == null) {
            return false;
        }
        if (!previousHash.equals("0")) {
            if (!transaction.processTransaction()) {
                System.out.println("交易执行失败！");
                return false;
            }
        }

        transactions.add(transaction);
        System.out.println("交易完成并且成功添加到区块中");
        return true;
    }
}
