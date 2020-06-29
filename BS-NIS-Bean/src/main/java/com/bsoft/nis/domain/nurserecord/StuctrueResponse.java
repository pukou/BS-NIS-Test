package com.bsoft.nis.domain.nurserecord;

import com.bsoft.nis.domain.nurserecord.db.StructurePlugIn;

import java.io.Serializable;
import java.util.List;

/**
 * Describtion:护理记录结构返回类
 * Created: dragon
 * Date： 2016/10/24.
 */
public class StuctrueResponse implements Serializable{
    private static final long serialVersionUID = -3017072355305618514L;

    /**
     * 类别名称
     */
    public String LBMC;

    /**
     * 类别号
     */
    public String LBH;

    /**
     * 组类型
     */
    public String ZLX;

    /**
     * 换页标识
     */
    public String HYBZ;


    public List<StructurePlugIn> NRControllist;
}
