package com.bsoft.nis.domain.dailywork;

import java.io.Serializable;

/**
 *今日工作
 * Created by king on 2016/11/18.
 */
public class DailyWork implements Serializable {

    //病人床号
    public String BRCH;

    //病人姓名
    public String BRXM ;

    //住院号
    public String ZYH ;

    //住院号码
    public String ZYHM ;


    /**
     * 以下风险提醒
     */
    //提醒内容
    public String TXNR;

    public String PGXH;

    public String PGDH;

    public String PGLX;

    public String BDMC;

    public String PGSJ;

    public String ZKMS;
    //
    public String KDSJ;

}
