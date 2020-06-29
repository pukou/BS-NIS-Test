package com.bsoft.nis.domain.evaluation.evalnew;

import com.bsoft.nis.domain.core.cached.Map2;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class EvalutionStyle extends EvalutionStyleKey {
    private Short YSLX;

    private String YSMC;

    private String PYDM;

    private String WBDM;

    private Integer PLSX;

    private Short ZTBZ;

    private String QYBQ;

    private Short DYWD;

    private String XML;

    private String SJHQFS;

    private List<Map2> baseInfo = new ArrayList<>();
    private List<EvalutionStyleProject> projects = new ArrayList<>();

    @JsonProperty(value = "YSLX")
    public Short getYSLX() {
        return YSLX;
    }

    public void setYSLX(Short YSLX) {
        this.YSLX = YSLX;
    }

    @JsonProperty(value = "YSMC")
    public String getYSMC() {
        return YSMC;
    }

    public void setYSMC(String YSMC) {
        this.YSMC = YSMC == null ? null : YSMC.trim();
    }

    @JsonProperty(value = "PYDM")
    public String getPYDM() {
        return PYDM;
    }

    public void setPYDM(String PYDM) {
        this.PYDM = PYDM == null ? null : PYDM.trim();
    }

    @JsonProperty(value = "WBDM")
    public String getWBDM() {
        return WBDM;
    }

    public void setWBDM(String WBDM) {
        this.WBDM = WBDM == null ? null : WBDM.trim();
    }

    @JsonProperty(value = "PLSX")
    public Integer getPLSX() {
        return PLSX;
    }

    public void setPLSX(Integer PLSX) {
        this.PLSX = PLSX;
    }

    @JsonProperty(value = "ZTBZ")
    public Short getZTBZ() {
        return ZTBZ;
    }

    public void setZTBZ(Short ZTBZ) {
        this.ZTBZ = ZTBZ;
    }

    @JsonProperty(value = "QYBQ")
    public String getQYBQ() {
        return QYBQ;
    }

    public void setQYBQ(String QYBQ) {
        this.QYBQ = QYBQ == null ? null : QYBQ.trim();
    }

    @JsonProperty(value = "DYWD")
    public Short getDYWD() {
        return DYWD;
    }

    public void setDYWD(Short DYWD) {
        this.DYWD = DYWD;
    }

    @JsonProperty(value = "XML")
    public String getXML() {
        return XML;
    }

    public void setXML(String XML) {
        this.XML = XML == null ? null : XML.trim();
    }

    @JsonProperty(value = "projects")
    public List<EvalutionStyleProject> getProjects() {
        return projects;
    }

    public void setProjects(List<EvalutionStyleProject> projects) {
        this.projects = projects;
    }

    @JsonProperty(value = "baseInfo")
    public List<Map2> getBaseInfo() {
        return baseInfo;
    }

    public void setBaseInfo(List<Map2> baseInfo) {
        this.baseInfo = baseInfo;
    }

    @JsonProperty(value = "SJHQFS")
    public String getSJHQFS() {
        return SJHQFS;
    }

    public void setSJHQFS(String SJHQFS) {
        this.SJHQFS = SJHQFS;
    }
}