package com.bsoft.nis.common.service.log;

import com.bsoft.nis.core.enumx.EnumBehaviour;

/**
 * Describtion:日志操作类型
 * Created: dragon
 * Date： 2016/11/22.
 */
public enum  LogOperSubType implements EnumBehaviour{
    EXECUTE("执行",1),
    ENTER("录入",2),
    DELETE("删除",3),
    CANCEL("作废",4),
    PROVIDE("发放",5),
    RECYCLE("回收",6);

    private String name;
    private int id;

    LogOperSubType(String name, int id){
        this.id = id;
        this.name = name;
    }

    @Override
    public Integer get() {
        return this.id;
    }
}
