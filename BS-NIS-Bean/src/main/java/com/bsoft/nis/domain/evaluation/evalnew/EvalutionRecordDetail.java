package com.bsoft.nis.domain.evaluation.evalnew;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EvalutionRecordDetail {
    public EvalutionRecordDetail(){}
    public EvalutionRecordDetail(Long XMXH,String XMNR,Short DZLX,Long DZBDJL,Short KJLX){
        this.XMXH = XMXH;
        this.XMNR = XMNR;
        this.DZLX = DZLX;
        this.DZBDJL = DZBDJL;
        this.KJLX = KJLX;
    }
    private Long MXXH;

    private Long JLXH;

    private Long XMXH;

    private Long SJXM;

    private String SJXMMC;

    private String XMNR;

    private Short DZLX;

    private Long DZBDJL;

    private Short KJLX;

    private String Status;

    @JsonProperty(value = "MXXH")
    public Long getMXXH() {
        return MXXH;
    }

    public void setMXXH(Long MXXH) {
        this.MXXH = MXXH;
    }

    @JsonProperty(value = "JLXH")
    public Long getJLXH() {
        return JLXH;
    }

    public void setJLXH(Long JLXH) {
        this.JLXH = JLXH;
    }

    @JsonProperty(value = "XMXH")
    public Long getXMXH() {
        return XMXH;
    }

    public void setXMXH(Long XMXH) {
        this.XMXH = XMXH;
    }

    @JsonProperty(value = "SJXM")
    public Long getSJXM() {
        return SJXM;
    }

    public void setSJXM(Long SJXM) {
        this.SJXM = SJXM;
    }

    @JsonProperty(value = "SJXMMC")
    public String getSJXMMC() {
        return SJXMMC;
    }

    public void setSJXMMC(String SJXMMC) {
        this.SJXMMC = SJXMMC == null ? null : SJXMMC.trim();
    }

    @JsonProperty(value = "XMNR")
    public String getXMNR() {
        return XMNR;
    }

    public void setXMNR(String XMNR) {
        this.XMNR = XMNR == null ? null : XMNR.trim();
    }

    @JsonProperty(value = "DZLX")
    public Short getDZLX() {
        return DZLX;
    }

    public void setDZLX(Short DZLX) {
        this.DZLX = DZLX;
    }

    @JsonProperty(value = "DZBDJL")
    public Long getDZBDJL() {
        return DZBDJL;
    }

    public void setDZBDJL(Long DZBDJL) {
        this.DZBDJL = DZBDJL;
    }

    @JsonProperty(value = "KJLX")
    public Short getKJLX() {
        return KJLX;
    }

    public void setKJLX(Short KJLX) {
        this.KJLX = KJLX;
    }

    @JsonProperty(value = "Status")
    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}