package com.bsoft.nis.domain.dangerevaluate.db;

import java.io.Serializable;

/**
 * Description: 风险评估记录(IENR_FXPGJL)
 * User: 苏泽雄
 * Date: 16/12/8
 * Time: 17:35:06
 */
public class DERecordVo implements Serializable {

	private static final long serialVersionUID = -8865802787940223784L;

	// 评估序号
	public String PGXH;

	// 住院号
	public String ZYH;

	// 病人病区
	public String BRBQ;

	// 评估类型
	public String PGLX;

	// 评估单号
	public String PGDH;

	// 评估时间
	public String PGSJ;

	// 评估工号
	public String PGGH;

	// 评估总分
	public String PGZF;

	// 风险程度
	public String FXCD;

	// 创建时间
	public String CJSJ;

	// 创建工号
	public String CJGH;

	// 护士长签名
	public String HSZQM;

	// 护士长签名时间
	public String HSZQMSJ;

	// 机构ID
	public String JGID;

	// 状态标志  0 作废  1 新增
	public String ZTBZ;

	// 程度描述
	public String CDMS;


	/**
	 * 外部属性
	 */
	// 数据库类型  用于执行SQL语句时区分数据库类型
	public String dbtype;
}
