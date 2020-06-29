package com.bsoft.nis.domain.evaluation.evalnew;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * description:重新设计护理评估单：
 * 1.支持无限级项目
 * 2.支持自伸缩
 * create by: dragon xinghl@bsoft.com.cn
 * create time:2017/11/23 10:54
 * since:5.6 update1
 */
public class ComboUi {
    private String XMDM;
    private String XMMC;

    @JsonProperty(value = "XMDM")
    public String getXMDM() {
        return XMDM;
    }

    public void setXMDM(String XMDM) {
        this.XMDM = XMDM;
    }
    @JsonProperty(value = "XMMC")
    public String getXMMC() {
        return XMMC;
    }

    public void setXMMC(String XMMC) {
        this.XMMC = XMMC;
    }
}
