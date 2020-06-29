package com.bsoft.nis.domain.core.barcode;

import java.io.Serializable;

/**
 * Describtion:条码设定(ENR_TMSD)
 * Created: dragon
 * Date： 2016/12/2.
 */
public class Barcode implements Serializable {

    private static final long serialVersionUID = -5709661166912303132L;

    /*
    * 设定号
    */
    public String SDH;

    /*
    * 条码分类
    */
    public String TMFL;

    /*
    * 分类标识
    * 1.病人腕带:0
    * 2.归属类型:ENR_GSLX.GSLX
    * 3.医护胸牌:0
    */
    public String FLBS;

    /*
    * 来源标志
    */
    public String LYBZ;

    /*
    * 条码前缀
    */
    public String TMQZ;

    /*
    * 作废标志
    */
    public String ZFBZ;

    /*
    * 条码规则
    * 1.前缀 2.长度 3.日期 4.校验位
    */
    public String TMGZ;

    /*
    * 规则内容
    */
    public String GZNR;

    /*
    * 条码内容
    */
    public String TMNR;

    /*
    * 条码源码
    */
    public String source;

    /*
    * 机构id
    */
    public String JGID;
}
