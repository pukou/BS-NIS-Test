package com.bsoft.nis.domain.timingserver.transferdata;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * Describtion:监控医嘱
 * User: dragon (xinghl@bsoft.com.cn)
 * Date： 2017/4/11.
 * Time:  15:06.
 */
public class MonitoreAdviceTrans implements Serializable{
    private static final long serialVersionUID = 6739628683941588505L;
    public Long YZXH  ;
    public Long ZYH   ;
    public Long BRBQ  ;
    @JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss")
    public Date FHSJ  ;
    @JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss")
    public Date TZFHSJ;
    @JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss")
    public Date JZSJ  ;
    public Integer ZFBZ  ;
    public String CZGH  ;
    public Date CZSJ  ;
    public String JGID  ;
}
