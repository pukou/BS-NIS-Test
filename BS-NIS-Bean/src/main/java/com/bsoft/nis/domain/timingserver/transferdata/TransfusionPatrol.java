package com.bsoft.nis.domain.timingserver.transferdata;

import java.io.Serializable;
import java.util.Date;

/**
 * Describtion:输液巡视记录
 * User: dragon (xinghl@bsoft.com.cn)
 * Date： 2017/4/11.
 * Time:  15:36.
 */
public class TransfusionPatrol implements Serializable{
    private static final long serialVersionUID = -8472125609437676743L;
    public Long XSBS   ;
    public Long SYDH   ;
    public Date XSSJ   ;
    public String XSGH ;
    public Integer SYDS;
    public Long SYFY   ;
}
