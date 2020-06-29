package com.bsoft.nis.domain.synchron;

import java.io.Serializable;
import java.util.List;

/**
 * Describtion:同步出参
 * Created: dragon
 * Date： 2017/1/3.
 */
public class OutArgument implements Serializable{
    private static final long serialVersionUID = 3088314498263974803L;
    public OutArgument(){}
    public OutArgument(InArgument inArgument){
        this.inArgument = inArgument;
    }
    /**
     * 入参对象
     */
    public InArgument inArgument;
    /**
     * 住院号
     */
    public String zyh;
    /**
     * 护士工号
     */
    public String hsgh;
    /**
     * 机构ID
     */
    public String jgid;
    /**
     * 病区代码
     */
    public String bqdm;
    /**
     * 记录时间
     */
    public String jlsj;
    /**
     * 表单类型
     */
    public String bdlx;
    /**
     * 表单样式
     */
    public String bdys;
    /**
     * 来源表单类型
     */
    public String lybdlx;
    /**
     * 来源记录序号
     */
    public String lyjlxh;
    /**
     * 目标记录序号
     */
    public String mbjlxh;
    /**
     * 状态标识 0 新增 1 修改 2 删除
     */
    public String flag;
    /**
     * 错误代码标识
     */
    public Integer errFlag;
    /**
     * 错误信息
     */
    public String errMsg;
    /**
     * 来源明细类型
     */
    public String lymxlx;
    /**
     * 来源明细
     */
    public String lymx;
    /**
     * 项目列表
     */
    public List<Project> projects;

    /**
     * 保存的项目
     */
    public List<Project> saveProjects;

}
