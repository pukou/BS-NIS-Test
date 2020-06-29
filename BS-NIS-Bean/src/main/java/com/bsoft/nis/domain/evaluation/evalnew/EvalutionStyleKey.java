package com.bsoft.nis.domain.evaluation.evalnew;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EvalutionStyleKey {
    public EvalutionStyleKey(){}

    public EvalutionStyleKey(Integer jgid,Long ysxh,Integer bbh){
        this.JGID = jgid;
        this.YSXH = ysxh;
        this.BBH = bbh;
    }

    private Long YSXH;

    private Integer BBH;

    private Integer JGID;

    @JsonProperty(value = "YSXH")
    public Long getYSXH() {
        return YSXH;
    }

    public void setYSXH(Long YSXH) {
        this.YSXH = YSXH;
    }

    @JsonProperty(value = "BBH")
    public Integer getBBH() {
        return BBH;
    }

    public void setBBH(Integer BBH) {
        this.BBH = BBH;
    }

    @JsonProperty(value = "JGID")
    public Integer getJGID() {
        return JGID;
    }

    public void setJGID(Integer JGID) {
        this.JGID = JGID;
    }
}