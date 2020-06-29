package com.bsoft.nis.core.config.bean.flow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Describtion:流程组
 * Created: dragon
 * Date： 2016/12/29.
 */
public class FlowGroup implements Serializable{
    private static final long serialVersionUID = 1835147310810160860L;
    public FlowGroup(){}

    public FlowGroup(String id,String remark){
        this.id = id;
        this.remark = remark;
    }
    public String id;
    public String remark;
    public List<Flow> flows = new ArrayList<>();
}
