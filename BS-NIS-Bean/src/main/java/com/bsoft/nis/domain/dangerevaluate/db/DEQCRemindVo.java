package com.bsoft.nis.domain.dangerevaluate.db;

import java.io.Serializable;

/**
 * Description: 风险质控提醒(IENR_FXZKTX)
 * User: 苏泽雄
 * Date: 16/12/9
 * Time: 10:53:13
 */
public class DEQCRemindVo implements Serializable {

	private static final long serialVersionUID = 3300303933105866921L;

	// 提醒序号
	public String TXXH;

	// 质控序号
	public String ZKXH;

	// 住院号
	public String ZYH;

	// 病人病区
	public String BRBQ;

	// 评估类型
	public String PGLX;

	// 评估序号
	public String PGXH;

	// 风险程度
	public String FXCD;

	// 提醒内容
	public String TXNR;

	// 提醒日期
	public String TXRQ;

	// 提醒状态  0 未提醒  1 已提醒  2 作废
	public String TXZT;

	// PDA提醒状态
	public String PDAZT;

	// 创建时间
	public String CJSJ;

	// 评估总分
	public String PGZF;

	// 机构ID
	public String JGID;


	/**
	 * 外部属性
	 */
	// 数据库类型  用于执行SQL语句时区分数据库类型
	public String dbtype;
}
