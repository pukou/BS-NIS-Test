package com.bsoft.nis.domain.inspection;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/28.
 * 检验
 */
public class InspectionVo implements Serializable {
    private static final long serialVersionUID = 1981756590472664311L;
    /**
     * 样本号码
     */
    public String YBHM;
    /**
     * 审核时间
     */
    public String SHSJ;
    /**
     * 项目名称
     */
    public String XMMC;
    /**
     * 异常标志 1异常 0 正常 异常此条记录为红色
     */
    public int YCBZ;
}
