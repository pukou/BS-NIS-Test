package com.bsoft.nis.domain.core.doublecheck;

import java.io.Serializable;

/**
 * 双人核对记录
 * Created by Administrator on 2016/11/3.
 */
public class DoubleCheckType implements Serializable {
    /**
     * 记录序号
     */
    private Long JLXH;
    /**
     * 核对标识1
     */
    private String HDBS1;
    /**
     * 核对标识2
     */
    private String HDBS2;
    /**
     * 住院号
     */
    private String ZYH;
    /**
     * 病人病区
     */
    private String BRBQ;
    /**
     * 核对时间
     */
    private String HDSJ;
    /**
     * 核对工号
     */
    private String HDGH;
    /**
     * 核对状态
     */
    private String HDZT;
    /**
     * 机构ID
     */
    private String JGID;

    public String getZFBZ() {
        return ZFBZ;
    }

    public void setZFBZ(String ZFBZ) {
        this.ZFBZ = ZFBZ;
    }

    public Long getJLXH() {
        return JLXH;
    }

    public void setJLXH(Long JLXH) {
        this.JLXH = JLXH;
    }

    public String getHDBS1() {
        return HDBS1;
    }

    public void setHDBS1(String HDBS1) {
        this.HDBS1 = HDBS1;
    }

    public String getHDBS2() {
        return HDBS2;
    }

    public void setHDBS2(String HDBS2) {
        this.HDBS2 = HDBS2;
    }

    public String getZYH() {
        return ZYH;
    }

    public void setZYH(String ZYH) {
        this.ZYH = ZYH;
    }

    public String getBRBQ() {
        return BRBQ;
    }

    public void setBRBQ(String BRBQ) {
        this.BRBQ = BRBQ;
    }

    public String getHDSJ() {
        return HDSJ;
    }

    public void setHDSJ(String HDSJ) {
        this.HDSJ = HDSJ;
    }

    public String getHDGH() {
        return HDGH;
    }

    public void setHDGH(String HDGH) {
        this.HDGH = HDGH;
    }

    public String getHDZT() {
        return HDZT;
    }

    public void setHDZT(String HDZT) {
        this.HDZT = HDZT;
    }

    public String getJGID() {
        return JGID;
    }

    public void setJGID(String JGID) {
        this.JGID = JGID;
    }

    /**
     * 作废标志

     */
    private String ZFBZ;

}
