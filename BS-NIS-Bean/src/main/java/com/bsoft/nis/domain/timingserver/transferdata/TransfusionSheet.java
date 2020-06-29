package com.bsoft.nis.domain.timingserver.transferdata;

import org.w3c.dom.stylesheets.LinkStyle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Describtion:输液单
 * User: dragon (xinghl@bsoft.com.cn)
 * Date： 2017/4/11.
 * Time:  15:31.
 */
public class TransfusionSheet implements Serializable{
    private static final long serialVersionUID = 6491093409891912159L;
    public Long SYDH      ;
    public Long ZYH       ;
    public Long BRBQ      ;
    public String BRCH    ;
    public String TMBH    ;
    public Integer YPYF   ;
    public String SYPC    ;
    public Long YZZH      ;
    public Date SYRQ      ;
    public Date SYSJ      ;
    public Integer SJBH   ;
    public String SJMC    ;
    public Integer SYSD   ;
    public Integer PYBZ   ;
    public Date PYSJ      ;
    public String PYGH    ;
    public Integer PYLY   ;
    public String LYBS    ;
    public Integer DYCS   ;
    public Date DYSJ      ;
    public String DYGH    ;
    public Integer SYZT   ;
    public Date KSSJ      ;
    public String KSGH    ;
    public Date JSSJ      ;
    public String JSGH    ;
    public Long ZXDH      ;
    public Integer SPBZ   ;
    public Integer MPBZ   ;
    public Integer PJDS   ;
    public Date KSHDSJ    ;
    public String KSHDGH  ;
    public Date JSHDSJ    ;
    public String JSHDGH  ;
    public Integer JYHDBZ ;
    public String JYHDGH  ;
    public Date JYHDSJ    ;
    public Integer BYHDBZ ;
    public String BYHDGH  ;
    public Date BYHDSJ    ;
    public String JGID    ;

    public List<TransfusionDetail> details = new ArrayList<>();
    public List<TransfusionSuspend> suspends = new ArrayList<>();
    public List<TransfusionPatrol> patrols = new ArrayList<>();
}
