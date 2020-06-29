package com.bsoft.nis.domain.user.db;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * 机构
 * Created by Administrator on 2016/10/17.
 */
public class Agency implements Serializable{
    private static final long serialVersionUID = 1337980950513258007L;
    private String JGID;
    private String JGMC;

    @JsonProperty(value = "JGID")
    public String getJGID() {
        return JGID;
    }

    public void setJGID(String JGID) {
        this.JGID = JGID;
    }

    @JsonProperty(value = "JGMC")
    public String getJGMC() {
        return JGMC;
    }

    public void setJGMC(String JGMC) {
        this.JGMC = JGMC;
    }
}
