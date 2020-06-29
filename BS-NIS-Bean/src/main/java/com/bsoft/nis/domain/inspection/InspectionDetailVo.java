package com.bsoft.nis.domain.inspection;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/28.
 * @类说明 检验详情
 */
public class InspectionDetailVo implements Serializable {
    private static final long serialVersionUID = 6161926354286093578L;


    /**
     * 异常标志
     */
    public int YCBZ;
    /**
     * 项目ID
     */
    public String XMID;
    /**
     * 化验结果
     */
    public String HYJG;
    /**
     * 单位
     */
    public String DW;
    /**
     * 中文名称
     */
    public String ZWMC;
    /**
     * 样本号码
     */
    public String YBHM;
    /**
     * 下限
     */
    public String CKXX;
    /**
     * 上限
     */
    public String CKSX;
    /**
     * 结果提示
     */
    public String JGTS;
}
