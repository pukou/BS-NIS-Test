package com.bsoft.nis.pojo.exchange;

import java.util.List;

/**
 * 所有service 服务接口正文相应类
 * Created by Administrator on 2016/10/11.
 */
public class BizResponse<T> {

    /**
     * 错误信息描述或提示性信息描述
     */
    public String message ;

    /**
     * 是否成功
     */
    public Boolean isSuccess;

    /**
     * 响应业务对象类
     */
    public T data;

    /**
     * 响应业务对象列表
     */
    public List<T> datalist;
}
