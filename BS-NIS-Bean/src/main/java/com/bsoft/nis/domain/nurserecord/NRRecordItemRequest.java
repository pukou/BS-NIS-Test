package com.bsoft.nis.domain.nurserecord;

import java.io.Serializable;

/**
 * Describtion:接受前端护理记录明细请求
 * Created: dragon
 * Date： 2016/11/24.
 */
public class NRRecordItemRequest implements Serializable{
    private static final long serialVersionUID = -4568092599430110617L;

    /**
     * 项目编号
     */
    public int XMBH;

    /**
     * 项目值value
     */
    public String VALUE;
    /**
     * 来源表单
     */
    public String LYBD = "0";
    /**
     * 来源标号
     */
    public String LYBH = "0";
    /**
     * 来源明细
     */
    public String LYMX = "0";
    /**
     * 来源明细类型
     */
    public String LYMXLX = "0";
}
