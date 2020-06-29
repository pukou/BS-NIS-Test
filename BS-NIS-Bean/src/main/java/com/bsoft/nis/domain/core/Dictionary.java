package com.bsoft.nis.domain.core;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * 代码字典
 * Created by Administrator on 2016/10/18.
 */
public class Dictionary implements Serializable{

    private static final long serialVersionUID = 2279726121801085852L;
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
     * 拼音简码
     */
    @JsonProperty("SRDM")
    public String SRDM;

    /**
     * 代码名称
     */
    @JsonProperty("DMMC")
    public String DMMC;

    /**
     *
     */
    @JsonProperty("BZBM")
    public String BZBM;
}
