package com.bsoft.nis.domain.patient.db;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Describtion:过敏药物
 * Created: dragon
 * Date： 2016/10/20.
 */
public class AllergicDrug implements Serializable{
    private String YPMC;

    private String GMZZ;

    @JsonProperty(value = "YPMC")
    public String getYPMC() {
        return YPMC;
    }

    public void setYPMC(String YPMC) {
        this.YPMC = YPMC;
    }

    @JsonProperty(value = "GMZZ")
    public String getGMZZ() {
        return GMZZ;
    }

    public void setGMZZ(String GMZZ) {
        this.GMZZ = GMZZ;
    }
}
