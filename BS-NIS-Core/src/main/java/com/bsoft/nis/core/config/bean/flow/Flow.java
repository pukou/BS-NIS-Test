package com.bsoft.nis.core.config.bean.flow;

import java.io.Serializable;

/**
 * Describtion:流程
 * Created: dragon
 * Date： 2016/12/29.
 */
public class Flow implements Serializable{
    private static final long serialVersionUID = -614213277426636532L;

    public Flow(){}

    public Flow(String flowName,String flowHandlerClass,String flowRemark){
        this.flowName = flowName;
        this.flowHandlerClass = flowHandlerClass;
        this.flowRemark = flowRemark;
    }

    public String flowName;

    public String flowHandlerClass;

    public String flowRemark;
}
