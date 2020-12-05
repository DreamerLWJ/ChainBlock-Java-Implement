package com.imitee.chainblock.security;

/**
 * @Description: TODO(请添加描述)
 * @Author 11740
 * @Date 2020/12/4 15:49
 */
public class TransactionInput {

    public String transactionOutputId;

    /**
     * 把所有未使用的交易输出
     */
    public TransactionOutput UTXO;

    public TransactionInput(String transactionOutputId) {
        this.transactionOutputId = transactionOutputId;
    }
}
