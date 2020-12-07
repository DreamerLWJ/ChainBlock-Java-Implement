package com.imitee.chainblock.repository;

import com.imitee.chainblock.core.block.Block;
import com.imitee.chainblock.core.transaction.Transaction;
import com.imitee.chainblock.core.transaction.TransactionInput;
import com.imitee.chainblock.core.transaction.TransactionOutput;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @Description: TODO(请添加描述)
 * @Author 11740
 * @Date 2020/12/6 0:32
 */
public class ChainBlockRepository {
    /**
     * 用于保存创世交易
     */
    private static Transaction genesisTransaction;
    /**
     * 保存创世区块
     */
    private static Block genesisBlock;
    /**
     * 比较粗略是用数组列表来实现这个区块链数据结构
     */
    private final static ArrayList<Block> blockChain = new ArrayList<>();
    /**
     * 整个区块的未花费输出
     */
    public static HashMap<String, TransactionOutput> UTXOs = new HashMap<>();
    /**
     * 工作量证明
     */
    public static int difficulty = 4;

    public static void addBlock(Block block) {
        blockChain.add(block);
    }

    public static boolean checkBlockValid(Block block) {
        return true;
    }

    /**
     * 验证整条链是否合理
     * @return 合理或者不合理
     */
    public static Boolean isChainValid() {
        // 检查前两个区块的 hash 值比对
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');
        // 检查 UTXO
        HashMap<String, TransactionOutput> tempUTXOs = new HashMap<>();
        tempUTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0));

        // 循环检查 hash
        for (int i = 1; i < blockChain.size(); i++) {
            currentBlock = blockChain.get(i);
            previousBlock = blockChain.get(i - 1);
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
}
