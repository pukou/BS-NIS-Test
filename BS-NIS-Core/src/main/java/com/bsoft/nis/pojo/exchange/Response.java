package com.bsoft.nis.pojo.exchange;

import java.io.Serializable;
import java.util.List;

/**
 * 所有WEB、移动终端 正文响应类
 * Created by Administrator on 2016/10/11.
 */
public class Response<T> implements Serializable{
    private static final long serialVersionUID = -1209928024297514930L;
    /**
     * 0 成功 非0为异常{100:session过期}
     */
    public int ReType;

    /**
     * 消息
     */
    public String Msg;

    /**
     * 数据
     */
    public T Data;
}
