package com.bsoft.nis.adviceexecute.FlowControl;

import com.bsoft.nis.domain.adviceexecute.ExecuteArg;

/**
 * Description: 流控制
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-12-19
 * Time: 14:02
 * Version:
 */
public interface IFlowControl {
    ExecuteArg BranchCall(ExecuteArg ea, Object[] objs);
}
