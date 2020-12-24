package com.imitee.chainblock.exception;

/**
 * @Description: TODO(请添加描述)
 * @Author 11740
 * @Date 2020/12/24 23:56
 */
public class JSONStateIllegalException extends Exception{
    public JSONStateIllegalException() {
        super("json data format is incorrect!");
    }
}
