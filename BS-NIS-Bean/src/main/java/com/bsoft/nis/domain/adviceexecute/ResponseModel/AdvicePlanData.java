package com.bsoft.nis.domain.adviceexecute.ResponseModel;

import com.bsoft.nis.domain.adviceexecute.PhraseModel;
import com.bsoft.nis.domain.adviceexecute.PlanInfo;

import java.util.List;

/**
 * Description:
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2017-01-17
 * Time: 16:03
 * Version:
 */
public class AdvicePlanData {

    /**
     * 医嘱计划列表
     */
    public List<PlanInfo> PlanInfoList;

    /**
     * 常用短语列表
     */
    public List<PhraseModel> PhraseModelList;


}
