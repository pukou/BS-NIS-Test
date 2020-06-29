package com.bsoft.nis.domain.patient;

import java.io.Serializable;

/**
 * 住院病人
 * Created by Administrator on 2016/10/10.
 */
public class Patient implements Serializable{
    private static final long serialVersionUID = -2602720459848606468L;
    public String userId;
    public String userName;
    /**
     * 住院号
     */
    public String ZYH;
    /**
     * 住院号码
     */
    public String ZYHM ;
    /**
     * 病人姓名
     */
    public String BRXM;
    /**
     * 出生年月
     */
    public String CSNY;
    /**
     * 入院日期
     */
    public String RYRQ;
    /**
     * 病人病区
     */
    public String BRBQ;
    /**
     * 病人科室
     */
    public String BRKS;
    /**
     * 病人床号
     */
    public String BRCH;

    public String JGID;

    public String BRXB;
    /**
     * 病人性别
     */
    public String BRXBVALUE;
    /**
     * 外部属性
     */

    public String BRBQVALUE;

    public String BRKSVALUE;

    public String BRNL ;

    public String CYRQ ;
}
