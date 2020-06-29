package com.bsoft.nis.domain.core;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * 模拟字典数据
 * Created by Administrator on 2016/10/18.
 */
public class FalseDict implements Serializable{

    private static final long serialVersionUID = 1753358806461221470L;

    @JsonProperty(value = "DMSB")
    public String DMSB ;

    @JsonProperty(value = "DMSB")
    public String DMMC ;

}
