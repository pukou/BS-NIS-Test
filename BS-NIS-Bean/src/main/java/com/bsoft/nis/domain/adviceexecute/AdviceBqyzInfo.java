package com.bsoft.nis.domain.adviceexecute;

/**
 * Description: 病区医嘱
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-12-19
 * Time: 16:46
 * Version:
 */
public class AdviceBqyzInfo {

    /**
     * 医嘱序号
     */
    public String YZXH;

    /**
     * 医生医嘱本号
     */
    public String YSYZBH;

    /**
     * 医嘱组号
     */
    public String YZZH;

    /**
     * 住院号
     */
    public String ZYH;

    /**
     * 药品序号
     */
    public String YPXH;

    /**
     * 项目类型
     */
    public String XMLX;

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
     * 双人核对标志
     */
    public String SRHDBZ;

    /**
     * 自负判别
     */
    public String ZFPB;

    /**
     * 婴儿判别
     */
    public String YEPB;

    /**
     * 婴儿唯一号
     */
    public String YEWYH;

    /**
     * 病人科室
     */
//    public String BRKS;

    /**
     * 病人病区
     */
//    public String BRBQ;

    /**
     * 病人床号
     */
//    public String BRCH;

    /**
     * 开嘱医生
     */
    public String KZYS;

    /**
     * 临时医嘱
     */
    public String LSYZ;


    /**
     * 皮试判断
     * 0 不需要
     * 1 需要
     */
    public String PSPB;

    /**
     * 皮试结果
     */
    public String PSJG;

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
    //  2018-03-15 14:54:17 【医嘱执行 费用信息也进行了核对，不能继续执行下去】
    public String YPLX;
}
