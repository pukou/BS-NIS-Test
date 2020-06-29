package com.bsoft.nis.domain.message;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * Describtion:
 * Created: dragon
 * Date： 2017/2/20.
 */
public class MessagerVo {
    /// <summary>
    /// 消息Id
    /// </summary>
    public String MsgId;

    /// <summary>
    /// 消息级别
    /// </summary>
    public Integer Level;

    /// <summary>
    /// 消息类型
    /// </summary>
    public Integer MsgType;

    /// <summary>
    /// 业务ID
    /// </summary>
    public String BusinessId;

    /// <summary>
    /// 消息内容
    /// </summary>
    public String Content;

    /// <summary>
    /// 住院号
    /// </summary>
    public Long PatientId;

    /// <summary>
    /// 病人病区
    /// </summary>
    public Long PatientWard;

    /// <summary>
    /// 病人病区
    /// </summary>
    public String UserId;

    /// <summary>
    /// 机构ID
    /// </summary>
    public String Agency;
    //类型名称
    public String LXMC;

    //主动提醒
    public String ZDTX;
    /// <summary>
    /// 时间
    /// </summary>
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    public Date Time;
}
