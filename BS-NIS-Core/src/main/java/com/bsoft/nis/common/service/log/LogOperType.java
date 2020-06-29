package com.bsoft.nis.common.service.log;

import com.bsoft.nis.core.enumx.EnumBehaviour;

/**
 * Describtion:日志操作类别
 * Created: dragon
 * Date： 2016/11/22.
 */
public enum LogOperType implements EnumBehaviour{

    SpecimenCollect("标本采集",1);

    private String name;
    private int id;

    LogOperType(String name, int id){
        this.id = id;
        this.name = name;
    }

    @Override
    public Integer get() {
        return this.id;
    }
}
