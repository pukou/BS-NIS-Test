package com.bsoft.nis.domain.advicesplit.plantype;

import com.bsoft.nis.domain.advicesplit.plantype.db.PlanTypeDetail;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Describtion:
 * Created: dragon
 * Date： 2016/12/8.
 */
public class DeptPlanTypes implements Serializable{
    private static final long serialVersionUID = -4482692052667917185L;
    /**
     * 归属类型
     */
    public Long GSLX;
    /**
     * 归属名称
     */
    public String GSMC;
    /**
     * 类型号
     */
    public Long LXH;
    /**
     * 类型名称
     */
    public String LXMC;

    /**
     * 科室代码
     */
    public String KSDM;
    /**
     * 是否拆分
     */
    public Integer SFCF;
    /**
     * 周期起始
     */
    public String ZQQS;
    /**
     * 周期结束
     */
    public String ZQJS;
    /**
     * 首日规则
     */
    public Integer SRGZ;
    /**
     * 停嘱截止
     */
    public String TZJZ;
    /**
     * 明细类型(ENR_JHMX)
     */
    public Long MXLX;
    /**
     * 明细序号(ENR_JHMX)
     */
    public Long MXXH;
    /**
     * 计划明细
     */
    public List<PlanTypeDetail> planDetails = new ArrayList<>();
}
