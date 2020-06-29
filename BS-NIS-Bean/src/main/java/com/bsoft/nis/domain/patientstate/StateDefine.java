package com.bsoft.nis.domain.patientstate;

import java.io.Serializable;

/**
 * Describtion:状态定义(IEMR_ZTDY)
 * Created: dragon
 * Date： 2017/2/7.
 */
public class StateDefine implements Serializable{
    private static final long serialVersionUID = -3465691090788242102L;
    /**
     * 状态标识
     */
    public Integer ZTBS;
    /**
     * 状态名称
     */
    public String ZTMC;
    /**
     * 起始时间
     */
    public String QSSJ;
    /**
     * 更新间隔
     */
    public Integer GXJG;
    /**
     * 有效期限
     */
    public Integer YXQX;
    /**
     * 状态图标
     */
    public String ZTTB;
    /**
     * 状态标识
     */
    public Integer ZDBZ;
    /**
     * 启用标识
     */
    public Integer QYBZ;
    /**
     * 系统标识
     */
    public Integer XTBZ;
    /**
     * 更新次数
     */
    public Long GXCS;
    /**
     * 备注信息
     */
    public String BZXX;
    /**
     * 科室代码
     */
    public Integer KSDM;
}
