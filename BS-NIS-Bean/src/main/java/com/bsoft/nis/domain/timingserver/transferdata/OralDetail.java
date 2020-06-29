package com.bsoft.nis.domain.timingserver.transferdata;

import java.io.Serializable;

/**
 * Describtion:口服包装明细
 * User: dragon (xinghl@bsoft.com.cn)
 * Date： 2017/4/11.
 * Time:  15:23.
 */
public class OralDetail implements Serializable{
    private static final long serialVersionUID = 2323829089619996994L;
    public Long BZMX   ;
    public Long KFMX   ;
    public Long KFDH   ;
    public Long YZXH   ;
    public Long YPXH   ;
    public Double BZJL ;
    public String JLDW ;
    public Double BZSL ;
    public String SLDW ;
    public String SJMC ;
    public Integer SJBH;
}
