package com.bsoft.nis.advicesplit.exception;

/**
 * Describtion:拆分必须数据组装异常
 * Created: dragon
 * Date： 2016/12/9.
 */
public class WrapperException extends Exception{
    private static final long serialVersionUID = -8480163368372400661L;
    public WrapperException(String message){
        super(message);
    }
}
