package com.bsoft.nis.domain.timingserver.transferdata;

import java.io.Serializable;
import java.util.Date;

/**
 * Describtion:输液暂停记录
 * User: dragon (xinghl@bsoft.com.cn)
 * Date： 2017/4/11.
 * Time:  15:38.
 */
public class TransfusionSuspend implements Serializable{
    private static final long serialVersionUID = 5831127138080049555L;
    public Long ZTBS   ;
    public Long SYDH   ;
    public Date KSSJ   ;
    public String KSGH ;
    public Date JSSJ   ;
    public String JSGH ;
    public String JGID ;
}
