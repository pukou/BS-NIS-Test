package com.bsoft.nis.domain.nurserecord;

import java.io.Serializable;

/**
 * Describtion:护理记录结构引用
 * Created: dragon
 * Date： 2016/10/24.
 */
public class ReferItem implements Serializable{
    private static final long serialVersionUID = -7955782520940329301L;
    /**
     * 引用编号
     */
    public String YYBH;

    /**
     * 引用内容
     */
    public String YYNR;
    /**
     * 记录时间
     */
    public String JLSJ;

    /**
     * 备注信息
     */
    public String BZXX;
}
