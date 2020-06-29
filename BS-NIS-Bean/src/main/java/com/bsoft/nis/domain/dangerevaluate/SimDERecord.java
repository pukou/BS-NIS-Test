package com.bsoft.nis.domain.dangerevaluate;

import java.io.Serializable;

/**
 * Description: 风险评估记录简略信息
 * User: 苏泽雄
 * Date: 16/12/1
 * Time: 16:48:18
 */
public class SimDERecord implements Serializable{

	private static final long serialVersionUID = -8010681622539314622L;

	// 评估序号
	public String PGXH;

	// 评估单号
	public String PGDH;

	// 评估时间
	public String PGSJ;

	// 评估总分
	public String PGZF;

	// 评估工号 员工代码
	public String PGGH;

	// 评估护士
	public String PGHS;

	// 质控描述
	public String ZKMS;

	// 评估类型
	public String PGLX;

	// 填写日期
	// 注：该字段在SQL语句中有获取，但前端的SimRiskRecord中没有该字段
	public String TXRQ;
}
