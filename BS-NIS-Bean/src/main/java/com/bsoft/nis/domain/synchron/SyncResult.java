package com.bsoft.nis.domain.synchron;

/**
 * Describtion:同步结果
 * Created: dragon
 * Date： 2017/1/17.
 */
public class SyncResult {
    /// <summary>
    /// 结果状态
    /// 0:失败 1:成功
    /// </summary>
    public String JGZT;
    /// <summary>
    /// 结果信息
    /// </summary>
    public String Msg;
    /// <summary>
    /// 是要插入数据
    /// </summary>
    public Boolean IsInsert= false;
    /// <summary>
    /// 来源类型
    /// </summary>
    public String LYLX;
    /// <summary>
    /// 来源记录序号
    /// </summary>
    public String LYJL;

    /// <summary>
    /// 来源明细
    /// </summary>
    public String LYMX;

    /// <summary>
    /// 来源明细类型
    /// </summary>
    public String LYMXLX;

    /// <summary>
    /// 目标类型
    /// </summary>
    public String MBLX;
    /// <summary>
    /// 目标记录序号
    /// </summary>
    public String MBJL;
    /// <summary>
    /// 机构ID
    /// </summary>
    public String JGID;
}
