package com.bsoft.nis.domain.adviceqyery.db;

import java.io.Serializable;
import java.util.Date;

/**
 * Describtion:输液暂停
 * User: dragon (xinghl@bsoft.com.cn)
 * Date： 2017/4/11.
 * Time:  14:31.
 */
public class TransusionSuspend implements Serializable{
    private static final long serialVersionUID = -9146931126798335773L;
    public String ZTBS;
    public String SYDH;
    public Date KSSJ;
    public String KSGH;
    public Date JSSJ;
    public String JSGH;
    public String JGID;
}
