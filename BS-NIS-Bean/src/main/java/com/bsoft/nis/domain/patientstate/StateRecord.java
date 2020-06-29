package com.bsoft.nis.domain.patientstate;

import java.io.Serializable;
import java.util.Date;

/**
 * Describtion:状态记录
 * Created: dragon
 * Date： 2017/2/7.
 */
public class StateRecord implements Serializable{
    private static final long serialVersionUID = -7970228281190673599L;
    public Long ZYH ;
    public Integer ZTBS;
    public String JGID;
    public Date GXSJ;
    public String JGNR;
}
