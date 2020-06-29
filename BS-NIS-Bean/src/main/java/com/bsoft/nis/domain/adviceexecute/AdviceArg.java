package com.bsoft.nis.domain.adviceexecute;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 所有流程里面准备好的要入库的对象
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-12-26
 * Time: 14:58
 * Version:
 */
public class AdviceArg {

    /**
     * 在哪个流程里面产生的数据
     */
    public String ClassName;

    /**
     * 病区医嘱对象
     */
    public List<AdviceBqyzInfo> AdviceBqyzInfoList = new ArrayList<>();

    /**
     * 医嘱计划对象
     */
    public List<PlanInfo> PlanInfoList = new ArrayList<>();

    /**
     * 病区医嘱计划对象
     */
    public List<PlanInfo> BQPlanInfoList = new ArrayList<>();

    /**
     * 医嘱本对象
     */
    public List<AdviceYzbInfo> AdviceYzbInfoList = new ArrayList<>();

	/**
	 * 输液单对象
	 */
	public List<TransfusionInfo> TransfusionInfoList = new ArrayList<>();

    /**
     * 电子单信息
     */
    public IRecordInfo RecordInfo;

}
