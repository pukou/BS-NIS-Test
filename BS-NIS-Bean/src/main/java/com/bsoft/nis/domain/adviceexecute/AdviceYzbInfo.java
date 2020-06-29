package com.bsoft.nis.domain.adviceexecute;

/**
 * Description: 医嘱本
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-12-19
 * Time: 16:47
 * Version:
 */
public class AdviceYzbInfo {

    /**
     * 医嘱序号
     */
    public String YZXH;

    /**
     * 医嘱组号
     */
    public String YZZH;

    /**
     * 医嘱序号
     */
    public String ZYH;

    /**
     * 药品序号
     */
    public String YPXH;

    /**
     * 药品用法
     */
    public String YPYF;

    /**
     * 使用频次
     */
    public String SYPC;

    /**
     * 剂量单位
     */
    public String JLDW;

    /**
     * 一次剂量
     */
    public String YCJL;

    /**
     * 一次数量
     */
    public String YCSL;

    /**
     * 数量单位
     */
    public String SLDW;

    /**
     * 医嘱状态
     * 0 新开
     * 1 已提交
     * 2 有疑问
     * 3 作废
     * 4 已停嘱
     * 5 复核通过
     */
    public String YZZT;

    /**
     * 开嘱时间
     */
    public String KZSJ;

    /**
     * 停嘱时间
     */
    public String TZSJ;

    /**
     * 医嘱名称
     */
    public String YZMC;

    /**
     * 护士执行时间
     */
    public String HSZXSJ;

    /**
     * 护士执行工号
     */
    public String HSZXGH;

    /**
     * 核对工号
     */
    public String HSZXGH2;

    /**
     * 数据库类型
     */
    public String dbtype;

	/**
	 * 医嘱期效  长期医嘱  临时医嘱
	 */
	public String YZQX;
}
