package com.bsoft.nis.advicesplit.args;

import com.bsoft.nis.domain.advicesplit.advice.AdviceCom;
import com.bsoft.nis.domain.advicesplit.advice.db.Advice;
import com.bsoft.nis.domain.advicesplit.plantype.DeptPlanTypes;
import com.bsoft.nis.domain.advicesplit.ratetime.db.ExcuteRate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Describtion:计划类型包装类
 * 1.计划类型所包含的医嘱
 * 2.计划类型所对应的频次
 * Created: dragon
 * Date： 2016/12/1.
 */
public class PlanTypeArgs extends DeptPlanTypes implements Serializable{
    private static final long serialVersionUID = 1457341346752889497L;
    public PlanTypeArgs(){}
    public PlanTypeArgs(DeptPlanTypes deptPlanTypes){
        this.GSLX = deptPlanTypes.GSLX;
        this.GSMC = deptPlanTypes.GSMC;
        this.KSDM = deptPlanTypes.KSDM;
        this.LXH = deptPlanTypes.LXH;
        this.LXMC = deptPlanTypes.LXMC;
        this.MXLX = deptPlanTypes.MXLX;
        this.MXXH = deptPlanTypes.MXXH;
        this.SFCF = deptPlanTypes.SFCF;
        this.TZJZ = deptPlanTypes.TZJZ;
        this.SRGZ = deptPlanTypes.SRGZ;
        this.ZQJS = deptPlanTypes.ZQJS;
        this.ZQQS = deptPlanTypes.ZQQS;
        this.planDetails = deptPlanTypes.planDetails;
    }

    /**
     * 计划类型拆分医嘱
     */
    public List<AdviceCom> advices = new ArrayList<>();

    /**
     * 该计划类型对应执行频次
     */
    public List<ExcuteRate> rates = new ArrayList<>();

    public void addRate(ExcuteRate rate){
        this.rates.add(rate);
    }

    public List<ExcuteRate> getRates(){
        return rates;
    }
}
