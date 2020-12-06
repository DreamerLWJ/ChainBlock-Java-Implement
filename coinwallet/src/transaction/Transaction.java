package transaction;

import com.imitee.chainblock.core.ChainCenter;
import com.imitee.chainblock.util.StringUtil;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

/**
 * @Description: 交易类
 * @Author 11740
 * @Date 2020/12/4 15:37
 */
public class Transaction {
    /**
     * 大致生成了多少交易数据
     */
    private static int sequence = 0;
    /**
     * 这笔交易的 hash 值
     */
    public String transactionId;
    /**
     * 发送者的公钥
     */
    public PublicKey sender;
    /**
     * 接收者的公钥
     */
    public PublicKey recipient;
    public float value;
    /**
     * 数字签名：防止多重支付
     */
    public byte[] signature;
    public ArrayList<TransactionInput> inputs;
    public ArrayList<TransactionOutput> outputs = new ArrayList<>();

    /**
     * 构造方法启动一次交易
     * @param from 比特币的转出方
     * @param to 比特币的转入方
     * @param value 比特币数量
     * @param inputs 交易的输入
     */
    public Transaction(PublicKey from, PublicKey to, float value, ArrayList<TransactionInput> inputs) {
        this.sender = from;
        this.recipient = to;
        this.value = value;
        this.inputs = inputs;
    }

    private String calculateHash() {
        sequence++;
        return StringUtil.applySha256(
                StringUtil.getStringFromKey(sender) +
                        StringUtil.getStringFromKey(recipient) +
                        value + sequence
        );
    }

    public void generateSignature(PrivateKey privateKey) {
        // 根据两者公钥和交易额生成签名
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(recipient) + value;
        signature = StringUtil.applyECDSASig(privateKey, data);
    }

    public boolean verifySignature() {
        // 根据两者公钥和交易额进行验证
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(recipient) + value;
        return !StringUtil.verifyECDSASig(sender, data, signature);
    }

    /**
     * 进行交易
     * @return 交易是否承认
     */
    public boolean processTransaction() {
        if (verifySignature()) {
            System.out.println("交易数字签名验证失败");
            return false;
        }

        // 将交易输入收集(保证他们都还没花费）
        for (TransactionInput i : inputs) {
            i.UTXO = ChainCenter.UTXOs.get(i.transactionOutputId);
        }

        // 生成余额
        float leftOver = getInputsValue() - value;
        // 生成交易的 id
        transactionId = calculateHash();
        /*
            UTXO思想：
            每次交易只有输入和输出
            1. 将交易额输出记录到接收者
            2. 将剩余部分输出记录到发送者
         */
        outputs.add(new TransactionOutput(this.recipient, value, transactionId));
        outputs.add(new TransactionOutput(this.sender, leftOver, transactionId));

        // 将本次交易输出记录到全局交易记录中
        for (TransactionOutput o : outputs) {
            ChainCenter.UTXOs.put(o.id, o);
        }

        // 最后从全局交易记录中删除这些已花费输入
        for (TransactionInput i : inputs) {
            if (i.UTXO == null) {
                continue;
            }
            ChainCenter.UTXOs.remove(i.UTXO.id);
        }
        return true;
    }

    /**
     * 对尚未输入的交易进行求和
     * @return 返回总和
     */
    public float getInputsValue() {
        float total = 0;
        for (TransactionInput i : inputs) {
            if (i.UTXO == null) {
                continue;
            }
            total += i.UTXO.value;
        }
        return total;
    }

    // 对输出进行求和
    public float getOutputsValue() {
        float total = 0;
        for (TransactionOutput o : outputs) {
            total += o.value;
        }
        return total;
    }
}
