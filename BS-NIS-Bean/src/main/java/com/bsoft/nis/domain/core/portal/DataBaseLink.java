package com.bsoft.nis.domain.core.portal;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Describtion:数据库连接
 * Created: dragon
 * Date： 2016/10/31.
 */
public class DataBaseLink implements Serializable{
    private static final long serialVersionUID = -5749259048367133216L;

    private String LJID;

    private String ZWMC;

    private String LJPZ;

    private String LJMC;

    @JsonProperty(value = "LJID")
    public String getLJID() {
        return LJID;
    }

    public void setLJID(String LJID) {
        this.LJID = LJID;
    }

    @JsonProperty(value = "ZWMC")
    public String getZWMC() {
        return ZWMC;
    }

    public void setZWMC(String ZWMC) {
        this.ZWMC = ZWMC;
    }

    @JsonProperty(value = "LJPZ")
    public String getLJPZ() {
        return LJPZ;
    }

    public void setLJPZ(String LJPZ) {
        this.LJPZ = LJPZ;
    }

    @JsonProperty(value = "LJMC")
    public String getLJMC() {
        return LJMC;
    }

    public void setLJMC(String LJMC) {
        this.LJMC = LJMC;
    }
}
