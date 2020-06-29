package com.bsoft.nis.domain.core;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Describtion:缓存配置
 * Created: dragon
 * Date： 2016/10/19.
 */
public class CachedConfig implements Serializable{
    /**
     * 字典名称
     */
    private String ZDMC ;

    /**
     * 字典说明
     */
    private String ZDSM ;

    /**
     * 主SQL
     */
    private String ZSQL ;

    /**
     * 数据连接
     */
    private String DBLJ ;

    @JsonProperty(value = "ZDMC")
    public String getZDMC() {
        return ZDMC;
    }

    public void setZDMC(String ZDMC) {
        this.ZDMC = ZDMC;
    }

    @JsonProperty(value = "ZDSM")
    public String getZDSM() {
        return ZDSM;
    }

    public void setZDSM(String ZDSM) {
        this.ZDSM = ZDSM;
    }

    @JsonProperty(value = "ZSQL")
    public String getZSQL() {
        return ZSQL;
    }

    public void setZSQL(String ZSQL) {
        this.ZSQL = ZSQL;
    }

    @JsonProperty(value = "DBLJ")
    public String getDBLJ() {
        return DBLJ;
    }

    public void setDBLJ(String DBLJ) {
        this.DBLJ = DBLJ;
    }
}
