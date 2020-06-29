package com.bsoft.nis.domain.inspection;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/28.
 *
 * @类说明 检查
 */
public class ExamineVo implements Serializable {
    private static final long serialVersionUID = -517437606151365432L;
    /**
     * 样本号码
     */
    public String YBHM;
    /**
     * 门诊号码
     */
    public String MZHM;
    /**
     * 住院号码
     */
    public String ZYHM;
    /**
     * 检查类型 1 放射 0 超声
     */
    public String JCLX;
    /**
     * 检查名称
     */
    public String JCMC;
    /**
     * 部位描述
     */
    public String BWMS;
    /**
     * 登记工号-统一登记的人
     */
    public String DJGH;
    /**
     * 登记时间
     */
    public String DJSJ;
    /**
     * 检查医生-很少用
     */
    public String JCYS;
    /**
     * 检查时间
     */
    public String JCSJ;
    /**
     * 报告医生-开单医生
     */
    public String BGYS;
    /**
     * 报告时间
     */
    public String BGSJ;
    /**
     * 审核医生-开单之后的审核人
     */
    public String SHYS;
    /**
     * 审核时间
     */
    public String SHSJ;
    /**
     * 报告医生姓名
     */
    public String BGYSXM;
}
