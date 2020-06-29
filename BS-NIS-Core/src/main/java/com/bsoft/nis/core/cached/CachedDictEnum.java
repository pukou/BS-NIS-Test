package com.bsoft.nis.core.cached;

import com.bsoft.nis.core.enumx.EnumBehaviour;

/**
 * Describtion:常用缓存字典
 * Created: dragon
 * Date： 2016/10/19.
 */
public enum CachedDictEnum implements EnumBehaviour{
    /**
     * 病人性质
     */
    MOB_BRXZ("MOB_BRXZ","BRXZ","XZMC",null),
    /**
     * 产地地址
     */
    MOB_CDDZ("MOB_CDDZ","YPCD","CDMC",null),
    /**
     * 代码字典
     */
    MOB_DMZD("MOB_DMZD","DMSB","DMMC",null),
    /**
     * EMR字典
     */
    MOB_EMRDMZD("MOB_EMRDMZD","DMSB","DMMC",null),
    /**
     * 费用信息
     */
    MOB_FYBM("MOB_FYBM","FYXH","FYMC",null),
    /**
     * 科室代码
     */
    MOB_KSDM("MOB_KSDM","KSDM","KSMC",""),
    /**
     * 手术名称
     */
    MOB_SSDM("MOB_SSDM","SSNM","SSMC",null),
    /**
     * 使用频次
     */
    MOB_SYPC("MOB_SYPC","PCBM","PCMC",null),
    /**
     * 通用品库
     */
    MOB_TYPK("MOB_TYPK","YPXH","YPMC",null),
    /**
     * 系统配置
     */
    MOB_XTPZ("MOB_XTPZ","PZBH","DMMC",null),
    /**
     * 员工代码
     */
    MOB_YGDM("MOB_YGDM","YGDM","YGXM",""),
    /**
     * 收费项目
     */
    MOB_YLSF("MOB_YLSF","FYXH","FYMC",null),
    /**
     * 药品属性
     */
    MOB_YPSX("MOB_YPYF","YPSX","SXMC",null),
    /**
     * 业务类别
     */
    MOB_YWLB("MOB_YWLB","YWLB","LBMC",null),
    /**
     * 疾病编码
     */
    GY_JBBM("GY_JBBM","JBXH","JBMC",null),
    /**
     * 药品用法
     */
    MOB_YPYF("MOB_YPYF","YPYF","XMMC",null);

    private String jgid;       // 机构ID
    private String dictName;   // 字典名称
    private String compareCol; // 比较字段  KSDM
    private String retCol;     // 返回字段  KSMC

    CachedDictEnum(String dictName,String compareCol,String retCol,String jgid){
        this.dictName = dictName;
        this.compareCol = compareCol;
        this.retCol = retCol;
        this.jgid = jgid;
    }

    @Override
    public Integer get() {
        return null;
    }

    public String getJgid(){
        return this.jgid;
    }

    public String getDictName(){
        return this.dictName;
    }

    public String getCompareCol(){
        return this.compareCol;
    }

    public String getRetCol(){
        return this.retCol;
    }
}
