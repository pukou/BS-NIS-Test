package com.bsoft.nis.domain.timingserver.transferdata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Describtion:注射单
 * User: dragon (xinghl@bsoft.com.cn)
 * Date： 2017/4/11.
 * Time:  15:26.
 */
public class InjectSheet implements Serializable{
    private static final long serialVersionUID = -6004518268436572768L;
    public Long ZSDH      ;
    public Long ZYH       ;
    public Long BRBQ      ;
    public String BRCH    ;
    public String TMBH    ;
    public Long YZZH      ;
    public Date ZSRQ      ;
    public Integer YPYF   ;
    public Date ZSSJ      ;
    public Integer SJBH   ;
    public String SJMC    ;
    public Integer ZSSD   ;
    public Integer PYBZ   ;
    public Date PYSJ      ;
    public String PYGH    ;
    public Integer PYLY   ;
    public String LYBS    ;
    public Integer DYCS   ;
    public Date DYSJ      ;
    public String DYGH    ;
    public Integer ZSZT   ;
    public Date ZXSJ      ;
    public String ZXGH    ;
    public Date JSSJ      ;
    public String JSGH    ;
    public Date ZXHDSJ    ;
    public String ZXHDGH  ;
    public Date JSHDSJ    ;
    public String JSHDGH  ;
    public Integer JYHDBZ ;
    public String JYHDGH  ;
    public Date JYHDSJ    ;
    public Integer BYHDBZ ;
    public String BYHDGH  ;
    public Date BYHDSJ    ;
    public String JGID    ;

    public List<InjectDetail> details = new ArrayList<>();
}
