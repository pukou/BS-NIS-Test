package com.bsoft.nis.domain.timingserver.transferdata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Describtion:口服单
 * User: dragon (xinghl@bsoft.com.cn)
 * Date： 2017/4/11.
 * Time:  15:10.
 */
public class OralSheet implements Serializable{
    private static final long serialVersionUID = -7945223158321381860L;
    public Long KFDH   ;
    public Long ZYH    ;
    public Long BRBQ   ;
    public String BRCH ;
    public Integer YPYF;
    public Date KFRQ   ;
    public Date KFSJ   ;
    public Integer KFSD;
    public Integer BYBS;
    public Date BYSJ   ;
    public String BYGH ;
    public Integer BYLY;
    public String LYBS ;
    public String JGID ;

    public List<OralPackage> packages = new ArrayList<>();
}
