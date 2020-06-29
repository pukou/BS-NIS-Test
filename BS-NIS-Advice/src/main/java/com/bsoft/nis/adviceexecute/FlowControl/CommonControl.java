package com.bsoft.nis.adviceexecute.FlowControl;

import com.bsoft.nis.domain.adviceexecute.ExecuteArg;

/**
 * Description: 通用流程控制器 - 此流程必须用
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-12-26
 * Time: 13:38
 * Version:
 */
public class CommonControl extends FlowControl {
    public CommonControl() {
        super("0");
    }

    @Override
    public ExecuteArg BranchCall(ExecuteArg ea, Object[] objs) {
        return super.BranchCall(ea, objs);
    }
}
