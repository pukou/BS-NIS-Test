package com.bsoft.nis.domain.message;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * Describtion:消息
 * Created: dragon
 * Date： 2017/2/16.
 */
public class Messager implements Serializable{
    private static final long serialVersionUID = 354915542512881177L;
    /**
     * 消息的唯一ID值
     */
    public Long TXBH;
    /**
     * 消息的类型 危机值1 医嘱 2,工作 3, 系统 4
     */
    public Integer TXLX;

    public Integer TXJB;

    public String TXSB;

    public String TXNR;

    public Integer TXZT;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    public Date CSSJ;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    public Date TXSJ;

    public Long TXCS;

    public String XTBS;

    public Long ZYH;

    public Long BRBQ;

    public String HSGH;

    public String BZXX;

    public String JGID;

    /***do test***/
    //类型名称
    public String LXMC;

    //主动提醒
    public String ZDTX;
}
