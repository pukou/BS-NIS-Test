package com.bsoft.nis.domain.adviceexecute.ResponseBody;

/**
 * Description: 医嘱执行返回结果明细
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-12-27
 * Time: 16:05
 * Version:
 */
public class REModel {

    public String JHZT;

    /**
     * 非0失败
     */
    public String ZXR;
    public String ZXSJ;
    public String YCXX;
    public String JHH;
    /**
     * 异常名称
     */
    public String YZMC;
    /**
     * 异常类型
     */
    public int YCLX;
    public String JHSJ;
    //
    public String JHSJ_NEW;
    public String YZZH;
    public String YPYF;
}
