package com.imitee.chainblock.service;

import com.imitee.chainblock.core.transaction.Transaction;

/**
 * @Description: 处理交易信息的服务
 * @Author 11740
 * @Date 2020/12/6 0:28
 */
public class TransactionService {

    /**
     * 用于通知与该结点连接的子结点
     * @param transaction 通知交易
     */
    public void notifyObserver(Transaction transaction) {

    }

    /**
     *
     * @param transaction
     * @return
     */
    public boolean accept(Transaction transaction) {
        return false;
    }
}
