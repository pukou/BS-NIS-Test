package com.bsoft.nis.domain.nurserecord;

import java.io.Serializable;
import java.util.List;

/**
 * Describtion:接收前端护理记录保存数据
 * Created: dragon
 * Date： 2016/11/24.
 */
public class NRRecordRequest implements Serializable{
    private static final long serialVersionUID = 6120632845898662662L;
    /**
     * 住院号
     */
    public String ZYH;

    /**
     * 换行标志
     */
    public String HHBZ;

    /**
     * 书写时间
     */
    public String SXSJ;

    /**
     * 记录编号
     */
    public String JLBH;

    /**
     * 结构编号
     */
    public String JGBH;
    /**
     * 记录时间
     */
    public String JLSJ;

    /**
     * 机构ID
     */
    public String JGID;

    /**
     * 用户ID
     */
    public String YHID;

    /**
     * 明细列表
     */
    public List<NRRecordItemRequest> ItemList;
}
