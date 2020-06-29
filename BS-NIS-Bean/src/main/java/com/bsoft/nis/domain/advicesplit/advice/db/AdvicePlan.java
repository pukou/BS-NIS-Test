package com.bsoft.nis.domain.advicesplit.advice.db;

import com.bsoft.nis.domain.advicesplit.advice.AdviceCom;
import com.bsoft.nis.domain.advicesplit.advice.PlanTime;
import com.bsoft.nis.domain.advicesplit.plantype.db.PlanType;

import java.io.Serializable;

/**
 * Describtion:医嘱计划
 * Created: dragon
 * Date： 2016/12/14.
 */
public class AdvicePlan implements Serializable{
    private static final long serialVersionUID = -4106039202084470560L;
    
    //计划号
    
    public String JHH ;

    public String JGID;

    public String BRBQ;


    //使用频次
    
    public String SYPC ;
    
    //药品用法
    
    public String YPYF ;
    
    //归属类型
    
    public String GSLX ;
    
    //类型序号
    
    public String LXH ;
    
    //计划时间
    
    public String JHSJ ;

    public String JHRQ;
    
    //开嘱时间
    
    public String KSSJ ;
    
    //执行状态
    
    public String ZXZT ;
    
    //住院号
    
    public String ZYH ;
    
    //医嘱组号
    
    public String YZZH ;
    
    //医嘱序号
    
    public String YZXH ;
    
    //医生医嘱本编号
    
    public String YSYZBH ;

    /**
     * 项目类型
     */
    public String XMLX;
    //项目序号
    
    public String XMXH ;
    
    //一次剂量
    
    public String YCJL ;
    
    //剂量单位
    
    public String JLDW ;
    
    //一次数量
    
    public String YCSL ;
    
    //数量单位
    
    public String SLDW ;
    
    //医嘱名称
    
    public String YZMC ;
    
    //医嘱主项
    
    public String YZZX ;
    
    //确认单号
    
    public String QRDH ;
    
    //时间编号
    
    public String SJBH ;
    
    //时间名称
    
    public String SJMC ;
    
    //周期日期
    
    public String ZQRQ ;

    public String JHSD;

    public String CSSJ;

    public String LSYZ;

    public String SRHDBZ;
    
    //生成状态
    //0: 不除理 1: 插入 2: 删除
    
    public Integer BUILDSTATUS ;
    
    //确认标志
    //指的是有没有配液过
    
    public Boolean QRBZ ;

    // 匹配标识，0 未匹配 1 匹配
    public Integer PPBZ = 0;

    public String dbtype;
}
