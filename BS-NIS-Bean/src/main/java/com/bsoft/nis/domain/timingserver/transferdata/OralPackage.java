package com.bsoft.nis.domain.timingserver.transferdata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Describtion:口服包装
 * User: dragon (xinghl@bsoft.com.cn)
 * Date： 2017/4/11.
 * Time:  15:21.
 */
public class OralPackage implements Serializable {
    private static final long serialVersionUID = 6023963839599919430L;
    public Long KFMX    ;
    public Long KFDH    ;
    public String TMBH  ;
    public String YDMS  ;
    public Integer ZFBZ ;
    public String LYBS  ;
    public Integer KFZT ;
    public Date ZXSJ    ;
    public String ZXGH  ;
    public Date HDSJ    ;
    public String HDGH  ;
    public Integer DYCS ;
    public String DYGH  ;
    public Date DYSJ    ;

    public List<OralDetail> details = new ArrayList<>();
}
