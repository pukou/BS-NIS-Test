package com.bsoft.nis.domain.core;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * 系统配置
 * Created by Administrator on 2016/10/18.
 */
public class SystemConfig implements Serializable{

    private static final long serialVersionUID = 6374494874830294063L;
    /**
     * 代码类别
     */
    @JsonProperty("DMLB")
    public String DMLB;

    /**
     * 代码识别
     */
    @JsonProperty("DMSB")
    public String DMSB;

    /**
     * 配制标志
     */
    @JsonProperty("PZBH")
    public String PZBH;

    /**
     * 代码名称
     */
    @JsonProperty("DMMC")
    public String DMMC;

    /**
     * 拼音简码
     */
    @JsonProperty("SRDM")
    public String SRDM;

    /**
     *
     */
    @JsonProperty("CPLB")
    public String CPLB;

    /**
     * 备注信息
     */
    @JsonProperty("BZXX")
    public String BZXX;
}
