package com.bsoft.nis.domain.inspection;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/2.
 */
public class SpecimenVo implements Serializable {
    /**
     * 条码编号
     */
    public String TMBH;
    /**
     * 项目名称
     */
    public String XMMC;
    /**
     * 开单时间
     */
    public String KDSJ;

    /**
     * 试管颜色  格式 十进制 R,B,G
     */
    public String SGYS;

    /**
     * 备注信息
     */
    public String BZXX;

    /**
     * 发放状态0 未发放 1 已发放。默认等于-1，表识服务器端未传输
     */
    public int FFZT = -1;

    /**
     * 采样标志 0 未采样 1 已采样
     */
    public int CYBZ;

    /**
     * 采样时间
     */
    public String CYRQ;
    public String CYR;
    //add 2018-5-8 14:59:59
    public String ZYHM;
    public String JYLX;
    public String JYLXMC;
    public String BBFL;
}
