package com.imitee.chainblock.security;

import com.imitee.chainblock.util.StringUtil;

import java.security.PublicKey;

/**
 * @Description: TODO(请添加描述)
 * @Author 11740
 * @Date 2020/12/4 15:49
 */
public class TransactionOutput {
    public String id;
    /**
     * 这些比特币的所有者公钥
     */
    public PublicKey recipient;
    /**
     * 比特币拥有的数量
     */
    public float value;

    public String parentTransactionId;

    public TransactionOutput(PublicKey recipient, float value, String parentTransactionId) {
        this.recipient = recipient;
        this.value = value;
        this.parentTransactionId = parentTransactionId;
        this.id = StringUtil.applySha256(StringUtil.getStringFromKey(recipient) + value + parentTransactionId);
    }

    /**
     * 验证比特币是否属于你
     * @param publicKey 公钥
     * @return 是否属于
     */
    public boolean isMine(PublicKey publicKey) {
        return publicKey == recipient;
    }
}
