package com.imitee.chainblock.core.block;

/**
 * @Description: 区块头部
 * @Author 11740
 * @Date 2020/12/6 0:54
 */
public class BlockHead {
    /**
     * 区块版本号
     */
    private String version;
    /**
     * 区块标识符，这里使用 hash
     */
    private String hash;
    /**
     * 前一个区块的 hash
     */
    private String previousHash;
    /**
     * merkle树的根
     */
    private String merkleRoot;
    /**
     * 时间戳
     */
    private long timestamp;
    /**
     * POW证明难度
     */
    private long difficulty;
    /**
     * 工作量证明的随机数
     */
    private int nonce;

    public BlockHead(String version, String previousHash, String merkleRoot, long timestamp, long difficulty, int nonce) {
        this.version = version;
        this.previousHash = previousHash;
        this.merkleRoot = merkleRoot;
        this.timestamp = timestamp;
        this.difficulty = difficulty;
        this.nonce = nonce;
    }

    public String getVersion() {
        return version;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public String getMerkleRoot() {
        return merkleRoot;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public long getDifficulty() {
        return difficulty;
    }

    public int getNonce() {
        return nonce;
    }
}
