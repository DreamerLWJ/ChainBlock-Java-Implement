package com.imitee.chainblock.core;

import com.imitee.chainblock.core.transaction.Transaction;
import com.imitee.chainblock.core.transaction.TransactionInput;
import com.imitee.chainblock.core.transaction.TransactionOutput;

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 比特币钱包
 * @Author 11740
 * @Date 2020/12/4 15:30
 */
public class Wallet {
    public PrivateKey privateKey;
    public PublicKey publicKey;

    /**
     * Unspent Transaction Output：还没花费的交易输出，通常是指上一次交易的输出，即指向本比特币钱包的输出
     */
    public HashMap<String, TransactionOutput> UTXOs = new HashMap<>();

    public Wallet() {
        generateKeyPair();
    }

    public void generateKeyPair() {
        try {
            // 利用椭圆曲线加密算法来生成公钥和私钥
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA", "BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
            // 初始化 key 生成器并且产生一个 keypair
            keyGen.initialize(ecSpec, random);
            KeyPair keyPair = keyGen.generateKeyPair();
            // 将密钥对中的公钥和私钥进行提取
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
        } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
    }

    // 返回余额
    public float getBalance(){
        float total = 0;
        for (Map.Entry<String, TransactionOutput> item : ChainCenter.UTXOs.entrySet()) {
            TransactionOutput UTXO = item.getValue();
            if (UTXO.isMine(publicKey)) {
                // 将它添加到还未花费的交易中
                UTXOs.put(UTXO.id, UTXO);
                total += UTXO.value;
            }
        }
        return total;
    }

    // 花费向接收者发送比特币交易
    public Transaction sendFunds(PublicKey _recipient, float value) {
        if (getBalance() < value) {
            System.out.println("余额不足以发起这一次交易");
            return null;
        }

        // 创建交易输入，其实交易输入是指全局的交易输出
        ArrayList<TransactionInput> inputs = new ArrayList<>();
        // 全局交易总额
        float total = 0;

        // 产生本次交易的输入
        for (Map.Entry<String, TransactionOutput> item : UTXOs.entrySet()) {
            TransactionOutput UTXO = item.getValue();
            total += UTXO.value;
            // 添加到交易输入
            inputs.add(new TransactionInput(UTXO.id));
            if (total > value) {
                break;
            }
        }

        // 启动交易
        Transaction newTransaction = new Transaction(publicKey, _recipient, value, inputs);
        // 是用私钥进行签名
        newTransaction.generateSignature(privateKey);

        // 遍历输入，将产生的输出从UTXO中删除
        for (TransactionInput input : inputs) {
            UTXOs.remove(input.transactionOutputId);
        }
        return newTransaction;
    }

}
