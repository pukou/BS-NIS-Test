package com.bsoft.nis.domain.adviceexecute.db;

import java.io.Serializable;

/**
 * Description: 计划拒绝(IENR_JHJJ)
 * User: 苏泽雄
 * Date: 17/1/6
 * Time: 11:40:58
 */
public class PlanRefusalVo implements Serializable {

	private static final long serialVersionUID = 6556982268826231064L;

	// 计划号
	public String JHH;

	// 原因序号
	public String YYXH;

	// 拒绝时间
	public String JJSJ;

	// 拒绝工号
	public String JJGH;

	// 机构ID
	public String JGID;

	// 数据库类型
	public String dbtype;
}
