package com.bsoft.nis.domain.core.dataset;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Describtion:数据组(ENR_SJZDY)
 * Created: dragon
 * Date： 2016/10/24.
 */
public class DataSet implements Serializable{

    private static final long serialVersionUID = -5783691516018019639L;

    private String SJZXH;

    private String SJZMC;

    private String GLSW;

    private String SJZDY;

    private String SJZSM;

    private String ZXBZ;

    @JsonProperty(value = "SJZXH")
    public String getSJZXH() {
        return SJZXH;
    }

    public void setSJZXH(String SJZXH) {
        this.SJZXH = SJZXH;
    }

    @JsonProperty(value = "SJZMC")
    public String getSJZMC() {
        return SJZMC;
    }

    public void setSJZMC(String SJZMC) {
        this.SJZMC = SJZMC;
    }

    @JsonProperty(value = "GLSW")
    public String getGLSW() {
        return GLSW;
    }

    public void setGLSW(String GLSW) {
        this.GLSW = GLSW;
    }

    @JsonProperty(value = "SJZDY")
    public String getSJZDY() {
        return SJZDY;
    }

    public void setSJZDY(String SJZDY) {
        this.SJZDY = SJZDY;
    }

    @JsonProperty(value = "SJZSM")
    public String getSJZSM() {
        return SJZSM;
    }

    public void setSJZSM(String SJZSM) {
        this.SJZSM = SJZSM;
    }

    @JsonProperty(value = "ZXBZ")
    public String getZXBZ() {
        return ZXBZ;
    }

    public void setZXBZ(String ZXBZ) {
        this.ZXBZ = ZXBZ;
    }
}
