package com.bsoft.nis.domain.timingserver.transferdata;

import java.io.Serializable;

/**
 * Describtion:输液明细
 * User: dragon (xinghl@bsoft.com.cn)
 * Date： 2017/4/11.
 * Time:  15:35.
 */
public class TransfusionDetail implements Serializable{
    private static final long serialVersionUID = -6304092037296683881L;
    public Long SYMX    ;
    public Long SYDH    ;
    public Long YZXH    ;
    public Long YPXH    ;
    public Integer YZZX ;
    public Double YCJL  ;
    public String JLDW  ;
    public Double YCSL  ;
    public String SLDW  ;
}
