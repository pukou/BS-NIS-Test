package com.bsoft.nis.domain.catheter.db;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by Administrator on 2016/12/29.
 */
//引流管记录表对象
public class CatheterYLGJLvo {

	public Long JLXH;

    public String YLGXH;

    public String YLGMC;

    public String YLL;

    public String JLGH;

    public String BRBQ;

    public String ZYH;

    public String JGID;

    public String JLSJ;

    public String YZXH;

    public String JLRQ;

    public String CJH;

    public String TZXM;

    @JsonIgnore
    public String dbtype;
}
