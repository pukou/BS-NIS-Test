package com.bsoft.nis.domain.core.log.db;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * Describtion:操作日志记录
 * Created: dragon
 * Date： 2016/11/22.
 */
public class OperLog implements Serializable {
    private static final long serialVersionUID = -1179494802427744655L;
    /**
     * 记录编号
     */
    public Long JLBH;
    /**
     * 住院号
     */
    public Long ZYH;
    /**
     * 操作内容
     */
    public String CZNR;
    /**
     * 操作类别
     */
    public Integer CZLB;
    /**
     * 操作类型
     */
    public Integer CZLX;
    /**
     * 关联编号
     */
    public String GLBH;
    /**
     * 操作工号
     */
    public String CZGH;
    /**
     * 病区代码
     */
    public Long BQDM;
    /**
     * 操作状态
     */
    public String CZZT;
    /**
     * 失败信息
     */
    public String SBXX;
    /**
     * 操作时间
     */
    public String CZSJ;
    /**
     * 备注信息
     */
    public String BZXX;
    /**
     * 结构ID
     */
    public String JGID;
    /**
     * 作废标识
     */
    public Integer ZFBZ;
    @JsonIgnore
    public String dbtype;
}
