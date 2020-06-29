package com.bsoft.nis.adviceexecute.FlowControl;

import com.bsoft.nis.domain.adviceexecute.ExecuteArg;
import com.bsoft.nis.domain.adviceexecute.PlanAttrType;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 核对时间是否合法验证器
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-12-27
 * Time: 14:52
 * Version:
 */
public class CheckExcuteTimeControl extends FlowControl {
    public CheckExcuteTimeControl() {
        super("0");
    }

    @Override
    public ExecuteArg BranchCall(ExecuteArg ea, Object[] objs) {
        if(objs != null && objs.length > 0 && Boolean.parseBoolean(String.valueOf(objs[0]))) {
            List<PlanAttrType> planAttrTypeList = new ArrayList<>();
            //todo 暂未实现




        }
        return null;
    }
}
