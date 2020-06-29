package com.bsoft.nis.domain.core.synchron.rules;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Describtion:同步方式(IENR_TBFS)
 * Created: dragon
 * Date： 2016/12/28.
 */
public class Synchron implements Serializable {
    private static final long serialVersionUID = -3120052541651328130L;
    /**
     * 同步序号
     */
    public Long TBXH;
    /**
     * 同步描述
     */
    public String TBMS;
    /**
     * 同步规则 预留
     1:弹出提示界面,手动选择同步
     2:指定同步到最后一次填写的记录类型中
     */
    public Integer TBGZ;
    /**
     * 对照类型
     DMLB = 445
     1	分类
     2	表单
     3	项目
     */
    public Integer DZLX;
    /**
     * 表单类型
     MOB_XTPZ  DMLB = 442
     1	护理评估  IENR_BDYS                      (表单类型)
     2	风险评估  IENR_FXPGD                    (表单类型)
     3	健康宣教  IENR_XMGL                      (分类类型)
     4	护理计划  IENR_HLJHWT                 (分类类型)
     5	生命体征                                             (项目类型)
     6	护理记录  ENR_JG01                         (表单类型)
     7  风险措施  IENR_FXCSD                     (分类类型 )
     */
    public Integer BDLX;
    /**
     * 来源表单
     */
    public Long LYBD;
    /**
     * 来源表单名称
     */
    public String LYBDMC;
    /**
     * 目标类型
     MOB_XTPZ  DMLB = 442
     1	护理评估  IENR_BDYS                      (表单类型)
     2	风险评估  IENR_FXPGD                    (表单类型)
     3	健康宣教  IENR_XMGL                      (分类类型)
     4	护理计划  IENR_HLJHWT                 (分类类型)
     5	生命体征                                             (项目类型)
     6	护理记录  ENR_JG01                         (表单类型)
     7  风险措施  IENR_FXCSD                     (分类类型 )
     */
    public Integer MBLX;
    /**
     * 目标表单
     */
    public Integer MBBD;
    /**
     * 目标表单名称
     */
    public String MBBDMC;
    /**
     * 机构ID
     */
    public String JGID;
    /**
     * 状态标识
     */
    public Integer ZTBZ;
    /**
     * 目标项目列表
     */
    public List<SynchronMission> missionProjects = new ArrayList<>();
}
