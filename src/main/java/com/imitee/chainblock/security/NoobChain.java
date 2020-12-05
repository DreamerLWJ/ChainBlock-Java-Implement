package com.imitee.chainblock.security;

import com.imitee.chainblock.entity.Block;

import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 测试类，通常用于测试区块链逻辑的工作
 */
public class NoobChain {
    /**
     * 比较粗略是用数组列表来实现这个区块链数据结构
     */
    public static ArrayList<Block> blockchain = new ArrayList<>();
    /**
     *
     */
    public static HashMap<String, TransactionOutput> UTXOs = new HashMap<>();
    /**
     * 工作量证明
     */
    public static int difficulty = 4;

    public static float minimumTransaction = 0.1f;
    /**
     * 两个交易钱包
     */
    public static Wallet walletA;
    public static Wallet walletB;
    /**
     * 这是创世交易
     */
    public static Transaction genesisTransaction;

    public static void main(String[] args) {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); //Setup Bouncey castle as a Security Provider

        // 创建两个比特币钱包
        walletA = new Wallet();
        walletB = new Wallet();
        // 创世区块的钱包，用于发行比特币
        Wallet coinbase = new Wallet();

        // 根据交易发起者和接收者的公钥发起交易，默认情况下是发起者向接收者发送比特币
        genesisTransaction = new Transaction(coinbase.publicKey, walletA.publicKey, 100f, null);
        // 根据发起者生成签名
        genesisTransaction.generateSignature(coinbase.privateKey);
        genesisTransaction.transactionId = "0"; //manually set the transaction id

        // 添加输出交易
        genesisTransaction.outputs.add(new TransactionOutput(
                // 接收方
                genesisTransaction.recipient,
                // 交易值
                genesisTransaction.value,
                // 生成交易id
                genesisTransaction.transactionId));

        // 添加创世交易额
        UTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0));

        System.out.println("正在创建创世区块");
        Block genesis = new Block("0");
        genesis.addTransaction(genesisTransaction);
        addBlock(genesis);

        // 测试
        Block block1 = new Block(genesis.hash);
        System.out.println("\nWalletA余额为: " + walletA.getBalance());
        System.out.println("\nWalletA正在发起转账40到WalletB...");
        block1.addTransaction(walletA.sendFunds(walletB.publicKey, 40f));
        addBlock(block1);
        System.out.println("\nWalletA余额为: " + walletA.getBalance());
        System.out.println("WalletB余额为:" + walletB.getBalance());

        Block block2 = new Block(block1.hash);
        System.out.println("\nWalletA正在发起转账1000到WalletB...");
        block2.addTransaction(walletA.sendFunds(walletB.publicKey, 1000f));
        addBlock(block2);
        System.out.println("\nWalletA余额为: " + walletA.getBalance());
        System.out.println("WalletB余额为: " + walletB.getBalance());

        Block block3 = new Block(block2.hash);
        System.out.println("\nWalletB正在发起转账20到WalletA...");
        block3.addTransaction(walletB.sendFunds(walletA.publicKey, 20));
        System.out.println("\nWalletA余额为: " + walletA.getBalance());
        System.out.println("WalletB余额为:" + walletB.getBalance());

        System.out.println("\nWalletB正在发起转账20到WalletA...");
        block3.addTransaction(walletB.sendFunds(walletA.publicKey, 20));
        System.out.println("\nWalletA余额为: " + walletA.getBalance());
        System.out.println("WalletB余额为:" + walletB.getBalance());

        System.out.println("\nWalletB正在发起转账20到WalletA...");
        block3.addTransaction(walletB.sendFunds(walletA.publicKey, 20));
        System.out.println("\nWalletA余额为: " + walletA.getBalance());
        System.out.println("WalletB余额为:" + walletB.getBalance());

        System.out.println("\nWalletB正在发起转账20到WalletA...");
        block3.addTransaction(walletB.sendFunds(walletA.publicKey, 20));
        System.out.println("\nWalletA余额为: " + walletA.getBalance());
        System.out.println("WalletB余额为:" + walletB.getBalance());
        addBlock(block3);
        isChainValid();
    }

    /**
     * 验证整条链是否合理
     * @return 合理或者不合理
     */
    public static Boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');
        // 检查 UTXO
        HashMap<String, TransactionOutput> tempUTXOs = new HashMap<>();
        tempUTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0));

        // 循环检查 hash
        for (int i = 1; i < blockchain.size(); i++) {

            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i - 1);
            // 检查区块 hash
            if (!currentBlock.hash.equals(currentBlock.calculateHash())) {
                System.out.println("区块 hash 验证不一致");
                return false;
            }
            // 检查区块前驱 hash
            if (!previousBlock.hash.equals(currentBlock.previousHash)) {
                System.out.println("前驱 hash 不一致！");
                return false;
            }
            // 检查是否此区块已经经过挖掘
            if (!currentBlock.hash.substring(0, difficulty).equals(hashTarget)) {
                System.out.println("此区块尚未被挖掘！");
                return false;
            }

            // 检查区块链是否为合理的
            TransactionOutput tempOutput;
            for (int t = 0; t < currentBlock.transactions.size(); t++) {
                Transaction currentTransaction = currentBlock.transactions.get(t);

                // 检查此区块的交易签名
                if (currentTransaction.verifySignature()) {
                    System.out.println("Transaction(" + t + ") 签名是非法的！");
                    return false;
                }

                // 检查交易输入输出总额是否一致
                if (currentTransaction.getInputsValue() != currentTransaction.getOutputsValue()) {
                    System.out.println("Transaction(" + t + ")的输入和输出不一致！");
                    return false;
                }


                for (TransactionInput input : currentTransaction.inputs) {
                    tempOutput = tempUTXOs.get(input.transactionOutputId);

                    if (tempOutput == null) {
                        System.out.println("Transaction(" + t + ")的输出丢失了！");
                        return false;
                    }

                    if (input.UTXO.value != tempOutput.value) {
                        System.out.println("Transaction(" + t + ")的输出丢失了！");
                        return false;
                    }

                    tempUTXOs.remove(input.transactionOutputId);
                }

                for (TransactionOutput output : currentTransaction.outputs) {
                    tempUTXOs.put(output.id, output);
                }

                if (currentTransaction.outputs.get(0).recipient != currentTransaction.recipient) {
                    System.out.println("#Transaction(" + t + ") 交易目标输出与提供的公钥不一致");
                    return false;
                }
                if (currentTransaction.outputs.get(1).recipient != currentTransaction.sender) {
                    System.out.println("#Transaction(" + t + ") 交易发起者与提供公钥不一致");
                    return false;
                }
            }

        }
        System.out.println("此区块链是合理的");
        return true;
    }

    public static void addBlock(Block newBlock) {
        // 挖矿来获得记账权
        newBlock.mineBlock(difficulty);
        blockchain.add(newBlock);
    }
}