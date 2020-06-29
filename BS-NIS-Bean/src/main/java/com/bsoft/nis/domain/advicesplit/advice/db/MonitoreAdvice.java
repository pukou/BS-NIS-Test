package com.bsoft.nis.domain.advicesplit.advice.db;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * Describtion:监控医嘱表
 * Created: dragon
 * Date： 2016/12/12.
 */
public class MonitoreAdvice implements Serializable{
    private static final long serialVersionUID = -641795302040520976L;

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
    public String CZSJ  ;
    public String JGID  ;
}
