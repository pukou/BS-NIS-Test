package com.bsoft.nis.domain.evaluation.evalnew;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EvalutionStyleProjectKey {
    private Long XMXH;

    private Integer BBH;

    @JsonProperty(value = "XMXH")
    public Long getXMXH() {
        return XMXH;
    }

    public void setXMXH(Long XMXH) {
        this.XMXH = XMXH;
    }

    @JsonProperty(value = "BBH")
    public Integer getBBH() {
        return BBH;
    }

    public void setBBH(Integer BBH) {
        this.BBH = BBH;
    }
}