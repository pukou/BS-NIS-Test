package com.bsoft.nis.domain.evaluation.evalnew;

import com.bsoft.nis.domain.CustomJsonDateDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EvalutionRecord {
    private Long JLXH;

    private Long ZYH;

    private Integer BRBQ;

    private Long YSXH;

    private Integer BBH;

    private Long YSLX;

    private Date TXSJ;

    private String TXGH;

    private Date JLSJ;

    private String QMGH;

    private Date QMSJ;

    private Short SYZT;

    private String SYGH;

    private Date SYSJ;

    private Short DYCS;

    private Short ZFBZ;

    private Integer JGID;

    private String PGNR;

    private String Status;

    private String TXXM;

    private String QMXM;

    private String SYXM;

    private List<EvalutionRecordDetail> details = new ArrayList<>();

    @JsonProperty(value = "JLXH")
    public Long getJLXH() {
        return JLXH;
    }

    public void setJLXH(Long JLXH) {
        this.JLXH = JLXH;
    }

    @JsonProperty(value = "ZYH")
    public Long getZYH() {
        return ZYH;
    }

    public void setZYH(Long ZYH) {
        this.ZYH = ZYH;
    }

    @JsonProperty(value = "BRBQ")
    public Integer getBRBQ() {
        return BRBQ;
    }

    public void setBRBQ(Integer BRBQ) {
        this.BRBQ = BRBQ;
    }

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

    @JsonProperty(value = "YSLX")
    public Long getYSLX() {
        return YSLX;
    }

    public void setYSLX(Long YSLX) {
        this.YSLX = YSLX;
    }

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonProperty(value = "TXSJ")
    public Date getTXSJ() {
        return TXSJ;
    }

    @JsonDeserialize(using = CustomJsonDateDeserializer.class)
    public void setTXSJ(Date TXSJ) {
        this.TXSJ = TXSJ;
    }

    @JsonProperty(value = "TXGH")
    public String getTXGH() {
        return TXGH;
    }

    public void setTXGH(String TXGH) {
        this.TXGH = TXGH == null ? null : TXGH.trim();
    }

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonProperty(value = "JLSJ")
    public Date getJLSJ() {
        return JLSJ;
    }

    @JsonDeserialize(using = CustomJsonDateDeserializer.class)
    public void setJLSJ(Date JLSJ) {
        this.JLSJ = JLSJ;
    }

    @JsonProperty(value = "QMGH")
    public String getQMGH() {
        return QMGH;
    }

    public void setQMGH(String QMGH) {
        this.QMGH = QMGH == null ? null : QMGH.trim();
    }

    @JsonProperty(value = "QMSJ")
    public Date getQMSJ() {
        return QMSJ;
    }

    @JsonDeserialize(using = CustomJsonDateDeserializer.class)
    public void setQMSJ(Date QMSJ) {
        this.QMSJ = QMSJ;
    }

    @JsonProperty(value = "SYZT")
    public Short getSYZT() {
        return SYZT;
    }

    public void setSYZT(Short SYZT) {
        this.SYZT = SYZT;
    }

    @JsonProperty(value = "SYGH")
    public String getSYGH() {
        return SYGH;
    }

    public void setSYGH(String SYGH) {
        this.SYGH = SYGH == null ? null : SYGH.trim();
    }

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonProperty(value = "SYSJ")
    public Date getSYSJ() {
        return SYSJ;
    }

    @JsonDeserialize(using = CustomJsonDateDeserializer.class)
    public void setSYSJ(Date SYSJ) {
        this.SYSJ = SYSJ;
    }

    @JsonProperty(value = "DYCS")
    public Short getDYCS() {
        return DYCS;
    }

    public void setDYCS(Short DYCS) {
        this.DYCS = DYCS;
    }

    @JsonProperty(value = "ZFBZ")
    public Short getZFBZ() {
        return ZFBZ;
    }

    public void setZFBZ(Short ZFBZ) {
        this.ZFBZ = ZFBZ;
    }

    @JsonProperty(value = "JGID")
    public Integer getJGID() {
        return JGID;
    }

    public void setJGID(Integer JGID) {
        this.JGID = JGID;
    }

    @JsonProperty(value = "PGNR")
    public String getPGNR() {
        return PGNR;
    }

    public void setPGNR(String PGNR) {
        this.PGNR = PGNR == null ? null : PGNR.trim();
    }

    @JsonProperty(value = "Status")
    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    @JsonProperty(value = "TXXM")
    public String getTXXM() {
        return TXXM;
    }

    public void setTXXM(String TXXM) {
        this.TXXM = TXXM;
    }

    @JsonProperty(value = "QMXM")
    public String getQMXM() {
        return QMXM;
    }

    public void setQMXM(String QMXM) {
        this.QMXM = QMXM;
    }

    @JsonProperty(value = "SYXM")
    public String getSYXM() {
        return SYXM;
    }

    public void setSYXM(String SYXM) {
        this.SYXM = SYXM;
    }

    @JsonProperty(value = "details")
    public List<EvalutionRecordDetail> getDetails() {
        return details;
    }

    public void setDetails(List<EvalutionRecordDetail> details) {
        this.details = details;
    }
}